package sifive.fpgashells.shell

import chisel3._
import chisel3.experimental.Analog

import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.tilelink.TLBusWrapper
import freechips.rocketchip.interrupts.IntInwardNode


import sifive.blocks.devices.spi._

//This one does controller also
case class SPIFlashShellInput(index: Int = 0, vcu118SU: Boolean = false)
case class SPIFlashDesignInput(node: BundleBridgeSource[SPIPortIO])(implicit val p: Parameters)
case class SPIFlashOverlayOutput()
case object SPIFlashOverlayKey extends Field[Seq[DesignPlacer[SPIFlashDesignInput, SPIFlashShellInput, SPIFlashOverlayOutput]]](Nil)
trait SPIFlashShellPlacer[Shell] extends ShellPlacer[SPIFlashDesignInput, SPIFlashShellInput, SPIFlashOverlayOutput]


class ShellSPIFlashPortIO extends Bundle {
  val qspi_sck = Analog(1.W)
  val qspi_cs  = Analog(1.W)
  val qspi_dq  = Vec(4, Analog(1.W))
}

abstract class SPIFlashPlacedOverlay(
  val name: String, val di: SPIFlashDesignInput, val si: SPIFlashShellInput)
    extends IOPlacedOverlay[ShellSPIFlashPortIO, SPIFlashDesignInput, SPIFlashShellInput, SPIFlashOverlayOutput]
{
  implicit val p = di.p

  def ioFactory = new ShellSPIFlashPortIO

  val tlqspiSink = sinkScope { di.node.makeSink }

  def overlayOutput = SPIFlashOverlayOutput()
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
