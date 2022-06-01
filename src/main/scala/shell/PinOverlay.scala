package sifive.fpgashells.shell

import chisel3._
import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy._
import sifive.blocks.devices.gpio._
import freechips.rocketchip.tilelink.TLBusWrapper
import freechips.rocketchip.interrupts.IntInwardNode
import chisel3.experimental.Analog

case class PinShellInput()
case class PinDesignInput()(implicit val p: Parameters)
case class PinOverlayOutput(pin: ModuleValue[PinPortIO])
case object PinOverlayKey extends Field[Seq[DesignPlacer[PinDesignInput, PinShellInput, PinOverlayOutput]]](Nil)
trait PinShellPlacer[Shell] extends ShellPlacer[PinDesignInput, PinShellInput, PinOverlayOutput]

class PinPortIO extends Bundle {
  val pins = Vec(8, Analog(1.W))
}

abstract class PinPlacedOverlay(
  val name: String, val di: PinDesignInput, val si: PinShellInput)
    extends IOPlacedOverlay[PinPortIO, PinDesignInput, PinShellInput, PinOverlayOutput]
{
  implicit val p = di.p

  def ioFactory = new PinPortIO

  val pinSource = BundleBridgeSource(() => new PinPortIO)
  val pinSink = shell { pinSource.makeSink }

  def overlayOutput = PinOverlayOutput(pin = InModuleBody { pinSource.bundle } )

  shell { InModuleBody {
    io <> pinSink.bundle
  }}
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
