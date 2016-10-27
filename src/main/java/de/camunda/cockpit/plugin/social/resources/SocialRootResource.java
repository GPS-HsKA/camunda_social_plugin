package de.camunda.cockpit.plugin.social.resources;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import de.camunda.cockpit.plugin.social.SocialPlugin;
import org.camunda.bpm.cockpit.plugin.resource.AbstractCockpitPluginRootResource;

/**
 * root resource to select sub resource for the selected engine (as you could run multiple engines)
 * 
 * @author ruecker
 *
 */
@Path("plugin/" + SocialPlugin.ID)
public class SocialRootResource extends AbstractCockpitPluginRootResource {

  public SocialRootResource() {
    super(SocialPlugin.ID);
  }

  @Path("{engineName}/")
  public SocialEngineSpecificResource getProcessInstanceResource(@PathParam("engineName") String engineName) {
    SocialEngineSpecificResource SocialEngineSpecificResource = new SocialEngineSpecificResource(engineName);
	return subResource(SocialEngineSpecificResource, engineName);
  }
}
