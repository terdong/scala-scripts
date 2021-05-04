/*
 * © 2021 DongHee Kim <terdong@gmail.com>
 */

package com.teamgehem.scala_scripts.wii

import better.files._

object WiiFileNameChanger extends App {

  val idRegex = "\\[(.*?)\\]".r.anchored
  val specialCharacters = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]"

  args.foreach(println)
  if (args.size != 2) {
    System.err.println("Not enough parameters. You have to input 2 parameters.")
    sys.exit(0)
  }

  val sourceDirectoryPath = args(0)
  val sourceInfoFilePath = args(1)

  val sourceDir = sourceDirectoryPath.toFile
  val infoFile = sourceInfoFilePath.toFile
  if (sourceDir.notExists && !sourceDir.isDirectory && infoFile.notExists) {
    System.err.println("One of them is wrong files. Check them out again.")
    sys.exit(0)
  }

  println(s"File Count: ${sourceDir.children.size}")

  val fileInfoMap = infoFile.contentAsString.split("\\r?\\n").filter(_.nonEmpty).foldLeft(("", Map.empty[String, String])) { (pair, value) =>
    val prevKey = pair._1
    val map = pair._2
    value match {
      case idRegex(id) => (id, map)
      case v if v.contains("Original title") => {
        (prevKey, map + (prevKey -> v.split('=').last.substring(1).replaceAll(specialCharacters, "").replace(' ', '_')))
      }
      case _ => pair
    }
  }._2.withDefaultValue("Not found")


  val changingFiles = sourceDir.children

  changingFiles.foreach { file =>
    val prevName = file.name.split('.').head
    fileInfoMap.get(prevName) match {
      case Some(newName) => {
        println(s"$prevName -> $newName")
        file.renameTo(s"$newName.wbfs")
      }
      case None => println(s"$prevName has no new Name.")
    }
  }
}
