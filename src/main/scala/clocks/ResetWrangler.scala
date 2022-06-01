package sifive.fpgashells.clocks

import chisel3._
import chisel3.util._
import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.util._

class ResetWrangler(debounceNs: Double = 100000)(implicit p: Parameters) extends LazyModule
{
  val node = ClockAdapterNode()

  lazy val module = new LazyRawModuleImp(this) {
    val (in, _) = node.in.unzip
    val (out, _) = node.out.unzip

    val status = IO(Output(UInt(in.size.W)))
    status := Cat(in.map(_.reset).reverse)

    val causes = in.map(_.reset).foldLeft(false.B)(_ || _)
    val (slowIn, slowEdge) = node.in.minBy(_._2.clock.freqMHz)
    val slowPeriodNs = 1000 / slowEdge.clock.freqMHz
    val slowTicks = math.ceil(debounceNs/slowPeriodNs).toInt max 7
    val slowBits = log2Ceil(slowTicks+1)

    // debounce
    val increment = Wire(Bool())
    val incremented = Wire(UInt(slowBits.W))
    val debounced = withClockAndReset(slowIn.clock, causes) {
      AsyncResetReg(incremented, 0, increment, Some("debounce"))
    }
    increment := debounced =/= slowTicks.U
    incremented := debounced + 1.U
    val deglitched = AsyncResetReg(increment, slowIn.clock, causes, true, Some("deglitch"))

    // catch and sync increment to each domain
    (in zip out) foreach { case (i, o) =>
      o.clock := i.clock
      o.reset := ResetCatchAndSync(o.clock, deglitched)
    }
  }
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
