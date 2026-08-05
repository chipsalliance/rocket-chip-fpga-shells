package sifive.fpgashells.shell.xilinx

import chisel3._
import chisel3.experimental.dataview._
import freechips.rocketchip.diplomacy.AddressSet
import freechips.rocketchip.prci._
import org.chipsalliance.cde.config._
import org.chipsalliance.diplomacy._
import org.chipsalliance.diplomacy.lazymodule._
import sifive.fpgashells.devices.xilinx.xilinxgenesys2mig._
import sifive.fpgashells.ip.xilinx._
import sifive.fpgashells.shell._

class SysClockGenesys2PlacedOverlay(val shell: Genesys2ShellBasicOverlays, name: String, val designInput: ClockInputDesignInput, val shellInput: ClockInputShellInput)
  extends LVDSClockInputXilinxPlacedOverlay(name, designInput, shellInput)
{
  val node = shell { ClockSourceNode(freqMHz = 200, jitterPS = 50)(ValName(name)) }
  shell { InModuleBody {
    shell.xdc.addPackagePin(io.p, "AD12")
    shell.xdc.addPackagePin(io.n, "AD11")
    shell.xdc.addIOStandard(io.p, "LVDS")
    shell.xdc.addIOStandard(io.n, "LVDS")
  }}
}
class SysClockGenesys2ShellPlacer(val shell: Genesys2ShellBasicOverlays, val shellInput: ClockInputShellInput)(implicit val valName: ValName)
  extends ClockInputShellPlacer[Genesys2ShellBasicOverlays] {
  def place(designInput: ClockInputDesignInput) = new SysClockGenesys2PlacedOverlay(shell, valName.name, designInput, shellInput)
}

// [CAUTION] PMOD JA, cJTAG also uses PMOD JA
class JTAGDebugGenesys2PlacedOverlay(val shell: Genesys2ShellBasicOverlays, name: String, val designInput: JTAGDebugDesignInput, val shellInput: JTAGDebugShellInput)
  extends JTAGDebugXilinxPlacedOverlay(name, designInput, shellInput)
{
  shell { InModuleBody {
    shell.sdc.addClock("JTCK", IOPin(io.jtag_TCK), 10)
    shell.sdc.addGroup(clocks = Seq("JTCK"))
    shell.xdc.clockDedicatedRouteFalse(IOPin(io.jtag_TCK))
    val packagePinsWithPackageIOs = Seq(
      ("T26", IOPin(io.jtag_TCK)),  // Sch=ja[2]
      ("T23", IOPin(io.jtag_TMS)),  // Sch=ja[5]
      ("T22",  IOPin(io.jtag_TDI)), // Sch=ja[4]
      ("U27", IOPin(io.jtag_TDO)),  // Sch=ja[0]
      ("U28", IOPin(io.srst_n)))    // Sch=ja[1]

    packagePinsWithPackageIOs foreach { case (pin, io) =>
      shell.xdc.addPackagePin(io, pin)
      shell.xdc.addIOStandard(io, "LVCMOS33")
      shell.xdc.addPullup(io)
    }
  } }
}
class JTAGDebugGenesys2ShellPlacer(val shell: Genesys2ShellBasicOverlays, val shellInput: JTAGDebugShellInput)(implicit val valName: ValName)
  extends JTAGDebugShellPlacer[Genesys2ShellBasicOverlays] {
  def place(designInput: JTAGDebugDesignInput) = new JTAGDebugGenesys2PlacedOverlay(shell, valName.name, designInput, shellInput)
}

