package org.jboss.bpmn2.editor.core;

import org.jboss.bpmn2.editor.core.features.sequenceflow.CreateSequenceFlowFeature
import org.jboss.bpmn2.editor.core.features.task.CreateTaskFeature
import org.jboss.bpmn2.editor.core.features.gateway.exclusive.CreateExclusiveGatewayFeature
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
      
    }
  }
}