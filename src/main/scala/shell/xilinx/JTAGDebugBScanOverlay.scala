package sifive.fpgashells.shell.xilinx

import chisel3._
import freechips.rocketchip.diplomacy._
import sifive.fpgashells.shell._
import sifive.fpgashells.ip.xilinx.bscan2._

abstract class JTAGDebugBScanXilinxPlacedOverlay(name: String, di: JTAGDebugBScanDesignInput, si: JTAGDebugBScanShellInput)
  extends JTAGDebugBScanPlacedOverlay(name, di, si)
{
  def shell: XilinxShell

  shell { InModuleBody {
    val tmp_tck = Wire(Bool())
    val tmp_tms = Wire(Bool())
    val tmp_tdi = Wire(Bool())
    val tmp_tdo = Wire(Bool())
    val tmp_tdo_en = Wire(Bool())

    JTAGTUNNEL(tmp_tck, tmp_tms, tmp_tdi, tmp_tdo, tmp_tdo_en)

    jtagDebugSink.bundle.TCK := tmp_tck.asClock
    jtagDebugSink.bundle.TMS := tmp_tms
    jtagDebugSink.bundle.TDI := tmp_tdi
    tmp_tdo := jtagDebugSink.bundle.TDO.data
    tmp_tdo_en := jtagDebugSink.bundle.TDO.driven
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
