package sifive.fpgashells.shell

import chisel3._
import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.tilelink._
import freechips.rocketchip.interrupts._
import sifive.fpgashells.clocks._

case class PCIeShellInput()
case class PCIeDesignInput(
  wrangler: ClockAdapterNode,
  bars: Seq[AddressSet] = Seq(AddressSet(0x40000000L, 0x1FFFFFFFL)),
  ecam: BigInt = 0x2000000000L,
  bases: Seq[BigInt] = Nil, // remap bars to these PCIe base addresses
  corePLL: PLLNode)(
  implicit val p: Parameters)

case class PCIeOverlayOutput(
  pcieNode: TLNode,
  intNode: IntOutwardNode)
trait PCIeShellPlacer[Shell] extends ShellPlacer[PCIeDesignInput, PCIeShellInput, PCIeOverlayOutput]

case object PCIeOverlayKey extends Field[Seq[DesignPlacer[PCIeDesignInput, PCIeShellInput, PCIeOverlayOutput]]](Nil)

abstract class PCIePlacedOverlay[IO <: Data](
  val name: String, val di: PCIeDesignInput, val si: PCIeShellInput)
    extends IOPlacedOverlay[IO, PCIeDesignInput, PCIeShellInput, PCIeOverlayOutput]
{
  implicit val p = di.p
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
