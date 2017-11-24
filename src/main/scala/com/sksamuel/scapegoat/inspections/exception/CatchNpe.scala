package com.sksamuel.scapegoat.inspections.exception

import com.sksamuel.scapegoat._

/** @author Stephen Samuel */
class CatchNpe extends Inspection("Catching NPE", Levels.Error) {

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def postTyperTraverser = Some apply new context.Traverser {

      import context.global._

      private def catchesNpe(cases: List[CaseDef]): Boolean = {
        cases.exists(_.pat.tpe.toString == "NullPointerException")
      }

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Try(block, catches, finalizer) if catchesNpe(catches) =>
            context.warn(tree.pos, self)
          case _ => continue(tree)
        }
      }
    }
  }
}
