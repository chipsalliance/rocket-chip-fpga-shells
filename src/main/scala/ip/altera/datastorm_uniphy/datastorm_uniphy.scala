package sifive.fpgashells.ip.altera.datastorm_uniphy

import chisel3._
import chisel3.experimental.Analog
import freechips.rocketchip.util.ElaborationArtefacts
import org.chipsalliance.cde.config._

// Black Box

class DatastormUniphyIODDR(depth : BigInt) extends Bundle {
  require((depth<=0x40000000L),"DatastormUniphyIODDR supports upto 1 GB depth configuraton")
  val ddr3_mem_a                = Output(Bits(15.W))
  val ddr3_mem_ba               = Output(Bits(3.W))
  val ddr3_mem_ras_n            = Output(Bool())
  val ddr3_mem_cas_n            = Output(Bool())
  val ddr3_mem_we_n             = Output(Bool())
  val ddr3_mem_reset_n          = Output(Bool())
  val ddr3_mem_ck               = Output(Bits(1.W))
  val ddr3_mem_ck_n             = Output(Bits(1.W))
  val ddr3_mem_cke              = Output(Bits(1.W))
  val ddr3_mem_cs_n             = Output(Bits(1.W))
  val ddr3_mem_dm               = Output(Bits(4.W))
  val ddr3_mem_odt              = Output(Bits(1.W))
  val oct_rzqin                 = Input(Bits(1.W))
  
  val ddr3_mem_dq               = Analog(32.W)
  val ddr3_mem_dqs_n            = Analog(4.W)
  val ddr3_mem_dqs              = Analog(4.W)
}

trait DatastormUniphyIOClocksReset extends Bundle {
  //inputs
  // Chip clock + pll clock
  val clk_clk                  = Input(Clock())
  val ddr_fpga_pll_ref_clk_clk = Input(Clock())
  
  //mem calibration signals
  val mem_status_local_init_done         = Output(Bool())
  val mem_status_local_cal_success       = Output(Bool())
  val mem_status_local_cal_fail          = Output(Bool())

  //misc
  val reset_reset_n         = Input(Bool())
}

//scalastyle:off
//turn off linter: blackbox name must match verilog module
class datastorm_uniphy(depth : BigInt)(implicit val p:Parameters) extends BlackBox
{
  require((depth<=0x40000000L),"datastorm_uniphy supports upto 1GB depth configuraton")

  val io = IO(new DatastormUniphyIODDR(depth) with DatastormUniphyIOClocksReset {
    //axi_s
    //slave interface write address ports
    val s_axi_awid            = Input(Bits(4.W))
    val s_axi_awaddr          = Input(Bits(32.W))
    val s_axi_awlen           = Input(Bits(8.W))
    val s_axi_awsize          = Input(Bits(3.W))
    val s_axi_awburst         = Input(Bits(2.W))
    val s_axi_awlock          = Input(Bits(1.W))
    val s_axi_awcache         = Input(Bits(4.W))
    val s_axi_awprot          = Input(Bits(3.W))
    val s_axi_awqos           = Input(Bits(4.W))
    val s_axi_awvalid         = Input(Bool())
    val s_axi_awready         = Output(Bool())
    //slave interface write data ports
    val s_axi_wdata           = Input(Bits(64.W))
    val s_axi_wstrb           = Input(Bits(8.W))
    val s_axi_wlast           = Input(Bool())
    val s_axi_wvalid          = Input(Bool())
    val s_axi_wready          = Output(Bool())
    //slave interface write response ports
    val s_axi_bready          = Input(Bool())
    val s_axi_bid             = Output(Bits(4.W))
    val s_axi_bresp           = Output(Bits(2.W))
    val s_axi_bvalid          = Output(Bool())
    //slave interface read address ports
    val s_axi_arid            = Input(Bits(4.W))
    val s_axi_araddr          = Input(Bits(32.W))
    val s_axi_arlen           = Input(Bits(8.W))
    val s_axi_arsize          = Input(Bits(3.W))
    val s_axi_arburst         = Input(Bits(2.W))
    val s_axi_arlock          = Input(Bits(1.W))
    val s_axi_arcache         = Input(Bits(4.W))
    val s_axi_arprot          = Input(Bits(3.W))
    val s_axi_arqos           = Input(Bits(4.W))
    val s_axi_arvalid         = Input(Bool())
    val s_axi_arready         = Output(Bool())
    //slave interface read data ports
    val s_axi_rready          = Input(Bool())
    val s_axi_rid             = Output(Bits(4.W))
    val s_axi_rdata           = Output(Bits(64.W))
    val s_axi_rresp           = Output(Bits(2.W))
    val s_axi_rlast           = Output(Bool())
    val s_axi_rvalid          = Output(Bool())
    //misc
  })

