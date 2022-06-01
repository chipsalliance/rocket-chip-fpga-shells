package sifive.fpgashells.ip.microsemi

import Chisel._
import chisel3.{Input, Output}
import chisel3.experimental.{Analog, attach}
import freechips.rocketchip.util.{ElaborationArtefacts}


//========================================================================
// This file contains common devices for Microsemi PolarFire FPGAs.
//========================================================================

//-------------------------------------------------------------------------
// Clock network macro
//-------------------------------------------------------------------------

class CLKBUF() extends BlackBox
{
  val io = new Bundle{
    val PAD = Clock(INPUT)
    val Y = Clock(OUTPUT)
  }
}

class CLKINT() extends BlackBox
{
  val io = new Bundle{
    val A = Clock(INPUT)
    val Y = Clock(OUTPUT)
  }
}

class ICB_CLKINT() extends BlackBox
{
  val io = new Bundle{
    val A = Clock(INPUT)
    val Y = Clock(OUTPUT)
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
