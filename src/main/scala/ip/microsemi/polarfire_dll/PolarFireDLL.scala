package sifive.fpgashells.ip.microsemi.polarfiredll

import Chisel._
import chisel3.experimental.{Analog,attach}
import freechips.rocketchip.util.{ElaborationArtefacts}
import freechips.rocketchip.util.GenericParameterizedBundle
import freechips.rocketchip.config._

// Black Box for Microsemi PolarFire Delay Locked Loop (DLL) Actel:SgCore:PF_CCC:1.0.112

class PolarFireDLLIOPads extends Bundle {

    val DLL_FB_CLK       = Clock(INPUT)
    val DLL_REF_CLK      = Clock(INPUT)
    val DLL_CLK_0_FABCLK = Clock(OUTPUT)
    val DLL_LOCK         = Bool(OUTPUT)
}

//scalastyle:off
//turn off linter: blackbox name must match verilog module
class PolarFireDLL(name: String)(implicit val p:Parameters) extends BlackBox
{
  val modulename = name
  override def desiredName = name

  val io = new PolarFireDLLIOPads
  
  ElaborationArtefacts.add(
    "AddIPInstance." ++ modulename ++".libero.tcl",
    """ 
create_design -id Actel:SgCore:PF_CCC:1.0.115 -design_name {""" ++ modulename ++"""} -config_file {} -params {} -inhibit_configurator 0
open_smartdesign -design {""" ++ modulename ++"""}
configure_design -component {""" ++ modulename ++"""} -library {} 
configure_vlnv_instance -component {""" ++ modulename ++"""} -library {} -name {""" ++ modulename ++"""_0} \
    -params {"DLL_ONLY_EN:true" \
             "DLL_IN:125" \
             "DLL_MODE:INJECTION_REM_MODE" \
             "DLL_CLK_0_FABCLK_EN:true" \
            } -validate_rules 0 
fix_vlnv_instance -component {""" ++ modulename ++"""} -library {} -name {""" ++ modulename ++"""_0} 
open_smartdesign -design {""" ++ modulename ++"""}
configure_design -component {""" ++ modulename ++"""} -library {}"""
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
