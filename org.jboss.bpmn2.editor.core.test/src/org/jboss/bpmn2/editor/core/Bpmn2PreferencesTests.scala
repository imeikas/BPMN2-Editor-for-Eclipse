package org.jboss.bpmn2.editor.core;

import NiceObject._
import org.junit.runner.RunWith
import org.specs._
import org.specs.matcher._
import org.specs.runner.{ JUnitSuiteRunner, JUnit }

import org.jboss.bpmn2.editor.core._;
import features._;

import scalaj.collection.Imports._

import org.eclipse.core.resources._;

@RunWith(classOf[JUnitSuiteRunner])
class Bpmn2PreferencesTests extends Specification with JUnit {
  def cleanProject(): IProject = {
    val project = ResourcesPlugin.getWorkspace().getRoot().getProject("test");
    if (project.exists)
      project.delete(IResource.FORCE, null);
    project.create(null)
    project.open(null);
    project
  }

  "Bpmn2Preferences" should {
    "load project specific settings" in {
      val project = cleanProject
      project.exists() must beTrue

      val pref = Bpmn2Preferences.getPreferences(project)
      pref must notBeNull
      "and give a list of tools" in {
        val tools = pref.getListOfTools.asScala
        tools must notBeNull
        "tools must contain Task" in {
          tools.exists(classOf[CreateTaskFeature] == _.getFeature.niceClass) must beTrue
        }
        "tools must contain ExclusiveGateway" in {
          tools.exists(classOf[CreateExclusiveGatewayFeature] == _.getFeature.niceClass) must beTrue
        }

        "and allow checking their enablement" in {
          tools.forall(_.getEnabled().booleanValue) must beTrue
        }

        "and allow setting them as disabled" in {
          tools.foreach(x => pref.setEnabled(x.getFeature, false))
          val tools2 = pref.getListOfTools.asScala
          tools2.forall(_.getEnabled().booleanValue != true) must beTrue
        }

      }
      "and give a list of connectors" in {
        val connectors = pref.getListOfConnectors.asScala
        connectors must notBeNull
        "connectors must contain SequenecFlow" in {
          connectors.exists(classOf[CreateSequenceFlowFeature] == _.getFeature.niceClass) must beTrue
        }
        "and allow checking their enablement" in {
          connectors.forall(_.getEnabled().booleanValue) must beTrue
        }

        "and allow setting them as disabled" in {
          connectors.foreach(x => pref.setEnabled(x.getFeature, false))
          val connectors2 = pref.getListOfConnectors.asScala
          connectors2.forall(_.getEnabled().booleanValue != true) must beTrue
        }
      }
    }
  }
}