package sifive.fpgashells.shell

import chisel3._
import chisel3.experimental.Analog

import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.subsystem.{BaseSubsystem, PeripheryBus, PeripheryBusKey}
import freechips.rocketchip.tilelink.TLBusWrapper
import freechips.rocketchip.interrupts.IntInwardNode


import sifive.blocks.devices.pwm._

case class PWMShellInput(index: Int = 0)
case class PWMDesignInput(node: BundleBridgeSource[PWMPortIO])(implicit val p: Parameters)
case class PWMOverlayOutput()
case object PWMOverlayKey extends Field[Seq[DesignPlacer[PWMDesignInput, PWMShellInput, PWMOverlayOutput]]](Nil)
trait PWMShellPlacer[Shell] extends ShellPlacer[PWMDesignInput, PWMShellInput, PWMOverlayOutput]

class ShellPWMPortIO extends Bundle {
  val pwm_gpio = Vec(4, Analog(1.W))
}

abstract class PWMPlacedOverlay(
  val name: String, val di: PWMDesignInput, val si: PWMShellInput)
    extends IOPlacedOverlay[ShellPWMPortIO, PWMDesignInput, PWMShellInput, PWMOverlayOutput]
{
  implicit val p = di.p

  def ioFactory = new ShellPWMPortIO

  val tlpwmSink = sinkScope { di.node.makeSink }

  def overlayOutput = PWMOverlayOutput()
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
