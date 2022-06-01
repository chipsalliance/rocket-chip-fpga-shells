package sifive.fpgashells.clocks

import Chisel._
import chisel3.internal.sourceinfo.SourceInfo
import freechips.rocketchip.config.Parameters
import freechips.rocketchip.diplomacy._

object ClockImp extends SimpleNodeImp[ClockSourceParameters, ClockSinkParameters, ClockEdgeParameters, ClockBundle]
{
  def edge(pd: ClockSourceParameters, pu: ClockSinkParameters, p: Parameters, sourceInfo: SourceInfo) = ClockEdgeParameters(pd, pu, p, sourceInfo)
  def bundle(e: ClockEdgeParameters) = new ClockBundle(e.bundle)
  def render(e: ClockEdgeParameters) = RenderedEdge(colour = "#00cc00" /* green */)
}

case class ClockSourceNode(val portParams: Seq[ClockSourceParameters])(implicit valName: ValName) extends SourceNode(ClockImp)(portParams)
case class ClockSinkNode(val portParams: Seq[ClockSinkParameters])(implicit valName: ValName) extends SinkNode(ClockImp)(portParams)
case class ClockAdapterNode(
  sourceFn: ClockSourceParameters => ClockSourceParameters = { m => m },
  sinkFn:   ClockSinkParameters   => ClockSinkParameters   = { s => s })(
  implicit valName: ValName)
  extends AdapterNode(ClockImp)(sourceFn, sinkFn)
case class ClockIdentityNode()(implicit valName: ValName) extends IdentityNode(ClockImp)()

object ClockSinkNode
{
  def apply(
    freqMHz:       Double,
    dutyCycle:     Double = 50,
    phaseDeg:      Double = 0,
    // Create SDC/TCL constraints that the clock matches these requirements:
    phaseErrorDeg: Double = 5,
    freqErrorPPM:  Double = 10000,
    jitterPS:      Double = 300)(implicit valName: ValName): ClockSinkNode =
    ClockSinkNode(Seq(ClockSinkParameters(
      phaseDeg      = phaseDeg,
      phaseErrorDeg = phaseErrorDeg,
      freqErrorPPM  = freqErrorPPM,
      jitterPS      = jitterPS,
      take          = Some(ClockParameters(
        freqMHz     = freqMHz,
        dutyCycle   = dutyCycle)))))
}

object ClockSourceNode
{
  def apply(
    freqMHz:   Double,
    dutyCycle: Double = 50,
    jitterPS:  Double = 300)(implicit valName: ValName): ClockSourceNode =
    ClockSourceNode(Seq(ClockSourceParameters(
      jitterPS    = Some(jitterPS),
      give        = Some(ClockParameters(
        freqMHz   = freqMHz,
        dutyCycle = dutyCycle)))))
}

object ClockGroupImp extends SimpleNodeImp[ClockGroupSourceParameters, ClockGroupSinkParameters, ClockGroupEdgeParameters, ClockGroupBundle]
{
  def edge(pd: ClockGroupSourceParameters, pu: ClockGroupSinkParameters, p: Parameters, sourceInfo: SourceInfo) = ClockGroupEdgeParameters(pd, pu, p, sourceInfo)
  def bundle(e: ClockGroupEdgeParameters) = new ClockGroupBundle(e.bundle)
  def render(e: ClockGroupEdgeParameters) = RenderedEdge(colour = "#00cc00" /* green */)
}

case class ClockGroupAdapterNode(
  sourceFn: ClockGroupSourceParameters => ClockGroupSourceParameters = { m => m },
  sinkFn:   ClockGroupSinkParameters   => ClockGroupSinkParameters   = { s => s })(
  implicit valName: ValName)
  extends AdapterNode(ClockGroupImp)(sourceFn, sinkFn)
case class ClockGroupIdentityNode()(implicit valName: ValName) extends IdentityNode(ClockGroupImp)()

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
