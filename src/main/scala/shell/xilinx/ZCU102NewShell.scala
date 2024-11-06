package sifive.fpgashells.shell.xilinx

import chisel3._
import chisel3.experimental.{attach, Analog}
import chisel3.experimental.dataview._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.prci._
import org.chipsalliance.cde.config._
import sifive.fpgashells.clocks._
import sifive.fpgashells.devices.xilinx.xdma._
import sifive.fpgashells.devices.xilinx.xilinxzcu102mig._
import sifive.fpgashells.ip.xilinx._
import sifive.fpgashells.ip.xilinx.xxv_ethernet._
import sifive.fpgashells.ip.xilinx.zcu102mig._
import sifive.fpgashells.shell._

class SysClockZCU102PlacedOverlay(val shell: ZCU102ShellBasicOverlays, name: String, val designInput: ClockInputDesignInput, val shellInput: ClockInputShellInput)
  extends LVDSClockInputXilinxPlacedOverlay(name, designInput, shellInput) 
{
  //pg 48 of ZCU102 Board User Guide, changed 250 to default 300 to use USER_SI570 default freq
  //SI570 300MHz 
  val node = shell { ClockSourceNode(freqMHz = 300, jitterPS = 50)(ValName(name)) }
  shell { InModuleBody {
    shell.xdc.addPackagePin(io.p, "AL8")
    shell.xdc.addPackagePin(io.n, "AL7")
    shell.xdc.addIOStandard(io.p, "DIFF_SSTL12")
    shell.xdc.addIOStandard(io.n, "DIFF_SSTL12")
  }}
}
class SysClockZCU102ShellPlacer(shell: ZCU102ShellBasicOverlays, val shellInput: ClockInputShellInput)(implicit val valName: ValName)
  extends ClockInputShellPlacer[ZCU102ShellBasicOverlays]
{
    def place(designInput: ClockInputDesignInput) = new SysClockZCU102PlacedOverlay(shell, valName.name, designInput, shellInput)
}

class RefClockZCU102PlacedOverlay(val shell: ZCU102ShellBasicOverlays, name: String, val designInput: ClockInputDesignInput, val shellInput: ClockInputShellInput)
  extends LVDSClockInputXilinxPlacedOverlay(name, designInput, shellInput) 
{
  //pg 48 of ZCU102 Board User Guide
  val node = shell { ClockSourceNode(freqMHz = 125, jitterPS = 50)(ValName(name)) }
  
  shell { InModuleBody {
    shell.xdc.addPackagePin(io.p, "H9")
    shell.xdc.addPackagePin(io.n, "G9")
    shell.xdc.addIOStandard(io.p, "LVDS")
    shell.xdc.addIOStandard(io.n, "LVDS")
  } }
}
class RefClockZCU102ShellPlacer(shell: ZCU102ShellBasicOverlays, val shellInput: ClockInputShellInput)(implicit val valName: ValName)
  extends ClockInputShellPlacer[ZCU102ShellBasicOverlays] {
  def place(designInput: ClockInputDesignInput) = new RefClockZCU102PlacedOverlay(shell, valName.name, designInput, shellInput)
}

class SDIOZCU102PlacedOverlay(val shell: ZCU102ShellBasicOverlays, name: String, val designInput: SPIDesignInput, val shellInput: SPIShellInput)
  extends SDIOXilinxPlacedOverlay(name, designInput, shellInput)
{
  //Comparing the PMOD GPIO Headers between the two boards (VCU118 = pg 91 and ZCU102 = pg 83)
  shell { InModuleBody {
    val packagePinsWithPackageIOs = Seq(("A21", IOPin(io.spi_clk)),      //PMOD0_3
                                        ("B20", IOPin(io.spi_cs)),       //PMOD0_1
                                        ("A22", IOPin(io.spi_dat(0))),   //PMOD0_2
                                        ("B21", IOPin(io.spi_dat(1))),   //PMOD0_4
                                        ("C21", IOPin(io.spi_dat(2))),   //PMOD0_5
                                        ("A20", IOPin(io.spi_dat(3))))   //PMOD0_0

    packagePinsWithPackageIOs foreach { case (pin, io) => {
      shell.xdc.addPackagePin(io, pin)
      shell.xdc.addIOStandard(io, "LVCMOS33")
    } }
    packagePinsWithPackageIOs drop 1 foreach { case (pin, io) => {
      shell.xdc.addPullup(io)
      shell.xdc.addIOB(io)
    } }
  } }
}
class SDIOZCU102ShellPlacer(shell: ZCU102ShellBasicOverlays, val shellInput: SPIShellInput)(implicit val valName: ValName)
  extends SPIShellPlacer[ZCU102ShellBasicOverlays] {
  def place(designInput: SPIDesignInput) = new SDIOZCU102PlacedOverlay(shell, valName.name, designInput, shellInput)
}

