package sifive.fpgashells.shell.xilinx

import chisel3._
import freechips.rocketchip.diplomacy._
import sifive.fpgashells.shell._
import sifive.fpgashells.ip.xilinx._

abstract class SDIOXilinxPlacedOverlay(name: String, di: SPIDesignInput, si: SPIShellInput)
  extends SPIPlacedOverlay(name, di, si)
{
  def shell: XilinxShell

  InModuleBody {
    val tlspiport = tlspiSink.bundle
    spiSource.bundle.sck := tlspiport.sck
    spiSource.bundle.dq.zip(tlspiport.dq).foreach { case(outerBundle, innerBundle) =>
      outerBundle.o := innerBundle.o
      outerBundle.oe := innerBundle.oe
      innerBundle.i := RegNext(RegNext(outerBundle.i))
    }
    spiSource.bundle.cs := tlspiport.cs
  }

  shell { InModuleBody {
    val sd_spi_sck = spiSink.bundle.sck
    val sd_spi_cs = spiSink.bundle.cs(0)

    val sd_spi_dq_i = Wire(Vec(4, Bool()))
    val sd_spi_dq_o = Wire(Vec(4, Bool()))

    spiSink.bundle.dq.zipWithIndex.foreach {
      case(pin, idx) =>
        sd_spi_dq_o(idx) := pin.o
        pin.i := sd_spi_dq_i(idx)
    }

    UIntToAnalog(sd_spi_sck, io.spi_clk, true.B)
    UIntToAnalog(sd_spi_cs, io.spi_dat(3), true.B)
    UIntToAnalog(sd_spi_dq_o(0), io.spi_cs, true.B)
    sd_spi_dq_i := Seq(false.B, AnalogToUInt(io.spi_dat(0)).asBool, false.B, false.B)
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
