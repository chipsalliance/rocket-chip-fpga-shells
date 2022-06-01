package sifive.fpgashells.devices.microsemi.polarfireddr3

import Chisel._
import freechips.rocketchip.config._
//import freechips.rocketchip.coreplex.HasMemoryBus
import freechips.rocketchip.subsystem.BaseSubsystem
import freechips.rocketchip.diplomacy.{LazyModule, LazyModuleImp, AddressRange}

case object MemoryMicrosemiDDR3Key extends Field[PolarFireEvalKitDDR3Params]

//trait HasMemoryPolarFireEvalKitDDR3 extends HasMemoryBus {
trait HasMemoryPolarFireEvalKitDDR3 { this: BaseSubsystem =>
  val module: HasMemoryPolarFireEvalKitDDR3ModuleImp

  val polarfireddrsubsys = LazyModule(new PolarFireEvalKitDDR3(p(MemoryMicrosemiDDR3Key)))

  polarfireddrsubsys.node := mbus.toDRAMController(Some("PolarFireDDR"))()
}

trait HasMemoryPolarFireEvalKitDDR3Bundle {
  val polarfireddrsubsys: PolarFireEvalKitDDR3IO
  def connectPolarFireEValKitDDR3ToPads(pads: PolarFireEvalKitDDR3Pads) {
    pads <> polarfireddrsubsys
  }
}

trait HasMemoryPolarFireEvalKitDDR3ModuleImp extends LazyModuleImp
    with HasMemoryPolarFireEvalKitDDR3Bundle {
  val outer: HasMemoryPolarFireEvalKitDDR3
  val ranges = AddressRange.fromSets(p(MemoryMicrosemiDDR3Key).address)
  require (ranges.size == 1, "DDR range must be contiguous")
  val depth = ranges.head.size
  val polarfireddrsubsys = IO(new PolarFireEvalKitDDR3IO(depth))

  polarfireddrsubsys <> outer.polarfireddrsubsys.module.io.port
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
