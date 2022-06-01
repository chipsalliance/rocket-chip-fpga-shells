package sifive.fpgashells.shell.xilinx

 import chisel3._
import freechips.rocketchip.diplomacy._
import sifive.fpgashells.shell._
import sifive.fpgashells.ip.xilinx._

 abstract class I2CXilinxPlacedOverlay(name: String, di: I2CDesignInput, si: I2CShellInput)
  extends I2CPlacedOverlay(name, di, si)
{
  def shell: XilinxShell

  shell { InModuleBody {
    UIntToAnalog(tli2cSink.bundle.scl.out, io.scl, tli2cSink.bundle.scl.oe)
    UIntToAnalog(tli2cSink.bundle.sda.out, io.sda, tli2cSink.bundle.sda.oe)

    tli2cSink.bundle.scl.in := AnalogToUInt(io.scl)
    tli2cSink.bundle.sda.in := AnalogToUInt(io.sda)
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