class SPIFlashZCU102PlacedOverlay(val shell: ZCU102ShellBasicOverlays, name: String, val designInput: SPIFlashDesignInput, val shellInput: SPIFlashShellInput)
  extends SPIFlashXilinxPlacedOverlay(name, designInput, shellInput)
{
  //Quad-SPI Component connected to FPGA: VCU118 pg. 39; ZCU102 pg. 39 (TODO: LWR/UPR?) // Checkout VCU118NewShell.scala:14453b3
  shell { InModuleBody {
    /*Commented out in the VCU118 Design
    val packagePinsWithPackageIOs = Seq(("A24", IOPin(io.qspi_sck)),
      ("D25", IOPin(io.qspi_cs)), 
      ("A25", IOPin(io.qspi_dq(0))),
      ("C24", IOPin(io.qspi_dq(1))),
      ("B24", IOPin(io.qspi_dq(2))),
      ("E25", IOPin(io.qspi_dq(3))))

    packagePinsWithPackageIOs foreach { case (pin, io) => {
      shell.xdc.addPackagePin(io, pin)
      shell.xdc.addIOStandard(io, "LVCMOS18")
      shell.xdc.addIOB(io)
    } }
    packagePinsWithPackageIOs drop 1 foreach { case (pin, io) => {
      shell.xdc.addPullup(io)
    } }
    */
  } }
}
class SPIFlashZCU102ShellPlacer(shell: ZCU102ShellBasicOverlays, val shellInput: SPIFlashShellInput)(implicit val valName: ValName)
  extends SPIFlashShellPlacer[ZCU102ShellBasicOverlays] {
  def place(designInput: SPIFlashDesignInput) = new SPIFlashZCU102PlacedOverlay(shell, valName.name, designInput, shellInput)
}

class UARTZCU102PlacedOverlay(val shell: ZCU102ShellBasicOverlays, name: String, val designInput: UARTDesignInput, val shellInput: UARTShellInput)
  extends UARTXilinxPlacedOverlay(name, designInput, shellInput, true)
{
  //Comparing the UART between the two boards (VCU118 = pg 81-82 and ZCU102 = pg 67-68), UART for ZCU102 doesn't seem t have cts/rts
  //TODO: UART2 which is a PL-Side USB UART has RTS pins, so I am using those for now and confirming later.
  shell { InModuleBody {  
    val packagePinsWithPackageIOs = Seq(("D12", IOPin(io.ctsn.get)),
                                        ("E12", IOPin(io.rtsn.get)),
                                        ("E13", IOPin(io.rxd)),
                                        ("F13", IOPin(io.txd)))

    packagePinsWithPackageIOs foreach { case (pin, io) => {
      shell.xdc.addPackagePin(io, pin)
      shell.xdc.addIOStandard(io, "LVCMOS33") //TODO: ZCU102 maps these to bank 64 which is 1.2V, so changed from LVCMOS18 to LVCMOS12
      shell.xdc.addIOB(io)
    } }
  } }
}
class UARTZCU102ShellPlacer(shell: ZCU102ShellBasicOverlays, val shellInput: UARTShellInput)(implicit val valName: ValName)
  extends UARTShellPlacer[ZCU102ShellBasicOverlays] {
  def place(designInput: UARTDesignInput) = new UARTZCU102PlacedOverlay(shell, valName.name, designInput, shellInput)
}

//VCU118 QSFP Jitter Attenuated Clock (Si5328B) pg. 65-66; ZCU102 SFP/SFP+ (Si5328B) pg. 80-81
class QSFP1ZCU102PlacedOverlay(val shell: ZCU102ShellBasicOverlays, name: String, val designInput: EthernetDesignInput, val shellInput: EthernetShellInput)
  extends EthernetUltraScalePlacedOverlay(name, designInput, shellInput, XXVEthernetParams(name = name, speed   = 10, dclkMHz = 125))
{
  val dclkSource = shell { BundleBridgeSource(() => Clock()) }
  val dclkSink = dclkSource.makeSink()
  InModuleBody {
    dclk := dclkSink.bundle
  }
  shell { InModuleBody {
    dclkSource.bundle := shell.ref_clock.get.get.overlayOutput.node.out(0)._1.clock
    shell.xdc.addPackagePin(io.tx_p, "E4")
    shell.xdc.addPackagePin(io.tx_n, "E3")
    shell.xdc.addPackagePin(io.rx_p, "D2")
    shell.xdc.addPackagePin(io.rx_n, "D1")
    shell.xdc.addPackagePin(io.refclk_p, "R10")
    shell.xdc.addPackagePin(io.refclk_n, "R9")
  } }
}
class QSFP1ZCU102ShellPlacer(shell: ZCU102ShellBasicOverlays, val shellInput: EthernetShellInput)(implicit val valName: ValName)
  extends EthernetShellPlacer[ZCU102ShellBasicOverlays] {
  def place(designInput: EthernetDesignInput) = new QSFP1ZCU102PlacedOverlay(shell, valName.name, designInput, shellInput)
}

