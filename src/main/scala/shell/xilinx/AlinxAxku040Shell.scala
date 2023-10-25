package sifive.fpgashells.shell.xilinx

import chisel3._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.prci._
import org.chipsalliance.cde.config._
import sifive.fpgashells.clocks._
import sifive.fpgashells.devices.xilinx.allinxaxku040mig.{AlinxAxku040MIG, AlinxAxku040MIGParams}
import sifive.fpgashells.ip.xilinx._
import sifive.fpgashells.ip.xilinx.alinx_axku040mig.AlinxAxku040MIGDDRPads
import sifive.fpgashells.shell._

class SysClockAlinxAxku040PlacedOverlay(
  val shell:       AlinxAxku040ShellBasicOverlays,
  name:            String,
  val designInput: ClockInputDesignInput,
  val shellInput:  ClockInputShellInput)
    extends LVDSClockInputXilinxPlacedOverlay(name, designInput, shellInput) {
  val node = shell { ClockSourceNode(freqMHz = 200, jitterPS = 1.2) }

  shell {
    InModuleBody {
      shell.xdc.addPackagePin(io.p, "AK17")
      shell.xdc.addPackagePin(io.n, "AK16")
      shell.xdc.addIOStandard(io.p, "LVDS")
      shell.xdc.addIOStandard(io.n, "LVDS")
    }
  }
}

class SysClockAlinxAxku040ShellPlacer(
  val shell:      AlinxAxku040ShellBasicOverlays,
  val shellInput: ClockInputShellInput
)(
  implicit val valName: ValName)
    extends ClockInputShellPlacer[AlinxAxku040ShellBasicOverlays] {
  override def place(designInput: ClockInputDesignInput) =
    new SysClockAlinxAxku040PlacedOverlay(shell, valName.name, designInput, shellInput)
}

class RefClockAlinxAxku040PlacedOverlay(val shell: AlinxAxku040ShellBasicOverlays, name: String, val designInput: ClockInputDesignInput, val shellInput: ClockInputShellInput)
extends LVDSClockInputXilinxPlacedOverlay(name, designInput, shellInput) {
  val node = shell { ClockSourceNode(freqMHz = 125.00, jitterPS = 1.2) }

  shell { InModuleBody {
    shell.xdc.addPackagePin(io.p, "AF6")
    shell.xdc.addPackagePin(io.n, "AF5")
    shell.xdc.addIOStandard(io.p, "LVDS")
    shell.xdc.addIOStandard(io.n, "LVDS")
  }}
}
class RefClockAlinxAxku040Placer(val shell: AlinxAxku040ShellBasicOverlays, val shellInput: ClockInputShellInput)(implicit val valName: ValName)
extends ClockInputShellPlacer[AlinxAxku040ShellBasicOverlays] {
  def place(designInput: ClockInputDesignInput) = new RefClockAlinxAxku040PlacedOverlay(shell, valName.name, designInput, shellInput)
}

class SPIFlashAlinxAxku040PlacedOverlay(val shell: AlinxAxku040ShellBasicOverlays, name: String, val designInput: SPIFlashDesignInput, val shellInput: SPIFlashShellInput)
extends SPIFlashXilinxPlacedOverlay(name, designInput, shellInput)
{
  shell { InModuleBody {
    val packagePinsWithIOs = Seq[(String, IOPin)](
      ("AA9", IOPin(io.qspi_sck)),
      ("U7", IOPin(io.qspi_cs)),
      ("AC7", IOPin(io.qspi_dq(0))),
      ("AB7", IOPin(io.qspi_dq(1))),
      ("AA7", IOPin(io.qspi_dq(2))),
      ("Y7", IOPin(io.qspi_dq(3)))
    )

    packagePinsWithIOs foreach { case (pin, io) =>
      shell.xdc.addPackagePin(io, pin)
      // 1.8V to 3.3V conversion is done on board
      shell.xdc.addIOStandard(io, "LVCMOS18")
    }

    packagePinsWithIOs.drop(1).map(_._2).foreach { io =>
      shell.xdc.addIOB(io)
      shell.xdc.addPullup(io)
    }
  }}
}
class SPIFlashAlinxAxku040Placer(val shell: AlinxAxku040ShellBasicOverlays, val shellInput: SPIFlashShellInput)(implicit val valName: ValName)
extends SPIFlashShellPlacer[AlinxAxku040ShellBasicOverlays] {
  def place(designInput: SPIFlashDesignInput) = new SPIFlashAlinxAxku040PlacedOverlay(shell, valName.name, designInput, shellInput)
}

