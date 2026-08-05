package sifive.fpgashells.shell.xilinx

import chisel3._
import chisel3.experimental.dataview._
import freechips.rocketchip.diplomacy.AddressSet
import freechips.rocketchip.prci._
import org.chipsalliance.cde.config._
import org.chipsalliance.diplomacy._
import org.chipsalliance.diplomacy.lazymodule._
import sifive.fpgashells.devices.xilinx.xilinxzcu104mig._
import sifive.fpgashells.ip.xilinx._
import sifive.fpgashells.ip.xilinx.zcu104mig._
import sifive.fpgashells.shell._

class SysClockZCU104PlacedOverlay(val shell: ZCU104ShellBasicOverlays, name: String, val designInput: ClockInputDesignInput, val shellInput: ClockInputShellInput)
  extends LVDSClockInputXilinxPlacedOverlay(name, designInput, shellInput) 
{
  //pg 44 of ZCU104 Board User Guide, sheet 5 of ZCU104 schematic
  val node = shell { ClockSourceNode(freqMHz = 300, jitterPS = 50)(ValName(name)) }
  shell { InModuleBody {
    shell.xdc.addPackagePin(io.p, "AH18")
    shell.xdc.addPackagePin(io.n, "AH17")
    shell.xdc.addIOStandard(io.p, "DIFF_SSTL12")
    shell.xdc.addIOStandard(io.n, "DIFF_SSTL12")
  }}
}
class SysClockZCU104ShellPlacer(shell: ZCU104ShellBasicOverlays, val shellInput: ClockInputShellInput)(implicit val valName: ValName)
  extends ClockInputShellPlacer[ZCU104ShellBasicOverlays]
{
    def place(designInput: ClockInputDesignInput) = new SysClockZCU104PlacedOverlay(shell, valName.name, designInput, shellInput)
}

class RefClockZCU104PlacedOverlay(val shell: ZCU104ShellBasicOverlays, name: String, val designInput: ClockInputDesignInput, val shellInput: ClockInputShellInput)
  extends LVDSClockInputXilinxPlacedOverlay(name, designInput, shellInput) 
{
  //pg 44 of ZCU104 Board User Guide, sheet 4 of ZCU104 schematic
  val node = shell { ClockSourceNode(freqMHz = 125, jitterPS = 50)(ValName(name)) }
  
  shell { InModuleBody {
    shell.xdc.addPackagePin(io.p, "F23")
    shell.xdc.addPackagePin(io.n, "E23")
    shell.xdc.addIOStandard(io.p, "LVDS")
    shell.xdc.addIOStandard(io.n, "LVDS")
  } }
}
class RefClockZCU104ShellPlacer(shell: ZCU104ShellBasicOverlays, val shellInput: ClockInputShellInput)(implicit val valName: ValName)
  extends ClockInputShellPlacer[ZCU104ShellBasicOverlays] {
  def place(designInput: ClockInputDesignInput) = new RefClockZCU104PlacedOverlay(shell, valName.name, designInput, shellInput)
}

class SDIOZCU104PlacedOverlay(val shell: ZCU104ShellBasicOverlays, name: String, val designInput: SPIDesignInput, val shellInput: SPIShellInput)
  extends SDIOXilinxPlacedOverlay(name, designInput, shellInput)
{
  //Comparing the PMOD GPIO Headers between the two boards (ZCU104 = pg 62 and ZCU102 = pg 83)
  shell {
    InModuleBody {
      val packagePinsWithPackageIOs = Seq(
        ("H7", IOPin(io.spi_clk)),    //PMOD0_3
        ("H8", IOPin(io.spi_cs)),     //PMOD0_1
        ("G7", IOPin(io.spi_dat(0))), //PMOD0_2
        ("G6", IOPin(io.spi_dat(1))), //PMOD0_4
        ("H6", IOPin(io.spi_dat(2))), //PMOD0_5
        ("G8", IOPin(io.spi_dat(3)))  //PMOD0_0
      )

      packagePinsWithPackageIOs foreach { case (pin, io) =>
        shell.xdc.addPackagePin(io, pin)
        shell.xdc.addIOStandard(io, "LVCMOS33")
      }
      packagePinsWithPackageIOs drop 1 foreach { case (pin, io) =>
        shell.xdc.addPullup(io)
        shell.xdc.addIOB(io)
      }
    }
  }
}
class SDIOZCU104ShellPlacer(shell: ZCU104ShellBasicOverlays, val shellInput: SPIShellInput)(implicit val valName: ValName)
  extends SPIShellPlacer[ZCU104ShellBasicOverlays] {
  def place(designInput: SPIDesignInput) = new SDIOZCU104PlacedOverlay(shell, valName.name, designInput, shellInput)
}