// [CAUTION] PMOD JA, JTAG also uses PMOD JA
class cJTAGDebugGenesys2PlacedOverlay(val shell: Genesys2ShellBasicOverlays, name: String, val designInput: cJTAGDebugDesignInput, val shellInput: cJTAGDebugShellInput)
  extends cJTAGDebugXilinxPlacedOverlay(name, designInput, shellInput)
{
  shell { InModuleBody {
    shell.sdc.addClock("JTCKC", IOPin(io.cjtag_TCKC), 10)
    shell.sdc.addGroup(clocks = Seq("JTCKC"))
    shell.xdc.clockDedicatedRouteFalse(IOPin(io.cjtag_TCKC))
    val packagePinsWithPackageIOs = Seq(
      ("T26", IOPin(io.cjtag_TCKC)), // Sch=ja[2]
      ("T23", IOPin(io.cjtag_TMSC)), // Sch=ja[5]
      ("T22", IOPin(io.srst_n)))     // Sch=ja[4]

    packagePinsWithPackageIOs foreach { case (pin, io) =>
      shell.xdc.addPackagePin(io, pin)
      shell.xdc.addIOStandard(io, "LVCMOS33")
    }
      shell.xdc.addPullup(IOPin(io.cjtag_TCKC))
      shell.xdc.addPullup(IOPin(io.srst_n))
  } }
}
class cJTAGDebugGenesys2ShellPlacer(val shell: Genesys2ShellBasicOverlays, val shellInput: cJTAGDebugShellInput)(implicit val valName: ValName)
  extends cJTAGDebugShellPlacer[Genesys2ShellBasicOverlays] {
  def place(designInput: cJTAGDebugDesignInput) = new cJTAGDebugGenesys2PlacedOverlay(shell, valName.name, designInput, shellInput)
}

// PMOD JC
class SDIOGenesys2PlacedOverlay(val shell: Genesys2ShellBasicOverlays, name: String, val designInput: SPIDesignInput, val shellInput: SPIShellInput)
  extends SDIOXilinxPlacedOverlay(name, designInput, shellInput)
{
  shell { InModuleBody {
    val packagePinsWithPackageIOs = Seq(
      ("AK29", IOPin(io.spi_clk)),    // Sch=jc[3]
      ("AJ27", IOPin(io.spi_cs)),     // Sch=jc[1]
      ("AH30", IOPin(io.spi_dat(0))), // Sch=jc[2]
      ("AD26", IOPin(io.spi_dat(1))), // Sch=jc[4]
      ("AG30", IOPin(io.spi_dat(2))), // Sch=jc[5]
      ("AC26", IOPin(io.spi_dat(3)))) // Sch=jc[0]

    packagePinsWithPackageIOs foreach { case (pin, io) =>
      shell.xdc.addPackagePin(io, pin)
      shell.xdc.addIOStandard(io, "LVCMOS33")
      shell.xdc.addIOB(io)
    }
    packagePinsWithPackageIOs drop 1 foreach { case (pin, io) =>
      shell.xdc.addPullup(io)
    }
  } }
}
class SDIOGenesys2ShellPlacer(val shell: Genesys2ShellBasicOverlays, val shellInput: SPIShellInput)(implicit val valName: ValName)
  extends SPIShellPlacer[Genesys2ShellBasicOverlays] {
  def place(designInput: SPIDesignInput) = new SDIOGenesys2PlacedOverlay(shell, valName.name, designInput, shellInput)
}

class SPIFlashGenesys2PlacedOverlay(val shell: Genesys2ShellBasicOverlays, name: String, val designInput: SPIFlashDesignInput, val shellInput: SPIFlashShellInput)
  extends SPIFlashXilinxPlacedOverlay(name, designInput, shellInput)
{
  shell { InModuleBody {
    val packagePinsWithPackageIOs = Seq(
      ("B10", IOPin(io.qspi_sck)),   // Sch=CCLK
      ("U19", IOPin(io.qspi_cs)),    // Sch=qspi_csn
      ("P24", IOPin(io.qspi_dq(0))), // Sch=qspi_d[0]
      ("R25", IOPin(io.qspi_dq(1))), // Sch=qspi_d[1]
      ("R20", IOPin(io.qspi_dq(2))), // Sch=qspi_d[2]
      ("R21", IOPin(io.qspi_dq(3)))) // Sch=qspi_d[3]

    packagePinsWithPackageIOs foreach { case (pin, io) =>
      shell.xdc.addPackagePin(io, pin)
      shell.xdc.addIOStandard(io, "LVCMOS33")
    }
    packagePinsWithPackageIOs drop 1 foreach { case (pin, io) =>
      shell.xdc.addPullup(io)
    }
  } }
}
class SPIFlashGenesys2ShellPlacer(val shell: Genesys2ShellBasicOverlays, val shellInput: SPIFlashShellInput)(implicit val valName: ValName)
  extends SPIFlashShellPlacer[Genesys2ShellBasicOverlays] {
  def place(designInput: SPIFlashDesignInput) = new SPIFlashGenesys2PlacedOverlay(shell, valName.name, designInput, shellInput)
}

