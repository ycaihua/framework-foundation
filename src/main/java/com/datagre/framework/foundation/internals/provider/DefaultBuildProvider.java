package com.datagre.framework.foundation.internals.provider;

import com.datagre.framework.foundation.internals.Utils;
import com.datagre.framework.foundation.spi.Logger;
import com.datagre.framework.foundation.spi.LoggerManager;
import com.datagre.framework.foundation.spi.provider.BuildProvider;
import com.datagre.framework.foundation.spi.provider.Provider;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Properties;

public class DefaultBuildProvider extends AbstractProvider implements BuildProvider {
   private static final Logger logger = LoggerManager.getLogger();
   private static final String BUILD_PROPERTIES_CLASSPATH = "/META-INF/build.properties";
   public static final String BUILD_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
   private Properties m_buildProperties = new Properties();

   private String m_appId;

   @Override
   public void initialize() {
      try {
         InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(BUILD_PROPERTIES_CLASSPATH);
         if (in == null) {
            in = DefaultBuildProvider.class.getResourceAsStream(BUILD_PROPERTIES_CLASSPATH);
         }

         if (in == null){
            logger.log("build.properties not found from classpath: " + BUILD_PROPERTIES_CLASSPATH + ".");
            return;
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
               m_buildProperties.load(new InputStreamReader(in, StandardCharsets.UTF_8));
            } finally {
               in.close();
            }
         }

         initAppId();
      } catch (Exception ex) {
         logger.log(ex);
      }
   }

   private void initAppId() {
      m_appId = m_buildProperties.getProperty("app.id");

      //fallback to app_id
      if (Utils.isBlank(m_appId)) {
         m_appId = m_buildProperties.getProperty("app_id");
      }
   }

   @Override
   public Class<? extends Provider> getType() {
      return BuildProvider.class;
   }

   @Override
   public String getProperty(String name, String defaultValue) {
      return m_buildProperties.getProperty(name, defaultValue);
   }

   @Override
   public String getAppId() {
      return m_appId;
   }

   @Override
   public String getBuildId() {
      return getProperty("build.id", null);
   }

   @Override
   public Date getBuildTime() {
      return getDateProperty("build.time", DefaultBuildProvider.BUILD_TIME_FORMAT, null);
   }
}