// The on-board QSPI flash is connected to PS MIO0-MIO5 and cannot be exposed
// as ordinary programmable-logic package pins.

class UARTZCU104PlacedOverlay(val shell: ZCU104ShellBasicOverlays, name: String, val designInput: UARTDesignInput, val shellInput: UARTShellInput)
  extends UARTXilinxPlacedOverlay(name, designInput, shellInput, true)
{
  // UART2 on PL side, pg 52 ZCU104 User Guide, sheet 4 ZCU104 schematic
  shell {
    InModuleBody {
      val packagePinsWithPackageIOs = Seq(
        ("A19", IOPin(io.ctsn.get)),
        ("C18", IOPin(io.rtsn.get)),
        ("A20", IOPin(io.rxd)),
        ("C19", IOPin(io.txd))
      )
      packagePinsWithPackageIOs foreach { case (pin, io) =>
        shell.xdc.addPackagePin(io, pin)
        shell.xdc.addIOStandard(io, "LVCMOS18")
        shell.xdc.addIOB(io)
      }
    }
  }
}
class UARTZCU104ShellPlacer(shell: ZCU104ShellBasicOverlays, val shellInput: UARTShellInput)(implicit val valName: ValName)
  extends UARTShellPlacer[ZCU104ShellBasicOverlays] {
  def place(designInput: UARTDesignInput) = new UARTZCU104PlacedOverlay(shell, valName.name, designInput, shellInput)
}

object LEDZCU104PinConstraints {
  //GPIO_LED Pins ZCU104: pg 67 ZCU104 User Guide, sheet 8 ZCU104 schematic
  val pins = Seq(
    "D5", // GPIO_LED_0
    "D6", // GPIO_LED_1
    "A5", // GPIO_LED_2
    "B5"  // GPIO_LED_3
  )
}
class LEDZCU104PlacedOverlay(val shell: ZCU104ShellBasicOverlays, name: String, val designInput: LEDDesignInput, val shellInput: LEDShellInput)
  extends LEDXilinxPlacedOverlay(name, designInput, shellInput, packagePin = Some(LEDZCU104PinConstraints.pins(shellInput.number)), ioStandard = "LVCMOS33")
class LEDZCU104ShellPlacer(shell: ZCU104ShellBasicOverlays, val shellInput: LEDShellInput)(implicit val valName: ValName)
  extends LEDShellPlacer[ZCU104ShellBasicOverlays] {
  def place(designInput: LEDDesignInput) = new LEDZCU104PlacedOverlay(shell, valName.name, designInput, shellInput)
}

object ButtonZCU104PinConstraints {
  //GPIO_PB_SW Pins ZCU104: pg 67 ZCU104 User Guide, sheet 8 ZCU104 schematic
  val pins = Seq(
    "B4", // GPIO_PB_SW0
    "C4", // GPIO_PB_SW1
    "B3", // GPIO_PB_SW2
    "C3"  // GPIO_PB_SW3
  )
}
class ButtonZCU104PlacedOverlay(val shell: ZCU104ShellBasicOverlays, name: String, val designInput: ButtonDesignInput, val shellInput: ButtonShellInput)
  extends ButtonXilinxPlacedOverlay(name, designInput, shellInput, packagePin = Some(ButtonZCU104PinConstraints.pins(shellInput.number)), ioStandard = "LVCMOS33")
class ButtonZCU104ShellPlacer(shell: ZCU104ShellBasicOverlays, val shellInput: ButtonShellInput)(implicit val valName: ValName)
  extends ButtonShellPlacer[ZCU104ShellBasicOverlays] {
  def place(designInput: ButtonDesignInput) = new ButtonZCU104PlacedOverlay(shell, valName.name, designInput, shellInput)
}

