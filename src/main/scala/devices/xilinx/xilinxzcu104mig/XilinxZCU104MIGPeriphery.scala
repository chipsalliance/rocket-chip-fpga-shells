package sifive.fpgashells.devices.xilinx.xilinxzcu104mig

import freechips.rocketchip.diplomacy.AddressRange
import freechips.rocketchip.subsystem.{BaseSubsystem, MBUS}
import freechips.rocketchip.tilelink.TLWidthWidget
import org.chipsalliance.cde.config._
import org.chipsalliance.diplomacy.lazymodule.{LazyModule, LazyModuleImp}
case object MemoryXilinxDDRKey extends Field[XilinxZCU104MIGParams]

trait HasMemoryXilinxZCU104MIG { this: BaseSubsystem =>
  val module: HasMemoryXilinxZCU104MIGModuleImp

  val xilinxzcu104mig = LazyModule(new XilinxZCU104MIG(p(MemoryXilinxDDRKey)))

  private val mbus = locateTLBusWrapper(MBUS)
  mbus.coupleTo("xilinxzcu104mig") { xilinxzcu104mig.node := TLWidthWidget(mbus.beatBytes) := _ }
}

trait HasMemoryXilinxZCU104MIGBundle {
  val xilinxzcu104mig: XilinxZCU104MIGIO
  def connectXilinxZCU104MIGToPads(pads: XilinxZCU104MIGPads): Unit = {
    pads <> xilinxzcu104mig
  }
}

trait HasMemoryXilinxZCU104MIGModuleImp extends LazyModuleImp
    with HasMemoryXilinxZCU104MIGBundle {
  val outer: HasMemoryXilinxZCU104MIG
  val ranges = AddressRange.fromSets(p(MemoryXilinxDDRKey).address)
  require (ranges.size == 1, "DDR range must be contiguous")
  val depth = ranges.head.size
  val xilinxzcu104mig = IO(new XilinxZCU104MIGIO(depth))

  xilinxzcu104mig <> outer.xilinxzcu104mig.module.io.port
}
