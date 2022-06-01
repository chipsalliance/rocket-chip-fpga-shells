package sifive.fpgashells.devices.microsemi.polarfireevalkitpciex4

import Chisel._
//import freechips.rocketchip.coreplex.{HasInterruptBus, HasSystemBus}
import freechips.rocketchip.diplomacy.{LazyModule, LazyModuleImp, BufferParams}
import freechips.rocketchip.subsystem.BaseSubsystem
import freechips.rocketchip.tilelink._

//trait HasSystemPolarFireEvalKitPCIeX4 extends HasSystemBus with HasInterruptBus {
trait HasSystemPolarFireEvalKitPCIeX4 { this: BaseSubsystem =>
  val pf_eval_kit_pcie = LazyModule(new PolarFireEvalKitPCIeX4)
  private val cname = "polarfirepcie"
  sbus.coupleFrom(s"master_named_$cname") { _ :=* TLFIFOFixer(TLFIFOFixer.all) :=* pf_eval_kit_pcie.crossTLOut(pf_eval_kit_pcie.master) }
  sbus.coupleTo(s"slave_named_$cname") { pf_eval_kit_pcie.crossTLIn(pf_eval_kit_pcie.slave) :*= TLWidthWidget(sbus.beatBytes) :*= _ }
  sbus.coupleTo(s"controller_named_$cname") { pf_eval_kit_pcie.crossTLIn(pf_eval_kit_pcie.control) :*= TLWidthWidget(sbus.beatBytes) :*= _ }
  ibus.fromSync := pf_eval_kit_pcie.crossIntOut(pf_eval_kit_pcie.intnode)
}

trait HasSystemPolarFireEvalKitPCIeX4Bundle {
  val pf_eval_kit_pcie: PolarFireEvalKitPCIeX4IO
  def connectPolarFireEvalKitPCIeX4ToPads(pads: PolarFireEvalKitPCIeX4Pads) {
    pads <> pf_eval_kit_pcie
  }
}

trait HasSystemPolarFireEvalKitPCIeX4ModuleImp extends LazyModuleImp
    with HasSystemPolarFireEvalKitPCIeX4Bundle {
  val outer: HasSystemPolarFireEvalKitPCIeX4
  val pf_eval_kit_pcie = IO(new PolarFireEvalKitPCIeX4IO)

  pf_eval_kit_pcie <> outer.pf_eval_kit_pcie.module.io.port

  outer.pf_eval_kit_pcie.module.clock := outer.pf_eval_kit_pcie.module.io.port.AXI_CLK
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
