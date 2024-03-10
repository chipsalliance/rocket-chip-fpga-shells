package sifive.fpgashells.devices.xilinx.xilinxvcu118mig

import freechips.rocketchip.diplomacy.{AddressRange, LazyModule, LazyModuleImp}
import freechips.rocketchip.subsystem.{BaseSubsystem, MBUS}
import freechips.rocketchip.tilelink.TLWidthWidget
import org.chipsalliance.cde.config._

case object MemoryXilinxDDRKey extends Field[XilinxVCU118MIGParams]

trait HasMemoryXilinxVCU118MIG { this: BaseSubsystem =>
  val module: HasMemoryXilinxVCU118MIGModuleImp

  val xilinxvcu118mig = LazyModule(new XilinxVCU118MIG(p(MemoryXilinxDDRKey)))

  private val mbus = locateTLBusWrapper(MBUS)
  mbus.coupleTo("xilinxvcu118mig") { xilinxvcu118mig.node := TLWidthWidget(mbus.beatBytes) := _ }
}

trait HasMemoryXilinxVCU118MIGBundle {
  val xilinxvcu118mig: XilinxVCU118MIGIO
  def connectXilinxVCU118MIGToPads(pads: XilinxVCU118MIGPads) {
    pads <> xilinxvcu118mig
  }
}

trait HasMemoryXilinxVCU118MIGModuleImp extends LazyModuleImp
    with HasMemoryXilinxVCU118MIGBundle {
  val outer: HasMemoryXilinxVCU118MIG
  val ranges = AddressRange.fromSets(p(MemoryXilinxDDRKey).address)
  require (ranges.size == 1, "DDR range must be contiguous")
  val depth = ranges.head.size
  val xilinxvcu118mig = IO(new XilinxVCU118MIGIO(depth))

  xilinxvcu118mig <> outer.xilinxvcu118mig.module.io.port
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
