package sifive.fpgashells.ip.xilinx.alinx_axku040mig

import chisel3._
import chisel3.experimental.{Analog, ExtModule}
import freechips.rocketchip.util.ElaborationArtefacts
import org.chipsalliance.cde.config.Parameters

// format: off
class AlinxAxku040MIGDDRPads extends Bundle {
  // Ordering of fields is significant as it needs to line up with pins in AlinxAxku040Shell
  val ck_c: UInt       = Output(UInt(1.W))
  val ck_t: UInt       = Output(UInt(1.W))
  val cke: UInt        = Output(UInt(1.W))
  val cs_n: UInt       = Output(UInt(1.W))
  val act_n: Bool      = Output(Bool())
  val odt: UInt        = Output(UInt(1.W))
  val adr: UInt        = Output(UInt(17.W))
  val ba: UInt         = Output(UInt(2.W))
  val bg: UInt         = Output(UInt(1.W))
  val reset_n: Bool    = Output(Bool())
  val dqs_c: Analog    = Analog(8.W)
  val dqs_t: Analog    = Analog(8.W)
  val dq: Analog       = Analog(64.W)
  val dm_dbi_n: Analog = Analog(8.W)
}

class axku040mig(implicit val p: Parameters) extends ExtModule {
  // clock and reset
  val c0_sys_clk_i: Clock             = IO(Input(Clock()))
  val sys_rst: Reset                  = IO(Input(Reset()))
  // slave ports driven by internal MMCM
  val c0_ddr4_ui_clk: Clock           = IO(Output(Clock()))
  val c0_ddr4_ui_clk_sync_rst: Reset  = IO(Output(Bool()))
  // misc
  val c0_init_calib_complete: Bool    = IO(Output(Bool()))
  // DDR4 interface
  val c0_ddr4_intf: Bundle            = IO(new AlinxAxku040MIGDDRPads).suggestName("c0_ddr4")
  // AXI interface
  val c0_ddr4_aresetn: Bool           = IO(Input(Bool()))
  // AXI write address port
  val c0_ddr4_s_axi_awid: UInt        = IO(Input(UInt(4.W)))
  val c0_ddr4_s_axi_awaddr: UInt      = IO(Input(UInt(31.W)))
  val c0_ddr4_s_axi_awlen: UInt       = IO(Input(UInt(8.W)))
  val c0_ddr4_s_axi_awsize: UInt      = IO(Input(UInt(3.W)))
  val c0_ddr4_s_axi_awburst: UInt     = IO(Input(UInt(2.W)))
  val c0_ddr4_s_axi_awlock: UInt      = IO(Input(UInt(1.W)))
  val c0_ddr4_s_axi_awcache: UInt     = IO(Input(UInt(4.W)))
  val c0_ddr4_s_axi_awprot: UInt      = IO(Input(UInt(3.W)))
  val c0_ddr4_s_axi_awqos: UInt       = IO(Input(UInt(4.W)))
  val c0_ddr4_s_axi_awvalid: Bool     = IO(Input(Bool()))
  val c0_ddr4_s_axi_awready: Bool     = IO(Output(Bool()))
  // AXI write data port
  val c0_ddr4_s_axi_wdata: UInt       = IO(Input(UInt(64.W)))
  val c0_ddr4_s_axi_wstrb: UInt       = IO(Input(UInt(8.W)))
  val c0_ddr4_s_axi_wlast: Bool       = IO(Input(Bool()))
  val c0_ddr4_s_axi_wvalid: Bool      = IO(Input(Bool()))
  val c0_ddr4_s_axi_wready: Bool      = IO(Output(Bool()))
  // AXI write response port
  val c0_ddr4_s_axi_bready: Bool      = IO(Input(Bool()))
  val c0_ddr4_s_axi_bvalid: Bool      = IO(Output(Bool()))
  val c0_ddr4_s_axi_bid: UInt         = IO(Output(UInt(4.W)))
  val c0_ddr4_s_axi_bresp: UInt       = IO(Output(UInt(2.W)))
  // AXI read address port
  val c0_ddr4_s_axi_arid: UInt        = IO(Input(UInt(4.W)))
  val c0_ddr4_s_axi_araddr: UInt      = IO(Input(UInt(31.W)))
  val c0_ddr4_s_axi_arlen: UInt       = IO(Input(UInt(8.W)))
  val c0_ddr4_s_axi_arsize: UInt      = IO(Input(UInt(3.W)))
  val c0_ddr4_s_axi_arburst: UInt     = IO(Input(UInt(2.W)))
  val c0_ddr4_s_axi_arlock: UInt      = IO(Input(UInt(1.W)))
  val c0_ddr4_s_axi_arcache: UInt     = IO(Input(UInt(4.W)))
  val c0_ddr4_s_axi_arprot: UInt      = IO(Input(UInt(3.W)))
  val c0_ddr4_s_axi_arqos: UInt       = IO(Input(UInt(4.W)))
  val c0_ddr4_s_axi_arvalid: Bool     = IO(Input(Bool()))
  val c0_ddr4_s_axi_arready: Bool     = IO(Output(Bool()))
  // AXI read data port
  val c0_ddr4_s_axi_rready: Bool      = IO(Input(Bool()))
  val c0_ddr4_s_axi_rvalid: Bool      = IO(Output(Bool()))
  val c0_ddr4_s_axi_rdata: UInt       = IO(Output(UInt(64.W)))
  val c0_ddr4_s_axi_rresp: UInt       = IO(Output(UInt(2.W)))
  val c0_ddr4_s_axi_rid: UInt         = IO(Output(UInt(4.W)))
  val c0_ddr4_s_axi_rlast: Bool       = IO(Output(Bool()))

  ElaborationArtefacts.add(
    "axku040mig.vivado.tcl",
    """create_ip -vendor xilinx.com -library ip -version 2.2 -name ddr4 -module_name axku040mig -dir $ipdir -force
      |set_property -dict [list \
      |CONFIG.C0.DDR4_MemoryPart {MT40A256M16LY-062E} \
      |CONFIG.C0.DDR4_DataWidth {64} \
      |CONFIG.C0.DDR4_AxiSelection {true} \
      |CONFIG.C0.DDR4_AxiDataWidth {64} \
      |CONFIG.C0.DDR4_AxiAddressWidth {31} \
      |CONFIG.C0.BANK_GROUP_WIDTH {1} \
      |CONFIG.C0.DDR4_InputClockPeriod {4998} \
      |CONFIG.System_Clock {No_Buffer} \
      |] [get_ips axku040mig]
      |""".stripMargin
  )
}
// format: on
