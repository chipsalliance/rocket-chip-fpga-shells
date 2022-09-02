package sifive.fpgashells.shell

import chisel3._
import chisel3.experimental.Analog

import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.subsystem.{BaseSubsystem, PeripheryBus, PeripheryBusKey}
import freechips.rocketchip.tilelink.TLBusWrapper
import freechips.rocketchip.interrupts.IntInwardNode


import sifive.blocks.devices.i2c._

case class I2CShellInput(index: Int = 0)
case class I2CDesignInput(node: BundleBridgeSource[I2CPort])(implicit val p: Parameters)
case class I2COverlayOutput()
trait I2CShellPlacer[Shell] extends ShellPlacer[I2CDesignInput, I2CShellInput, I2COverlayOutput]

case object I2COverlayKey extends Field[Seq[DesignPlacer[I2CDesignInput, I2CShellInput, I2COverlayOutput]]](Nil)

class ShellI2CPortIO extends Bundle {
  val scl = Analog(1.W)
  val sda = Analog(1.W)
}

abstract class I2CPlacedOverlay(
  val name: String, val di: I2CDesignInput, val si: I2CShellInput)
    extends IOPlacedOverlay[ShellI2CPortIO, I2CDesignInput, I2CShellInput, I2COverlayOutput]
{
  implicit val p = di.p

  def ioFactory = new ShellI2CPortIO

  val tli2cSink = sinkScope { di.node.makeSink }

  def overlayOutput = I2COverlayOutput()
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
