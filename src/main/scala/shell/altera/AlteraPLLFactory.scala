package sifive.fpgashells.shell.altera

import chisel3._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.prci._
import org.chipsalliance.cde.config._
import sifive.fpgashells.shell._
import sifive.fpgashells.clocks._

case object AlteraPLLFactoryKey extends Field[PLLFactory]
class AlteraPLLFactory(scope: IOShell, maxOutputs: Int, gen: PLLParameters => PLLInstance) extends PLLFactory(scope, maxOutputs, gen)
{
  private var pllNodes: Seq[PLLNode] = Nil

  override def apply(feedback: Boolean = false)(implicit valName: ValName, p: Parameters): PLLNode = {
    val node = scope { PLLNode(feedback) }
    pllNodes = node +: pllNodes
    node
  }

  override val plls: ModuleValue[Seq[(PLLInstance, PLLNode)]] = scope { InModuleBody {
    val plls = pllNodes.flatMap { case node =>
      require (node.in.size == 1)
      val (in, edgeIn) = node.in(0)
      val (out, edgeOut) = node.out.unzip

      val params = PLLParameters(
        name = node.valName.name,
        input = PLLInClockParameters(
          freqMHz  = edgeIn.clock.get.freqMHz,
          jitter   = edgeIn.source.jitterPS.getOrElse(50),
          feedback = node.feedback),
        req = edgeOut.flatMap(_.members).map { e =>
          PLLOutClockParameters(
            freqMHz       = e._2.clock.get.freqMHz,
            phaseDeg      = e._2.sink.phaseDeg,
            dutyCycle     = e._2.clock.get.dutyCycle,
            jitterPS      = e._2.sink.jitterPS,
            freqErrorPPM  = e._2.sink.freqErrorPPM,
            phaseErrorDeg = e._2.sink.phaseErrorDeg)})

      val pll = gen(params)
      pll.getInput := in.clock
      pll.getReset.foreach { _ := in.reset }
      (out.flatMap(_.member.data) zip pll.getClocks) foreach { case (o, i) =>
        o.clock := i
        o.reset := !pll.getLocked || in.reset.asBool
      }
      Some((pll, node))
    }

    // Require all clock group names to be distinct
    val sdcGroups = plls.flatMap { case tuple =>
        val (pll, node) = tuple
        val clkHierarchies = pll.getClockNames
        clkHierarchies.map { case clkName =>
            val clkpath = clkName.split("/")
            val pllOutNum = ("""\d+""".r findAllIn clkpath(1)).next()
            s"${clkpath(0)}|altera_pll_i|general[${pllOutNum}].gpll~PLL_OUTPUT_COUNTER|divclk"
        }
    }
    println("Clocks:")
    println(sdcGroups)
    // Ensure there are no clock groups with the same name
    require (sdcGroups.size == pllNodes.map(_.edges.out.size).sum)
    sdcGroups.foreach { case clk_names => scope.sdc.addGroup(clocks = Seq(clk_names)) }

    plls
  } }
}
