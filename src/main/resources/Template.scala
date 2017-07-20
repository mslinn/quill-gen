package model.dao

import model.{Ctx, Template}
import model.persistence._
import model.persistence.Types.IdOptionLong

object Templates extends CachedPersistence[Long, Option[Long], Template]
    with StrongCacheLike[Long, Option[Long], Template] {
  import Ctx._

  @inline def _findAll: List[Template] = run { quote { query[Template] } }

  val queryById: IdOptionLong => Quoted[EntityQuery[Template]] =
    (id: IdOptionLong) =>
      quote { query[Template].filter(_.id == lift(id)) }

  val _deleteById: (IdOptionLong) => Unit =
    (id: IdOptionLong) => {
      run { quote { queryById(id).delete } }
      ()
    }

  val _findById: IdOptionLong => Option[Template] =
    (id: Id[Option[Long]]) =>
      run { quote { queryById(id) } }.headOption

  val _insert: Template => Template =
    (user: Template) => {
      val id: Id[Option[Long]] = try {
        run { quote { query[Template].insert(lift(user)) }.returning(_.id) }
      } catch {
        case e: Throwable =>
          logger.error(e.getMessage)
          throw e
      }
      user.setId(id)
    }

  val _update: Template => Template =
    (user: Template) => {
      run { queryById(user.id).update(lift(user)) }
      user
    }

  @inline override def findById(id: IdOptionLong): Option[Template] =
    id.value.map(theCache.get).getOrElse { run { queryById(id) }.headOption }
}
