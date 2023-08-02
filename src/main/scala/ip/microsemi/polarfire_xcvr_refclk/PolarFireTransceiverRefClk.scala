package sifive.fpgashells.ip.microsemi.polarfirexcvrrefclk

import chisel3._
import freechips.rocketchip.util.ElaborationArtefacts
import org.chipsalliance.cde.config._

// Black Box for Microsemi:SgCore:PF_XCVR_REF_CLK:1.0.103

trait PolarFireTransceiverRefClkIOPads extends Bundle {

    val REF_CLK_PAD_P   = Input(Bool())
    val REF_CLK_PAD_N   = Input(Bool())
    val REF_CLK         = Output(Clock())
    val FAB_REF_CLK     = Output(Clock())
}

//scalastyle:off
//turn off linter: blackbox name must match verilog module
class PolarFireTransceiverRefClk(implicit val p:Parameters) extends BlackBox
{
  override def desiredName = "transceiver_refclk"

  val io = IO(new PolarFireTransceiverRefClkIOPads {})
  
  ElaborationArtefacts.add(
    "Libero.polarfire_xcvr_refclk.libero.tcl",
    """ 
create_design -id Actel:SgCore:PF_XCVR_REF_CLK:1.0.103 -design_name {transceiver_refclk} -config_file {} -params {} -inhibit_configurator 0
open_smartdesign -design {transceiver_refclk}
configure_design -component {transceiver_refclk} -library {} 
configure_vlnv_instance -component {transceiver_refclk} -library {} -name {transceiver_refclk_0} \
    -params {"ENABLE_FAB_CLK_0:1" \
            } -validate_rules 0 

fix_vlnv_instance -component {transceiver_refclk} -library {} -name {transceiver_refclk_0} 
open_smartdesign -design {transceiver_refclk}
configure_design -component {transceiver_refclk} -library {} 
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
