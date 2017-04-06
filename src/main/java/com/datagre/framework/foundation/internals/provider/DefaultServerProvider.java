package com.datagre.framework.foundation.internals.provider;

import com.datagre.framework.foundation.EnvFamily;
import com.datagre.framework.foundation.internals.NetworkInterfaceManager;
import com.datagre.framework.foundation.internals.Utils;
import com.datagre.framework.foundation.spi.Logger;
import com.datagre.framework.foundation.spi.LoggerManager;
import com.datagre.framework.foundation.spi.provider.Provider;
import com.datagre.framework.foundation.spi.provider.ServerProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class DefaultServerProvider extends AbstractProvider implements ServerProvider {
   private static final Logger logger = LoggerManager.getLogger();
   private static final String SERVER_PROPERTIES_LINUX = "/opt/settings/server.properties";
   private static final String SERVER_PROPERTIES_WINDOWS = "C:/opt/settings/server.properties";
   static final String DEFAULT_CLUSTER_NAME = "default";

   // 10.9.0.0/16 is for both prod and QA, and should not be included.
   private String[] CTRIP_PROD_IP_PREFIX = {"10.8.", "10.15.", "10.28."};

   private String m_env;
   private String m_subEnv;
   private String m_dc;
   private EnvFamily m_envFamily;
   private String m_clusterName;

   private Properties m_serverProperties = new Properties();

   @Override
   public void initialize() {
      try {
         File file = new File(SERVER_PROPERTIES_LINUX);
         if (file.exists() && file.canRead()) {
            logger.log("Loading " + file.getAbsolutePath());
            FileInputStream fis = new FileInputStream(file);
            initialize(fis);
            return;
         }
         logger.log(SERVER_PROPERTIES_LINUX + " does not exist or is not readable.");

         file = new File(SERVER_PROPERTIES_WINDOWS);
         if (file.exists() && file.canRead()) {
            logger.log("Loading " + file.getAbsolutePath());
            FileInputStream fis = new FileInputStream(file);
            initialize(fis);
            return;
         }
         logger.log(SERVER_PROPERTIES_WINDOWS + " does not exist or is not readable.");
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
               m_serverProperties.load(new InputStreamReader(in, StandardCharsets.UTF_8));
            } finally {
               in.close();
            }
         }

         initEnvType();
         initSubEnv();
         initDataCenter();
         initEnvFamily();
         initClusterName();
      } catch (Exception ex) {
         logger.log(ex);
      }
   }

   @Override
   public String getDataCenter() {
      return m_dc;
   }

   @Override
   public boolean isDataCenterSet() {
      return m_dc != null;
   }

   @Override
   public boolean isTooling() {
      return getBooleanProperty("tooling", false);
   }

   @Override
   public boolean isPci() {
      return getBooleanProperty("pci", false);
   }

   @Override
   public EnvFamily getEnvFamily() {
      return m_envFamily;
   }

   @Override
   public String getClusterName() {
      return m_clusterName;
   }

   @Override
   public String getEnvType() {
      return m_env;
   }

   @Override
   public boolean isEnvTypeSet() {
      return m_env != null;
   }

   @Override
   public String getSubEnv() {
      return m_subEnv;
   }

   @Override
   public boolean isSubEnvSet() {
      return m_subEnv != null;
   }

   @Override
   public String getProperty(String name, String defaultValue) {
      if ("env".equalsIgnoreCase(name)) {
         String val = getEnvType();
         return val == null ? defaultValue : val;
      } else if ("subenv".equalsIgnoreCase(name)) {
         String val = getSubEnv();
         return val == null ? defaultValue : val;
      } else if ("dc".equalsIgnoreCase(name)) {
         String val = getDataCenter();
         return val == null ? defaultValue : val;
      } else {
         String val = m_serverProperties.getProperty(name, defaultValue);
         return val == null ? defaultValue : val.trim();
      }
   }

   @Override
   public Class<? extends Provider> getType() {
      return ServerProvider.class;
   }

   private void initEnvType() {
      // 1. Try to get environment from JVM system property
      m_env = System.getProperty("env");
      if (!Utils.isBlank(m_env)) {
         m_env = m_env.trim();
         logger.log("Environment is set to [" + m_env + "] by JVM system property 'env'.");
         return;
      } else {
         logger.log("JVM system property 'env' is blank. Will try to get environment by OS environment variable 'ENV'.");
      }

      // 2. Try to get environment from OS environment variable
      m_env = System.getenv("ENV");
      if (!Utils.isBlank(m_env)) {
         m_env = m_env.trim();
         logger.log("Environment is set to [" + m_env + "] by OS env variable 'ENV'.");
         return;
      } else {
         logger.log("OS environment variable 'ENV' is blank. Will try to get environment by property 'env' from the properties InputStream.");
      }

      // 3. Try to get environment from file "/opt/settings/server.properties"
      m_env = m_serverProperties.getProperty("env");
      if (Utils.isBlank(m_env)) {
         m_env = m_serverProperties.getProperty("environment", null);
      }

      if (!Utils.isBlank(m_env)) {
         m_env = m_env.trim();
         logger.log("Environment is set to [" + m_env + "] by property 'env' in server.properties.");
         return;
      } else {
         logger.log("Property 'env' is not available from  server.properties. Will try to get environment by IP zone identification.");
      }

      // 4. If IP belongs to one of the prod network zones, set environment to prod
      m_env = identifyProdByIP();
      if (!Utils.isBlank(m_env)) {
         logger.log("Environment is automatically set to [" + m_env + "] by IP zone identification. IP: " + NetworkInterfaceManager.INSTANCE.getLocalHostAddress());
         return;
      } else {
         logger.log("IP " + NetworkInterfaceManager.INSTANCE.getLocalHostAddress() + " does not belong to one of the prod network zones. Will set environment to null.");
      }

      // 5. Set environment to null.
      if (Utils.isBlank(m_env)) {
         m_env = null;
         logger.log("Environment is set to null. " +
            "Because it is not available in either (1) JVM system property 'env', (2) OS env variable 'ENV' nor (3) property 'env' from the properties InputStream.");
      }
   }

   private void initSubEnv() {
      //1. Try to get sub env from server.properties
      m_subEnv = m_serverProperties.getProperty("subenv", null);
      if (!Utils.isBlank(m_subEnv)) {
         m_subEnv = m_subEnv.trim();
         logger.log("Sub env is set to [" + m_subEnv + "] by property 'subenv' in  server.properties.");
         return;
      }

      //2. Set sub env to null.
      if (Utils.isBlank(m_subEnv)) {
         m_subEnv = null;
         logger.log("Sub env is set to null. " +
            "Because it is not available in property 'subenv' from the properties InputStream.");
      }
   }

   private void initDataCenter() {
      // idc in server.properties
      m_dc = m_serverProperties.getProperty("idc");
      if (!Utils.isBlank(m_dc)) {
         logger.log("Data Center is set to [" + m_dc + "] by property 'idc' in server.properties.");
         return;
      } else {
         logger.log("Data Center is not available from server.properties.");
      }

      // Linux
      m_dc = System.getenv("ci_located_code");
      if (!Utils.isBlank(m_dc)) {
         logger.log("Data Center is set to [" + m_dc + "] by OS environment variable ci_located_code.");
         return;
      } else {
         logger.log("Data Center is not available by OS environment variable ci_located_code.");
      }

      // Windows
      m_dc = System.getenv("CI_located_code");
      if (!Utils.isBlank(m_dc)) {
         logger.log("Data Center is set to [" + m_dc + "] by OS environment variable CI_located_code.");
      } else {
         m_dc = null;
         logger.log("Data Center is not available by OS environment variable CI_located_code. It is set to null.");
      }
   }

   private void initEnvFamily() {
      m_envFamily = EnvFamily.getByName(m_env, null);
   }

   private void initClusterName() {
      // 1. Set to data center(PRO environment) or sub env(Others)
      if (m_envFamily == EnvFamily.PRO) {
         m_clusterName = m_dc; //no case conversion
      } else if (!Utils.isBlank(m_subEnv)){
         m_clusterName = m_subEnv.toLowerCase(); //lower case
      }

      // 2. Set to environment
      if (Utils.isBlank(m_clusterName) && !Utils.isBlank(m_env)) {
         m_clusterName = m_env.toLowerCase(); //lower case
      }

      // 3. Set to default
      if (Utils.isBlank(m_clusterName)) {
         m_clusterName = DEFAULT_CLUSTER_NAME;
      }
   }

   private String identifyProdByIP() {
      String ip = NetworkInterfaceManager.INSTANCE.getLocalHostAddress();
      for (String prefix : CTRIP_PROD_IP_PREFIX) {
         if (ip.startsWith(prefix)) {
            System.out.println("IP [" + ip + "] matches with prod network zone [" + prefix + "]. " +
               "Environment is set to PRO.");
            return "PRO";
         }
      }
      return null;
   }

   @Override
   public String toString() {
      return "environment [" + getEnvType() + "] data center [" + getDataCenter() + "] properties: " + m_serverProperties + " (DefaultServerProvider)";
   }
}
