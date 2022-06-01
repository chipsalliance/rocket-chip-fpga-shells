package sifive.fpgashells.shell

import chisel3._
import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy._

//Core-To-Shell Reset Overlay: No IOs, but passes a Bool into the shell to be orred into the pllReset, allowing core signals to reset the shell

case class CTSResetShellInput()
case class CTSResetDesignInput(rst: Bool)(implicit val p: Parameters)
case class CTSResetOverlayOutput()
case object CTSResetOverlayKey extends Field[Seq[DesignPlacer[CTSResetDesignInput, CTSResetShellInput, CTSResetOverlayOutput]]](Nil)
trait CTSResetShellPlacer[Shell] extends ShellPlacer[CTSResetDesignInput, CTSResetShellInput, CTSResetOverlayOutput]

abstract class CTSResetPlacedOverlay(
  val name: String, val di: CTSResetDesignInput, si: CTSResetShellInput)
    extends PlacedOverlay[CTSResetDesignInput, CTSResetShellInput, CTSResetOverlayOutput]
{
  implicit val p = di.p

  def overlayOutput = CTSResetOverlayOutput()
}

/*
   Copyright 2016 SiFive, Inc.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
