package com.datagre.framework.foundation.internals;

import com.datagre.framework.foundation.internals.provider.*;
import com.datagre.framework.foundation.spi.Logger;
import com.datagre.framework.foundation.spi.LoggerManager;
import com.datagre.framework.foundation.spi.ProviderManager;
import com.datagre.framework.foundation.spi.provider.BuildProvider;
import com.datagre.framework.foundation.spi.provider.Provider;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultProviderManager implements ProviderManager {
   private static final Logger logger = LoggerManager.getLogger();
   private Map<Class<? extends Provider>, Provider> m_providers = new LinkedHashMap<Class<? extends Provider>, Provider>();

   public DefaultProviderManager() {
      // Load build configuration, like app id, from classpath://META-INF/build.properties
      BuildProvider buildProvider = new DefaultBuildProvider();
      buildProvider.initialize();
      register(buildProvider);

      // Load per-application configuration, like app id, from classpath://META-INF/app.properties
      Provider applicationProvider = new DefaultApplicationProvider(buildProvider);
      applicationProvider.initialize();
      register(applicationProvider);

      // Load network parameters
      Provider networkProvider = new DefaultNetworkProvider();
      networkProvider.initialize();
      register(networkProvider);

      // Load environment (fat, fws, uat, prod ...) and dc, from /opt/settings/server.properties, JVM property and/or OS environment variables.
      Provider serverProvider = new DefaultServerProvider();
      serverProvider.initialize();
      register(serverProvider);

      // Load Tomcat/Jetty ServletContext
      Provider webContainerProvider = new DefaultWebContainerProvider();
      webContainerProvider.initialize();
      register(webContainerProvider);

      // Load deploy related properties, from /opt/settings/deploy.properties
      Provider deploymentProvider = new DefaultDeploymentProvider();
      deploymentProvider.initialize();
      register(deploymentProvider);

      // Load server status and deploy status, from /opt/status/server.status and /opt/status/webapp.status
      Provider statusProvider = new DefaultStatusProvider();
      statusProvider.initialize();
      register(statusProvider);
   }

   public synchronized void register(Provider provider) {
      m_providers.put(provider.getType(), provider);
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T extends Provider> T provider(Class<T> clazz) {
      Provider provider = m_providers.get(clazz);

      if (provider != null) {
         return (T) provider;
      } else {
         logger.log(String.format("No provider [%s] found in DefaultProviderManager, please make sure it is registered in DefaultProviderManager ", clazz.getName()));
         return (T) NullProviderManager.provider;
      }
   }

   @Override
   public String getProperty(String name, String defaultValue) {
      for (Provider provider : m_providers.values()) {
         String value = provider.getProperty(name, null);

         if (value != null) {
            return value;
         }
      }

      return defaultValue;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder(512);
      if (null != m_providers) {
         for (Map.Entry<Class<? extends Provider>, Provider> entry : m_providers.entrySet()) {
            sb.append(entry.getValue()).append("\n");
         }
      }
      sb.append("(DefaultProviderManager)").append("\n");
      return sb.toString();
   }
}