  val modulename = """datastorm_uniphy"""
  
  val ddr_pins = new {
    val a = Seq("PIN_AJ14", "PIN_AK14", "PIN_AH12", "PIN_AJ12", "PIN_AG15", "PIN_AH15", "PIN_AK12", "PIN_AK13", "PIN_AH13", "PIN_AH14", "PIN_AJ9", "PIN_AK9", "PIN_AK7", "PIN_AK8", "PIN_AG12")
    val ba = Seq("PIN_AH10", "PIN_AJ11", "PIN_AK11")
    val cas_n = "PIN_AH7"
    val ck_p = "PIN_AA14"
    val ck_n = "PIN_AA15"
    val cke = "PIN_AJ21"
    val cs_n = "PIN_AB15"
    val dm = Seq("PIN_AH17", "PIN_AG23", "PIN_AK23", "PIN_AJ27")
    val dq = Seq("PIN_AF18", "PIN_AE17", "PIN_AG16", "PIN_AF16", "PIN_AH20", "PIN_AG21", "PIN_AJ16", "PIN_AH18", "PIN_AK18", "PIN_AJ17", "PIN_AG18", "PIN_AK19", "PIN_AG20", "PIN_AF19", "PIN_AJ20", "PIN_AH24", "PIN_AE19", "PIN_AE18", "PIN_AG22", "PIN_AK22", "PIN_AF21", "PIN_AF20", "PIN_AH23", "PIN_AK24", "PIN_AF24", "PIN_AF23", "PIN_AJ24", "PIN_AK26", "PIN_AE23", "PIN_AE22", "PIN_AG25", "PIN_AK27")
    val dqs_p = Seq("PIN_V16", "PIN_V17", "PIN_Y17", "PIN_AC20")
    val dqs_n = Seq("PIN_W16", "PIN_W17", "PIN_AA18", "PIN_AD19")
    val odt = "PIN_AE16"
    val ras_n = "PIN_AH8"
    val reset_n = "PIN_AK21"
    val we_n = "PIN_AJ6"
    val rzqin = "PIN_AG17"
  }