object SwitchZCU104PinConstraints {
  //GPIO_DIP_SW Pins ZCU104: pg 67 ZCU104 User Guide, sheet 8 ZCU104 schematic
  val pins = Seq(
    "E4", // GPIO_DIP_SW0
    "D4", // GPIO_DIP_SW1
    "F5", // GPIO_DIP_SW2
    "F4"  // GPIO_DIP_SW3
  )
}
class SwitchZCU104PlacedOverlay(val shell: ZCU104ShellBasicOverlays, name: String, val designInput: SwitchDesignInput, val shellInput: SwitchShellInput)
  extends SwitchXilinxPlacedOverlay(name, designInput, shellInput, packagePin = Some(SwitchZCU104PinConstraints.pins(shellInput.number)), ioStandard = "LVCMOS33")
class SwitchZCU104ShellPlacer(shell: ZCU104ShellBasicOverlays, val shellInput: SwitchShellInput)(implicit val valName: ValName)
  extends SwitchShellPlacer[ZCU104ShellBasicOverlays] {
  def place(designInput: SwitchDesignInput) = new SwitchZCU104PlacedOverlay(shell, valName.name, designInput, shellInput)
}

// JTAG
// TODO: Test it
class JTAGDebugZCU104PlacedOverlay(val shell: ZCU104ShellBasicOverlays, name: String, val designInput: JTAGDebugDesignInput, val shellInput: JTAGDebugShellInput)
  extends JTAGDebugXilinxPlacedOverlay(name, designInput, shellInput)
{
  shell {
    InModuleBody {
      val pin_locations = Map(
        // PMOD right-angle female, pg 62 ZCU104 User Guide
        "PMOD_J55" -> Seq(
          "G7", // PMOD0_2
          "H6", // PMOD0_5
          "G6", // PMOD0_4
          "G8", // PMOD0_0
          "H8"  // PMOD0_1
        ),
        // PMOD vertical male, pg 62 ZCU104 User Guide
        "PMOD_J87" -> Seq(
          "K8",  // PMOD1_2
          "M10", // PMOD1_5
          "L10", // PMOD1_4
          "J9",  // PMOD1_0
          "K9"   // PMOD1_1
        )
      )
      val pins = Seq(
        io.jtag_TCK, // PMOD0_2 or PMOD1_2
        io.jtag_TMS, // PMOD0_5 or PMOD1_5
        io.jtag_TDI, // PMOD0_4 or PMOD1_4
        io.jtag_TDO, // PMOD0_0 or PMOD1_0
        io.srst_n    // PMOD0_1 or PMOD1_1
      )

      shell.sdc.addClock("JTCK", IOPin(io.jtag_TCK), 10)
      shell.sdc.addGroup(clocks = Seq("JTCK"))
      shell.xdc.clockDedicatedRouteFalse(IOPin(io.jtag_TCK))

      val pin_voltage:String = "LVCMOS33"

      (pin_locations(shellInput.location.get) zip pins) foreach { case (pin_location, ioport) =>
        val io = IOPin(ioport)
        shell.xdc.addPackagePin(io, pin_location)
        shell.xdc.addIOStandard(io, pin_voltage)
        shell.xdc.addPullup(io)
        shell.xdc.addIOB(io)
      }
    }
  }
}
class JTAGDebugZCU104ShellPlacer(shell: ZCU104ShellBasicOverlays, val shellInput: JTAGDebugShellInput)(implicit val valName: ValName)
  extends JTAGDebugShellPlacer[ZCU104ShellBasicOverlays] {
  def place(designInput: JTAGDebugDesignInput) = new JTAGDebugZCU104PlacedOverlay(shell, valName.name, designInput, shellInput)
}

class JTAGDebugBScanZCU104PlacedOverlay(val shell: ZCU104ShellBasicOverlays, name: String, val designInput: JTAGDebugBScanDesignInput, val shellInput: JTAGDebugBScanShellInput)
  extends JTAGDebugBScanXilinxPlacedOverlay(name, designInput, shellInput)
class JTAGDebugBScanZCU104ShellPlacer(val shell: ZCU104ShellBasicOverlays, val shellInput: JTAGDebugBScanShellInput)(implicit val valName: ValName)
  extends JTAGDebugBScanShellPlacer[ZCU104ShellBasicOverlays] {
  def place(designInput: JTAGDebugBScanDesignInput) = new JTAGDebugBScanZCU104PlacedOverlay(shell, valName.name, designInput, shellInput)
}

