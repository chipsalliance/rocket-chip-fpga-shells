package sifive.fpgashells.shell

import chisel3._
import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy._

case class LEDShellInput(
  color: String = "",
  header: String = "",
  rgb: Boolean = false,
  number: Int = 0)

case class LEDDesignInput()(implicit val p: Parameters)
case class LEDOverlayOutput(led: ModuleValue[Bool])
case object LEDOverlayKey extends Field[Seq[DesignPlacer[LEDDesignInput, LEDShellInput, LEDOverlayOutput]]](Nil)
trait LEDShellPlacer[Shell] extends ShellPlacer[LEDDesignInput, LEDShellInput, LEDOverlayOutput]

abstract class LEDPlacedOverlay(
  val name: String, val di: LEDDesignInput, si: LEDShellInput)
    extends IOPlacedOverlay[Bool, LEDDesignInput, LEDShellInput, LEDOverlayOutput]
{
  implicit val p = di.p

  def ioFactory = Output(Bool())

  val ledSource = BundleBridgeSource(() => Bool())
  val ledSink = shell { ledSource.makeSink() }
  def overlayOutput = LEDOverlayOutput(InModuleBody { ledSource.out(0)._1 })
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