object LEDZCU102PinConstraints {
  //GPIO_LED Pins VCU118: pg 90 ZCU102: pg 88
  val pins = Seq("AG14", "AF13", "AE13", "AJ14", "AJ15", "AH13", "AH14", "AL12")
}
class LEDZCU102PlacedOverlay(val shell: ZCU102ShellBasicOverlays, name: String, val designInput: LEDDesignInput, val shellInput: LEDShellInput)
  extends LEDXilinxPlacedOverlay(name, designInput, shellInput, packagePin = Some(LEDZCU102PinConstraints.pins(shellInput.number)), ioStandard = "LVCMOS33")
class LEDZCU102ShellPlacer(shell: ZCU102ShellBasicOverlays, val shellInput: LEDShellInput)(implicit val valName: ValName)
  extends LEDShellPlacer[ZCU102ShellBasicOverlays] {
  def place(designInput: LEDDesignInput) = new LEDZCU102PlacedOverlay(shell, valName.name, designInput, shellInput)
}

object ButtonZCU102PinConstraints {
  //GPIO_SW_N, GPIO_SW_E,GPIO_SW_W, GPIO_SW_S, GPIO_SW_C (VCU118 bank 64 LVCMOS18 pg 90; ZCU102 LVCMOS12 pg 88)
  val pins = Seq("AG15", "AE14", "AF15", "AE15", "AG13")
}
class ButtonZCU102PlacedOverlay(val shell: ZCU102ShellBasicOverlays, name: String, val designInput: ButtonDesignInput, val shellInput: ButtonShellInput)
  extends ButtonXilinxPlacedOverlay(name, designInput, shellInput, packagePin = Some(ButtonZCU102PinConstraints.pins(shellInput.number)), ioStandard = "LVCMOS33")
class ButtonZCU102ShellPlacer(shell: ZCU102ShellBasicOverlays, val shellInput: ButtonShellInput)(implicit val valName: ValName)
  extends ButtonShellPlacer[ZCU102ShellBasicOverlays] {
  def place(designInput: ButtonDesignInput) = new ButtonZCU102PlacedOverlay(shell, valName.name, designInput, shellInput)
}

object SwitchZCU102PinConstraints {
  //VCU118 LVCMOS12 4-Pole DOP SW12 pg 90; ZCU102 GPIO DIP SW 0-7 pg 88-89 LVCMOS18
  val pins = Seq("AN14", "AP14", "AM14", "An13")
}
class SwitchZCU102PlacedOverlay(val shell: ZCU102ShellBasicOverlays, name: String, val designInput: SwitchDesignInput, val shellInput: SwitchShellInput)
  extends SwitchXilinxPlacedOverlay(name, designInput, shellInput, packagePin = Some(SwitchZCU102PinConstraints.pins(shellInput.number)), ioStandard = "LVCMOS33")
class SwitchZCU102ShellPlacer(shell: ZCU102ShellBasicOverlays, val shellInput: SwitchShellInput)(implicit val valName: ValName)
  extends SwitchShellPlacer[ZCU102ShellBasicOverlays] {
  def place(designInput: SwitchDesignInput) = new SwitchZCU102PlacedOverlay(shell, valName.name, designInput, shellInput)
}

