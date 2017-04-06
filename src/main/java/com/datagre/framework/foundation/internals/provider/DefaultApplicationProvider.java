package com.datagre.framework.foundation.internals.provider;

import com.datagre.framework.foundation.internals.Utils;
import com.datagre.framework.foundation.spi.Logger;
import com.datagre.framework.foundation.spi.LoggerManager;
import com.datagre.framework.foundation.spi.provider.ApplicationProvider;
import com.datagre.framework.foundation.spi.provider.BuildProvider;
import com.datagre.framework.foundation.spi.provider.Provider;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class DefaultApplicationProvider extends AbstractProvider implements ApplicationProvider {
   private static final Logger logger = LoggerManager.getLogger();
   public static final String APP_PROPERTIES_CLASSPATH = "/META-INF/app.properties";
   private Properties m_appProperties = new Properties();
   private BuildProvider m_buildProvider;

   private String m_appId;
   private boolean m_appIdConflict = false;

   public DefaultApplicationProvider(BuildProvider buildProvider) {
      m_buildProvider = buildProvider;
   }

   @Override
   public void initialize() {
      try {
         InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(APP_PROPERTIES_CLASSPATH);
         if (in == null) {
            in = DefaultApplicationProvider.class.getResourceAsStream(APP_PROPERTIES_CLASSPATH);
         }

         if (in == null) {
            logger.log("Cannot create InputStream by classpath: " + APP_PROPERTIES_CLASSPATH + ".");
         }
         initialize(in);
      } catch (Exception ex) {
         logger.log(ex);
      }
   }

   @Override
   public void initialize(InputStream in) {
      try {
         if (in != null) {
            try {
               m_appProperties.load(new InputStreamReader(in, StandardCharsets.UTF_8));
            } finally {
               in.close();
            }
         }

         initAppId();
      } catch (Exception ex) {
         logger.log(ex);
      }
   }

   @Override
   public String getAppId() {
      return m_appId;
   }

   @Override
   public String getJdkVersion() {
      String jdkVersion = getProperty("jdk.version", null);
      if (jdkVersion == null) {
         jdkVersion = getProperty("jdkVersion", null);
      }
      return jdkVersion;
   }

   @Override
   public boolean isAppIdSet() {
      return !Utils.isBlank(m_appId);
   }

   @Override
   public boolean isAppIdConflicted() {
      return m_appIdConflict;
   }

   @Override
   public String getProperty(String name, String defaultValue) {
      if ("app.id".equals(name)) {
         String val = getAppId();
         return val == null ? defaultValue : val;
      } else {
         String val = m_appProperties.getProperty(name, defaultValue);
         return val == null ? defaultValue : val;
      }
   }

   @Override
   public Class<? extends Provider> getType() {
      return ApplicationProvider.class;
   }

   private void initAppId() {
      // 1. Try to get app id from app.properties.
      m_appId = m_appProperties.getProperty("app.id");

      // 2. Try to get app id from build.properties
      String appIdFromBuildProperties = m_buildProvider.getAppId();

      if (!Utils.isBlank(m_appId)) {
         m_appId = m_appId.trim();

         logger.log("App ID is set to " + m_appId + " by app.id property in app.properties InputStream.");

         if (!Utils.isBlank(appIdFromBuildProperties)) {
            appIdFromBuildProperties = appIdFromBuildProperties.trim();
            if (!m_appId.equals(appIdFromBuildProperties)) {
               m_appIdConflict = true;
               logger.log(String.format("Found inconsistent App Id from build.properties(%s) v.s. from app.properties(%s)!", appIdFromBuildProperties, m_appId));
            }
         }
      } else if (!Utils.isBlank(appIdFromBuildProperties)) {
         m_appId = appIdFromBuildProperties.trim();
         logger.log("App ID is set to " + m_appId + " by app.id property in build.properties InputStream.");
      } else {
         logger.log("app.id is not available from properties InputStream."
            + " It is set to null");
         m_appId = null;
      }
   }

   @Override
   public String toString() {
      return "appId [" + getAppId() + "] properties: " + m_appProperties + " (DefaultApplicationProvider)";
   }
}
