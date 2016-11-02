package de.camunda.cockpit.plugin.social.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.camunda.bpm.cockpit.db.QueryParameters;
import org.camunda.bpm.cockpit.plugin.resource.AbstractCockpitPluginResource;

import de.camunda.cockpit.plugin.social.dto.SocialContainerDto;

public class SocialEngineSpecificResource extends AbstractCockpitPluginResource {


	public SocialEngineSpecificResource(String engineName) {
		super(engineName);
	}

	@GET
	@Path("process-definition/{id}/taskstats")
	public List<SocialContainerDto> getTaskActititySocial(
			@PathParam("id") String processDefinitionId) {

		QueryParameters<SocialContainerDto> parameter = new QueryParameters<SocialContainerDto>();
		parameter.setParameter(processDefinitionId);
		return getQueryService().executeQuery(
				"cockpit.plugin.social-plugin.selectSocialForTaskActitvity",
				parameter);

	}

	@GET
	@Path("process-definition/{id}/stats")
	public SocialContainerDto getProcessInstanceSocial(
			@PathParam("id") String processDefinitionId) {
		return getQueryService().executeQuery(
				"cockpit.plugin.social-plugin.selectSocialForProcessInstances",
				processDefinitionId, SocialContainerDto.class);
	}

}