  ElaborationArtefacts.add(
    modulename++".quartus.tcl",
s"""set uniphy_qsys [file join $$boarddir tcl datastorm_uniphy.tcl]
exec -ignorestderr qsys-script "--script=$$uniphy_qsys"
exec -ignorestderr qsys-generate ${modulename}.qsys "--block-symbol-file" "--clear-output-directory" "--synthesis=VERILOG"

set_global_assignment -name QSYS_FILE datastorm_uniphy.qsys
set_global_assignment -name QIP_FILE datastorm_uniphy/synthesis/datastorm_uniphy.qip

set_location_assignment PIN_AJ14 -to ddr_ddr3_mem_a[0]
set_location_assignment PIN_AK14 -to ddr_ddr3_mem_a[1]
set_location_assignment PIN_AH12 -to ddr_ddr3_mem_a[2]
set_location_assignment PIN_AJ12 -to ddr_ddr3_mem_a[3]
set_location_assignment PIN_AG15 -to ddr_ddr3_mem_a[4]
set_location_assignment PIN_AH15 -to ddr_ddr3_mem_a[5]
set_location_assignment PIN_AK12 -to ddr_ddr3_mem_a[6]
set_location_assignment PIN_AK13 -to ddr_ddr3_mem_a[7]
set_location_assignment PIN_AH13 -to ddr_ddr3_mem_a[8]
set_location_assignment PIN_AH14 -to ddr_ddr3_mem_a[9]
set_location_assignment PIN_AJ9 -to ddr_ddr3_mem_a[10]
set_location_assignment PIN_AK9 -to ddr_ddr3_mem_a[11]
set_location_assignment PIN_AK7 -to ddr_ddr3_mem_a[12]
set_location_assignment PIN_AK8 -to ddr_ddr3_mem_a[13]
set_location_assignment PIN_AG12 -to ddr_ddr3_mem_a[14]
set_location_assignment PIN_AH10 -to ddr_ddr3_mem_ba[0]
set_location_assignment PIN_AJ11 -to ddr_ddr3_mem_ba[1]
set_location_assignment PIN_AK11 -to ddr_ddr3_mem_ba[2]
set_location_assignment PIN_AH7 -to ddr_ddr3_mem_cas_n
set_location_assignment PIN_AA14 -to ddr_ddr3_mem_ck
set_location_assignment PIN_AA15 -to ddr_ddr3_mem_ck_n
set_location_assignment PIN_AJ21 -to ddr_ddr3_mem_cke
set_location_assignment PIN_AB15 -to ddr_ddr3_mem_cs_n
set_location_assignment PIN_AH17 -to ddr_ddr3_mem_dm[0]
set_location_assignment PIN_AG23 -to ddr_ddr3_mem_dm[1]
set_location_assignment PIN_AK23 -to ddr_ddr3_mem_dm[2]
set_location_assignment PIN_AJ27 -to ddr_ddr3_mem_dm[3]
set_location_assignment PIN_AF18 -to ddr_ddr3_mem_dq[0]
set_location_assignment PIN_AE17 -to ddr_ddr3_mem_dq[1]
set_location_assignment PIN_AG16 -to ddr_ddr3_mem_dq[2]
set_location_assignment PIN_AF16 -to ddr_ddr3_mem_dq[3]
set_location_assignment PIN_AH20 -to ddr_ddr3_mem_dq[4]
set_location_assignment PIN_AG21 -to ddr_ddr3_mem_dq[5]
set_location_assignment PIN_AJ16 -to ddr_ddr3_mem_dq[6]
set_location_assignment PIN_AH18 -to ddr_ddr3_mem_dq[7]
set_location_assignment PIN_AK18 -to ddr_ddr3_mem_dq[8]
set_location_assignment PIN_AJ17 -to ddr_ddr3_mem_dq[9]
set_location_assignment PIN_AG18 -to ddr_ddr3_mem_dq[10]
set_location_assignment PIN_AK19 -to ddr_ddr3_mem_dq[11]
set_location_assignment PIN_AG20 -to ddr_ddr3_mem_dq[12]
set_location_assignment PIN_AF19 -to ddr_ddr3_mem_dq[13]
set_location_assignment PIN_AJ20 -to ddr_ddr3_mem_dq[14]
set_location_assignment PIN_AH24 -to ddr_ddr3_mem_dq[15]
set_location_assignment PIN_AE19 -to ddr_ddr3_mem_dq[16]
set_location_assignment PIN_AE18 -to ddr_ddr3_mem_dq[17]
set_location_assignment PIN_AG22 -to ddr_ddr3_mem_dq[18]
set_location_assignment PIN_AK22 -to ddr_ddr3_mem_dq[19]
set_location_assignment PIN_AF21 -to ddr_ddr3_mem_dq[20]
set_location_assignment PIN_AF20 -to ddr_ddr3_mem_dq[21]
set_location_assignment PIN_AH23 -to ddr_ddr3_mem_dq[22]
set_location_assignment PIN_AK24 -to ddr_ddr3_mem_dq[23]
set_location_assignment PIN_AF24 -to ddr_ddr3_mem_dq[24]
set_location_assignment PIN_AF23 -to ddr_ddr3_mem_dq[25]
set_location_assignment PIN_AJ24 -to ddr_ddr3_mem_dq[26]
set_location_assignment PIN_AK26 -to ddr_ddr3_mem_dq[27]
set_location_assignment PIN_AE23 -to ddr_ddr3_mem_dq[28]
set_location_assignment PIN_AE22 -to ddr_ddr3_mem_dq[29]
set_location_assignment PIN_AG25 -to ddr_ddr3_mem_dq[30]
set_location_assignment PIN_AK27 -to ddr_ddr3_mem_dq[31]
set_location_assignment PIN_V16 -to ddr_ddr3_mem_dqs[0]
set_location_assignment PIN_W16 -to ddr_ddr3_mem_dqs_n[0]
set_location_assignment PIN_V17 -to ddr_ddr3_mem_dqs[1]
set_location_assignment PIN_W17 -to ddr_ddr3_mem_dqs_n[1]
set_location_assignment PIN_Y17 -to ddr_ddr3_mem_dqs[2]
set_location_assignment PIN_AA18 -to ddr_ddr3_mem_dqs_n[2]
set_location_assignment PIN_AC20 -to ddr_ddr3_mem_dqs[3]
set_location_assignment PIN_AD19 -to ddr_ddr3_mem_dqs_n[3]
set_location_assignment PIN_AE16 -to ddr_ddr3_mem_odt
set_location_assignment PIN_AH8 -to ddr_ddr3_mem_ras_n
set_location_assignment PIN_AK21 -to ddr_ddr3_mem_reset_n
set_location_assignment PIN_AJ6 -to ddr_ddr3_mem_we_n
set_location_assignment PIN_AG17 -to ddr_oct_rzqin
set_instance_assignment -name D5_DELAY 2 -to ddr_ddr3_mem_ck
set_instance_assignment -name D5_DELAY 2 -to ddr_ddr3_mem_ck_n
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_a[0]
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_a[1]
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_a[2]
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_a[3]
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_a[4]
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_a[5]
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_a[6]
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_a[7]
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_a[8]
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_a[9]
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_a[10]
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_a[11]
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_a[12]
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_a[13]
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_a[14]
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_a[15]
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_ba[0]
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_ba[1]
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_ba[2]
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_cas_n
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_cke
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_cs_n
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_odt
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_ras_n
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_reset_n
set_instance_assignment -name CURRENT_STRENGTH_NEW "MAXIMUM CURRENT" -to ddr_ddr3_mem_we_n
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[0]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[1]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[2]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[3]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[4]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[5]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[6]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[7]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[8]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[9]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[10]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[11]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[12]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[13]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[14]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[15]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[16]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[17]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[18]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[19]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[20]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[21]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[22]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[23]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[24]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[25]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[26]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[27]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[28]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[29]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[30]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[31]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dqs[0]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dqs[1]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dqs[2]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dqs[3]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dqs_n[0]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dqs_n[1]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dqs_n[2]
set_instance_assignment -name INPUT_TERMINATION "PARALLEL 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dqs_n[3]
set_instance_assignment -name IO_STANDARD "DIFFERENTIAL 1.5-V SSTL CLASS I" -to ddr_ddr3_mem_ck
set_instance_assignment -name IO_STANDARD "DIFFERENTIAL 1.5-V SSTL CLASS I" -to ddr_ddr3_mem_ck_n
set_instance_assignment -name IO_STANDARD "DIFFERENTIAL 1.5-V SSTL CLASS I" -to ddr_ddr3_mem_dqs[0]
set_instance_assignment -name IO_STANDARD "DIFFERENTIAL 1.5-V SSTL CLASS I" -to ddr_ddr3_mem_dqs[1]
set_instance_assignment -name IO_STANDARD "DIFFERENTIAL 1.5-V SSTL CLASS I" -to ddr_ddr3_mem_dqs[2]
set_instance_assignment -name IO_STANDARD "DIFFERENTIAL 1.5-V SSTL CLASS I" -to ddr_ddr3_mem_dqs[3]
set_instance_assignment -name IO_STANDARD "DIFFERENTIAL 1.5-V SSTL CLASS I" -to ddr_ddr3_mem_dqs_n[0]
set_instance_assignment -name IO_STANDARD "DIFFERENTIAL 1.5-V SSTL CLASS I" -to ddr_ddr3_mem_dqs_n[1]
set_instance_assignment -name IO_STANDARD "DIFFERENTIAL 1.5-V SSTL CLASS I" -to ddr_ddr3_mem_dqs_n[2]
set_instance_assignment -name IO_STANDARD "DIFFERENTIAL 1.5-V SSTL CLASS I" -to ddr_ddr3_mem_dqs_n[3]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_a[0]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_a[1]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_a[2]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_a[3]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_a[4]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_a[5]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_a[6]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_a[7]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_a[8]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_a[9]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_a[10]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_a[11]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_a[12]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_a[13]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_a[14]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_a[15]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_ba[0]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_ba[1]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_ba[2]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_cas_n
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_cke
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_cs_n
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dm[0]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dm[1]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dm[2]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dm[3]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[0]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[1]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[2]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[3]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[4]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[5]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[6]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[7]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[8]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[9]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[10]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[11]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[12]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[13]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[14]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[15]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[16]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[17]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[18]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[19]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[20]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[21]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[22]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[23]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[24]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[25]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[26]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[27]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[28]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[29]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[30]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_dq[31]
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_odt
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_ras_n
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_reset_n
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_ddr3_mem_we_n
set_instance_assignment -name IO_STANDARD "SSTL-15 CLASS I" -to ddr_oct_rzqin
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dm[0]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dm[1]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dm[2]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dm[3]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[0]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[1]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[2]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[3]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[4]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[5]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[6]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[7]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[8]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[9]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[10]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[11]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[12]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[13]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[14]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[15]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[16]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[17]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[18]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[19]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[20]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[21]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[22]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[23]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[24]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[25]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[26]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[27]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[28]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[29]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[30]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dq[31]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dqs[0]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dqs[1]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dqs[2]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dqs[3]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dqs_n[0]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dqs_n[1]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dqs_n[2]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITH CALIBRATION" -to ddr_ddr3_mem_dqs_n[3]
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITHOUT CALIBRATION" -to ddr_ddr3_mem_ck
set_instance_assignment -name OUTPUT_TERMINATION "SERIES 50 OHM WITHOUT CALIBRATION" -to ddr_ddr3_mem_ck_n
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_a[0]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_a[1]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_a[2]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_a[3]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_a[4]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_a[5]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_a[6]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_a[7]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_a[8]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_a[9]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_a[10]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_a[11]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_a[12]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_a[13]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_a[14]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_a[15]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_ba[0]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_ba[1]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_ba[2]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_cas_n
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_ck
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_ck_n
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_cke
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_cs_n
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dm[0]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dm[1]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dm[2]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dm[3]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[0]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[1]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[2]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[3]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[4]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[5]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[6]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[7]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[8]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[9]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[10]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[11]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[12]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[13]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[14]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[15]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[16]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[17]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[18]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[19]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[20]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[21]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[22]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[23]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[24]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[25]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[26]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[27]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[28]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[29]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[30]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dq[31]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dqs[0]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dqs[1]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dqs[2]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dqs[3]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dqs_n[0]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dqs_n[1]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dqs_n[2]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_dqs_n[3]
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_odt
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_ras_n
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_reset_n
set_instance_assignment -name PACKAGE_SKEW_COMPENSATION OFF -to ddr_ddr3_mem_we_n
"""
  )
}

