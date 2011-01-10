package org.jboss.bpmn2.editor.core.diagram

import org.jboss.bpmn2.editor.core._
import org.eclipse.core.runtime.Status
import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.CoreException

import org.eclipse.emf.common.util.URI
import org.eclipse.bpmn2.impl._

import org.junit.runner.RunWith
import org.specs._
import org.specs.matcher._
import org.specs.runner.{ JUnitSuiteRunner, JUnit };

class NiceObject[T <: AnyRef](x: T) {
  def niceClass: Class[_ <: T] = x.getClass.asInstanceOf[Class[T]]
}

@RunWith(classOf[JUnitSuiteRunner])
class ModelHandlerTests extends Specification with JUnit {
  implicit def toNiceObject[T <: AnyRef](x: T) = new NiceObject(x)

  "ModelHandler" should {
    val path = URI.createURI("file:///tmp/test.bpmn2")
    val handler = new ModelHandler(path)

    "be able to create resources" in {
      val resource = handler.createNewResource()
      resource must notBeNull
      "that contain not null ResoureSets" in {
        resource.getResourceSet must notBeNull
      }

      "have correct path for resouce" in {
        resource.getURI must_== path
      }

      "have document root set" in {
        val documentRoot = resource.getContents.get(0)
        classOf[DocumentRootImpl] must_== documentRoot.niceClass
        "that contains BPMN2 definitions" in {
          val definitions = documentRoot.eContents.get(0)
          classOf[DefinitionsImpl] must_== definitions.niceClass
        }
      }
    }

    "be able to create Task" in {
      val resource = handler.createNewResource()
      val task = handler.createTask
      task.isInstanceOf[TaskImpl] must beTrue

      "that is placed to model Resources" in {
        val resource = handler.getResource
        
        "have document root set" in {
          val documentRoot = resource.getContents.get(0)
          classOf[DocumentRootImpl] must_== documentRoot.niceClass
        
          "that contains BPMN2 definitions" in {
            val definitions = documentRoot.eContents.get(0)
            classOf[DefinitionsImpl] must_== definitions.niceClass
          
            "definitions must contain at least one process" in {
              val process = definitions.eContents.get(0)
              process.isInstanceOf[ProcessImpl] must beTrue

              "process must contain at least one task" in {
                val task = process.eContents.get(0)
                task.isInstanceOf[TaskImpl] must beTrue
              }
            }
          }
        }
      }
    }
  }
}

object ModelHandlerMain {
  def main(args: Array[String]) {
    new ModelHandlerTests().main(args)
  }
}