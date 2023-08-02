package sifive.fpgashells.ip.xilinx.vcu118mig

import chisel3._
import chisel3.experimental.Analog
import freechips.rocketchip.util.ElaborationArtefacts
import org.chipsalliance.cde.config._

// IP VLNV: xilinx.com:customize_ip:vcu118mig:1.0
// Black Box

class VCU118MIGIODDR(depth : BigInt) extends Bundle {
  require((depth<=0x80000000L),"VCU118MIGIODDR supports upto 2GB depth configuraton")
  val c0_ddr4_adr           = Output(Bits(17.W))
  val c0_ddr4_bg            = Output(Bits(1.W))
  val c0_ddr4_ba            = Output(Bits(2.W))
  val c0_ddr4_reset_n       = Output(Bool())
  val c0_ddr4_act_n         = Output(Bool())
  val c0_ddr4_ck_c          = Output(Bits(1.W))
  val c0_ddr4_ck_t          = Output(Bits(1.W))
  val c0_ddr4_cke           = Output(Bits(1.W))
  val c0_ddr4_cs_n          = Output(Bits(1.W))
  val c0_ddr4_odt           = Output(Bits(1.W))

  val c0_ddr4_dq            = Analog(64.W)
  val c0_ddr4_dqs_c         = Analog(8.W)
  val c0_ddr4_dqs_t         = Analog(8.W)
  val c0_ddr4_dm_dbi_n      = Analog(8.W)
}

//reused directly in io bundle for sifive.blocks.devices.xilinxvcu118mig
trait VCU118MIGIOClocksReset extends Bundle {
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

//scalastyle:off
//turn off linter: blackbox name must match verilog module
class vcu118mig(depth : BigInt)(implicit val p:Parameters) extends BlackBox
{
  require((depth<=0x80000000L),"vcu118mig supports upto 2GB depth configuraton")

