package sifive.fpgashells.shell

import chisel3._
import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy._
import sifive.blocks.devices.gpio._
import freechips.rocketchip.tilelink.TLBusWrapper
import freechips.rocketchip.interrupts.IntInwardNode
import chisel3.experimental.Analog

case class GPIOPMODShellInput()
case class GPIOPMODDesignInput()(implicit val p: Parameters)
case class GPIOPMODOverlayOutput(pmod: ModuleValue[GPIOPMODPortIO])
case object GPIOPMODOverlayKey extends Field[Seq[DesignPlacer[GPIOPMODDesignInput, GPIOPMODShellInput, GPIOPMODOverlayOutput]]](Nil)
trait GPIOPMODShellPlacer[Shell] extends ShellPlacer[GPIOPMODDesignInput, GPIOPMODShellInput, GPIOPMODOverlayOutput]

class GPIOPMODPortIO extends Bundle {
  val gpio_pmod_0 = Analog(1.W)
  val gpio_pmod_1 = Analog(1.W)
  val gpio_pmod_2 = Analog(1.W)
  val gpio_pmod_3 = Analog(1.W)
  val gpio_pmod_4 = Analog(1.W)
  val gpio_pmod_5 = Analog(1.W)
  val gpio_pmod_6 = Analog(1.W)
  val gpio_pmod_7 = Analog(1.W)
}

abstract class GPIOPMODPlacedOverlay(
  val name: String, val di: GPIOPMODDesignInput, val si: GPIOPMODShellInput)
    extends IOPlacedOverlay[GPIOPMODPortIO, GPIOPMODDesignInput, GPIOPMODShellInput, GPIOPMODOverlayOutput]
{
  implicit val p = di.p

  def ioFactory = new GPIOPMODPortIO

  val pmodgpioSource = BundleBridgeSource(() => new GPIOPMODPortIO)
  val pmodgpioSink = shell { pmodgpioSource.makeSink }

  def overlayOutput = GPIOPMODOverlayOutput(pmod = InModuleBody { pmodgpioSource.bundle } )

  shell { InModuleBody {
    io <> pmodgpioSink.bundle
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
