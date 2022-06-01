package sifive.fpgashells.shell

import chisel3._
import chisel3.experimental.Analog
import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.util._
import sifive.blocks.devices.spi._
import freechips.rocketchip.tilelink.TLBusWrapper
import freechips.rocketchip.interrupts.IntInwardNode

//This should not do the controller placement either
case class SPIShellInput()
case class SPIDesignInput(spiParam: SPIParams, node: BundleBridgeSource[SPIPortIO])(implicit val p: Parameters)
case class SPIOverlayOutput()
case object SPIOverlayKey extends Field[Seq[DesignPlacer[SPIDesignInput, SPIShellInput, SPIOverlayOutput]]](Nil)
trait SPIShellPlacer[Shell] extends ShellPlacer[SPIDesignInput, SPIShellInput, SPIOverlayOutput]

// SPI Port. Not sure how generic this is, it might need to move.
class ShellSPIPortIO extends Bundle {
  val spi_clk = Analog(1.W)
  val spi_cs = Analog(1.W)
  val spi_dat = Vec(4, Analog(1.W))
}

abstract class SPIPlacedOverlay(
  val name: String, val di: SPIDesignInput, val si: SPIShellInput)
    extends IOPlacedOverlay[ShellSPIPortIO, SPIDesignInput, SPIShellInput, SPIOverlayOutput]
{
  implicit val p = di.p

  def ioFactory = new ShellSPIPortIO
  val tlspiSink = di.node.makeSink

  val spiSource = BundleBridgeSource(() => new SPIPortIO(di.spiParam))
  val spiSink = sinkScope { spiSource.makeSink }
  def overlayOutput = SPIOverlayOutput()

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
