package org.jboss.bpmn2.editor.ui.util

import org.jboss.bpmn2.editor.ui.Activator
import org.eclipse.core.runtime.Status
import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.CoreException

import org.junit.runner.RunWith
import org.specs._
import org.specs.matcher._
import org.specs.runner.{ JUnitSuiteRunner, JUnit }

@RunWith(classOf[JUnitSuiteRunner])
class UtilTest extends Specification with JUnit { /*with ScalaCheck*/

  "ErrorUtils" should {
    "when called" in {
      val message = "Message"
      "should throw CoreException" in {
        try {
          ErrorUtils.throwCoreException(message)
        } catch {
          case e: CoreException => {
            "with message" in {
              message must_== e.getStatus.getMessage
            }
            "with correct plugin ID" in {
              Activator.PLUGIN_ID must_== e.getStatus().getPlugin
            }
          }
        }
      }
    }
  }
}

object UtilMain {
  def main(args: Array[String]) {
    new UtilTest().main(args)
  }
}