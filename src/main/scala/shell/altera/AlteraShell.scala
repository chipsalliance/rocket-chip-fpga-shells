package sifive.fpgashells.shell.altera

import chisel3._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.util._
import org.chipsalliance.cde.config._
import sifive.fpgashells.clocks._
import sifive.fpgashells.ip.altera._
import sifive.fpgashells.shell._

class IO_TCL(val name: String)
{
  private var constraints: Seq[() => String] = Nil
  protected def addConstraint(command: => String) { constraints = (() => command) +: constraints }
  ElaborationArtefacts.add(name, constraints.map(_()).reverse.mkString("\n") + "\n")

  def addPackagePin(io: IOPin, pin: String) {
    addConstraint(s"set_location_assignment {${pin}} -to ${io.name}")
  }
  def addIOStandard(io: IOPin, standard: String) {
    addConstraint(s"set_instance_assignment -name IO_STANDARD {${standard}} -to ${io.name}")
  }
  // def addPullup(io: IOPin) {
  //   addConstraint(s"set_property PULLUP {TRUE} ${io.sdcPin}")
  // }
  // def addSlew(io: IOPin, speed: String) {
  //   addConstraint(s"set_property SLEW {${speed}} ${io.sdcPin}")
  // }
  // TODO: Add input/output termination
  // def addTermination(io: IOPin, kind: String) {
  //   addConstraint(s"set_property OFFCHIP_TERM {${kind}} ${io.sdcPin}")
  // }
  def addDriveStrength(io: IOPin, drive: String) {
    addConstraint(s"set_instance_assignment -name CURRENT_STRENGTH_NEW {${drive}} -to ${io.name}")
  }
}

abstract class AlteraShell()(implicit p: Parameters) extends IOShell
{
  val sdc = new AlteraSDC("shell.sdc")
  val io_tcl = new IO_TCL("assign.tcl")
  def pllReset: ModuleValue[Bool]

  val pllFactory = new AlteraPLLFactory(this, 9, p => Module(new AlteraPLL(p)))

  sdc.addSDCDirective("derive_pll_clocks")
  sdc.addSDCDirective("derive_clock_uncertainty")
  
  override def designParameters = super.designParameters.alterPartial {
    case PLLFactoryKey => pllFactory
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
