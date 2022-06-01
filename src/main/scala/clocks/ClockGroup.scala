package sifive.fpgashells.clocks

import Chisel._
import chisel3.internal.sourceinfo.SourceInfo
import freechips.rocketchip.config.Parameters
import freechips.rocketchip.diplomacy._

case class ClockGroupNode(groupName: String)(implicit valName: ValName)
  extends MixedNexusNode(ClockGroupImp, ClockImp)(
	dFn = { _ => ClockSourceParameters() },
	uFn = { seq => ClockGroupSinkParameters(name = groupName, members = seq) })

class ClockGroup(groupName: String)(implicit p: Parameters) extends LazyModule
{
  val node = ClockGroupNode(groupName)

  lazy val module = new LazyModuleImp(this) {
    val (in, _) = node.in(0)
    val (out, _) = node.out.unzip

    require (node.in.size == 1)
    require (in.member.size == out.size)

    (in.member zip out) foreach { case (i, o) => o := i }
  }
}

object ClockGroup
{
  def apply()(implicit p: Parameters, valName: ValName) =
    LazyModule(new ClockGroup(valName.name)).node
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
