<img src='https://raw.githubusercontent.com/mslinn/quill-cache/media/quill-cache.jpg' align='right' width='33%'>

[![Build Status](https://travis-ci.org/mslinn/quill-gen.svg?branch=master)](https://travis-ci.org/mslinn/quill-gen)
[![GitHub version](https://badge.fury.io/gh/mslinn%2Fquill-gen.svg)](https://badge.fury.io/gh/mslinn%2Fquill-gen)

A code generator for [quill-cache](https://github.com/mslinn/quill-cache/) DAOs.

## How to Use QuillGen
Download this project and run it like this:

    $ git clone https://github.com/mslinn/quill-gen.git
    $ cd quill-gen
    
To view the help message:
```    
$ sbt run
Error: Missing argument Class1, Class2, etc
Try --help for more information.
QuillGen 0.1.0
Usage: quillGen [options] Class1, Class2, etc

  -p, --package <value>    Package for generated quill-cache DAOs (defaults to model.dao)
  -d, --outputDir <value>  Directory to write generated quill-cache DAOs to (defaults to target/quillGen/)
  Class1, Class2, etc      Names of domain objects to generate DAOs for
  --help                   prints this usage text

Generated quill-cache DAOs.

Example usage:
  quillGen Blah                            // Write generated DAO for Blah to target/quillGen//model/dao/Blahs.scala
  quillGen Blah Foo Bar                    // Write generated DAOs for Blah, Foo and Bar to target/quillGen//model/dao/{Blah,Foo,Bar}.scala
  quillGen -d src/main/scala/ Blah         // Write generated DAO for Blah.scala to src/main/scala/model/dao/
  quillGen -d src/main/scala/ -p "" Blah   // Write generated DAO for Blah to src/main/scala/Blah.scala
  quillGen -p "" Blah                      // Write generated DAO for Blah to target/quillGen//Blah/scala
```
