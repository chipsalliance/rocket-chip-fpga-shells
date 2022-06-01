package sifive.fpgashells.shell.microsemi

 import chisel3._
import freechips.rocketchip.diplomacy._
import sifive.fpgashells.shell._
import sifive.fpgashells.ip.microsemi._

 abstract class LEDMicrosemiPlacedOverlay(name: String, di: LEDDesignInput, si: LEDShellInput, pins: Seq[String] = Nil)
  extends LEDPlacedOverlay(name, di, si)
{
  def shell: MicrosemiShell
  val width = pins.size

   shell { InModuleBody {
    io := ledSink.bundle // could/should put OBUFs here?
    (pins zip IOPin.of(io)) foreach { case (pin, io) => shell.io_pdc.addPin(io, pin) }
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
