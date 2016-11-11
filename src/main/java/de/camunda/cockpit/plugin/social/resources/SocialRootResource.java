package de.camunda.cockpit.plugin.social.resources;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import de.camunda.cockpit.plugin.social.SocialPlugin;
import org.camunda.bpm.cockpit.plugin.resource.AbstractCockpitPluginRootResource;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// Einstiegspunkt des Plugins
  @Path("plugin/" + SocialPlugin.ID)
  public class SocialRootResource extends AbstractCockpitPluginRootResource {

  public SocialRootResource() {
    super(SocialPlugin.ID);
  }

  // Ermöglicht Zugriff auf die Process-Engine
  @Path("{engineName}/")
  public SocialEngineSpecificResource getProcessInstanceResource(@PathParam("engineName") String engineName) {
    SocialEngineSpecificResource socialEngineSpecificResource = new SocialEngineSpecificResource(engineName);
    return subResource(socialEngineSpecificResource, engineName);
  }
}