class ChipLinkZCU102PlacedOverlay(val shell: ZCU102ShellBasicOverlays, name: String, val designInput: ChipLinkDesignInput, val shellInput: ChipLinkShellInput)
  extends ChipLinkXilinxPlacedOverlay(name, designInput, shellInput, rxPhase= -120, txPhase= -90, rxMargin=0.6, txMargin=0.5)
{
  val ereset_n = shell { InModuleBody {
    val ereset_n = IO(Analog(1.W))
    ereset_n.suggestName("ereset_n")
    val pin = IOPin(ereset_n, 0)
    shell.xdc.addPackagePin(pin, "AA6") //VCU118 pg 98 FMC_HPC1_CLK0_M2C_N; ZCU102 pg 108 FMC_HPC0_CLK0_M2C_N
    shell.xdc.addIOStandard(pin, "LVCMOS18")
    shell.xdc.addTermination(pin, "NONE")
    shell.xdc.addPullup(pin)

    val iobuf = Module(new IOBUF)
    iobuf.suggestName("chiplink_ereset_iobuf")
    attach(ereset_n, iobuf.io.IO)
    iobuf.io.T := true.B // !oe
    iobuf.io.I := false.B

    iobuf.io.O
  } }

  //VCU118 seems to use HPC1 -> 68 single-ended or 34 differential user-definedd pairs (pg 96)
  //ZCU102 HPC1 has half the capability, but HPCO is defined the same (pg 104)
  shell { InModuleBody {
    val dir1 = Seq("E15", "D17", "C17", /* clk = FMC_HPC1_CLK0_M2C_P, rst = FMC_HPC1_LA16_N, send = FMC_HPC1_LA16_P*/
                  //FMC_HPC1_LA[00:15]..._P, FMC_HPC1_LA[00:15]..._N, 
                   "F17",  "F16",
                   "H18",  "H17",  
                   "L20",  "K20", 
                   "K19",  "K18", 
                   "L17",  "L16", 
                   "K17",  "J17", 
                   "H19",  "G19", 
                   "J16", "J15",
                   "E18", "E17", 
                   "H16", "G16", 
                   "L15", "K15", 
                   "A13", "A12",
                   "G18", "F18", 
                   "G15", "F15",  
                   "C13", "C12",  
                   "D16", "C16")
    val dir2 = Seq("G10", "C9", "C8", /* clk = FMC_HPC1_CLK1_M2C_P, rst = FMC_HPC1_LA33_N, send = FMC_HPC1_LA33_P*/
                   //FMC_HPC1_LA[17:32]_CC_P, FMC_HPC1_LA[17:32]_CC_N
                   "F11", "E10",
                   "D11", "D10", 
                   "D12", "C11", 
                   "F12", "D16",
                   "B10", "A10", 
                   "H13", "H12", 
                   "B11", "A11", 
                   "B6",  "A6",
                   "C7",  "C6", 
                   "B9",  "B8", 
                   "A8",  "A7", 
                   "M13", "L13",
                   "K10", "J10", 
                   "E9",  "D9", 
                   "F7",  "E7", 
                   "F8",  "E8")
    (IOPin.of(io.b2c) zip dir1) foreach { case (io, pin) => shell.xdc.addPackagePin(io, pin) }
    (IOPin.of(io.c2b) zip dir2) foreach { case (io, pin) => shell.xdc.addPackagePin(io, pin) }
  } }
}
class ChipLinkZCU102ShellPlacer(shell: ZCU102ShellBasicOverlays, val shellInput: ChipLinkShellInput)(implicit val valName: ValName)
  extends ChipLinkShellPlacer[ZCU102ShellBasicOverlays] {
  def place(designInput: ChipLinkDesignInput) = new ChipLinkZCU102PlacedOverlay(shell, valName.name, designInput, shellInput)
}

// JTAG is untested (VCU118 pg 91-92; ZCU102 pg 83-84)
class JTAGDebugZCU102PlacedOverlay(val shell: ZCU102ShellBasicOverlays, name: String, val designInput: JTAGDebugDesignInput, val shellInput: JTAGDebugShellInput)
  extends JTAGDebugXilinxPlacedOverlay(name, designInput, shellInput)
{
  shell { InModuleBody {
    val pin_locations = Map(
      //PMOD Rt-Angle Female (VCU118 J52; ZCU102 J55) PMOD0_2, PMOD0_5, PMOD0_4, PMOD0_0, PMOD0_1
      "PMOD_J55" -> Seq("A22",      "C21",      "B21",      "A20",      "B20"),
      //PMOD Male Vertical   (VCU118 J53; ZCU102 J87) PMOD1_2, PMOD1_5, PMOD1_4, PMOD1_0, PMOD1_1
      "PMOD_J53" -> Seq( "D22",       "G20",       "F20",       "D20",       "E20"),
      //VCU118 J2 FMC HPC 1 pg 99:  FMC_HPC1_LA30_N, FMC_HPC1_LA29_P, FMC_HPC1_LA29_N, FMC_HPC1_LA31_N, FMC_HPC1_LA30_P
      //ZCU102 J5 FMC HPC 0 pg 108: FMC_HPC0_LA30_N, FMC_HPC0_LA29_P, FMC_HPC0_LA29_N, FMC_HPC0_LA31_N, FMC_HPC0_LA30_P
      "FMC_J5"   -> Seq("U6",      "U9",      "U8",      "V7",      "V6"))
    val pins      = Seq(io.jtag_TCK, io.jtag_TMS, io.jtag_TDI, io.jtag_TDO, io.srst_n)

    shell.sdc.addClock("JTCK", IOPin(io.jtag_TCK), 10)
    shell.sdc.addGroup(clocks = Seq("JTCK"))
    shell.xdc.clockDedicatedRouteFalse(IOPin(io.jtag_TCK))

    val pin_voltage:String = if(shellInput.location.get == "PMOD_J53") "LVCMOS12" else "LVCMOS18"; //ZCU102 uses one I/O Standard VCU118 uses 2 if(shellInput.location.get == "PMOD_J53") "LVCMOS12" else "LVCMOS18"

    (pin_locations(shellInput.location.get) zip pins) foreach { case (pin_location, ioport) =>
      val io = IOPin(ioport)
      shell.xdc.addPackagePin(io, pin_location)
      shell.xdc.addIOStandard(io, pin_voltage)
      shell.xdc.addPullup(io)
      shell.xdc.addIOB(io)
    }
  } }
}
class JTAGDebugZCU102ShellPlacer(shell: ZCU102ShellBasicOverlays, val shellInput: JTAGDebugShellInput)(implicit val valName: ValName)
  extends JTAGDebugShellPlacer[ZCU102ShellBasicOverlays] {
  def place(designInput: JTAGDebugDesignInput) = new JTAGDebugZCU102PlacedOverlay(shell, valName.name, designInput, shellInput)
}

