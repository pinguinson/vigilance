package com.github.pinguinson.vigilance.util

import scala.tools.nsc.Global

trait CompilerAccess {
  val global: Global
}
