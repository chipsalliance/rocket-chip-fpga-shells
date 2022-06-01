package sifive.fpgashells.shell

import chisel3._
import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy._
import sifive.blocks.devices.gpio._
import freechips.rocketchip.tilelink.TLBusWrapper
import freechips.rocketchip.interrupts.IntInwardNode
import chisel3.experimental.Analog

case class PMODShellInput(index: Int)
case class PMODDesignInput()(implicit val p: Parameters)
case class PMODOverlayOutput(pin: ModuleValue[PMODPortIO])
case object PMODOverlayKey extends Field[Seq[DesignPlacer[PMODDesignInput, PMODShellInput, PMODOverlayOutput]]](Nil)
trait PMODShellPlacer[Shell] extends ShellPlacer[PMODDesignInput, PMODShellInput, PMODOverlayOutput]

class PMODPortIO extends Bundle {
  val pins = Vec(8, Analog(1.W))
}

abstract class PMODPlacedOverlay(
  val name: String, val di: PMODDesignInput, val si: PMODShellInput)
    extends IOPlacedOverlay[PMODPortIO, PMODDesignInput, PMODShellInput, PMODOverlayOutput]
{
  implicit val p = di.p

  def ioFactory = new PMODPortIO

  val pinSource = BundleBridgeSource(() => new PMODPortIO)
  val pinSink = shell { pinSource.makeSink }

  def overlayOutput = PMODOverlayOutput(pin = InModuleBody { pinSource.bundle } )

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