object LEDAlinxAxku040PinConstraints {
  val pins = Seq("L20", "M20", "M21", "N21")
}
class LEDAlinxAxku040PlacedOverlay(
  val shell:       AlinxAxku040ShellBasicOverlays,
  name:            String,
  val designInput: LEDDesignInput,
  val shellInput:  LEDShellInput)
    extends LEDXilinxPlacedOverlay(
      name,
      designInput,
      shellInput,
      packagePin = Some(LEDAlinxAxku040PinConstraints.pins(shellInput.number)),
      ioStandard = "LVCMOS18"
    )
class LEDAlinxAxku040ShellPlacer(
  shell:          AlinxAxku040ShellBasicOverlays,
  val shellInput: LEDShellInput
)(
  implicit val valName: ValName)
    extends LEDShellPlacer[AlinxAxku040ShellBasicOverlays] {
  def place(designInput: LEDDesignInput) =
    new LEDAlinxAxku040PlacedOverlay(shell, valName.name, designInput, shellInput)
}

class JTAGDebugBScanAlinxAxku040PlacedOverlay(
  val shell:       AlinxAxku040ShellBasicOverlays,
  name:            String,
  val designInput: JTAGDebugBScanDesignInput,
  val shellInput:  JTAGDebugBScanShellInput)
    extends JTAGDebugBScanXilinxPlacedOverlay(name, designInput, shellInput)
class JTAGDebugBScanAlinxAxku040ShellPlacer(
  val shell:      AlinxAxku040ShellBasicOverlays,
  val shellInput: JTAGDebugBScanShellInput
)(
  implicit val valName: ValName)
    extends JTAGDebugBScanShellPlacer[AlinxAxku040ShellBasicOverlays] {
  def place(designInput: JTAGDebugBScanDesignInput) =
    new JTAGDebugBScanAlinxAxku040PlacedOverlay(shell, valName.name, designInput, shellInput)
}

class DDRAlinxAxku040PlacedOverlay(
  val shell:       AlinxAxku040ShellBasicOverlays,
  name:            String,
  val designInput: DDRDesignInput,
  val shellInput:  DDRShellInput)
    extends DDRPlacedOverlay[AlinxAxku040MIGDDRPads](name, designInput, shellInput) {
  private val mig = LazyModule(
    new AlinxAxku040MIG(AlinxAxku040MIGParams(address = AddressSet.misaligned(di.baseAddress, 0x40000000L * 2)))
  )

  override def overlayOutput: DDROverlayOutput = DDROverlayOutput(ddr = mig.node)
  override def ioFactory = new AlinxAxku040MIGDDRPads

  private val ddrIONode = BundleBridgeSource(() => mig.module.ddr4_port.cloneType)
  private val ddrIOSink = shell { ddrIONode.makeSink() }
  private val auxIONode = BundleBridgeSource(() => mig.module.auxio.cloneType)
  private val auxIOSink = shell { auxIONode.makeSink() }
  private val ddrUIClockNode = shell { ClockSourceNode(freqMHz = 200.0) }
  private val ddrBusAreset = shell { ClockSinkNode(Seq(ClockSinkParameters())) }

  val placedAuxIO = InModuleBody { Wire(mig.module.auxio.cloneType) }

  ddrBusAreset := designInput.wrangler := ddrUIClockNode

  InModuleBody {
    placedAuxIO := DontCare
    placedAuxIO <> mig.module.auxio
    ddrIONode.bundle <> mig.module.ddr4_port
    auxIONode.bundle <> mig.module.auxio
  }

  shell {
    InModuleBody {
      require(shell.sys_clock.get().isDefined, "Use of DDR overlay depends on System clock overlay")
      io <> ddrIOSink.bundle

      val (sys_clk, _) = shell.sys_clock.get().get.overlayOutput.node.out.head
      val (ui_node, _) = ddrUIClockNode.out.head
      val (areset_port, _) = ddrBusAreset.in.head
      val auxio = auxIOSink.bundle
      ui_node.clock := auxio.UIClock
      ui_node.reset := auxio.UISyncedReset

      auxio.sysClockInput := sys_clk.clock
      auxio.sysResetInput := sys_clk.reset
      auxio.AXIaresetn := !(areset_port.reset.asBool)

      // format: off
      val allddrpins = Seq(
        // ck_c, ck_t, cke, cs_n, act_n, odt
        "AE15", "AE16", "AJ16", "AE18", "AF18", "AG19",
        // adr
        "AG14", "AF17", "AF15", "AJ14", "AD18", "AG17", "AE17", "AK18",
        "AD16", "AH18", "AD19", "AD15", "AH16", "AL17", "AL15", "AL19",
        "AM19",
        // ba, bg
        "AG15", "AL18", "AJ15",
        // reset_n
        "AG16",
        // dqs_c
        "AH21", "AJ25", "AK20", "AP21", "AL28", "AP30", "AJ33", "AP34",
        // dqs_t
        "AG21", "AH24", "AJ20", "AP20", "AL27", "AN29", "AH33", "AN34",
        // dq
        "AE20", "AG20", "AF20", "AE22", "AD20", "AG22", "AF22", "AE23",
        "AF24", "AJ23", "AF23", "AH23", "AG25", "AJ24", "AG24", "AH22",
        "AK22", "AL22", "AM20", "AL23", "AK23", "AL25", "AL20", "AL24",
        "AM22", "AP24", "AN22", "AN24", "AN23", "AP25", "AP23", "AM24",
        "AM26", "AJ28", "AM27", "AK28", "AH27", "AH28", "AK26", "AK27",
        "AN28", "AM30", "AP28", "AM29", "AN27", "AL30", "AL29", "AP29",
        "AK31", "AH34", "AK32", "AJ31", "AJ30", "AH31", "AJ34", "AH32",
        "AN31", "AL34", "AN32", "AN33", "AM32", "AM34", "AP31", "AP33",
        // dm_dbi_n
        "AD21", "AE25", "AJ21", "AM21", "AH26", "AN26", "AJ29", "AL32",
      )

      (IOPin.of(io) zip allddrpins).foreach { case (io, pin) => shell.xdc.addPackagePin(io, pin) }
      // format: on
    }
  }
}
class DDRAlinxAxku040ShellPlacer(
  shell:          AlinxAxku040ShellBasicOverlays,
  val shellInput: DDRShellInput
)(
  implicit val valName: ValName)
    extends DDRShellPlacer[AlinxAxku040ShellBasicOverlays] {
  def place(designInput: DDRDesignInput) =
    new DDRAlinxAxku040PlacedOverlay(shell, valName.name, designInput, shellInput)
}