// PMOD JD
class TracePMODGenesys2PlacedOverlay(val shell: Genesys2ShellBasicOverlays, name: String, val designInput: TracePMODDesignInput, val shellInput: TracePMODShellInput)
  extends TracePMODXilinxPlacedOverlay(name, designInput, shellInput, packagePins = Seq(
    "V27", // Sch=jd[0]
    "U24", // Sch=jd[4]
    "Y30", // Sch=jd[1]
    "Y26", // Sch=jd[5]
    "V24", // Sch=jd[2]
    "V22", // Sch=jd[6]
    "W22", // Sch=jd[3]
    "W21"  // Sch=jd[7]
  )
)
class TracePMODGenesys2ShellPlacer(val shell: Genesys2ShellBasicOverlays, val shellInput: TracePMODShellInput)(implicit val valName: ValName)
  extends TracePMODShellPlacer[Genesys2ShellBasicOverlays] {
  def place(designInput: TracePMODDesignInput) = new TracePMODGenesys2PlacedOverlay(shell, valName.name, designInput, shellInput)
}

// PMOD JB used for GPIO
class GPIOPMODGenesys2PlacedOverlay(val shell: Genesys2ShellBasicOverlays, name: String, val designInput: GPIOPMODDesignInput, val shellInput: GPIOPMODShellInput)
  extends GPIOPMODXilinxPlacedOverlay(name, designInput, shellInput)
{
  shell { InModuleBody {
    val packagePinsWithPackageIOs = Seq(
      ("V29", IOPin(io.gpio_pmod_0)), // Sch=jb[0]
      ("V30", IOPin(io.gpio_pmod_1)), // Sch=jb[1]
      ("V25", IOPin(io.gpio_pmod_2)), // Sch=jb[2]
      ("W26", IOPin(io.gpio_pmod_3)), // Sch=jb[3]
      ("T25", IOPin(io.gpio_pmod_4)), // Sch=jb[4]
      ("U25", IOPin(io.gpio_pmod_5)), // Sch=jb[5]
      ("U22", IOPin(io.gpio_pmod_6)), // Sch=jb[6]
      ("U23", IOPin(io.gpio_pmod_7))) // Sch=jb[7]

    packagePinsWithPackageIOs foreach { case (pin, io) =>
      shell.xdc.addPackagePin(io, pin)
      shell.xdc.addIOStandard(io, "LVCMOS33")
    }
    packagePinsWithPackageIOs drop 7 foreach { case (pin, io) =>
      shell.xdc.addPullup(io)
    }
  } }
}
class GPIOPMODGenesys2ShellPlacer(val shell: Genesys2ShellBasicOverlays, val shellInput: GPIOPMODShellInput)(implicit val valName: ValName)
  extends GPIOPMODShellPlacer[Genesys2ShellBasicOverlays] {
  def place(designInput: GPIOPMODDesignInput) = new GPIOPMODGenesys2PlacedOverlay(shell, valName.name, designInput, shellInput)
}

class UARTGenesys2PlacedOverlay(val shell: Genesys2ShellBasicOverlays, name: String, val designInput: UARTDesignInput, val shellInput: UARTShellInput)
  extends UARTXilinxPlacedOverlay(name, designInput, shellInput, false)
{
  shell { InModuleBody {
    val packagePinsWithPackageIOs = Seq(
      ("Y20", IOPin(io.rxd)), // Sch=uart_tx_in
      ("Y23", IOPin(io.txd))  // Sch=uart_rx_out
    )

    packagePinsWithPackageIOs foreach { case (pin, io) =>
      shell.xdc.addPackagePin(io, pin)
      shell.xdc.addIOStandard(io, "LVCMOS33")
      shell.xdc.addIOB(io)
    }
  } }
}
class UARTGenesys2ShellPlacer(val shell: Genesys2ShellBasicOverlays, val shellInput: UARTShellInput)(implicit val valName: ValName)
  extends UARTShellPlacer[Genesys2ShellBasicOverlays] {
  def place(designInput: UARTDesignInput) = new UARTGenesys2PlacedOverlay(shell, valName.name, designInput, shellInput)
}

