package sifive.fpgashells.shell.altera

import chisel3._
import chisel3.experimental.dataview._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.prci._
import org.chipsalliance.cde.config._
import sifive.fpgashells.clocks._
import sifive.fpgashells.devices.altera.altera_datastorm_uniphy._
import sifive.fpgashells.devices.altera._
import sifive.fpgashells.shell._

class SysClockDatastormPlacedOverlay(val shell: DatastormShellBasicOverlays, name: String, val designInput: ClockInputDesignInput, val shellInput: ClockInputShellInput)
  extends SingleEndedClockInputAlteraPlacedOverlay(name, designInput, shellInput)
{
  val node = shell { ClockSourceNode(freqMHz = 50, jitterPS = 50) }

  shell { InModuleBody {
    val clk: Clock = io
    shell.io_tcl.addPackagePin(clk, "PIN_AF14")
    shell.io_tcl.addIOStandard(clk, "1.5-V")
  } }
}
class SysClockDatastormShellPlacer(val shell: DatastormShellBasicOverlays, val shellInput: ClockInputShellInput)(implicit val valName: ValName)
  extends ClockInputShellPlacer[DatastormShellBasicOverlays] {
  def place(designInput: ClockInputDesignInput) = new SysClockDatastormPlacedOverlay(shell, valName.name, designInput, shellInput)
}

object LEDDatastormPinConstraints{
  val pins = Seq("PIN_AC28", "PIN_AC29")
}
class LEDDatastormPlacedOverlay(val shell: DatastormShellBasicOverlays, name: String, val designInput: LEDDesignInput, val shellInput: LEDShellInput)
  extends LEDAlteraPlacedOverlay(name, designInput, shellInput, packagePin = Some(LEDDatastormPinConstraints.pins(shellInput.number)))
class LEDDatastormShellPlacer(val shell: DatastormShellBasicOverlays, val shellInput: LEDShellInput)(implicit val valName: ValName)
  extends LEDShellPlacer[DatastormShellBasicOverlays] {
  def place(designInput: LEDDesignInput) = new LEDDatastormPlacedOverlay(shell, valName.name, designInput, shellInput)
}

case object DatastormDDRSize extends Field[BigInt](0x40000000L * 1) // 1 GB
class DDRDatastormPlacedOverlay(val shell: DatastormShellBasicOverlays, name: String, val designInput: DDRDesignInput, val shellInput: DDRShellInput)
  extends DDRPlacedOverlay[AlteraDatastormUniphyPads](name, designInput, shellInput)
{
  val size = p(DatastormDDRSize)
  
  val migParams = AlteraDatastormUniphyParams(address = AddressSet.misaligned(di.baseAddress, size))
  val mig = LazyModule(new AlteraDatastormUniphy(migParams))
  def overlayOutput = DDROverlayOutput(ddr = mig.node)
  def ioFactory = new AlteraDatastormUniphyPads(size)

  shell { InModuleBody {
    require (shell.sys_clock.get.isDefined, "Use of DDRDatastormPlacedOverlay depends on SysClockDatastormPlacedOverlay")

    val port = mig.module.io.port
    
    io <> port.viewAsSupertype(new AlteraDatastormUniphyPads(mig.depth))
    port.ddr_fpga_pll_ref_clk_clk := shell.pllFactory.plls.getWrappedValue(0)._1.getInput
  } }
}

class DDRDatastormShellPlacer(val shell: DatastormShellBasicOverlays, val shellInput: DDRShellInput)(implicit val valName: ValName)
  extends DDRShellPlacer[DatastormShellBasicOverlays] {
  def place(designInput: DDRDesignInput) = new DDRDatastormPlacedOverlay(shell, valName.name, designInput, shellInput)
}

abstract class DatastormShellBasicOverlays()(implicit p: Parameters) extends AlteraShell {
  // Order matters; ddr depends on sys_clock
  val sys_clock = Overlay(ClockInputOverlayKey, new SysClockDatastormShellPlacer(this, ClockInputShellInput()))
  val led       = Seq.tabulate(2)(i => Overlay(LEDOverlayKey, new LEDDatastormShellPlacer(this, LEDShellInput(color = "green", number = i))(valName = ValName(s"led_$i"))))
  val ddr       = Overlay(DDROverlayKey, new DDRDatastormShellPlacer(this, DDRShellInput()))
}

class DatastormShell()(implicit p: Parameters) extends DatastormShellBasicOverlays
{
  val resetPin = InModuleBody { Wire(Bool()) }
  // PLL reset causes
  val pllReset = InModuleBody { Wire(Bool()) }

  val topDesign = LazyModule(p(DesignKey)(designParameters))

  // Place the sys_clock at the Shell if the user didn't ask for it
  p(ClockInputOverlayKey).foreach(_.place(ClockInputDesignInput()))
  override lazy val module = new Impl
  class Impl extends LazyRawModuleImp(this) {
    override def provideImplicitClockToLazyChildren = true

    val reset = IO(Input(Bool()))
    io_tcl.addPackagePin(reset, "PIN_AA21")
    io_tcl.addIOStandard(reset, "1.5-V")

    val sysclk: Clock = sys_clock.get() match {
      case Some(x: SysClockDatastormPlacedOverlay) => x.clock
    }

    resetPin := reset

    pllReset := !reset
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