class cJTAGDebugZCU102PlacedOverlay(val shell: ZCU102ShellBasicOverlays, name: String, val designInput: cJTAGDebugDesignInput, val shellInput: cJTAGDebugShellInput)
  extends cJTAGDebugXilinxPlacedOverlay(name, designInput, shellInput)
{
  shell { InModuleBody {
    shell.sdc.addClock("JTCKC", IOPin(io.cjtag_TCKC), 10)
    shell.sdc.addGroup(clocks = Seq("JTCKC"))
    shell.xdc.clockDedicatedRouteFalse(IOPin(io.cjtag_TCKC))
    val packagePinsWithPackageIOs = Seq(("N13", IOPin(io.cjtag_TCKC)), //VCU118 J2 FMC HPC 1 pg 99: FMC_HPC1_LA20_P; ZCU102 pg 108 FMC_HPC0_LA20_P
                                        ("L12",  IOPin(io.cjtag_TMSC)), //VCU118 J2 FMC HPC 1 pg 99: FMC_HPC1_LA24_P; ZCU102 pg 108 FMC_HPC0_LA24_P
                                        ("M13", IOPin(io.srst_n)))     //VCU118 J2 FMC HPC 1 pg 99: FMC_HPC1_LA20_N; ZCU102 pg 108 FMC_HPC0_LA20_N

    packagePinsWithPackageIOs foreach { case (pin, io) => {
      shell.xdc.addPackagePin(io, pin)
      shell.xdc.addIOStandard(io, "LVCMOS18")
    } }
      shell.xdc.addPullup(IOPin(io.cjtag_TCKC))
      shell.xdc.addPullup(IOPin(io.srst_n))
  } }
}
class cJTAGDebugZCU102ShellPlacer(shell: ZCU102ShellBasicOverlays, val shellInput: cJTAGDebugShellInput)(implicit val valName: ValName)
  extends cJTAGDebugShellPlacer[ZCU102ShellBasicOverlays] {
  def place(designInput: cJTAGDebugDesignInput) = new cJTAGDebugZCU102PlacedOverlay(shell, valName.name, designInput, shellInput)
}

class JTAGDebugBScanZCU102PlacedOverlay(val shell: ZCU102ShellBasicOverlays, name: String, val designInput: JTAGDebugBScanDesignInput, val shellInput: JTAGDebugBScanShellInput)
  extends JTAGDebugBScanXilinxPlacedOverlay(name, designInput, shellInput)
class JTAGDebugBScanZCU102ShellPlacer(val shell: ZCU102ShellBasicOverlays, val shellInput: JTAGDebugBScanShellInput)(implicit val valName: ValName)
  extends JTAGDebugBScanShellPlacer[ZCU102ShellBasicOverlays] {
  def place(designInput: JTAGDebugBScanDesignInput) = new JTAGDebugBScanZCU102PlacedOverlay(shell, valName.name, designInput, shellInput)
}