// 8 LEDs
object LEDGenesys2PinConstraints{
  val pins = Seq(
    "T28", // Sch=led[0]
    "V19", // Sch=led[1]
    "U30", // Sch=led[2]
    "U29", // Sch=led[3]
    "V20", // Sch=led[4]
    "V26", // Sch=led[5]
    "W24", // Sch=led[6]
    "W23"  // Sch=led[7]
  )
}
class LEDGenesys2PlacedOverlay(val shell: Genesys2ShellBasicOverlays, name: String, val designInput: LEDDesignInput, val shellInput: LEDShellInput)
  extends LEDXilinxPlacedOverlay(name, designInput, shellInput, packagePin = Some(LEDGenesys2PinConstraints.pins(shellInput.number)))
class LEDGenesys2ShellPlacer(val shell: Genesys2ShellBasicOverlays, val shellInput: LEDShellInput)(implicit val valName: ValName)
  extends LEDShellPlacer[Genesys2ShellBasicOverlays] {
  def place(designInput: LEDDesignInput) = new LEDGenesys2PlacedOverlay(shell, valName.name, designInput, shellInput)
}

// 8 Switches
object SwitchGenesys2PinConstraints{
  val pins = Seq(
    "G19", // Sch=sw[0]
    "G25", // Sch=sw[1]
    "H24", // Sch=sw[2]
    "K19", // Sch=sw[3]
    "N19", // Sch=sw[4]
    "P19", // Sch=sw[5]
    "P26", // Sch=sw[6]
    "P27"  // Sch=sw[7]
  )
}
class SwitchGenesys2PlacedOverlay(val shell: Genesys2ShellBasicOverlays, name: String, val designInput: SwitchDesignInput, val shellInput: SwitchShellInput)
  extends SwitchXilinxPlacedOverlay(
    name,
    designInput,
    shellInput,
    packagePin = Some(SwitchGenesys2PinConstraints.pins(shellInput.number)),
    ioStandard = if (shellInput.number < 6) "LVCMOS12" else "LVCMOS33")
class SwitchGenesys2ShellPlacer(val shell: Genesys2ShellBasicOverlays, val shellInput: SwitchShellInput)(implicit val valName: ValName)
  extends SwitchShellPlacer[Genesys2ShellBasicOverlays] {
  def place(designInput: SwitchDesignInput) = new SwitchGenesys2PlacedOverlay(shell, valName.name, designInput, shellInput)
}

// 5 Buttons (cpu_resetn excluded)
object ButtonGenesys2PinConstraints {
  val pins = Seq(
    "E18", // Sch=btnc
    "M19", // Sch=btnd
    "M20", // Sch=btnl
    "C19", // Sch=btnr
    "B19"  // Sch=btnu
  )
}
class ButtonGenesys2PlacedOverlay(val shell: Genesys2ShellBasicOverlays, name: String, val designInput: ButtonDesignInput, val shellInput: ButtonShellInput)
  extends ButtonXilinxPlacedOverlay(
    name,
    designInput,
    shellInput,
    packagePin = Some(ButtonGenesys2PinConstraints.pins(shellInput.number)),
    ioStandard = "LVCMOS12")
class ButtonGenesys2ShellPlacer(val shell: Genesys2ShellBasicOverlays, val shellInput: ButtonShellInput)(implicit val valName: ValName)
  extends ButtonShellPlacer[Genesys2ShellBasicOverlays] {
  def place(designInput: ButtonDesignInput) = new ButtonGenesys2PlacedOverlay(shell, valName.name, designInput, shellInput)
}

class JTAGDebugBScanGenesys2PlacedOverlay(val shell: Genesys2ShellBasicOverlays, name: String, val designInput: JTAGDebugBScanDesignInput, val shellInput: JTAGDebugBScanShellInput)
 extends JTAGDebugBScanXilinxPlacedOverlay(name, designInput, shellInput)
class JTAGDebugBScanGenesys2ShellPlacer(val shell: Genesys2ShellBasicOverlays, val shellInput: JTAGDebugBScanShellInput)(implicit val valName: ValName)
  extends JTAGDebugBScanShellPlacer[Genesys2ShellBasicOverlays] {
  def place(designInput: JTAGDebugBScanDesignInput) = new JTAGDebugBScanGenesys2PlacedOverlay(shell, valName.name, designInput, shellInput)
}

