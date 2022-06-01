package sifive.fpgashells.shell.xilinx

import chisel3._
import chisel3.util.Cat
import freechips.rocketchip.diplomacy._
import sifive.fpgashells.shell._
import sifive.fpgashells.ip.xilinx._

abstract class SPIFlashXilinxPlacedOverlay(name: String, di: SPIFlashDesignInput, si: SPIFlashShellInput)
  extends SPIFlashPlacedOverlay(name, di, si)
{
  def shell: XilinxShell

  //val dqiVec = VecInit.tabulate(4)(j =>tlqspiSink.bundle.dq(j))
  shell { InModuleBody {
    if (!si.vcu118SU) {
      UIntToAnalog(tlqspiSink.bundle.sck  , io.qspi_sck, true.B)
      UIntToAnalog(tlqspiSink.bundle.cs(0), io.qspi_cs , true.B)

      tlqspiSink.bundle.dq.zip(io.qspi_dq).foreach { case(design_dq, io_dq) => 
        UIntToAnalog(design_dq.o, io_dq, design_dq.oe)
        design_dq.i := AnalogToUInt(io_dq)
      }
    } else {
      // If on vcu118, to communicate with Flash, STARTUPE3 primitive needs to be connected and hooked uo tp 
      // spi, rather than a top level connection
      val se3 = Module(new STARTUPE3())
      se3.io.USRDONEO   := true.B
      se3.io.USRDONETS  := true.B
      se3.io.USRCCLKO   := tlqspiSink.bundle.sck.asClock
      se3.io.USRCCLKTS  := false.B
      se3.io.FCSBO      := tlqspiSink.bundle.cs(0)
      se3.io.FCSBTS     := false.B
      se3.io.DO         := Cat(tlqspiSink.bundle.dq.map(_.o))
      se3.io.DTS        := Cat(tlqspiSink.bundle.dq.map(_.oe))
      tlqspiSink.bundle.dq(0).i            := se3.io.DI(0)
      tlqspiSink.bundle.dq(1).i            := se3.io.DI(1)
      tlqspiSink.bundle.dq(2).i            := se3.io.DI(2)
      tlqspiSink.bundle.dq(3).i            := se3.io.DI(3)
      se3.io.GSR        := false.B
      se3.io.GTS        := false.B
      se3.io.KEYCLEARB  := false.B
      se3.io.PACK       := false.B
    }
  } }
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
