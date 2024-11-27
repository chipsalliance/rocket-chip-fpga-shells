package sifive.fpgashells.ip.altera

import chisel3._
import chisel3.experimental.Analog
import chisel3.util.HasBlackBoxInline
import freechips.rocketchip.util.ElaborationArtefacts
import sifive.fpgashells.clocks._

class AlteraPLL(c : PLLParameters) extends BlackBox with PLLInstance {
  val io = IO(new Bundle {
    val refclk    = Input(Clock())
    val outclk_0  = if (c.req.size >= 1) Some(Output(Clock())) else None
    val outclk_1  = if (c.req.size >= 2) Some(Output(Clock())) else None
    val outclk_2  = if (c.req.size >= 3) Some(Output(Clock())) else None
    val outclk_3  = if (c.req.size >= 4) Some(Output(Clock())) else None
    val outclk_4  = if (c.req.size >= 5) Some(Output(Clock())) else None
    val outclk_5  = if (c.req.size >= 6) Some(Output(Clock())) else None
    val outclk_6  = if (c.req.size >= 7) Some(Output(Clock())) else None
    val outclk_7  = if (c.req.size >= 8) Some(Output(Clock())) else None
    val outclk_8  = if (c.req.size >= 9) Some(Output(Clock())) else None
    val rst       = Input(Bool())
    val locked    = Output(Bool())
  })

  val moduleName = c.name
  override def desiredName = c.name

  def getClocks = Seq() ++ io.outclk_0 ++ io.outclk_1 ++ 
                           io.outclk_2 ++ io.outclk_3 ++ 
                           io.outclk_4 ++ io.outclk_5 ++ 
                           io.outclk_6 ++ io.outclk_7 ++
                           io.outclk_8
  def getInput = io.refclk
  def getReset = Some(io.rst)
  def getLocked = io.locked

  def getClockNames = Seq.tabulate (c.req.size) { i =>
    s"${c.name}/${c.name}_0/pll_inst_0/OUT${i}"
  }

  val outputs = c.req.zipWithIndex.map { case (r, i) =>
    s"""                               \"--component-param=gui_output_clock_frequency${i}=${r.freqMHz}\" \\
       |                               \"--component-param=gui_phase_shift${i}=${r.phaseDeg}\" \\
       |                               \"--component-param=gui_duty_cycle${i}=${r.dutyCycle}\" \\""".stripMargin
  }.mkString

  ElaborationArtefacts.add(s"${moduleName}.quartus.tcl",
    s"""exec -ignorestderr ip-generate \"--output-directory=.\" \\
       |                               \"--file-set=QUARTUS_SYNTH\" \\
       |                               \"--component-name=altera_pll\" \\
       |                               \"--output-name=${c.name}\" \\
       |                               \"--system-info=DEVICE_FAMILY=Cyclone V\" \\
       |                               \"--component-param=gui_number_of_clocks=${c.req.size.toString}\" \\
       |                               \"--component-param=gui_reference_clock_frequency=${c.input.freqMHz.toString}\" \\
       |${outputs}
       |
       |set_global_assignment -name QIP_FILE ${c.name}.qip
       |set_global_assignment -name VERILOG_FILE ${c.name}.v""".stripMargin)
}
