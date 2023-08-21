package sifive.fpgashells.shell

import chisel3._
import freechips.rocketchip.diplomacy._
import org.chipsalliance.cde.config._

case class SwitchShellInput(number: Int = 0)
case class SwitchDesignInput()(implicit val p: Parameters)
case class SwitchOverlayOutput(sw: ModuleValue[Bool])
case object SwitchOverlayKey extends Field[Seq[DesignPlacer[SwitchDesignInput, SwitchShellInput, SwitchOverlayOutput]]](Nil)
trait SwitchShellPlacer[Shell] extends ShellPlacer[SwitchDesignInput, SwitchShellInput, SwitchOverlayOutput]

abstract class SwitchPlacedOverlay(
  val name: String, val di: SwitchDesignInput, val si: SwitchShellInput)
    extends IOPlacedOverlay[Bool, SwitchDesignInput, SwitchShellInput, SwitchOverlayOutput]
{
  implicit val p = di.p

  def ioFactory = Input(Bool())

  val switchWire = shell { InModuleBody { Wire(Bool()) }}
  def overlayOutput = SwitchOverlayOutput(sw = switchWire)
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