case object Genesys2DDRSize extends Field[BigInt](0x40000000L) // 1 GiB
class DDRGenesys2PlacedOverlay(val shell: Genesys2ShellBasicOverlays, name: String, val designInput: DDRDesignInput, val shellInput: DDRShellInput)
  extends DDRPlacedOverlay[XilinxGenesys2MIGPads](name, designInput, shellInput)
{
  val size = p(Genesys2DDRSize)

  val ddrClk1 = shell { ClockSinkNode(freqMHz = 100)}
  val ddrClk2 = shell { ClockSinkNode(freqMHz = 200)}
  val ddrGroup = shell { ClockGroup() }
  ddrClk1 := di.wrangler := ddrGroup := di.corePLL
  ddrClk2 := di.wrangler := ddrGroup
  
  val migParams = XilinxGenesys2MIGParams(address = AddressSet.misaligned(di.baseAddress, size))
  val mig = LazyModule(new XilinxGenesys2MIG(migParams))
  val ddrUI     = shell { ClockSourceNode(freqMHz = 200) }
  val areset    = shell { ClockSinkNode(Seq(ClockSinkParameters())) }
  areset := di.wrangler := ddrUI

  def overlayOutput = DDROverlayOutput(ddr = mig.node)
  def ioFactory = new XilinxGenesys2MIGPads(size)

  shell { InModuleBody {
    require (shell.sys_clock.get.isDefined, "Use of DDRGenesys2PlacedOverlay depends on SysClockGenesys2PlacedOverlay")
    val (sys, _) = shell.sys_clock.get.get.overlayOutput.node.out(0)
    val (ui, _) = ddrUI.out(0)
    val (dclk1, _) = ddrClk1.in(0)
    val (dclk2, _) = ddrClk2.in(0)
    val (ar, _) = areset.in(0)
    val port = mig.module.io.port
    
    io <> port.viewAsSupertype(new XilinxGenesys2MIGPads(mig.depth))
    ui.clock := port.ui_clk
    ui.reset := !port.mmcm_locked || port.ui_clk_sync_rst
    port.sys_clk_i := dclk1.clock.asUInt
    port.clk_ref_i := dclk2.clock.asUInt
    port.sys_rst := shell.pllReset
    port.aresetn := !(ar.reset.asBool)
  } }

  shell.sdc.addGroup(clocks = Seq("clk_pll_i"), pins = Seq(mig.island.module.blackbox.io.ui_clk))
}
class DDRGenesys2ShellPlacer(val shell: Genesys2ShellBasicOverlays, val shellInput: DDRShellInput)(implicit val valName: ValName)
  extends DDRShellPlacer[Genesys2ShellBasicOverlays] {
  def place(designInput: DDRDesignInput) = new DDRGenesys2PlacedOverlay(shell, valName.name, designInput, shellInput)
}

// Core to shell external resets
class CTSResetGenesys2PlacedOverlay(val shell: Genesys2ShellBasicOverlays, name: String, val designInput: CTSResetDesignInput, val shellInput: CTSResetShellInput)
  extends CTSResetPlacedOverlay(name, designInput, shellInput)
class CTSResetGenesys2ShellPlacer(val shell: Genesys2ShellBasicOverlays, val shellInput: CTSResetShellInput)(implicit val valName: ValName)
  extends CTSResetShellPlacer[Genesys2ShellBasicOverlays] {
  def place(designInput: CTSResetDesignInput) = new CTSResetGenesys2PlacedOverlay(shell, valName.name, designInput, shellInput)
}

// Optional DDR
case object Genesys2ShellDDR extends Field[Boolean](true)
class WithNoGenesys2ShellDDR extends Config((site, here, up) => {
  case Genesys2ShellDDR => false
})

