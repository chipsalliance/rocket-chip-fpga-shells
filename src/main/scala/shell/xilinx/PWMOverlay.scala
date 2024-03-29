package sifive.fpgashells.shell.xilinx

 import chisel3._
 import freechips.rocketchip.diplomacy._
 import sifive.fpgashells.shell._

abstract class PWMXilinxPlacedOverlay(name: String, di: PWMDesignInput, si: PWMShellInput)
  extends PWMPlacedOverlay(name, di, si)
{
  def shell: XilinxShell

  shell { InModuleBody {
    tlpwmSink.bundle.gpio.zip(io.pwm_gpio).foreach { case(design_pwm, io_pwm) =>
      UIntToAnalog(design_pwm, io_pwm, true.B)
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
