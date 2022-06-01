package sifive.fpgashells.ip.microsemi.polarfireglitchlessmux

import Chisel._
import chisel3.experimental.{Analog,attach}
import freechips.rocketchip.util.{ElaborationArtefacts}
import freechips.rocketchip.util.GenericParameterizedBundle
import freechips.rocketchip.config._

// Black Box forMicrosemi PolarFire glitchless mux Actel:SgCore:PF_NGMUX:1.0.101

trait PolarFireGlitchlessMuxIOPads extends Bundle {

    val CLK_OUT = Clock(OUTPUT)
    val CLK0    = Clock(INPUT)
    val CLK1    = Clock(INPUT)
    val SEL     = Bool(INPUT)
}

//scalastyle:off
//turn off linter: blackbox name must match verilog module
class PolarFireGlitchlessMux(implicit val p:Parameters) extends BlackBox
{
  override def desiredName = "pf_glitchless_mux"

  val io = new PolarFireGlitchlessMuxIOPads {
  }
  
  ElaborationArtefacts.add(
    "Libero.polarfire_glitchless_mux.libero.tcl",
    """ 
create_design -id Actel:SgCore:PF_NGMUX:1.0.101 -design_name {pf_glitchless_mux} -config_file {} -params {} -inhibit_configurator 0
open_smartdesign -design {pf_glitchless_mux}
configure_design -component {pf_glitchless_mux} -library {} 
fix_vlnv_instance -component {pf_glitchless_mux} -library {} -name {pf_glitchless_mux_0} 
open_smartdesign -design {pf_glitchless_mux}
configure_design -component {pf_glitchless_mux} -library {} 
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
