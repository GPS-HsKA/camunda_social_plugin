package de.camunda.cockpit.plugin.social;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.camunda.bpm.cockpit.plugin.spi.impl.AbstractCockpitPlugin;
import de.camunda.cockpit.plugin.social.resources.SocialRootResource;

public class SocialPlugin extends AbstractCockpitPlugin {

  public static final String ID = "social";

  private static final String[] MAPPING_FILES = {
	    "org/camunda/social/cockpit/plugin/social-plugin/queries/social.xml"
	  };

  public String getId() {
    return ID;
  }

  @Override
  public Set<Class<?>> getResourceClasses() {
    Set<Class<?>> classes = new HashSet<Class<?>>();

    classes.add(SocialRootResource.class);

    return classes;
  }

  @Override
	public List<String> getMappingFiles() {
	  return Arrays.asList(MAPPING_FILES);
	}



}