abstract class Genesys2ShellBasicOverlays()(implicit p: Parameters) extends Series7Shell {
  // Order matters; ddr depends on sys_clock
  val sys_clock = Overlay(ClockInputOverlayKey, new SysClockGenesys2ShellPlacer(this, ClockInputShellInput()))
  val led       = Seq.tabulate(8)(i => Overlay(LEDOverlayKey, new LEDGenesys2ShellPlacer(this, LEDMetas(i))(valName = ValName(s"led_$i"))))
  val switch    = Seq.tabulate(8)(i => Overlay(SwitchOverlayKey, new SwitchGenesys2ShellPlacer(this, SwitchShellInput(number = i))(valName = ValName(s"switch_$i"))))
  val button    = Seq.tabulate(5)(i => Overlay(ButtonOverlayKey, new ButtonGenesys2ShellPlacer(this, ButtonShellInput(number = i))(valName = ValName(s"button_$i"))))
  val ddr       = if (p(Genesys2ShellDDR)) Some(Overlay(DDROverlayKey, new DDRGenesys2ShellPlacer(this, DDRShellInput()))) else None
  val uart      = Overlay(UARTOverlayKey, new UARTGenesys2ShellPlacer(this, UARTShellInput()))
  val sdio      = Overlay(SPIOverlayKey, new SDIOGenesys2ShellPlacer(this, SPIShellInput()))
  val jtag      = Overlay(JTAGDebugOverlayKey, new JTAGDebugGenesys2ShellPlacer(this, JTAGDebugShellInput()))
  val cjtag     = Overlay(cJTAGDebugOverlayKey, new cJTAGDebugGenesys2ShellPlacer(this, cJTAGDebugShellInput()))
  val spi_flash = Overlay(SPIFlashOverlayKey, new SPIFlashGenesys2ShellPlacer(this, SPIFlashShellInput()))
  val cts_reset = Overlay(CTSResetOverlayKey, new CTSResetGenesys2ShellPlacer(this, CTSResetShellInput()))
  val jtagBScan = Overlay(JTAGDebugBScanOverlayKey, new JTAGDebugBScanGenesys2ShellPlacer(this, JTAGDebugBScanShellInput()))

  def LEDMetas(i: Int): LEDShellInput =
    LEDShellInput(
      color = "white", // There are no RGB LEDs on Nexys Video board
      rgb = false,
      number = i
    )
}

class Genesys2Shell()(implicit p: Parameters) extends Genesys2ShellBasicOverlays
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
    xdc.addBoardPin(reset, "reset")

    val reset_ibuf = Module(new IBUF)
    reset_ibuf.io.I := reset
    val sysclk: Clock = sys_clock.get() match {
      case Some(x: SysClockGenesys2PlacedOverlay) => x.clock
    }
    val powerOnReset = PowerOnResetFPGAOnly(sysclk)
    sdc.addAsyncPath(Seq(powerOnReset))

    resetPin := reset_ibuf.io.O

    pllReset := (!reset_ibuf.io.O) || powerOnReset //Genesys2 is active low reset
  }
}

class Genesys2ShellGPIOPMOD()(implicit p: Parameters) extends Genesys2ShellBasicOverlays
//This is the Shell used for coreip Genesys2 builds, with GPIOS and trace signals on the pmods
{
  // PLL reset causes
  val pllReset = InModuleBody { Wire(Bool()) }

  val gpio_pmod = Overlay(GPIOPMODOverlayKey, new GPIOPMODGenesys2ShellPlacer(this, GPIOPMODShellInput()))
  val trace_pmod = Overlay(TracePMODOverlayKey, new TracePMODGenesys2ShellPlacer(this, TracePMODShellInput()))

  val topDesign = LazyModule(p(DesignKey)(designParameters))

  // Place the sys_clock at the Shell if the user didn't ask for it
  p(ClockInputOverlayKey).foreach(_.place(ClockInputDesignInput()))

  override lazy val module = new LazyRawModuleImp(this) {

    override def provideImplicitClockToLazyChildren = true
    val reset = IO(Input(Bool()))
    xdc.addBoardPin(reset, "reset")

    val reset_ibuf = Module(new IBUF)
    reset_ibuf.io.I := reset

    val sysclk: Clock = sys_clock.get() match {
      case Some(x: SysClockGenesys2PlacedOverlay) => x.clock
    }
    val powerOnReset = PowerOnResetFPGAOnly(sysclk)
    sdc.addAsyncPath(Seq(powerOnReset))
    val ctsReset: Bool = cts_reset.get() match {
      case Some(x: CTSResetGenesys2PlacedOverlay) => x.designInput.rst
      case None => false.B
    }

    pllReset := (!reset_ibuf.io.O) || powerOnReset || ctsReset // Genesys2 is active low reset
  }
}
