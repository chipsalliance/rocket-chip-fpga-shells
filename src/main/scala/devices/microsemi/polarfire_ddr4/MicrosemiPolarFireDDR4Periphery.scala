package sifive.fpgashells.devices.microsemi.polarfireddr4

import Chisel._
import freechips.rocketchip.config._
//import freechips.rocketchip.coreplex.HasMemoryBus
import freechips.rocketchip.subsystem.BaseSubsystem
import freechips.rocketchip.diplomacy.{LazyModule, LazyModuleImp, AddressRange}

case object MemoryMicrosemiDDR4Key extends Field[PolarFireEvalKitDDR4Params]

//trait HasMemoryPolarFireEvalKitDDR4 extends HasMemoryBus {
trait HasMemoryPolarFireEvalKitDDR4 { this: BaseSubsystem =>
  val module: HasMemoryPolarFireEvalKitDDR4ModuleImp

  val polarfireddrsubsys = LazyModule(new PolarFireEvalKitDDR4(p(MemoryMicrosemiDDR4Key)))

  polarfireddrsubsys.node := mbus.toDRAMController(Some("PolarFireDDR"))()
}

trait HasMemoryPolarFireEvalKitDDR4Bundle {
  val polarfireddrsubsys: PolarFireEvalKitDDR4IO
  def connectPolarFireEValKitDDR4ToPads(pads: PolarFireEvalKitDDR4Pads) {
    pads <> polarfireddrsubsys
  }
}

trait HasMemoryPolarFireEvalKitDDR4ModuleImp extends LazyModuleImp
    with HasMemoryPolarFireEvalKitDDR4Bundle {
  val outer: HasMemoryPolarFireEvalKitDDR4
  val ranges = AddressRange.fromSets(p(MemoryMicrosemiDDR4Key).address)
  require (ranges.size == 1, "DDR range must be contiguous")
  val depth = ranges.head.size
  val polarfireddrsubsys = IO(new PolarFireEvalKitDDR4IO(depth))

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
