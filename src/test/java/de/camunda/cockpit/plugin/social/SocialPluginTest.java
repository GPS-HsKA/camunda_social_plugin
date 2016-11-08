package de.camunda.cockpit.plugin.social;

import de.camunda.cockpit.plugin.social.dto.SocialContainerDto;
import org.camunda.bpm.cockpit.Cockpit;
import org.camunda.bpm.cockpit.db.QueryParameters;
import org.camunda.bpm.cockpit.db.QueryService;
import org.camunda.bpm.cockpit.plugin.spi.CockpitPlugin;
import org.camunda.bpm.cockpit.plugin.test.AbstractCockpitPluginTest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

public class SocialPluginTest extends AbstractCockpitPluginTest {

	@Test
	public void testPluginDiscovery() {
		CockpitPlugin socialPlugin = Cockpit.getRuntimeDelegate()
				.getPluginRegistry().getPlugin(SocialPlugin.ID);
		Assert.assertNotNull(socialPlugin);
	}

	@Test
	public void testPluginQueryWorks() {

		QueryService queryService = getQueryService();

		List<SocialContainerDto> instanceSocial =
				queryService
						.executeQuery(
								"cockpit.plugin.social.selectSocialForProcessInstances",
								new QueryParameters<SocialContainerDto>());

		Assert.assertEquals(0, instanceSocial.size());
	}

}
