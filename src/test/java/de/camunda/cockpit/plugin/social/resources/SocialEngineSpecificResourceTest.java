package de.camunda.cockpit.plugin.social.resources;

import java.util.List;

import org.camunda.bpm.cockpit.plugin.test.AbstractCockpitPluginTest;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.camunda.cockpit.plugin.social.dto.SocialContainerDto;

public class SocialEngineSpecificResourceTest extends AbstractCockpitPluginTest {

	private SocialEngineSpecificResource socialEngineSpecificResource;
	private ProcessEngine processEngine;
	private RuntimeService runtimeService;
	private RepositoryService repositoryService;
	private ManagementService managementService;
	private TaskService taskService;
	private HistoryService historyService;

	@Before
	public void setup(){
		super.before();
		socialEngineSpecificResource = new SocialEngineSpecificResource("engineName");
		processEngine = getProcessEngine();	    
	    runtimeService = processEngine.getRuntimeService();
	    repositoryService = processEngine.getRepositoryService();
	    managementService = processEngine.getManagementService();
	    taskService = processEngine.getTaskService();
		historyService = processEngine.getHistoryService();
	}

	@Deployment(resources = "processes/simple-user-task-process.bpmn")

	@Test
	public void testGetProcessInstanceSocial() {
		SocialContainerDto processInstanceSocial = socialEngineSpecificResource.getProcessInstanceSocial("processDefinitionId");
		Assert.assertNull(processInstanceSocial);
	}
	
}
