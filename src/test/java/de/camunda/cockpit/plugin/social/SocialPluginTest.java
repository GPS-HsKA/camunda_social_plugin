package de.camunda.cockpit.plugin.social;

import org.camunda.bpm.cockpit.Cockpit;
import org.camunda.bpm.cockpit.plugin.spi.CockpitPlugin;
import org.camunda.bpm.cockpit.plugin.test.AbstractCockpitPluginTest;
import org.junit.Assert;
import org.junit.Test;

public class SocialPluginTest extends AbstractCockpitPluginTest {

	@Test
	public void testPluginDiscovery() {
		CockpitPlugin samplePlugin = Cockpit.getRuntimeDelegate()
				.getPluginRegistry().getPlugin(SocialPlugin.ID);
		Assert.assertNotNull(samplePlugin);		
	}

}
