package sifive.fpgashells.shell.xilinx

import chisel3._
import freechips.rocketchip.diplomacy._
import sifive.fpgashells.shell._
import sifive.fpgashells.ip.xilinx._

abstract class JTAGDebugXilinxPlacedOverlay(name: String, di: JTAGDebugDesignInput, si: JTAGDebugShellInput)
  extends JTAGDebugPlacedOverlay(name, di, si)
{
  def shell: XilinxShell

  shell { InModuleBody {
    jtagDebugSink.bundle.TCK := AnalogToUInt(io.jtag_TCK).asBool.asClock
    jtagDebugSink.bundle.TMS := AnalogToUInt(io.jtag_TMS)
    jtagDebugSink.bundle.TDI := AnalogToUInt(io.jtag_TDI)
    UIntToAnalog(jtagDebugSink.bundle.TDO.data,io.jtag_TDO,jtagDebugSink.bundle.TDO.driven)
    jtagDebugSink.bundle.srst_n := AnalogToUInt(io.srst_n)
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