case object ZCU104DDRSize extends Field[BigInt](0x100000000L) // 4 GiB PL-side SODIMM
class DDRZCU104PlacedOverlay(val shell: ZCU104ShellBasicOverlays, name: String, val designInput: DDRDesignInput, val shellInput: DDRShellInput)
  extends DDRPlacedOverlay[XilinxZCU104MIGPads](name, designInput, shellInput)
{
  val size = p(ZCU104DDRSize)

  val migParams = XilinxZCU104MIGParams(address = AddressSet.misaligned(di.baseAddress, size))
  val mig = LazyModule(new XilinxZCU104MIG(migParams))

  // DDR4-2133 uses a 938 ps memory clock and a 4:1 PHY ratio, producing a
  // roughly 266.5 MHz user-interface clock.
  val ddrUI = shell { ClockSourceNode(freqMHz = 266.5) }
  val areset = shell { ClockSinkNode(Seq(ClockSinkParameters())) }
  areset := designInput.wrangler := ddrUI

  def overlayOutput = DDROverlayOutput(ddr = mig.node)
  def ioFactory = new XilinxZCU104MIGPads(size)

  shell {
    InModuleBody {
      require (shell.sys_clock.get().isDefined, "Use of DDRZCU104Overlay depends on SysClockZCU104Overlay")
      val (sys, _) = shell.sys_clock.get().get.overlayOutput.node.out.head
      val (ui, _) = ddrUI.out.head
      val (ar, _) = areset.in.head
      val port = mig.module.io.port
      io <> port.viewAsSupertype(new ZCU104MIGIODDR(mig.depth))
      ui.clock := port.c0_ddr4_ui_clk
      ui.reset := /*!port.mmcm_locked ||*/ port.c0_ddr4_ui_clk_sync_rst
      port.c0_sys_clk_i := sys.clock.asUInt
      port.sys_rst := sys.reset // pllReset
      port.c0_ddr4_aresetn := !(ar.reset.asBool)
      //DDR4_SODIMM, pg 29 ZCU104 User Guide
      val adr_IOs = Seq(
        "AH16", // DDR4_SODIMM_A0
        "AG14", // DDR4_SODIMM_A1
        "AG15", // DDR4_SODIMM_A2
        "AF15", // DDR4_SODIMM_A3
        "AF16", // DDR4_SODIMM_A4
        "AJ14", // DDR4_SODIMM_A5
        "AH14", // DDR4_SODIMM_A6
        "AF17", // DDR4_SODIMM_A7
        "AK17", // DDR4_SODIMM_A8
        "AJ17", // DDR4_SODIMM_A9
        "AK14", // DDR4_SODIMM_A10
        "AK15", // DDR4_SODIMM_A11
        "AL18", // DDR4_SODIMM_A12
        "AK18", // DDR4_SODIMM_A13
        "AA16", // DDR4_SODIMM_A14
        "AA14", // DDR4_SODIMM_A15
        "AD15"  // DDR4_SODIMM_A16
      )
      (adr_IOs zip IOPin.of(io.c0_ddr4_adr)) foreach { case (pin, io) =>
        shell.xdc.addPackagePin(io, pin)
        shell.xdc.addIOStandard(io, "SSTL12_DCI")
      }
      val ba_IOs = Seq(
        "AL15", // DDR4_SODIMM_BA0
        "AL16"  // DDR4_SODIMM_BA1
      )
      (ba_IOs zip IOPin.of(io.c0_ddr4_ba)) foreach { case (pin, io) =>
        shell.xdc.addPackagePin(io, pin)
        shell.xdc.addIOStandard(io, "SSTL12_DCI")
      }
      val bg_IOs = Seq(
        "AC16", // DDR4_SODIMM_BG0
        "AB16"  // DDR4_SODIMM_BG1
      )
      (bg_IOs zip IOPin.of(io.c0_ddr4_bg)) foreach { case (pin, io) =>
        shell.xdc.addPackagePin(io, pin)
        shell.xdc.addIOStandard(io, "SSTL12_DCI")
      }
      val dq_IOs = Seq(
        "AE24", // DDR4_SODIMM_DQ0
        "AE23", // DDR4_SODIMM_DQ1
        "AF22", // DDR4_SODIMM_DQ2
        "AF21", // DDR4_SODIMM_DQ3
        "AG20", // DDR4_SODIMM_DQ4
        "AG19", // DDR4_SODIMM_DQ5
        "AH21", // DDR4_SODIMM_DQ6
        "AG21", // DDR4_SODIMM_DQ7

        "AA20", // DDR4_SODIMM_DQ8
        "AA19", // DDR4_SODIMM_DQ9
        "AD19", // DDR4_SODIMM_DQ10
        "AC18", // DDR4_SODIMM_DQ11
        "AE20", // DDR4_SODIMM_DQ12
        "AD20", // DDR4_SODIMM_DQ13
        "AC19", // DDR4_SODIMM_DQ14
        "AB19", // DDR4_SODIMM_DQ15

        "AJ22", // DDR4_SODIMM_DQ16
        "AJ21", // DDR4_SODIMM_DQ17
        "AK20", // DDR4_SODIMM_DQ18
        "AJ20", // DDR4_SODIMM_DQ19
        "AK19", // DDR4_SODIMM_DQ20
        "AJ19", // DDR4_SODIMM_DQ21
        "AL23", // DDR4_SODIMM_DQ22
        "AL22", // DDR4_SODIMM_DQ23

        "AN23", // DDR4_SODIMM_DQ24
        "AM23", // DDR4_SODIMM_DQ25
        "AP23", // DDR4_SODIMM_DQ26
        "AN22", // DDR4_SODIMM_DQ27
        "AP22", // DDR4_SODIMM_DQ28
        "AP21", // DDR4_SODIMM_DQ29
        "AN19", // DDR4_SODIMM_DQ30
        "AM19", // DDR4_SODIMM_DQ31

        "AC13", // DDR4_SODIMM_DQ32
        "AB13", // DDR4_SODIMM_DQ33
        "AF12", // DDR4_SODIMM_DQ34
        "AE12", // DDR4_SODIMM_DQ35
        "AF13", // DDR4_SODIMM_DQ36
        "AE13", // DDR4_SODIMM_DQ37
        "AE14", // DDR4_SODIMM_DQ38
        "AD14", // DDR4_SODIMM_DQ39

        "AG8",  // DDR4_SODIMM_DQ40
        "AF8",  // DDR4_SODIMM_DQ41
        "AG10", // DDR4_SODIMM_DQ42
        "AG11", // DDR4_SODIMM_DQ43
        "AH13", // DDR4_SODIMM_DQ44
        "AG13", // DDR4_SODIMM_DQ45
        "AJ11", // DDR4_SODIMM_DQ46
        "AH11", // DDR4_SODIMM_DQ47

        "AK9",  // DDR4_SODIMM_DQ48
        "AJ9",  // DDR4_SODIMM_DQ49
        "AK10", // DDR4_SODIMM_DQ50
        "AJ10", // DDR4_SODIMM_DQ51
        "AL12", // DDR4_SODIMM_DQ52
        "AK12", // DDR4_SODIMM_DQ53
        "AL10", // DDR4_SODIMM_DQ54
        "AL11", // DDR4_SODIMM_DQ55

        "AM8",  // DDR4_SODIMM_DQ56
        "AM9",  // DDR4_SODIMM_DQ57
        "AM10", // DDR4_SODIMM_DQ58
        "AM11", // DDR4_SODIMM_DQ59
        "AP11", // DDR4_SODIMM_DQ60
        "AN11", // DDR4_SODIMM_DQ61
        "AP9",  // DDR4_SODIMM_DQ62
        "AP10"  // DDR4_SODIMM_DQ63
      )
      (IOPin.of(io.c0_ddr4_dq) zip dq_IOs) foreach { case (io, pin) =>
        shell.xdc.addPackagePin(io, pin)
        shell.xdc.addIOStandard(io, "POD12_DCI")
      }
      val dbi_IOs = Seq(
        "AH22", // DDR4_SODIMM_DM0_B
        "AE18", // DDR4_SODIMM_DM1_B
        "AL20", // DDR4_SODIMM_DM2_B
        "AP19", // DDR4_SODIMM_DM3_B
        "AF11", // DDR4_SODIMM_DM4_B
        "AH12", // DDR4_SODIMM_DM5_B
        "AK13", // DDR4_SODIMM_DM6_B
        "AN12"  // DDR4_SODIMM_DM7_B
      )
      (IOPin.of(io.c0_ddr4_dm_dbi_n) zip dbi_IOs) foreach { case (io, pin) =>
        shell.xdc.addPackagePin(io, pin)
        shell.xdc.addIOStandard(io, "POD12_DCI")
      }
      val dqs_t_IOs = Seq(
        "AF23", // DDR4_SODIMM_DQS0_T
        "AA18", // DDR4_SODIMM_DQS1_T
        "AK22", // DDR4_SODIMM_DQS2_T
        "AM21", // DDR4_SODIMM_DQS3_T
        "AC12", // DDR4_SODIMM_DQS4_T
        "AG9",  // DDR4_SODIMM_DQS5_T
        "AK8",  // DDR4_SODIMM_DQS6_T
        "AN9"   // DDR4_SODIMM_DQS7_T
      )
      (IOPin.of(io.c0_ddr4_dqs_t) zip dqs_t_IOs) foreach { case (io, pin) =>
        shell.xdc.addPackagePin(io, pin)
        shell.xdc.addIOStandard(io, "DIFF_POD12_DCI")
      }
      val dqs_c_IOs = Seq(
        "AG23", // DDR4_SODIMM_DQS0_C
        "AB18", // DDR4_SODIMM_DQS1_C
        "AK23", // DDR4_SODIMM_DQS2_C
        "AN21", // DDR4_SODIMM_DQS3_C
        "AD12", // DDR4_SODIMM_DQS4_C
        "AH9",  // DDR4_SODIMM_DQS5_C
        "AL8",  // DDR4_SODIMM_DQS6_C
        "AN8"   // DDR4_SODIMM_DQS7_C
      )
      (IOPin.of(io.c0_ddr4_dqs_c) zip dqs_c_IOs) foreach { case (io, pin) =>
        shell.xdc.addPackagePin(io, pin)
        shell.xdc.addIOStandard(io, "DIFF_POD12_DCI")
      }
      val ck_IOs = Seq(
        ("AF18", IOPin.of(io.c0_ddr4_ck_t).head), // DDR4_SODIMM_CK0_T
        ("AG18", IOPin.of(io.c0_ddr4_ck_c).head)  // DDR4_SODIMM_CK0_C
      )
      ck_IOs foreach { case (pin, io) =>
        shell.xdc.addPackagePin(io, pin)
        shell.xdc.addIOStandard(io, "DIFF_SSTL12_DCI")
      }
      val other_IOs = Seq(
        ("AA15", IOPin.of(io.c0_ddr4_cs_n).head), // DDR4_SODIMM_CS0_B
        ("AD17", IOPin.of(io.c0_ddr4_cke).head),  // DDR4_SODIMM_CKE0
        ("AE15", IOPin.of(io.c0_ddr4_odt).head)   // DDR4_SODIMM_ODT0
      )
      val act_n_IO = Seq(
        ("AC17", IOPin(io.c0_ddr4_act_n))    // DDR4_SODIMM_ACT_B
      )
      act_n_IO foreach { case (pin, io) =>
        shell.xdc.addPackagePin(io, pin)
        shell.xdc.addIOStandard(io, "SSTL12_DCI")
      }
      other_IOs foreach { case (pin, io) =>
        shell.xdc.addPackagePin(io, pin)
        shell.xdc.addIOStandard(io, "SSTL12_DCI")
      }
      val reset_IO = Seq(
        ("AB14", IOPin(io.c0_ddr4_reset_n))  // DDR4_SODIMM_RESET_B
      )
      reset_IO foreach { case (pin, io) =>
        shell.xdc.addPackagePin(io, pin)
        shell.xdc.addIOStandard(io, "LVCMOS12")
      }
    }
  }
  shell.sdc.addGroup(pins = Seq(mig.island.module.blackbox.io.c0_ddr4_ui_clk))
}
class DDRZCU104ShellPlacer(shell: ZCU104ShellBasicOverlays, val shellInput: DDRShellInput)(implicit val valName: ValName)
  extends DDRShellPlacer[ZCU104ShellBasicOverlays] {
  def place(designInput: DDRDesignInput) = new DDRZCU104PlacedOverlay(shell, valName.name, designInput, shellInput)
}

