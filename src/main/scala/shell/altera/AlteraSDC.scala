package sifive.fpgashells.shell

import chisel3._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.util._
import org.chipsalliance.cde.config._
import sifive.fpgashells.shell._


class AlteraSDC(val sdc_name: String) extends SDC(sdc_name)
{

  override def addGroup(clocks: => Seq[String] = Nil, pins: => Seq[IOPin] = Nil) {
    def thunk = {
      val clocksList = clocks
      val (pinsList, portsList) = pins.map(_.name).partition(_.contains("/"))
      val sep = " \\\n      "
      val clocksStr = (" [get_clocks {" +: clocksList).mkString(sep) + " \\\n    }]"
      val pinsStr   = (" [get_clocks -of_objects [get_pins {"  +: pinsList ).mkString(sep) + " \\\n    }]]"
      val portsStr  = (" [get_clocks -of_objects [get_ports {" +: portsList).mkString(sep) + " \\\n    }]]"
      val str = s"  -group [list${if (clocksList.isEmpty) "" else clocksStr}${if (pinsList.isEmpty) "" else pinsStr}${if (portsList.isEmpty) "" else portsStr}]"  
      if (clocksList.isEmpty && pinsList.isEmpty && portsList.isEmpty) "" else str
    }
    addRawGroup(thunk)
  }

  override def addClock(name: => String, pin: => IOPin, freqMHz: => Double, jitterNs: => Double = 0.5) {
    addRawClock(s"create_clock -name ${name} -period ${1000/freqMHz} ${pin.sdcPin}")
  }
  private def flatten(x: Seq[() => String], sep: String = "\n") = x.map(_()).filter(_ != "").reverse.mkString(sep)

  // TODO: Hack, figure out how to add sdc directives more elegantly
  def addSDCDirective(command: => String) { addRawClock(command)}
}