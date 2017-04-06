package com.datagre.framework.foundation.internals.provider;

import com.datagre.framework.foundation.spi.Logger;
import com.datagre.framework.foundation.spi.LoggerManager;
import com.datagre.framework.foundation.spi.provider.DeploymentProvider;
import com.datagre.framework.foundation.spi.provider.Provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class DefaultDeploymentProvider extends AbstractProvider implements DeploymentProvider {
   private static final Logger logger = LoggerManager.getLogger();
   private static final String DEPLOY_PROPERTIES_LINUX = "/opt/settings/deploy.properties";
   private static final String DEPLOY_PROPERTIES_WINDOWS = "C:/opt/settings/deploy.properties";

   private Properties m_deployProperties = new Properties();

   @Override
   public Class<? extends Provider> getType() {
      return DeploymentProvider.class;
   }

   @Override
   public String getProperty(String name, String defaultValue) {
      String val = m_deployProperties.getProperty(name, defaultValue);
      return val == null ? defaultValue : val.trim();
   }

   @Override
   public void initialize() {
      try {
         File file = new File(DEPLOY_PROPERTIES_LINUX);
         if (file.exists() && file.canRead()) {
            logger.log("Loading " + file.getAbsolutePath());
            FileInputStream fis = new FileInputStream(file);
            initialize(fis);
            return;
         }
         logger.log(DEPLOY_PROPERTIES_LINUX + " does not exist or is not readable.");

         file = new File(DEPLOY_PROPERTIES_WINDOWS);
         if (file.exists() && file.canRead()) {
            logger.log("Loading " + file.getAbsolutePath());
            FileInputStream fis = new FileInputStream(file);
            initialize(fis);
            return;
         }
         logger.log(DEPLOY_PROPERTIES_WINDOWS + " does not exist or is not readable.");
         initialize(null);
      } catch (Exception ex) {
         logger.log(ex);
      }
   }

   @Override
   public void initialize(InputStream in) {
      try {
         if (in != null) {
            try {
               m_deployProperties.load(new InputStreamReader(in, StandardCharsets.UTF_8));
            } finally {
               in.close();
            }
         }
      } catch (Exception ex) {
         logger.log(ex);
      }
   }
}