case object ZCU102DDRSize extends Field[BigInt](0x40000000L * 2) // 2GB
class DDRZCU102PlacedOverlay(val shell: ZCU102ShellBasicOverlays, name: String, val designInput: DDRDesignInput, val shellInput: DDRShellInput)
  extends DDRPlacedOverlay[XilinxZCU102MIGPads](name, designInput, shellInput)
{
  val size = p(ZCU102DDRSize)

  val migParams = XilinxZCU102MIGParams(address = AddressSet.misaligned(di.baseAddress, size))
  val mig = LazyModule(new XilinxZCU102MIG(migParams))

  val ddrUI     = shell { ClockSourceNode(freqMHz = 300) }
  val areset    = shell { ClockSinkNode(Seq(ClockSinkParameters())) }
  areset := designInput.wrangler := ddrUI

  def overlayOutput = DDROverlayOutput(ddr = mig.node)
  def ioFactory = new XilinxZCU102MIGPads(size)



  shell { InModuleBody {
    require (shell.sys_clock.get.isDefined, "Use of DDRZCU102Overlay depends on SysClockZCU102Overlay")
    val (sys, _) = shell.sys_clock.get.get.overlayOutput.node.out(0)
    val (ui, _) = ddrUI.out(0)
    val (ar, _) = areset.in(0)
    val port = mig.module.io.port
    io <> port.viewAsSupertype(new ZCU102MIGIODDR(mig.depth))
    ui.clock := port.c0_ddr4_ui_clk
    ui.reset := /*!port.mmcm_locked ||*/ port.c0_ddr4_ui_clk_sync_rst
    port.c0_sys_clk_i := sys.clock.asUInt
    port.sys_rst := sys.reset // pllReset
    port.c0_ddr4_aresetn := !(ar.reset.asBool)
    //ZCU102 pg 34-37 VCU118 pg 23-32
    val allddrpins = Seq(  
      "AM8", "AM9", "AP8", "AN8", "AK10", "AJ10", "AP9", "AN9", "AP10", "AP11", "AM10", "AL10", "AM11", "AL11",  // adr[0->13]
      "AJ7", "AL5", "AJ9", "AK7", // we_n, cas_n, ras_n, bg
      "AK12", "AJ12", // ba[0->1]
      "AH9", "AK8", "AP7", "AN7", "AM3", "AP2", "AK9", // reset_n, act_n, ck_c, ck_t, cke, cs_n, odt
      // "AK4", "AK5", "AN4", "AM4", "AP4", "AP5", "AM5", "AM6", "AK2", "AK3", "AL1", "AK1", "AN1", "AM1", "AP3", "AN3", // dq[0->15]
     //   "AP6", "AL2", // dqs_c[0->1]
     // "AN6", "AL3", // dqs_t[0->1]
     // "AL6", "AN2") // dm_dbi_n[0->1]

//	val allddrpins2 = Seq( 

      "AK4", "AK5", "AN4", "AM4", "AP4", "AP5", "AM5", "AM6", "AK2", "AK3", "AL1", "AK1", "AN1", "AM1", "AP3", "AN3", // dq[0->15]
      "AP6", "AL2",  // dqs_c[0->1]
      "AN6", "AL3",  // dqs_t[0->1]
      "AL6", "AN2")  // dm_dbi_n[0->1]

    (IOPin.of(io) zip allddrpins) foreach { case (io, pin) => shell.xdc.addPackagePin(io, pin) }
  } }

  shell.sdc.addGroup(pins = Seq(mig.island.module.blackbox.io.c0_ddr4_ui_clk))
}
class DDRZCU102ShellPlacer(shell: ZCU102ShellBasicOverlays, val shellInput: DDRShellInput)(implicit val valName: ValName)
  extends DDRShellPlacer[ZCU102ShellBasicOverlays] {
  def place(designInput: DDRDesignInput) = new DDRZCU102PlacedOverlay(shell, valName.name, designInput, shellInput)
}

//TODO: VCU118 Has FMC+ (FMCP) support whereas the ZCU102 does not (going to just use HPC0 J5 for the ZCU102)
class PCIeZCU102FMCPlacedOverlay(val shell: ZCU102ShellBasicOverlays, name: String, val designInput: PCIeDesignInput, val shellInput: PCIeShellInput)
  extends PCIeUltraScalePlacedOverlay(name, designInput, shellInput, XDMAParams(
    name     = "fmc_xdma",
    location = "X0Y3", //TODO: Update for ZCU102?
    bars     = designInput.bars,
    control  = designInput.ecam,
    bases    = designInput.bases,
    lanes    = 4))
{
  shell { InModuleBody {
    // Work-around incorrectly pre-assigned pins (Note was for the VCU118, still applicable for the ZCU102?)
    IOPin.of(io).foreach { shell.xdc.addPackagePin(_, "") }

    // We need some way to connect both of these to reach x8
    val ref226 = Seq("G8",  "G7") /* [pn] VCU118: GBT0 Bank 126 MGTREFCLK0[PN]_126/FMCP_HSPC_GBT0_1_; ZCU102: MGTREFCLK0/FMC_HPC0_GBTCLK0_M2C_C_*/
    val ref227 = Seq("L8",  "L7") /* [pn] VCU118: GBT0 Bank 121 MGTREFCLK0[PN]_121/FMCP_HSPC_GBT0_0_; ZCU102: MGTREFCLK0/FMC_HPC0_GBTCLK1_M2C_C_*/
    val ref = ref227

    //TODO: Finish the rx and tx mappings
    // VCU118 (pg 52-53): Bank 126 (DP5, DP6, DP4, DP7), Bank 121 (DP3, DP2, DP1, DP0)
    // ZCU102 (pg 91-92): Bank 227 (DP5, DP6, DP4, DP7), Bank 226 (DP3, DP2, DP1, DP0)
    val rxp = Seq("K33", "H33", "L31", "F33", "B33", "C31", "AN45", "D33") /* MGTYRXP1_126[0-7] */ //Order is 1, 6, 0, 3, 
    val rxn = Seq("K34", "H34", "L32", "F34", "B34", "C32", "AN46", "D34") /* [0-7] */
    val txp = Seq("J31", "H29", "T42", "K29", "A31", "B29", "D29", "F29") /* [0-7] */
    val txn = Seq("J32", "H30", "T43", "K43", "A32", "B30", "D30", "F30") /* [0-7] */

    def bind(io: Seq[IOPin], pad: Seq[String]) {
      (io zip pad) foreach { case (io, pad) => shell.xdc.addPackagePin(io, pad) }
    }

    bind(IOPin.of(io.refclk), ref)
    // We do these individually so that zip falls off the end of the lanes:
    bind(IOPin.of(io.lanes.pci_exp_txp), txp)
    bind(IOPin.of(io.lanes.pci_exp_txn), txn)
    bind(IOPin.of(io.lanes.pci_exp_rxp), rxp)
    bind(IOPin.of(io.lanes.pci_exp_rxn), rxn)
  } }
}
class PCIeZCU102FMCShellPlacer(shell: ZCU102ShellBasicOverlays, val shellInput: PCIeShellInput)(implicit val valName: ValName)
  extends PCIeShellPlacer[ZCU102ShellBasicOverlays] {
  def place(designInput: PCIeDesignInput) = new PCIeZCU102FMCPlacedOverlay(shell, valName.name, designInput, shellInput)
}

