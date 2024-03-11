package sifive.fpgashells.devices.xilinx.xilinxvc707mig

import freechips.rocketchip.diplomacy.{AddressRange, LazyModule, LazyModuleImp}
import freechips.rocketchip.subsystem.{BaseSubsystem, MBUS}
import freechips.rocketchip.tilelink.TLWidthWidget
import org.chipsalliance.cde.config._

case object MemoryXilinxDDRKey extends Field[XilinxVC707MIGParams]

trait HasMemoryXilinxVC707MIG { this: BaseSubsystem =>
  val module: HasMemoryXilinxVC707MIGModuleImp

  val xilinxvc707mig = LazyModule(new XilinxVC707MIG(p(MemoryXilinxDDRKey)))

  private val mbus = locateTLBusWrapper(MBUS)
  mbus.coupleTo("xilinxvc707mig") { xilinxvc707mig.node := TLWidthWidget(mbus.beatBytes) := _ }
}

trait HasMemoryXilinxVC707MIGBundle {
  val xilinxvc707mig: XilinxVC707MIGIO
  def connectXilinxVC707MIGToPads(pads: XilinxVC707MIGPads) {
    pads <> xilinxvc707mig
  }
}

trait HasMemoryXilinxVC707MIGModuleImp extends LazyModuleImp
    with HasMemoryXilinxVC707MIGBundle {
  val outer: HasMemoryXilinxVC707MIG
  val ranges = AddressRange.fromSets(p(MemoryXilinxDDRKey).address)
  require (ranges.size == 1, "DDR range must be contiguous")
  val depth = ranges.head.size
  val xilinxvc707mig = IO(new XilinxVC707MIGIO(depth))

  xilinxvc707mig <> outer.xilinxvc707mig.module.io.port
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
