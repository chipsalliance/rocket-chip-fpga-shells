package sifive.fpgashells.shell

import chisel3._
import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy._

case class ButtonShellInput(
  header: String = "",
  number: Int = 0)

case class ButtonDesignInput()(implicit val p: Parameters)
case class ButtonOverlayOutput(but: ModuleValue[Bool])
case object ButtonOverlayKey extends Field[Seq[DesignPlacer[ButtonDesignInput, ButtonShellInput, ButtonOverlayOutput]]](Nil)
trait ButtonShellPlacer[Shell] extends ShellPlacer[ButtonDesignInput, ButtonShellInput, ButtonOverlayOutput]

abstract class ButtonPlacedOverlay(
  val name: String, val di: ButtonDesignInput, si:ButtonShellInput)
    extends IOPlacedOverlay[Bool, ButtonDesignInput, ButtonShellInput, ButtonOverlayOutput]
{
  implicit val p = di.p

  def ioFactory = Input(Bool())

  val buttonSource = shell { BundleBridgeSource(() => Bool()) }
  val buttonSink = buttonSource.makeSink()
  def overlayOutput = ButtonOverlayOutput(but = InModuleBody { buttonSink.bundle })
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
