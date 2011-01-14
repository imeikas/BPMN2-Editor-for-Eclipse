package org.jboss.bpmn2.editor.core.diagram

import NiceObject._
import org.jboss.bpmn2.editor.core.Activator
import org.jboss.bpmn2.editor.core.temp._
import org.eclipse.core.runtime.Status
import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.CoreException

import org.junit.runner.RunWith
import org.specs._
import org.specs.matcher._
import org.specs.runner.{ JUnitSuiteRunner, JUnit }


@RunWith(classOf[JUnitSuiteRunner])
class MainProviderTests extends Specification with JUnit { /*with ScalaCheck*/

  val provider = new MainBPMNDiagramTypeProvider

  "MainDiagramTypeProvider" should {
    "have BPMNFeatureProvider as the feature provider" in {
      provider.getFeatureProvider must haveClass[BPMNFeatureProvider]
    }
  }

  "BPMNFeatureProvider" should {
    val fProvider = provider.getFeatureProvider
    "have required creators" in {
      fProvider.getCreateFeatures.toList.exists(_.niceClass == classOf[CreateActivityFeature]) must beTrue
    }
  }
}

object MainProviderMain {
  def main(args: Array[String]) {
    new MainProviderTests().main(args)
  }
}