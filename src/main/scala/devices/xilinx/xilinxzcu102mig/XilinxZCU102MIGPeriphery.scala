package sifive.fpgashells.devices.xilinx.xilinxzcu102mig

import chisel3._
import org.chipsalliance.cde.config._
import freechips.rocketchip.subsystem.{BaseSubsystem, MBUS}
import freechips.rocketchip.diplomacy.{LazyModule, LazyModuleImp, AddressRange}
import freechips.rocketchip.tilelink.{TLWidthWidget}
case object MemoryXilinxDDRKey extends Field[XilinxZCU102MIGParams]

trait HasMemoryXilinxZCU102MIG { this: BaseSubsystem =>
  val module: HasMemoryXilinxZCU102MIGModuleImp

  val xilinxzcu102mig = LazyModule(new XilinxZCU102MIG(p(MemoryXilinxDDRKey)))

  private val mbus = locateTLBusWrapper(MBUS)
  mbus.coupleTo("xilinxzcu102mig") { xilinxzcu102mig.node := TLWidthWidget(mbus.beatBytes) := _ }
}

trait HasMemoryXilinxZCU102MIGBundle {
  val xilinxzcu102mig: XilinxZCU102MIGIO
  def connectXilinxZCU102MIGToPads(pads: XilinxZCU102MIGPads): Unit = {
    pads <> xilinxzcu102mig
  }
}

trait HasMemoryXilinxZCU102MIGModuleImp extends LazyModuleImp
    with HasMemoryXilinxZCU102MIGBundle {
  val outer: HasMemoryXilinxZCU102MIG
  val ranges = AddressRange.fromSets(p(MemoryXilinxDDRKey).address)
  require (ranges.size == 1, "DDR range must be contiguous")
  val depth = ranges.head.size
  val xilinxzcu102mig = IO(new XilinxZCU102MIGIO(depth))

  xilinxzcu102mig <> outer.xilinxzcu102mig.module.io.port
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
