package com.github.pinguinson.vigilance.io

import java.io.File

import scala.sys.process.ProcessLogger
import scala.util.Try
import scala.util.control.NonFatal

object GitUtils {

  private lazy val workingDirectory = new File(System.getProperty("user.dir"))

  private def exec(cmd: Seq[String]): Try[Seq[String]] = {
    val gitRes: Try[String] = Try {
      val lastError = new StringBuilder
      val swallowStderr = ProcessLogger(_ => Unit, err => lastError.append(err))
      try {
        sys.process.Process(cmd, workingDirectory).!!(swallowStderr).trim
      } catch {
        case NonFatal(e) => throw new IllegalStateException(s"Failed to run command ${cmd.mkString(" ")} Error: ${lastError.toString}", e)
      }
    }

    gitRes.map(_.lines.toSeq)
  }

  def diff(branch: String): Seq[String] = {
    val cmd = Seq(
      "git",
      "diff",
      "--name-only",
      "--diff-filter=d",
      branch
    )
    exec(cmd).get.map(file => s"$workingDirectory/$file")
  }
}