class I2CAlinxAxku040PlacedOverlay(val shell: AlinxAxku040ShellBasicOverlays, name: String, val designInput: I2CDesignInput, val shellInput: I2CShellInput)
  extends I2CXilinxPlacedOverlay(name, designInput, shellInput) {
  shell { InModuleBody {
    shell.xdc.addPackagePin(IOPin(io.sda), "P25")
    shell.xdc.addPackagePin(IOPin(io.scl), "P24")
    shell.xdc.addIOStandard(IOPin(io.sda), "LVCMOS18")
    shell.xdc.addIOStandard(IOPin(io.scl), "LVCMOS18")
  }}
}

class I2CAlinxAxku040ShellPlacer(val shell: AlinxAxku040ShellBasicOverlays, val shellInput: I2CShellInput)(implicit val valName: ValName)
extends I2CShellPlacer[AlinxAxku040ShellBasicOverlays] {
  def place(designInput: I2CDesignInput) = new I2CAlinxAxku040PlacedOverlay(shell, valName.name, designInput, shellInput)
}

abstract class AlinxAxku040ShellBasicOverlays()(implicit p: Parameters) extends UltraScaleShell {
  val sys_clock = Overlay(ClockInputOverlayKey, new SysClockAlinxAxku040ShellPlacer(this, ClockInputShellInput()))
  val gt_ref_clock = Overlay(ClockInputOverlayKey, new RefClockAlinxAxku040Placer(this, ClockInputShellInput()))
  val led = Seq.tabulate(4)(i =>
    Overlay(
      LEDOverlayKey,
      new LEDAlinxAxku040ShellPlacer(this, LEDShellInput(color = "red", number = i))(valName = ValName(s"led_$i"))
    )
  )
  val qspi = Overlay(SPIFlashOverlayKey, new SPIFlashAlinxAxku040Placer(this, SPIFlashShellInput()))
  val jtag_bscan =
    Overlay(JTAGDebugBScanOverlayKey, new JTAGDebugBScanAlinxAxku040ShellPlacer(this, JTAGDebugBScanShellInput()))
  val ddr = Overlay(DDROverlayKey, new DDRAlinxAxku040ShellPlacer(this, DDRShellInput()))
  val i2c = Overlay(I2COverlayKey, new I2CAlinxAxku040ShellPlacer(this, I2CShellInput()))

  val pllReset = InModuleBody { Wire(Bool()) }
}

class UARTAlinxAxku040FmcPlacedOverlay(val shell: AlinxAxku040ShellBasicOverlays, name: String, val designInput: UARTDesignInput, val shellInput: UARTShellInput)
extends UARTXilinxPlacedOverlay(name, designInput, shellInput, false) {
  shell { InModuleBody {
    val packagePinsAndIOs = Seq(
      ("T24", IOPin(io.rxd)), // LPC2_LA20_P (J2 36)
      ("T25", IOPin(io.txd))  // LPC2_LA20_N (J2 35)
    )

    packagePinsAndIOs.foreach { case (pin, io) =>
      shell.xdc.addPackagePin(io, pin)
      shell.xdc.addIOStandard(io, "LVCMOS18")
      shell.xdc.addIOB(io)
    }
  }}
}
class UARTAlinxAxku040FmcPlacer(val shell: AlinxAxku040ShellBasicOverlays, val shellInput: UARTShellInput)(implicit val valName: ValName)
extends UARTShellPlacer[AlinxAxku040ShellBasicOverlays] {
  override def place(designInput: UARTDesignInput) = new UARTAlinxAxku040FmcPlacedOverlay(shell, valName.name, designInput, shellInput)
}

