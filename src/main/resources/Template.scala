package $package

import model.Ctx
import model.$className
import model.persistence._
import model.persistence.Types.IdOptionLong

object $classNames extends $extend {
  import Ctx._

  @inline def _findAll: List[$className] = run { quote { query[$className] } }

  val queryById: IdOptionLong => Quoted[EntityQuery[$className]] =
    (id: IdOptionLong) =>
      quote { query[$className].filter(_.id == lift(id)) }

  val _deleteById: (IdOptionLong) => Unit =
    (id: IdOptionLong) => {
      run { quote { queryById(id).delete } }
      ()
    }

  val _findById: IdOptionLong => Option[$className] =
    (id: Id[Option[Long]]) =>
      run { quote { queryById(id) } }.headOption

  val _insert: $className => $className =
    (user: $className) => {
      val id: Id[Option[Long]] = try {
        run { quote { query[$className].insert(lift(user)) }.returning(_.id) }
      } catch {
        case e: Throwable =>
          logger.error(e.getMessage)
          throw e
      }
      user.setId(id)
    }

  val _update: $className => $className =
    (user: $className) => {
      run { queryById(user.id).update(lift(user)) }
      user
    }

  @inline override def findById(id: IdOptionLong): Option[$className] =
    id.value.map(theCache.get).getOrElse { run { queryById(id) }.headOption }
}
