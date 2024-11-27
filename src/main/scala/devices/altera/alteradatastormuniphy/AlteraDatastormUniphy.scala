package sifive.fpgashells.devices.altera.altera_datastorm_uniphy

import chisel3._
import chisel3.experimental.attach
import freechips.rocketchip.amba.axi4._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.prci._
import freechips.rocketchip.subsystem._
import freechips.rocketchip.tilelink._
import org.chipsalliance.cde.config.Parameters
import sifive.fpgashells.ip.altera.datastorm_uniphy.{DatastormUniphyIOClocksReset, DatastormUniphyIODDR, datastorm_uniphy}

case class AlteraDatastormUniphyParams(
  address : Seq[AddressSet]
)

class AlteraDatastormUniphyPads(depth : BigInt) extends DatastormUniphyIODDR(depth) {
  def this(c : AlteraDatastormUniphyParams) {
    this(AddressRange.fromSets(c.address).head.size)
  }
}

class AlteraDatastormUniphyIO(depth : BigInt) extends DatastormUniphyIODDR(depth) {
  //pll refclk
  val ddr_fpga_pll_ref_clk_clk = Input(Clock())
  //mem calibration signals
  val mem_status_local_init_done         = Output(Bool())
  val mem_status_local_cal_success       = Output(Bool())
  val mem_status_local_cal_fail          = Output(Bool())

}

class AlteraDatastormUniphyIsland(c : AlteraDatastormUniphyParams)(implicit p: Parameters) extends LazyModule {
  val ranges = AddressRange.fromSets(c.address)
  require (ranges.size == 1, "DDR range must be contiguous")
  val offset = ranges.head.base
  val depth = ranges.head.size
  require((depth<=0x40000000L),"datastorm_uniphy supports upto 1 GB depth configuraton")
  
  val device = new MemoryDevice
  val node = AXI4SlaveNode(Seq(AXI4SlavePortParameters(
      slaves = Seq(AXI4SlaveParameters(
      address       = c.address,
      resources     = device.reg,
      regionType    = RegionType.UNCACHED,
      executable    = true,
      supportsWrite = TransferSizes(1, 64),
      supportsRead  = TransferSizes(1, 64))),
    beatBytes = 8)))

  lazy val module = new Impl
  class Impl extends LazyModuleImp(this) {
    val io = IO(new Bundle {
      val port = new AlteraDatastormUniphyIO(depth)
    })

    //MIG black box instantiation
    val blackbox =  Module(new datastorm_uniphy(depth))
    val (axi_async, _) = node.in(0)

    //pins to top level

    //inouts
    attach(io.port.ddr3_mem_dq, blackbox.io.ddr3_mem_dq)
    attach(io.port.ddr3_mem_dqs_n,blackbox.io.ddr3_mem_dqs_n)
    attach(io.port.ddr3_mem_dqs,blackbox.io.ddr3_mem_dqs)

    //outputs
    io.port.ddr3_mem_a            := blackbox.io.ddr3_mem_a
    io.port.ddr3_mem_ba           := blackbox.io.ddr3_mem_ba
    io.port.ddr3_mem_ras_n        := blackbox.io.ddr3_mem_ras_n
    io.port.ddr3_mem_cas_n        := blackbox.io.ddr3_mem_cas_n
    io.port.ddr3_mem_we_n         := blackbox.io.ddr3_mem_we_n
    io.port.ddr3_mem_reset_n      := blackbox.io.ddr3_mem_reset_n
    io.port.ddr3_mem_ck           := blackbox.io.ddr3_mem_ck
    io.port.ddr3_mem_ck_n         := blackbox.io.ddr3_mem_ck_n
    io.port.ddr3_mem_cke          := blackbox.io.ddr3_mem_cke
    io.port.ddr3_mem_cs_n         := blackbox.io.ddr3_mem_cs_n
    io.port.ddr3_mem_dm           := blackbox.io.ddr3_mem_dm
    io.port.ddr3_mem_odt          := blackbox.io.ddr3_mem_odt

    blackbox.io.oct_rzqin         := io.port.oct_rzqin

    //inputs
    //NO_BUFFER clock
    blackbox.io.clk_clk     := clock
    blackbox.io.ddr_fpga_pll_ref_clk_clk     := io.port.ddr_fpga_pll_ref_clk_clk

