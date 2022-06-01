package sifive.fpgashells.shell.xilinx

import chisel3._
import freechips.rocketchip.diplomacy._
import sifive.fpgashells.shell._
import sifive.fpgashells.ip.xilinx._

abstract class PMODXilinxPlacedOverlay(name: String, di: PMODDesignInput, si: PMODShellInput, boardPin: Seq[String] = Seq(), packagePin: Seq[String] = Seq(), ioStandard: String = "LVCMOS33")
  extends PMODPlacedOverlay(name, di, si)
{
  def shell: XilinxShell

  shell { InModuleBody {
    require((boardPin.isEmpty || packagePin.isEmpty), "can't provide both boardpin and packagepin, this is ambiguous")
    val cutAt = boardPin.length
    val ios = IOPin.of(io)
    val boardIO = ios.take(cutAt)
    val packageIO = ios.drop(cutAt)

    (boardPin   zip boardIO)   foreach { case (pin, io) => shell.xdc.addBoardPin  (io, pin) }
    (packagePin zip packageIO) foreach { case (pin, io) =>
      shell.xdc.addPackagePin(io, pin)
      shell.xdc.addIOStandard(io, ioStandard)
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
