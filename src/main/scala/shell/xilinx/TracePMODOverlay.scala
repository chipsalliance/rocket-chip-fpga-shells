package sifive.fpgashells.shell.xilinx

import chisel3._
import freechips.rocketchip.diplomacy._
import sifive.fpgashells.shell._
import sifive.fpgashells.ip.xilinx._

abstract class TracePMODXilinxPlacedOverlay(name: String, di: TracePMODDesignInput, si: TracePMODShellInput, boardPins: Seq[String] = Nil, packagePins: Seq[String] = Nil, ioStandard: String = "LVCMOS33")
  extends TracePMODPlacedOverlay(name, di, si)
{
  def shell: XilinxShell
  val width = boardPins.size + packagePins.size

  shell { InModuleBody {
    io := pmodTraceSink.bundle

    val cutAt = boardPins.size
    val ios = IOPin.of(io)
    val boardIOs = ios.take(cutAt)
    val packageIOs = ios.drop(cutAt)

    (boardPins   zip boardIOs)   foreach { case (pin, io) => shell.xdc.addBoardPin  (io, pin) }
    (packagePins zip packageIOs) foreach { case (pin, io) => 
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
