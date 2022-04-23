package sifive.fpgashells.shell

import chisel3._
import chisel3.experimental.Analog

import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.subsystem.{BaseSubsystem, PeripheryBus, PeripheryBusKey}
import freechips.rocketchip.tilelink.TLBusWrapper
import freechips.rocketchip.interrupts.IntInwardNode


import sifive.blocks.devices.porgen._

case class PorGenShellInput(index: Int = 0)
case class PorGenDesignInput(node: BundleBridgeSource[PorGenPortIO])(implicit val p: Parameters)
case class PorGenOverlayOutput()
case object PorGenOverlayKey extends Field[Seq[DesignPlacer[PorGenDesignInput, PorGenShellInput, PorGenOverlayOutput]]](Nil)
trait PorGenShellPlacer[Shell] extends ShellPlacer[PorGenDesignInput, PorGenShellInput, PorGenOverlayOutput]

class ShellPorGenPortIO extends Bundle {
  val poreset_n = Analog(1.W)
  val ereset_n = Analog(1.W)
}

abstract class PorGenPlacedOverlay(
  val name: String, val di: PorGenDesignInput, val si: PorGenShellInput)
    extends IOPlacedOverlay[ShellPorGenPortIO, PorGenDesignInput, PorGenShellInput, PorGenOverlayOutput]
{
  implicit val p = di.p

  def ioFactory = new ShellPorGenPortIO

  val tlporgenSink = sinkScope { di.node.makeSink }

  def overlayOutput = PorGenOverlayOutput()
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
