package com.daypaytechnologies;

import com.daypaytechnologies.core.boot.AbstractApplicationConfiguration;
import com.daypaytechnologies.core.boot.ApplicationExitUtil;
import com.daypaytechnologies.core.boot.EmbeddedTomcatWithoutSSLConfiguration;
import com.daypaytechnologies.core.boot.db.DataSourceConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

/**
 * Fineract main() application which launches Fineract in an embedded Tomcat HTTP
 * (using Spring Boot).
 *
 * The DataSource used is a to a "normal" external database (not use MariaDB4j).
 *
 * You can easily launch this via Debug as Java Application in your IDE -
 * without needing command line Gradle stuff, no need to build and deploy a WAR,
 * remote attachment etc.
 *
 * It's the old/classic Mifos (non-X) Workspace 2.0 reborn for Fineract! ;-)
 *
 */
public class ServerApplication {

	@Import({ DataSourceConfiguration.class, EmbeddedTomcatWithoutSSLConfiguration.class })
	private static class Configuration extends AbstractApplicationConfiguration { }

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext ctx = SpringApplication.run(Configuration.class, args);
		ApplicationExitUtil.waitForKeyPressToCleanlyExit(ctx);
	}

}