class JTAGDebugAlinxAxku040FmcPlacedOverlay(val shell: AlinxAxku040ShellBasicOverlays, name: String, val designInput: JTAGDebugDesignInput, val shellInput: JTAGDebugShellInput)
extends JTAGDebugXilinxPlacedOverlay(name, designInput, shellInput)
{
  shell { InModuleBody {
    val packagePinsAndIOs = Seq(
      ("AH13", IOPin(io.jtag_TDI)), // LPC2_LA06_P (J3 34)
      ("AJ13", IOPin(io.jtag_TDO)), // LPC2_LA06_N
      ("AF10", IOPin(io.jtag_TCK)), // LPC2_LA01_P (J3 36, clock capable)
      ("AG10", IOPin(io.jtag_TMS))  // LPC2_LA01_N
    )

    shell.sdc.addClock("jtag_TCK", IOPin(io.jtag_TCK), 10.0)
    shell.sdc.addGroup(clocks = Seq("jtag_TCK"))

    packagePinsAndIOs.foreach { case (pin, io) =>
      shell.xdc.addPackagePin(io, pin)
      shell.xdc.addIOStandard(io, "LVCMOS18")
      shell.xdc.addPullup(io)
      shell.xdc.addIOB(io)
    }
  }}
}
class JTAGDebugAlinxAxku040FmcPlacer(val shell: AlinxAxku040ShellBasicOverlays, val shellInput: JTAGDebugShellInput)(implicit val valName: ValName)
extends JTAGDebugShellPlacer[AlinxAxku040ShellBasicOverlays] {
  override def place(designInput: JTAGDebugDesignInput) = new JTAGDebugAlinxAxku040FmcPlacedOverlay(shell, valName.name, designInput, shellInput)
}

// HACK: pull the enable pin of the chip powers the FMC connector
case object FmcPwrEnableOverlayKey extends Field[Seq[DesignPlacer[Null, Null, Null]]](Nil)

class AlinxAxku040Fmc2PwrEnableOverlay(val shell: AlinxAxku040ShellBasicOverlays, val name: String, val designInput: Null = null, val shellInput: Null = null)
extends IOPlacedOverlay[Bool, Null, Null, Null] {
  override def ioFactory: Bool = Bool()
  override def overlayOutput: Null = null

  shell { InModuleBody {
    shell.xdc.addPackagePin(IOPin(io), "L17")
    shell.xdc.addIOStandard(IOPin(io), "LVCMOS18")
    io := true.B
  }}
}

class AlinxAxku040Fmc2PwrEnablePlacer(val shell: AlinxAxku040ShellBasicOverlays, val shellInput: Null = null)(implicit val valName: ValName)
extends ShellPlacer[Null, Null, Null] {
  override def place(di: Null) = new AlinxAxku040Fmc2PwrEnableOverlay(shell, valName.name)
}

class AlinxAxku040Shell()(implicit p: Parameters) extends AlinxAxku040ShellBasicOverlays {
  val uart_fmc = Overlay(UARTOverlayKey, new UARTAlinxAxku040FmcPlacer(this, UARTShellInput()))
  val jtag_fmc = Overlay(JTAGDebugOverlayKey, new JTAGDebugAlinxAxku040FmcPlacer(this, JTAGDebugShellInput()))
  val pwr_fmc = Overlay(FmcPwrEnableOverlayKey, new AlinxAxku040Fmc2PwrEnablePlacer(this, null))
  val topDesign = LazyModule(p(DesignKey)(designParameters))

  // Place the sys_clock at the Shell if the user didn't ask for it
  designParameters(ClockInputOverlayKey).foreach { unused =>
    if (unused.name == "sys_clock") {
      val source = unused.place(ClockInputDesignInput()).overlayOutput.node
      val sink = ClockSinkNode(Seq(ClockSinkParameters()))
      sink := source
    }
  }

  designParameters(FmcPwrEnableOverlayKey).foreach { unused =>
    unused.place(null)
  }

  override lazy val module = new LazyRawModuleImp(this) {
    override def provideImplicitClockToLazyChildren = true
    val sysclk = sys_clock.get() match {
      case Some(x: SysClockAlinxAxku040PlacedOverlay) => x.clock
    }

    val powerOnReset: Bool = PowerOnResetFPGAOnly(sysclk)
    sdc.addAsyncPath(Seq(powerOnReset))

    pllReset := powerOnReset
  }
}
