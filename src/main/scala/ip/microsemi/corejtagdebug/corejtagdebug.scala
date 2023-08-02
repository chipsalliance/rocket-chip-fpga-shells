package sifive.fpgashells.ip.microsemi.corejtagdebug

import chisel3._
import freechips.rocketchip.util.ElaborationArtefacts
import org.chipsalliance.cde.config._

// Black Box for Microsemi DirectCore IP block Actel:DirectCore:COREJTAGDEBUG:2.0.100

trait CoreJtagDebugIOJTAGPads extends Bundle {

  val TCK       = Input(Clock())
  val TDI       = Input(Bool())
  val TMS       = Input(Bool())
  val TRSTB     = Input(Bool())
  val TDO       = Output(Bool())
}

trait CoreJtagDebugIOTarget extends Bundle {

  val TGT_TCK   = Output(Clock())   
  val TGT_TDI   = Output(Bool())
  val TGT_TMS   = Output(Bool())
  val TGT_TRST  = Output(Bool())
  val TGT_TDO   = Input(Bool())
}

//scalastyle:off
//turn off linter: blackbox name must match verilog module
class CoreJtagDebugBlock(implicit val p:Parameters) extends BlackBox
{
  override def desiredName = "corejtagdebug_wrapper"

  val io = IO(new CoreJtagDebugIOJTAGPads with CoreJtagDebugIOTarget {
    // chain inputs
    val UTDO_IN_0    = Input(Bool())
    val UTDO_IN_1    = Input(Bool())
    val UTDO_IN_2    = Input(Bool())
    val UTDO_IN_3    = Input(Bool())
    val UTDODRV_0    = Input(Bool())
    val UTDODRV_1    = Input(Bool())
    val UTDODRV_2    = Input(Bool())
    val UTDODRV_3    = Input(Bool())
    
    // chain outputs
    val UTDI_OUT     = Output(Bool())
    val URSTB_OUT    = Output(Bool())
    val UIREG_OUT    = Output(Bits(8.W))
    val UDRUPD_OUT   = Output(Bool())
    val UDRSH_OUT    = Output(Bool())   
    val UDRCK_OUT    = Output(Bool())
    val UDRCAP_OUT   = Output(Bool())
  })
  
  ElaborationArtefacts.add(
    "Libero.corejtagdebug.tcl",
    """ 
create_design -id Actel:DirectCore:COREJTAGDEBUG:2.0.100 -design_name {corejtagdebug_wrapper} -config_file {} -params {} -inhibit_configurator 0 
open_smartdesign -design {corejtagdebug_wrapper}
configure_design -component {corejtagdebug_wrapper} -library {} 
configure_vlnv_instance -component {corejtagdebug_wrapper} -library {} -name {corejtagdebug_wrapper_0} -params {"IR_CODE:0x55" "ACTIVE_HIGH_TGT_RESET:1"} -validate_rules 0 
fix_vlnv_instance -component {corejtagdebug_wrapper} -library {} -name {corejtagdebug_wrapper_0} 
open_smartdesign -design {corejtagdebug_wrapper}
configure_design -component {corejtagdebug_wrapper} -library {} 
"""
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
