package sifive.fpgashells.ip.microsemi.polarfireinitmonitor

import Chisel._
import freechips.rocketchip.util.{ElaborationArtefacts}
import freechips.rocketchip.config._

// Black Box for Microsemi PolarFire Clock Conditioning Circuit (CCC) Actel:SgCore:PF_INIT_MONITOR:2.0.103

trait PolarFireInitMonitorIOPads {
  val DEVICE_INIT_DONE  = Bool(OUTPUT)
  val FABRIC_POR_N      = Bool(OUTPUT)
  val PCIE_INIT_DONE    = Bool(OUTPUT)
  val SRAM_INIT_DONE    = Bool(OUTPUT)
  val USRAM_INIT_DONE   = Bool(OUTPUT)
}

class PolarFireInitMonitor(implicit val p:Parameters) extends BlackBox
{
  override def desiredName = "polarfire_init_monitor"

  val io = new Bundle with PolarFireInitMonitorIOPads

  ElaborationArtefacts.add(s"${desiredName}.libero.tcl",
    s"""create_design -id Actel:SgCore:PF_INIT_MONITOR:2.0.103 -design_name {polarfire_init_monitor} -config_file {} -params {} -inhibit_configurator 0
       |open_smartdesign -design {polarfire_init_monitor}
       |configure_design -component {polarfire_init_monitor} -library {}
       |fix_vlnv_instance -component {polarfire_init_monitor} -library {} -name {polarfire_init_monitor_0}
       |open_smartdesign -design {polarfire_init_monitor}
       |configure_design -component {polarfire_init_monitor} -library {}
       |""".stripMargin)
}  

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
