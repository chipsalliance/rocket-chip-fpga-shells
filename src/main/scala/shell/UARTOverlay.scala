package sifive.fpgashells.shell

import chisel3._
import chisel3.experimental.Analog

import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.subsystem.{BaseSubsystem, PeripheryBus, PeripheryBusKey}
import freechips.rocketchip.tilelink.TLBusWrapper
import freechips.rocketchip.interrupts.IntInwardNode


import sifive.blocks.devices.uart._

case class UARTShellInput(index: Int = 0)
case class UARTDesignInput(node: BundleBridgeSource[UARTPortIO])(implicit val p: Parameters)
case class UARTOverlayOutput()
case object UARTOverlayKey extends Field[Seq[DesignPlacer[UARTDesignInput, UARTShellInput, UARTOverlayOutput]]](Nil)
trait UARTShellPlacer[Shell] extends ShellPlacer[UARTDesignInput, UARTShellInput, UARTOverlayOutput]

// Tack on cts, rts signals available on some FPGAs. They are currently unused
// by our designs.
class ShellUARTPortIO(val flowControl: Boolean) extends Bundle {
  val txd = Analog(1.W)
  val rxd = Analog(1.W)
  val rtsn = if (flowControl) Some(Analog(1.W)) else None
  val ctsn = if (flowControl) Some(Analog(1.W)) else None
}

abstract class UARTPlacedOverlay(
  val name: String, val di: UARTDesignInput, val si: UARTShellInput, val flowControl: Boolean)
    extends IOPlacedOverlay[ShellUARTPortIO, UARTDesignInput, UARTShellInput, UARTOverlayOutput]
{
  implicit val p = di.p

  def ioFactory = new ShellUARTPortIO(flowControl)

  val tluartSink = sinkScope { di.node.makeSink }

  def overlayOutput = UARTOverlayOutput()
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
