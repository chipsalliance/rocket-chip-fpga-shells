package sifive.fpgashells.ip.xilinx.zcu104mig

import chisel3._
import chisel3.experimental.Analog
import freechips.rocketchip.util.ElaborationArtefacts
import org.chipsalliance.cde.config._

class ZCU104MIGIODDR(depth: BigInt) extends Bundle {
  require(depth <= 0x100000000L, "ZCU104MIGIODDR supports up to 4 GiB depth configuration.")
  val c0_ddr4_adr           = Output(Bits(17.W))
  val c0_ddr4_bg            = Output(Bits(2.W))
  val c0_ddr4_ba            = Output(Bits(2.W))
  val c0_ddr4_reset_n       = Output(Bool())
  val c0_ddr4_act_n         = Output(Bool())
  val c0_ddr4_ck_c          = Output(Bits(1.W))
  val c0_ddr4_ck_t          = Output(Bits(1.W))
  val c0_ddr4_cke           = Output(Bits(1.W))
  val c0_ddr4_cs_n          = Output(Bits(1.W))
  val c0_ddr4_odt           = Output(Bits(1.W))

  val c0_ddr4_dq            = Analog(64.W)
  val c0_ddr4_dqs_c         = Analog( 8.W)
  val c0_ddr4_dqs_t         = Analog( 8.W)
  val c0_ddr4_dm_dbi_n      = Analog( 8.W)
}

//reused directly in io bundle for sifive.blocks.devices.xilinxzcu104mig
trait ZCU104MIGIOClocksReset extends Bundle {
  //inputs
  //"NO_BUFFER" clock source (must be connected to IBUF outside of IP)
  val c0_sys_clk_i              = Input(Bool())
  //user interface signals
  val c0_ddr4_ui_clk            = Output(Clock())
  val c0_ddr4_ui_clk_sync_rst   = Output(Bool())
  val c0_ddr4_aresetn           = Input(Bool())
  //misc
  val c0_init_calib_complete    = Output(Bool())
  val sys_rst                   = Input(Bool())
}

class zcu104mig(depth: BigInt) (implicit val p: Parameters) extends BlackBox {
  require(depth <= 0x100000000L, "zcu104mig supports up to 4 GiB depth configuration.")

  val io = IO(new ZCU104MIGIODDR(depth) with ZCU104MIGIOClocksReset {
    //slave interface write address ports
    val c0_ddr4_s_axi_awid            = Input(Bits(4.W))
    val c0_ddr4_s_axi_awaddr          = Input(Bits(32.W))
    val c0_ddr4_s_axi_awlen           = Input(Bits(8.W))
    val c0_ddr4_s_axi_awsize          = Input(Bits(3.W))
    val c0_ddr4_s_axi_awburst         = Input(Bits(2.W))
    val c0_ddr4_s_axi_awlock          = Input(Bits(1.W))
    val c0_ddr4_s_axi_awcache         = Input(Bits(4.W))
    val c0_ddr4_s_axi_awprot          = Input(Bits(3.W))
    val c0_ddr4_s_axi_awqos           = Input(Bits(4.W))
    val c0_ddr4_s_axi_awvalid         = Input(Bool())
    val c0_ddr4_s_axi_awready         = Output(Bool())
    //slave interface write data ports
    val c0_ddr4_s_axi_wdata           = Input(Bits(64.W))
    val c0_ddr4_s_axi_wstrb           = Input(Bits(8.W))
    val c0_ddr4_s_axi_wlast           = Input(Bool())
    val c0_ddr4_s_axi_wvalid          = Input(Bool())
    val c0_ddr4_s_axi_wready          = Output(Bool())
    //slave interface write response ports
    val c0_ddr4_s_axi_bready          = Input(Bool())
    val c0_ddr4_s_axi_bid             = Output(Bits(4.W))
    val c0_ddr4_s_axi_bresp           = Output(Bits(2.W))
    val c0_ddr4_s_axi_bvalid          = Output(Bool())
    //slave interface read address ports
    val c0_ddr4_s_axi_arid            = Input(Bits(4.W))
    val c0_ddr4_s_axi_araddr          = Input(Bits(32.W))
    val c0_ddr4_s_axi_arlen           = Input(Bits(8.W))
    val c0_ddr4_s_axi_arsize          = Input(Bits(3.W))
    val c0_ddr4_s_axi_arburst         = Input(Bits(2.W))
    val c0_ddr4_s_axi_arlock          = Input(Bits(1.W))
    val c0_ddr4_s_axi_arcache         = Input(Bits(4.W))
    val c0_ddr4_s_axi_arprot          = Input(Bits(3.W))
    val c0_ddr4_s_axi_arqos           = Input(Bits(4.W))
    val c0_ddr4_s_axi_arvalid         = Input(Bool())
    val c0_ddr4_s_axi_arready         = Output(Bool())
    //slave interface read data ports
    val c0_ddr4_s_axi_rready          = Input(Bool())
    val c0_ddr4_s_axi_rid             = Output(Bits(4.W))
    val c0_ddr4_s_axi_rdata           = Output(Bits(64.W))
    val c0_ddr4_s_axi_rresp           = Output(Bits(2.W))
    val c0_ddr4_s_axi_rlast           = Output(Bool())
    val c0_ddr4_s_axi_rvalid          = Output(Bool())
  })

  ElaborationArtefacts.add(
    "zcu104mig.vivado.tcl",
    """ 
      create_ip -vendor xilinx.com -library ip -version 2.2 -name ddr4 -module_name zcu104mig -dir $ipdir -force
      set_property -dict [list \
      CONFIG.ADDN_UI_CLKOUT1_FREQ_HZ {None} \
      CONFIG.C0.DDR4_AxiAddressWidth {32} \
      CONFIG.C0.DDR4_AxiDataWidth {64} \
      CONFIG.C0.DDR4_AxiIDWidth {4} \
      CONFIG.C0.DDR4_AxiNarrowBurst {false} \
      CONFIG.C0.DDR4_AxiSelection {true} \
      CONFIG.C0.DDR4_InputClockPeriod {3335} \
      CONFIG.C0.DDR4_MemoryType {SODIMMs} \
      CONFIG.C0.DDR4_TimePeriod {938} \
      CONFIG.C0_DDR4_BOARD_INTERFACE {ddr4_sdram} \
      CONFIG.System_Clock {No_Buffer} \
      ] [get_ips zcu104mig]"""
  )
}