    io.port.mem_status_local_init_done     := blackbox.io.mem_status_local_init_done  
    io.port.mem_status_local_cal_success   := blackbox.io.mem_status_local_cal_success
    io.port.mem_status_local_cal_fail      := blackbox.io.mem_status_local_cal_fail   

    val awaddr = axi_async.aw.bits.addr - offset.U
    val araddr = axi_async.ar.bits.addr - offset.U

    //slave AXI interface write address ports
    blackbox.io.s_axi_awid    := axi_async.aw.bits.id
    blackbox.io.s_axi_awaddr  := awaddr //truncated
    blackbox.io.s_axi_awlen   := axi_async.aw.bits.len
    blackbox.io.s_axi_awsize  := axi_async.aw.bits.size
    blackbox.io.s_axi_awburst := axi_async.aw.bits.burst
    blackbox.io.s_axi_awlock  := axi_async.aw.bits.lock
    blackbox.io.s_axi_awcache := "b0011".U
    blackbox.io.s_axi_awprot  := axi_async.aw.bits.prot
    blackbox.io.s_axi_awqos   := axi_async.aw.bits.qos
    blackbox.io.s_axi_awvalid := axi_async.aw.valid
    axi_async.aw.ready        := blackbox.io.s_axi_awready

    //slave interface write data ports
    blackbox.io.s_axi_wdata   := axi_async.w.bits.data
    blackbox.io.s_axi_wstrb   := axi_async.w.bits.strb
    blackbox.io.s_axi_wlast   := axi_async.w.bits.last
    blackbox.io.s_axi_wvalid  := axi_async.w.valid
    axi_async.w.ready         := blackbox.io.s_axi_wready

    //slave interface write response
    blackbox.io.s_axi_bready  := axi_async.b.ready
    axi_async.b.bits.id       := blackbox.io.s_axi_bid
    axi_async.b.bits.resp     := blackbox.io.s_axi_bresp
    axi_async.b.valid         := blackbox.io.s_axi_bvalid

    //slave AXI interface read address ports
    blackbox.io.s_axi_arid    := axi_async.ar.bits.id
    blackbox.io.s_axi_araddr  := araddr // truncated
    blackbox.io.s_axi_arlen   := axi_async.ar.bits.len
    blackbox.io.s_axi_arsize  := axi_async.ar.bits.size
    blackbox.io.s_axi_arburst := axi_async.ar.bits.burst
    blackbox.io.s_axi_arlock  := axi_async.ar.bits.lock
    blackbox.io.s_axi_arcache := "b0011".U
    blackbox.io.s_axi_arprot  := axi_async.ar.bits.prot
    blackbox.io.s_axi_arqos   := axi_async.ar.bits.qos
    blackbox.io.s_axi_arvalid := axi_async.ar.valid
    axi_async.ar.ready        := blackbox.io.s_axi_arready

    //slace AXI interface read data ports
    blackbox.io.s_axi_rready  := axi_async.r.ready
    axi_async.r.bits.id       := blackbox.io.s_axi_rid
    axi_async.r.bits.data     := blackbox.io.s_axi_rdata
    axi_async.r.bits.resp     := blackbox.io.s_axi_rresp
    axi_async.r.bits.last     := blackbox.io.s_axi_rlast
    axi_async.r.valid         := blackbox.io.s_axi_rvalid

    //misc
    blackbox.io.reset_reset_n   := !reset.asBool
  }
}

class AlteraDatastormUniphy(c : AlteraDatastormUniphyParams)(implicit p: Parameters) extends LazyModule {
  val ranges = AddressRange.fromSets(c.address)
  val depth = ranges.head.size

  val buffer  = LazyModule(new TLBuffer)
  val toaxi4  = LazyModule(new TLToAXI4(adapterName = Some("mem")))
  val indexer = LazyModule(new AXI4IdIndexer(idBits = 4))
  val deint   = LazyModule(new AXI4Deinterleaver(p(CacheBlockBytes)))
  val yank    = LazyModule(new AXI4UserYanker)
  val island  = LazyModule(new AlteraDatastormUniphyIsland(c))

  val node: TLInwardNode =
    island.node := yank.node := deint.node := indexer.node := toaxi4.node := buffer.node

  lazy val module = new Impl
  class Impl extends LazyModuleImp(this) {
    val io = IO(new Bundle {
      val port = new AlteraDatastormUniphyIO(depth)
    })

    io.port <> island.module.io.port
  }
}