  val io = IO(new VCU118MIGIODDR(depth) with VCU118MIGIOClocksReset {
    //slave interface write address ports
    val c0_ddr4_s_axi_awid            = Input(Bits(4.W))
    val c0_ddr4_s_axi_awaddr          = Input(Bits(31.W))
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
    val c0_ddr4_s_axi_araddr          = Input(Bits(31.W))
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
    "vcu118mig.vivado.tcl",
    """ 
      create_ip -vendor xilinx.com -library ip -version 2.2 -name ddr4 -module_name vcu118mig -dir $ipdir -force
      set_property -dict [list \
      CONFIG.AL_SEL                               {0} \
      CONFIG.C0.ADDR_WIDTH                        {17} \
      CONFIG.C0.BANK_GROUP_WIDTH                  {1} \
      CONFIG.C0.CKE_WIDTH                         {1} \
      CONFIG.C0.CK_WIDTH                          {1} \
      CONFIG.C0.CS_WIDTH                          {1} \
      CONFIG.C0.ControllerType                    {DDR4_SDRAM} \
      CONFIG.C0.DDR4_AUTO_AP_COL_A3               {false} \
      CONFIG.C0.DDR4_AutoPrecharge                {false} \
      CONFIG.C0.DDR4_AxiAddressWidth              {31} \
      CONFIG.C0.DDR4_AxiArbitrationScheme         {RD_PRI_REG} \
      CONFIG.C0.DDR4_AxiDataWidth                 {64} \
      CONFIG.C0.DDR4_AxiIDWidth                   {4} \
      CONFIG.C0.DDR4_AxiNarrowBurst               {false} \
      CONFIG.C0.DDR4_AxiSelection                 {true} \
      CONFIG.C0.DDR4_BurstLength                  {8} \
      CONFIG.C0.DDR4_BurstType                    {Sequential} \
      CONFIG.C0.DDR4_CLKFBOUT_MULT                {8} \
      CONFIG.C0.DDR4_CLKOUT0_DIVIDE               {5} \
      CONFIG.C0.DDR4_Capacity                     {512} \
      CONFIG.C0.DDR4_CasLatency                   {11} \
      CONFIG.C0.DDR4_CasWriteLatency              {9} \
      CONFIG.C0.DDR4_ChipSelect                   {true} \
      CONFIG.C0.DDR4_Clamshell                    {false} \
      CONFIG.C0.DDR4_CustomParts                  {no_file_loaded} \
      CONFIG.C0.DDR4_DIVCLK_DIVIDE                {2} \
      CONFIG.C0.DDR4_DataMask                     {DM_NO_DBI} \
      CONFIG.C0.DDR4_DataWidth                    {64} \
      CONFIG.C0.DDR4_Ecc                          {false} \
      CONFIG.C0.DDR4_MCS_ECC                      {false} \
      CONFIG.C0.DDR4_Mem_Add_Map                  {ROW_COLUMN_BANK} \
      CONFIG.C0.DDR4_MemoryName                   {MainMemory} \
      CONFIG.C0.DDR4_MemoryPart                   {MT40A256M16GE-083E} \
      CONFIG.C0.DDR4_MemoryType                   {Components} \
      CONFIG.C0.DDR4_MemoryVoltage                {1.2V} \
      CONFIG.C0.DDR4_OnDieTermination             {RZQ/6} \
      CONFIG.C0.DDR4_Ordering                     {Normal} \
      CONFIG.C0.DDR4_OutputDriverImpedenceControl {RZQ/7} \
      CONFIG.C0.DDR4_PhyClockRatio                {4:1} \
      CONFIG.C0.DDR4_SAVE_RESTORE                 {false} \
      CONFIG.C0.DDR4_SELF_REFRESH                 {false} \
      CONFIG.C0.DDR4_Slot                         {Single} \
      CONFIG.C0.DDR4_Specify_MandD                {true} \
      CONFIG.C0.DDR4_TimePeriod                   {1250} \
      CONFIG.C0.DDR4_UserRefresh_ZQCS             {false} \
      CONFIG.C0.DDR4_isCKEShared                  {false} \
      CONFIG.C0.DDR4_isCustom                     {false} \
      CONFIG.C0.LR_WIDTH                          {1} \
      CONFIG.C0.ODT_WIDTH                         {1} \
      CONFIG.C0.StackHeight                       {1} \
      CONFIG.C0_CLOCK_BOARD_INTERFACE             {Custom} \
      CONFIG.C0_DDR4_BOARD_INTERFACE              {Custom} \
      CONFIG.DCI_Cascade                          {false} \
      CONFIG.DIFF_TERM_SYSCLK                     {false} \
      CONFIG.Debug_Signal                         {Disable} \
      CONFIG.Default_Bank_Selections              {false} \
      CONFIG.Enable_SysPorts                      {true} \
      CONFIG.IOPowerReduction                     {OFF} \
      CONFIG.IO_Power_Reduction                   {false} \
      CONFIG.IS_FROM_PHY                          {1} \
      CONFIG.MCS_DBG_EN                           {false} \
      CONFIG.No_Controller                        {1} \
      CONFIG.PARTIAL_RECONFIG_FLOW_MIG            {false} \
      CONFIG.PING_PONG_PHY                        {1} \
      CONFIG.Phy_Only                             {Complete_Memory_Controller} \
      CONFIG.RECONFIG_XSDB_SAVE_RESTORE           {false} \
      CONFIG.RESET_BOARD_INTERFACE                {Custom} \
      CONFIG.Reference_Clock                      {Differential} \
      CONFIG.SET_DW_TO_40                         {false} \
      CONFIG.System_Clock                         {No_Buffer} \
      CONFIG.TIMING_3DS                           {false} \
      CONFIG.TIMING_OP1                           {false} \
      CONFIG.TIMING_OP2                           {false} \
      ] [get_ips vcu118mig]"""
  )
   
}
//scalastyle:on

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
