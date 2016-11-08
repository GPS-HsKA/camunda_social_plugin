package de.camunda.cockpit.plugin.social.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.camunda.bpm.cockpit.db.QueryParameters;
import org.camunda.bpm.cockpit.plugin.resource.AbstractCockpitPluginResource;

import de.camunda.cockpit.plugin.social.dto.SocialContainerDto;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.VariableInstance;

public class SocialEngineSpecificResource extends AbstractCockpitPluginResource {


	public SocialEngineSpecificResource(String engineName) {
		super(engineName);
	}

	@GET
	@Path("{process-definition-key}/tags")
	public SocialContainerDto[] getProcessInstance(
			@PathParam("process-definition-key") String processDefinitionKey) {

		List<ProcessInstance> processInstances = getProcessEngine().getRuntimeService().createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).list();
		if (processInstances.size() == 0) {
			return new SocialContainerDto[]{};
		}

		List<String> processInstanceIds = new ArrayList<String>(processInstances.size());
		for (ProcessInstance processInstance : processInstances ) {
			processInstanceIds.add(processInstance.getId());
		}

		List<VariableInstance> variableInstances = getProcessEngine().getRuntimeService().createVariableInstanceQuery().processInstanceIdIn(processInstanceIds.toArray(new String[processInstanceIds.size()])).list();
		List<SocialContainerDto> dtos = new ArrayList<SocialContainerDto>();

		for (VariableInstance variableInstance : variableInstances) {
			SocialContainerDto dto = new SocialContainerDto();
			dto.setTag(variableInstance.getName());
			dtos.add(dto);
		}

		return  dtos.toArray(new SocialContainerDto[dtos.size()]);
	}
}
