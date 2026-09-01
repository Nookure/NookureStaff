/**
 * This package contains the configuration classes for the plugin.
 * See {@link com.nookure.staff.api.config.ConfigurationContainer} if you want to create a new configuration file.
 * To obtain a configuration file, you must use the injector to inject the configuration file into the class.
 * <pre>
 * {@code
 * @Inject private ConfigurationContainer<YourConfiguration> yourConfiguration;
 *
 * public void foo() {
 *   // An atomic reference is used to store the configuration file.
 *   // This is because the configuration file can change at any time.
 *   YourConfiguration configuration = yourConfiguration.get();
 * }
 * }
 * </pre>
 *
 * @since 1.0.0
 */
package com.nookure.staff.api.config;