//VCU118 Supports GTY 8-lane edge connector (pg 52 & 60-64); the ZCU102 supports a GTH 4-lane edge connector P3 (pg 100 & 95)
class PCIeZCU102EdgePlacedOverlay(val shell: ZCU102ShellBasicOverlays, name: String, val designInput: PCIeDesignInput, val shellInput: PCIeShellInput)
  extends PCIeUltraScalePlacedOverlay(name, designInput, shellInput, XDMAParams(
    name     = "edge_xdma",
    location = "X1Y2", //TODO: Update this for the ZCU102?
    bars     = designInput.bars,
    control  = designInput.ecam,
    bases    = designInput.bases,
    lanes    = 4))
{
  shell { InModuleBody {
    // Work-around incorrectly pre-assigned pins (note was for the VCU118, does this still apply for the ZCU102?)
    IOPin.of(io).foreach { shell.xdc.addPackagePin(_, "") }
    /**VCU118
      PCIe Edge connector U2
       Lanes 00-03 Bank 227
       Lanes 04-07 Bank 226
       Lanes 08-11 Bank 225
       Lanes 12-15 Bank 224
    **/
    
    val ref224 = Seq("AB8", "AB7")  /* [pn]  VCU118 (pg 64) Bank 227 PCIE_CLK2_; ZCU102 (pg 96) PCI_CLK_*/
    val ref = ref224

    // PCIe Edge connector VCU 118 U2 : Bank 227, 226 (pg 72); ZCU102 Bank 224
    val rxp = Seq("AE2", "AF4", "AG2", "AJ2") // PCIE_RX_[0-7]_P; [0-3] for the ZCU102
    val rxn = Seq("AE1", "AF3", "AG1", "AJ1") // PCIE_RX_[0-7]_N; [0-3] for the ZCU102
    val txp = Seq("AD4", "AE6", "AG6", "AH4") // PCIE_TX_[0-7]_P; [0-3] for the ZCU102
    val txn = Seq("AD3", "AE5", "AG5", "AH3") // PCIE_TX_[0-7]_N; [0-3] for the ZCU102

    def bind(io: Seq[IOPin], pad: Seq[String]) {
      (io zip pad) foreach { case (io, pad) => shell.xdc.addPackagePin(io, pad) }
    }

    bind(IOPin.of(io.refclk), ref)
    // We do these individually so that zip falls off the end of the lanes:
    bind(IOPin.of(io.lanes.pci_exp_txp), txp)
    bind(IOPin.of(io.lanes.pci_exp_txn), txn)
    bind(IOPin.of(io.lanes.pci_exp_rxp), rxp)
    bind(IOPin.of(io.lanes.pci_exp_rxn), rxn)
  } }
}
class PCIeZCU102EdgeShellPlacer(shell: ZCU102ShellBasicOverlays, val shellInput: PCIeShellInput)(implicit val valName: ValName)
  extends PCIeShellPlacer[ZCU102ShellBasicOverlays] {
  def place(designInput: PCIeDesignInput) = new PCIeZCU102EdgePlacedOverlay(shell, valName.name, designInput, shellInput)
}

abstract class ZCU102ShellBasicOverlays()(implicit p: Parameters) extends UltraScaleShell{
  // PLL reset causes
  val pllReset = InModuleBody { Wire(Bool()) }

