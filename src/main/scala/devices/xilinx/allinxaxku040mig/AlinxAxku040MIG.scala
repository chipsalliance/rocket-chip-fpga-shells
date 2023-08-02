package sifive.fpgashells.devices.xilinx.allinxaxku040mig

import chisel3._
import freechips.rocketchip.amba.axi4._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.subsystem.{CacheBlockBytes, CrossesToOnlyOneClockDomain}
import freechips.rocketchip.tilelink.{TLBuffer, TLInwardNode, TLToAXI4}
import org.chipsalliance.cde.config.Parameters
import sifive.fpgashells.ip.xilinx.alinx_axku040mig._

case class AlinxAxku040MIGParams(address: Seq[AddressSet])

class AlinxAxku040MIGAuxPads extends Bundle {
  val sysClockInput = Input(Clock())
  val sysResetInput = Input(Reset())
  val AXIaresetn = Input(Bool())
  val UIClock = Output(Clock())
  val UISyncedReset = Output(Reset())
  val MIGCalibComplete = Output(Bool())
}

class AlinxAxku040MIGIsland(c: AlinxAxku040MIGParams)(implicit p: Parameters)
    extends LazyModule
    with CrossesToOnlyOneClockDomain {
  private val ranges = AddressRange.fromSets(c.address)
  require(ranges.size == 1, "DDR range must be contiguous")
  private val offset = ranges.head.base

  override val crossing: ClockCrossingType = AsynchronousCrossing(8)

  val device = new MemoryDevice
  val node = AXI4SlaveNode(
    Seq(
      AXI4SlavePortParameters(
        slaves = Seq(
          AXI4SlaveParameters(
            address = c.address,
            resources = device.reg,
            regionType = RegionType.UNCACHED,
            executable = true,
            supportsWrite = TransferSizes(1, 256 * 8),
            supportsRead = TransferSizes(1, 256 * 8)
          )
        ),
        beatBytes = 8
      )
    )
  )
  lazy val module = new Impl

  class Impl extends LazyModuleImp(this) {

    val auxio = IO(new AlinxAxku040MIGAuxPads)
    val ddr4_port = IO(new AlinxAxku040MIGDDRPads)

    private val blackbox = Module(new axku040mig)
    private val (axi4_async, _) = node.in.head

    ddr4_port <> blackbox.c0_ddr4_intf

    blackbox.c0_sys_clk_i := auxio.sysClockInput
    blackbox.sys_rst := auxio.sysResetInput
    blackbox.c0_ddr4_aresetn := auxio.AXIaresetn
    auxio.UIClock := blackbox.c0_ddr4_ui_clk
    auxio.UISyncedReset := blackbox.c0_ddr4_ui_clk_sync_rst
    auxio.MIGCalibComplete := blackbox.c0_init_calib_complete

    val mapped_waddr = axi4_async.aw.bits.addr - offset.U
    val mapped_raddr = axi4_async.ar.bits.addr - offset.U

    blackbox.c0_ddr4_s_axi_awvalid := axi4_async.aw.valid
    axi4_async.aw.ready := blackbox.c0_ddr4_s_axi_awready
    blackbox.c0_ddr4_s_axi_awid := axi4_async.aw.bits.id
    blackbox.c0_ddr4_s_axi_awburst := axi4_async.aw.bits.burst
    blackbox.c0_ddr4_s_axi_awprot := axi4_async.aw.bits.prot
    blackbox.c0_ddr4_s_axi_awlock := axi4_async.aw.bits.lock
    blackbox.c0_ddr4_s_axi_awlen := axi4_async.aw.bits.len
    blackbox.c0_ddr4_s_axi_awsize := axi4_async.aw.bits.size
    blackbox.c0_ddr4_s_axi_awqos := axi4_async.aw.bits.qos
    blackbox.c0_ddr4_s_axi_awcache := "b0011".U
    blackbox.c0_ddr4_s_axi_awaddr := mapped_waddr

    blackbox.c0_ddr4_s_axi_wvalid := axi4_async.w.valid
    axi4_async.w.ready := blackbox.c0_ddr4_s_axi_wready
    blackbox.c0_ddr4_s_axi_wdata := axi4_async.w.bits.data
    blackbox.c0_ddr4_s_axi_wstrb := axi4_async.w.bits.strb
    blackbox.c0_ddr4_s_axi_wlast := axi4_async.w.bits.last

    blackbox.c0_ddr4_s_axi_bready := axi4_async.b.ready
    axi4_async.b.valid := blackbox.c0_ddr4_s_axi_bvalid
    axi4_async.b.bits.id := blackbox.c0_ddr4_s_axi_bid
    axi4_async.b.bits.resp := blackbox.c0_ddr4_s_axi_bresp

    blackbox.c0_ddr4_s_axi_arvalid := axi4_async.ar.valid
    axi4_async.ar.ready := blackbox.c0_ddr4_s_axi_arready
    blackbox.c0_ddr4_s_axi_arid := axi4_async.ar.bits.id
    blackbox.c0_ddr4_s_axi_arburst := axi4_async.ar.bits.burst
    blackbox.c0_ddr4_s_axi_arprot := axi4_async.ar.bits.prot
    blackbox.c0_ddr4_s_axi_arlock := axi4_async.ar.bits.lock
    blackbox.c0_ddr4_s_axi_arlen := axi4_async.ar.bits.len
    blackbox.c0_ddr4_s_axi_arsize := axi4_async.ar.bits.size
    blackbox.c0_ddr4_s_axi_arqos := axi4_async.ar.bits.qos
    blackbox.c0_ddr4_s_axi_arcache := "b0011".U
    blackbox.c0_ddr4_s_axi_araddr := mapped_raddr

    blackbox.c0_ddr4_s_axi_rready := axi4_async.r.ready
    axi4_async.r.valid := blackbox.c0_ddr4_s_axi_rvalid
    axi4_async.r.bits.data := blackbox.c0_ddr4_s_axi_rdata
    axi4_async.r.bits.resp := blackbox.c0_ddr4_s_axi_rresp
    axi4_async.r.bits.last := blackbox.c0_ddr4_s_axi_rlast
    axi4_async.r.bits.id := blackbox.c0_ddr4_s_axi_rid
  }
}

class AlinxAxku040MIG(c: AlinxAxku040MIGParams)(implicit p: Parameters) extends LazyModule {
  private val buffer = LazyModule(new TLBuffer())
  private val toaxi4 = LazyModule(new TLToAXI4(adapterName = Some("mem")))
  private val indexer = LazyModule(new AXI4IdIndexer(idBits = 4))
  private val deint = LazyModule(new AXI4Deinterleaver(p(CacheBlockBytes)))
  private val yank = LazyModule(new AXI4UserYanker())
  private val island = LazyModule(new AlinxAxku040MIGIsland(c))

  val node: TLInwardNode = buffer.node
  island.crossAXI4In(island.node) := yank.node := deint.node := indexer.node := toaxi4.node := buffer.node

  lazy val module = new Impl

  class Impl extends LazyModuleImp(this) {
    val auxio = IO(new AlinxAxku040MIGAuxPads)
    val ddr4_port = IO(new AlinxAxku040MIGDDRPads).suggestName("c0_ddr4")

    auxio <> island.module.auxio
    ddr4_port <> island.module.ddr4_port

    island.module.clock := auxio.UIClock
    island.module.reset := auxio.UISyncedReset
  }
}