// Allow scratchpad-only configurations to omit the MIG and DDR package ports.
case object ZCU104ShellDDR extends Field[Boolean](true)
class WithNoZCU104ShellDDR extends Config((site, here, up) => {
  case ZCU104ShellDDR => false
})

abstract class ZCU104ShellBasicOverlays()(implicit p: Parameters) extends UltraScaleShell{
  // PLL reset causes
  val pllReset = InModuleBody { Wire(Bool()) }

  val sys_clock = Overlay(ClockInputOverlayKey, new SysClockZCU104ShellPlacer(this, ClockInputShellInput()))
  val ref_clock = Overlay(ClockInputOverlayKey, new RefClockZCU104ShellPlacer(this, ClockInputShellInput()))
  val led       = Seq.tabulate(4)(i => Overlay(LEDOverlayKey, new LEDZCU104ShellPlacer(this, LEDShellInput(color = "red", number = i))(valName = ValName(s"led_$i"))))
  val switch    = Seq.tabulate(4)(i => Overlay(SwitchOverlayKey, new SwitchZCU104ShellPlacer(this, SwitchShellInput(number = i))(valName = ValName(s"switch_$i"))))
  val button    = Seq.tabulate(4)(i => Overlay(ButtonOverlayKey, new ButtonZCU104ShellPlacer(this, ButtonShellInput(number = i))(valName = ValName(s"button_$i"))))
  val ddr       = if (p(ZCU104ShellDDR)) Some(Overlay(DDROverlayKey, new DDRZCU104ShellPlacer(this, DDRShellInput()))) else None
}

