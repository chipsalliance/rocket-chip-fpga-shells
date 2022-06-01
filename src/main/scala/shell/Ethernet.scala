package sifive.fpgashells.shell

import chisel3._
import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy._
import sifive.blocks.devices.uart._
import freechips.rocketchip.subsystem.{BaseSubsystem, PeripheryBus, PeripheryBusKey}
import freechips.rocketchip.tilelink.TLBusWrapper
import freechips.rocketchip.interrupts.IntInwardNode

case class EthernetShellInput()
case class EthernetDesignInput()(implicit val p: Parameters)
case class EthernetOverlayOutput(eth: ModuleValue[EthernetPCS])
case object EthernetOverlayKey extends Field[Seq[DesignPlacer[EthernetDesignInput, EthernetShellInput, EthernetOverlayOutput]]](Nil)
trait EthernetShellPlacer[Shell] extends ShellPlacer[EthernetDesignInput, EthernetShellInput, EthernetOverlayOutput]

class EthernetPads extends Bundle {
  val tx_p = Output(Bool())
  val tx_n = Output(Bool())
  val rx_p = Input(Bool())
  val rx_n = Input(Bool())
  val refclk_p = Input(Clock())
  val refclk_n = Input(Clock())
}

class EthernetPCS extends Bundle {
  val rx_clock = Output(Clock())
  val rx_reset = Output(Reset())
  val rx_d = Output(UInt(64.W))
  val rx_c = Output(UInt(8.W))
  val tx_clock = Output(Clock())
  val tx_reset = Output(Reset())
  val tx_d = Input(UInt(64.W))
  val tx_c = Input(UInt(8.W))
  val loopback = Input(UInt(3.W))
  val rx_lock = Output(Bool())
  val sfp_detect = Output(Bool())
}

abstract class EthernetPlacedOverlay(
  val name: String, val di: EthernetDesignInput, val si: EthernetShellInput)
    extends IOPlacedOverlay[EthernetPads, EthernetDesignInput, EthernetShellInput, EthernetOverlayOutput]
{
  implicit val p = di.p

  val pcsPads = InModuleBody { Wire(new EthernetPCS) }

  def ioFactory = new EthernetPads
  def overlayOutput = EthernetOverlayOutput(eth = pcsPads)
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
