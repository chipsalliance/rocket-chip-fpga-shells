package sifive.fpgashells.shell.altera

import freechips.rocketchip.diplomacy._
import sifive.fpgashells.shell._

abstract class LEDAlteraPlacedOverlay(name: String, di: LEDDesignInput, si: LEDShellInput, packagePin: Option[String] = None, ioStandard: String = "3.3-V LVTTL")
  extends LEDPlacedOverlay(name, di, si)
{
  def shell: AlteraShell

  shell { InModuleBody {
    io := ledWire // could/should put OBUFs here?

    val ios = IOPin.of(io)

    (packagePin.toSeq zip ios) foreach { case (pin, io) =>
      shell.io_tcl.addPackagePin(io, pin)
      shell.io_tcl.addIOStandard(io, ioStandard)
    }
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