case object ZCU104ShellPMOD extends Field[String]("JTAG")
case object ZCU104ShellPMOD2 extends Field[String]("JTAG")

class WithZCU104ShellPMOD(device: String) extends Config((site, here, up) => {
  case ZCU104ShellPMOD => device
})

// Due to the level shifter is from 1.2V to 3.3V, the frequency of JTAG should be slow down to 1Mhz
class WithZCU104ShellPMOD2(device: String) extends Config((site, here, up) => {
  case ZCU104ShellPMOD2 => device
})

class WithZCU104ShellPMODJTAG extends WithZCU104ShellPMOD("JTAG")
class WithZCU104ShellPMODSDIO extends WithZCU104ShellPMOD("SDIO")

class ZCU104Shell()(implicit p: Parameters) extends ZCU104ShellBasicOverlays
{
  val pmod_is_sdio  = p(ZCU104ShellPMOD) == "SDIO"
  val jtag_location = Some(if (pmod_is_sdio) "PMOD_J87" else "PMOD_J55")

  // Order matters; ddr depends on sys_clock
  val uart      = Overlay(UARTOverlayKey, new UARTZCU104ShellPlacer(this, UARTShellInput()))
  val sdio      = if (pmod_is_sdio) Some(Overlay(SPIOverlayKey, new SDIOZCU104ShellPlacer(this, SPIShellInput()))) else None
  val jtag      = Overlay(JTAGDebugOverlayKey, new JTAGDebugZCU104ShellPlacer(this, JTAGDebugShellInput(location = jtag_location)))
  val jtagBScan = Overlay(JTAGDebugBScanOverlayKey, new JTAGDebugBScanZCU104ShellPlacer(this, JTAGDebugBScanShellInput()))

  val topDesign = LazyModule(p(DesignKey)(designParameters))

  // Place the sys_clock at the Shell if the user didn't ask for it
  designParameters(ClockInputOverlayKey).foreach { unused =>
    val source = unused.place(ClockInputDesignInput()).overlayOutput.node
    val sink = ClockSinkNode(Seq(ClockSinkParameters()))
    sink := source
  }

  override lazy val module = new LazyRawModuleImp(this) {
    val reset = IO(Input(Bool()))

    //zcu104 pg 89, vcu118 pg 90
    xdc.addPackagePin(reset, "M11")
    xdc.addIOStandard(reset, "LVCMOS33")

    val reset_ibuf = Module(new IBUF)
    reset_ibuf.io.I := reset

    val sysclk: Clock = sys_clock.get() match {
      case Some(x: SysClockZCU104PlacedOverlay) => x.clock
    }

    val powerOnReset: Bool = PowerOnResetFPGAOnly(sysclk)
    sdc.addAsyncPath(Seq(powerOnReset))

    pllReset := (reset_ibuf.io.O || powerOnReset)
  }
}