//TODO Finish nicer tcl script generator
/*
    ddr_pins.a.zipWithIndex.map{case (p, i) => s"set_location_assignment ${p} -to ddr_ddr3_mem_a[${i}]\n"}.mkString +
    ddr_pins.ba.zipWithIndex.map{case (p, i) => s"set_location_assignment ${p} -to ddr_ddr3_mem_ba[${i}]\n"}.mkString +
    s"set_location_assignment ${ddr_pins.cas_n} -to ddr_ddr3_mem_cas_n\n" +
    s"set_location_assignment ${ddr_pins.ck_p} -to ddr_ddr3_mem_ck_p\n" +
    s"set_location_assignment ${ddr_pins.ck_n} -to ddr_ddr3_mem_ck_n\n" +
    s"set_location_assignment ${ddr_pins.cke} -to ddr_ddr3_mem_cke\n" +
    s"set_location_assignment ${ddr_pins.cs_n} -to ddr_ddr3_mem_cs_n\n" +
    ddr_pins.dm.zipWithIndex.map{case (p, i) => s"set_location_assignment ${p} -to ddr_ddr3_mem_dm[${i}]\n"}.mkString +
    ddr_pins.dq.zipWithIndex.map{case (p, i) => s"set_location_assignment ${p} -to ddr_ddr3_mem_dq[${i}]\n"}.mkString +
    ddr_pins.dqs_p.zipWithIndex.map{case (p, i) => s"set_location_assignment ${p} -to ddr_ddr3_mem_dqs_p[${i}]\n"}.mkString +
    ddr_pins.dqs_n.zipWithIndex.map{case (p, i) => s"set_location_assignment ${p} -to ddr_ddr3_mem_dqs_n[${i}]\n"}.mkString +
    s"set_location_assignment ${ddr_pins.odt} -to ddr_ddr3_mem_odt\n" +
    s"set_location_assignment ${ddr_pins.ras_n} -to ddr_ddr3_mem_ras_n\n" +
    s"set_location_assignment ${ddr_pins.reset_n} -to ddr_ddr3_mem_reset_n\n" +
    s"set_location_assignment ${ddr_pins.we_n} -to ddr_ddr3_mem_we_n\n" +
    s"set_location_assignment ${ddr_pins.rzqin} -to ddr_ddr3_mem_rzqin\n" +
    ddr_pins.a.zipWithIndex.map{case (p, i) => s"set_instance_assignment -name CURRENT_STRENGTH_NEW \"MAXIMUM CURRENT\" -to ddr_ddr3_mem_a[${i}]\n"}.mkString +
    ddr_pins.ba.zipWithIndex.map{case (p, i) => s"set_instance_assignment -name CURRENT_STRENGTH_NEW \"MAXIMUM CURRENT\" -to ddr_ddr3_mem_ba[${i}]\n"}.mkString +
    s"set_instance_assignment -name CURRENT_STRENGTH_NEW \"MAXIMUM CURRENT\" -to ddr_ddr3_mem_cas_n\n" +
    s"set_instance_assignment -name CURRENT_STRENGTH_NEW \"MAXIMUM CURRENT\" -to ddr_ddr3_mem_cke\n" +
    s"set_instance_assignment -name CURRENT_STRENGTH_NEW \"MAXIMUM CURRENT\" -to ddr_ddr3_mem_cs_n\n" +
    s"set_instance_assignment -name CURRENT_STRENGTH_NEW \"MAXIMUM CURRENT\" -to ddr_ddr3_mem_odt\n" +
    s"set_instance_assignment -name CURRENT_STRENGTH_NEW \"MAXIMUM CURRENT\" -to ddr_ddr3_mem_ras_n\n" +
    s"set_instance_assignment -name CURRENT_STRENGTH_NEW \"MAXIMUM CURRENT\" -to ddr_ddr3_mem_reset_n\n" +
    s"set_instance_assignment -name CURRENT_STRENGTH_NEW \"MAXIMUM CURRENT\" -to ddr_ddr3_mem_we_n\n" +
    ddr_pins.dq.zipWithIndex.map{case (p, i) => s"set_instance_assignment -name INPUT_TERMINATION \"PARALLEL 50 OHM WITH CALIBRATION\" -to ddr_ddr3_mem_dq[${i}]\n"}.mkString +
    ddr_pins.dqs_p.zipWithIndex.map{case (p, i) => s"set_instance_assignment -name INPUT_TERMINATION \"PARALLEL 50 OHM WITH CALIBRATION\" -to ddr_ddr3_mem_dqs_p[${i}]\n"}.mkString +
    ddr_pins.dqs_n.zipWithIndex.map{case (p, i) => s"set_instance_assignment -name INPUT_TERMINATION \"PARALLEL 50 OHM WITH CALIBRATION\" -to ddr_ddr3_mem_dqs_n[${i}]\n"}.mkString +

    s"set_instance_assignment -name IO_STANDARD \"DIFFERENTIAL 1.5-V SSTL CLASS I\" -to ddr_ddr3_mem_ck_p" +
    s"set_instance_assignment -name IO_STANDARD \"DIFFERENTIAL 1.5-V SSTL CLASS I\" -to ddr_ddr3_mem_ck_n" +
    ddr_pins.dqs_p.zipWithIndex.map{case (p, i) => s"set_instance_assignment -name IO_STANDARD \"DIFFERENTIAL 1.5-V SSTL CLASS I\" -to ddr_ddr3_mem_dqs_p[${i}]\n"}.mkString +
    ddr_pins.dqs_n.zipWithIndex.map{case (p, i) => s"set_instance_assignment -name IO_STANDARD \"DIFFERENTIAL 1.5-V SSTL CLASS I\" -to ddr_ddr3_mem_dqs_n[${i}]\n"}.mkString +

    ddr_pins.a.zipWithIndex.map{case (p, i) => s"set_instance_assignment -name IO_STANDARD \"SSTL-15 CLASS I\" -to ddr_ddr3_mem_a[${i}]\n"}.mkString +
    ddr_pins.ba.zipWithIndex.map{case (p, i) => s"set_instance_assignment -name IO_STANDARD \"SSTL-15 CLASS I\" -to ddr_ddr3_mem_ba[${i}]\n"}.mkString +
    s"set_instance_assignment -name IO_STANDARD \"SSTL-15 CLASS I\" -to ddr_ddr3_mem_cas_n\n" +
    s"set_instance_assignment -name IO_STANDARD \"SSTL-15 CLASS I\" -to ddr_ddr3_mem_cke\n" +
    s"set_instance_assignment -name IO_STANDARD \"SSTL-15 CLASS I\" -to ddr_ddr3_mem_cs_n\n" +
    ddr_pins.dm.zipWithIndex.map{case (p, i) => s"set_instance_assignment -name IO_STANDARD \"SSTL-15 CLASS I\" -to ddr_ddr3_mem_dm[${i}]\n"}.mkString +
    ddr_pins.dq.zipWithIndex.map{case (p, i) => s"set_instance_assignment -name IO_STANDARD \"SSTL-15 CLASS I\" -to ddr_ddr3_mem_dq[${i}]\n"}.mkString +
    s"set_instance_assignment -name IO_STANDARD \"SSTL-15 CLASS I\" -to ddr_ddr3_mem_odt\n" +
    s"set_instance_assignment -name IO_STANDARD \"SSTL-15 CLASS I\" -to ddr_ddr3_mem_ras_n\n" +
    s"set_instance_assignment -name IO_STANDARD \"SSTL-15 CLASS I\" -to ddr_ddr3_mem_reset_n\n" +
    s"set_instance_assignment -name IO_STANDARD \"SSTL-15 CLASS I\" -to ddr_ddr3_mem_we_n\n" +
    s"set_instance_assignment -name IO_STANDARD \"SSTL-15 CLASS I\" -to ddr_ddr3_mem_rzqin\n" 

*/
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