  val sys_clock = Overlay(ClockInputOverlayKey, new SysClockZCU102ShellPlacer(this, ClockInputShellInput()))
  val ref_clock = Overlay(ClockInputOverlayKey, new RefClockZCU102ShellPlacer(this, ClockInputShellInput()))
  val led       = Seq.tabulate(8)(i => Overlay(LEDOverlayKey, new LEDZCU102ShellPlacer(this, LEDShellInput(color = "red", number = i))(valName = ValName(s"led_$i"))))
  val switch    = Seq.tabulate(8)(i => Overlay(SwitchOverlayKey, new SwitchZCU102ShellPlacer(this, SwitchShellInput(number = i))(valName = ValName(s"switch_$i"))))
  val button    = Seq.tabulate(5)(i => Overlay(ButtonOverlayKey, new ButtonZCU102ShellPlacer(this, ButtonShellInput(number = i))(valName = ValName(s"button_$i"))))
  val ddr       = Overlay(DDROverlayKey, new DDRZCU102ShellPlacer(this, DDRShellInput()))
  val qsfp1     = Overlay(EthernetOverlayKey, new QSFP1ZCU102ShellPlacer(this, EthernetShellInput()))
  // val qsfp2     = Overlay(EthernetOverlayKey, new QSFP2ZCU102ShellPlacer(this, EthernetShellInput()))
  val chiplink  = Overlay(ChipLinkOverlayKey, new ChipLinkZCU102ShellPlacer(this, ChipLinkShellInput())) // TODO: ??
  //val spi_flash = Overlay(SPIFlashOverlayKey, new SPIFlashZCU102ShellPlacer(this, SPIFlashShellInput()))
  //SPI Flash not functional
}

case object ZCU102ShellPMOD extends Field[String]("JTAG")
case object ZCU102ShellPMOD2 extends Field[String]("JTAG")

class WithZCU102ShellPMOD(device: String) extends Config((site, here, up) => {
  case ZCU102ShellPMOD => device
})

// Due to the level shifter is from 1.2V to 3.3V, the frequency of JTAG should be slow down to 1Mhz
class WithZCU102ShellPMOD2(device: String) extends Config((site, here, up) => {
  case ZCU102ShellPMOD2 => device
})

class WithZCU102ShellPMODJTAG extends WithZCU102ShellPMOD("JTAG")
class WithZCU102ShellPMODSDIO extends WithZCU102ShellPMOD("SDIO")

// Reassign JTAG pinouts location to PMOD J53 (VCU118) or J55/J87 (ZCU102)
class WithZCU102ShellPMOD2JTAG extends WithZCU102ShellPMOD2("PMODJ87_JTAG")

class ZCU102Shell()(implicit p: Parameters) extends ZCU102ShellBasicOverlays
{
  val pmod_is_sdio  = p(ZCU102ShellPMOD) == "SDIO"
  val pmod_j87_is_jtag = p(ZCU102ShellPMOD2) == "PMODJ87_JTAG"
  val jtag_location = Some(if (pmod_is_sdio) (if (pmod_j87_is_jtag) "PMODJ87_JTAG" else "FMC_J5") else "PMOD_J55")

  // Order matters; ddr depends on sys_clock
  val uart      = Overlay(UARTOverlayKey, new UARTZCU102ShellPlacer(this, UARTShellInput()))
  val sdio      = if (pmod_is_sdio) Some(Overlay(SPIOverlayKey, new SDIOZCU102ShellPlacer(this, SPIShellInput()))) else None
  val jtag      = Overlay(JTAGDebugOverlayKey, new JTAGDebugZCU102ShellPlacer(this, JTAGDebugShellInput(location = jtag_location)))
  val cjtag     = Overlay(cJTAGDebugOverlayKey, new cJTAGDebugZCU102ShellPlacer(this, cJTAGDebugShellInput()))
  val jtagBScan = Overlay(JTAGDebugBScanOverlayKey, new JTAGDebugBScanZCU102ShellPlacer(this, JTAGDebugBScanShellInput()))
  val fmc       = Overlay(PCIeOverlayKey, new PCIeZCU102FMCShellPlacer(this, PCIeShellInput()))
  //val edge      = Overlay(PCIeOverlayKey, new PCIeZCU102EdgeShellPlacer(this, PCIeShellInput()))

  val topDesign = LazyModule(p(DesignKey)(designParameters))

  // Place the sys_clock at the Shell if the user didn't ask for it
  designParameters(ClockInputOverlayKey).foreach { unused =>
    val source = unused.place(ClockInputDesignInput()).overlayOutput.node
    val sink = ClockSinkNode(Seq(ClockSinkParameters()))
    sink := source
  }

  override lazy val module = new LazyRawModuleImp(this) {
    val reset = IO(Input(Bool()))

    //zcu102 pg 89, vcu118 pg 90
    xdc.addPackagePin(reset, "AM13")
    xdc.addIOStandard(reset, "LVCMOS33")

    val reset_ibuf = Module(new IBUF)
    reset_ibuf.io.I := reset

    val sysclk: Clock = sys_clock.get() match {
      case Some(x: SysClockZCU102PlacedOverlay) => x.clock
    }

    val powerOnReset: Bool = PowerOnResetFPGAOnly(sysclk)
    sdc.addAsyncPath(Seq(powerOnReset))

    val ereset: Bool = chiplink.get() match {
      case Some(x: ChipLinkZCU102PlacedOverlay) => !x.ereset_n
      case _ => false.B
    }

    pllReset := (reset_ibuf.io.O || powerOnReset || ereset)
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
