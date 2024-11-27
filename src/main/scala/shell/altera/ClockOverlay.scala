package sifive.fpgashells.shell.altera

 import chisel3._
 import freechips.rocketchip.diplomacy._
 import sifive.fpgashells.ip.altera._
 import sifive.fpgashells.shell._

 abstract class SingleEndedClockInputAlteraPlacedOverlay(name: String, di: ClockInputDesignInput, si: ClockInputShellInput)
  extends SingleEndedClockInputPlacedOverlay(name, di, si)
{
  def shell: AlteraShell

   shell { InModuleBody {
    val (c, _) = node.out(0)

    c.clock := io
    c.reset := shell.pllReset
  } }
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
