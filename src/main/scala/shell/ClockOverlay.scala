package sifive.fpgashells.shell

import chisel3._
import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy._
import sifive.fpgashells.clocks._

case class ClockInputShellInput()
case class ClockOutputShellInput()
case class ClockInputDesignInput()(implicit val p: Parameters)
case class ClockOutputDesignInput()(implicit val p: Parameters)
case class ClockInputOverlayOutput(node: ClockSourceNode)
case class ClockOutputOverlayOutput(clock: ClockSinkNode)

trait ClockInputShellPlacer[Shell] extends ShellPlacer[ClockInputDesignInput, ClockInputShellInput, ClockInputOverlayOutput]
trait ClockOutputShellPlacer[Shell] extends ShellPlacer[ClockOutputDesignInput, ClockOutputShellInput, ClockOutputOverlayOutput]

case object ClockInputOverlayKey  extends Field[Seq[DesignPlacer[ClockInputDesignInput, ClockInputShellInput, ClockInputOverlayOutput]]](Nil)
case object ClockOutputOverlayKey extends Field[Seq[DesignPlacer[ClockOutputDesignInput, ClockOutputShellInput, ClockOutputOverlayOutput]]](Nil)

class LVDSClock extends Bundle
{
  val p = Clock()
  val n = Clock()
}

abstract class LVDSClockInputPlacedOverlay(
  val name: String, val di: ClockInputDesignInput, val si: ClockInputShellInput)
    extends IOPlacedOverlay[LVDSClock, ClockInputDesignInput, ClockInputShellInput, ClockInputOverlayOutput]
{
  implicit val p = di.p
  def node: ClockSourceNode

  def ioFactory = Input(new LVDSClock)

  val clock = shell { InModuleBody {
    val (bundle, edge) = node.out.head
    shell.sdc.addClock(name, io.p, edge.clock.freqMHz)
    bundle.clock
  } }
  def overlayOutput = ClockInputOverlayOutput(node)
}


abstract class SingleEndedClockInputPlacedOverlay(
  val name: String, val di: ClockInputDesignInput, val si: ClockInputShellInput)
    extends IOPlacedOverlay[Clock, ClockInputDesignInput, ClockInputShellInput, ClockInputOverlayOutput]
{
  implicit val p = di.p
  def node: ClockSourceNode

  def ioFactory = Input(Clock())

  val clock = shell { InModuleBody {
    val (bundle, edge) = node.out.head
    shell.sdc.addClock(name, io:Clock, edge.clock.freqMHz)
    bundle.clock
  } }
  def overlayOutput = ClockInputOverlayOutput(node)
}

abstract class SingleEndedClockBundleInputPlacedOverlay(
  val name: String, val di: ClockInputDesignInput, val si: ClockInputShellInput)
    extends IOPlacedOverlay[ClockBundle, ClockInputDesignInput, ClockInputShellInput, ClockInputOverlayOutput]
{
  implicit val p = di.p
  def node: ClockSourceNode

  def ioFactory = Input(new ClockBundle(ClockBundleParameters()))

  val clock = shell { InModuleBody {
    val (bundle, edge) = node.out.head
    bundle.clock
  } }
  val reset = shell { InModuleBody {
    val (bundle, edge) = node.out.head
    bundle.reset
  } }
  def overlayOutput = ClockInputOverlayOutput(node)
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
