package sifive.fpgashells.ip.microsemi.polarfire_oscillator

import Chisel._
import chisel3.experimental.{Analog,attach}
import freechips.rocketchip.util.{ElaborationArtefacts}
import freechips.rocketchip.util.GenericParameterizedBundle
import freechips.rocketchip.config._

// Black Box for Microsemi PolarFire internal oscillator Actel:SgCore:PF_OSC:1.0.102

trait PolarFireOscillatorIOPads extends Bundle {

    val RCOSC_160MHZ_GL = Clock(OUTPUT)
}

//scalastyle:off
//turn off linter: blackbox name must match verilog module
class PolarFireOscillator(implicit val p:Parameters) extends BlackBox
{
  override def desiredName = "pf_oscillator"

  val io = new PolarFireOscillatorIOPads {
  }
  
  ElaborationArtefacts.add(
    "Libero.polarfire_oscillator.libero.tcl",
    """ 
create_design -id Actel:SgCore:PF_OSC:1.0.102 -design_name {pf_oscillator} -config_file {} -params {} -inhibit_configurator 0
open_smartdesign -design {pf_oscillator}
configure_design -component {pf_oscillator} -library {} 
fix_vlnv_instance -component {pf_oscillator} -library {} -name {pf_oscillator_0} 
open_smartdesign -design {pf_oscillator}
configure_design -component {pf_oscillator} -library {} """
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
