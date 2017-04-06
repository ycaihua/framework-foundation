package com.datagre.framework.foundation.internals.provider;

import com.datagre.framework.foundation.internals.Utils;
import com.datagre.framework.foundation.spi.provider.Provider;
import com.datagre.framework.foundation.spi.provider.StatusProvider;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class DefaultStatusProvider extends AbstractProvider implements StatusProvider {
   private static final String SERVER_STATUS_LINUX = "/opt/status/server.status";
   private static final String WEBAPP_STATUS_LINUX = "/opt/status/webapp.status";
   private static final int STATUS_RELOAD_INTERVAL_IN_SECONDS = 1;
   public static final String ON = "on";
   public static final String OFF = "off";

   private File m_serverStatusFile;
   private volatile long m_serverStatusFileLastModified = -1;
   private volatile String m_serverStatus;

   private File m_webAppStatusFile;
   private volatile long m_webAppStatusFileLastModified = -1;
   private volatile String m_webAppStatus;

   private ScheduledExecutorService m_reloadStatusService;

   @Override
   public Class<? extends Provider> getType() {
      return StatusProvider.class;
   }

   @Override
   public String getProperty(String name, String defaultValue) {
      if ("server.status".equalsIgnoreCase(name)) {
         String val = getServerStatus();
         return val == null ? defaultValue : val;
      } else if ("webapp.status".equalsIgnoreCase(name)) {
         String val = getWebAppStatus();
         return val == null ? defaultValue : val;
      }
      return defaultValue;
   }

   @Override
   public void initialize() {
      initialize(SERVER_STATUS_LINUX, WEBAPP_STATUS_LINUX, STATUS_RELOAD_INTERVAL_IN_SECONDS, TimeUnit.SECONDS);
   }

   void initialize(String serverStatusPath, String webAppStatusPath, long reloadInterval, TimeUnit timeUnit) {
      //For Windows, always return on
      if (Utils.isOSWindows()) {
         m_serverStatus = ON;
         m_webAppStatus = ON;
         return;
      }

      m_serverStatusFile = new File(serverStatusPath);
      m_webAppStatusFile = new File(webAppStatusPath);
      reloadStatus();//eager load
      m_reloadStatusService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
         @Override
         public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, "DefaultStatusProvider");
            thread.setDaemon(true);
            return thread;
         }
      });
      m_reloadStatusService.scheduleWithFixedDelay(new Runnable() {
         @Override
         public void run() {
            reloadStatus();
         }
      }, reloadInterval, reloadInterval, timeUnit);
   }

   private void reloadStatus() {
      reloadServerStatus();
      reloadWebAppStatus();
   }

   private void reloadServerStatus() {
      try {
         //check if exists
         if (m_serverStatusFile.exists() && m_serverStatusFile.canRead()) {
            long lastModified = m_serverStatusFile.lastModified();
            //not modified
            if (lastModified == m_serverStatusFileLastModified) {
               return;
            }

            m_serverStatusFileLastModified = lastModified;
            String result = readFirstNonEmptyLine(m_serverStatusFile);
            if (!Utils.isBlank(result)) {
               m_serverStatus = result.trim().toLowerCase();
               return;
            }
         }
      } catch (Throwable ex) {
         //will retry later
      }
      m_serverStatusFileLastModified = -1;
      m_serverStatus = OFF; //default value is off
   }

   private void reloadWebAppStatus() {
      try {
         //check if exists
         if (m_webAppStatusFile.exists() && m_webAppStatusFile.canRead()) {
            //not modified
            if (m_webAppStatusFile.lastModified() == m_webAppStatusFileLastModified) {
               return;
            }

            String result = readFirstNonEmptyLine(m_webAppStatusFile);
            m_webAppStatusFileLastModified = m_webAppStatusFile.lastModified();
            if (!Utils.isBlank(result)) {
               m_webAppStatus = result.trim().toLowerCase();
               return;
            }
         }
      } catch (Throwable ex) {
         //will retry later
      }
      m_webAppStatusFileLastModified = -1;
      m_webAppStatus = OFF; //default value is off
   }

   private String readFirstNonEmptyLine(File file) throws IOException {
      List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
      String result = null;
      for (String line : lines) {
         if (!Utils.isBlank(line)) {
            result = line;
            break;
         }
      }
      return result;
   }

   /**
    * Retrieve the server status from /opt/status/server.status.
    * If this file doesn't exist or has no content, return off.
    * For Windows environment, always return on.
    *
    * @return the latest server status
    */
   @Override
   public String getServerStatus() {
      return m_serverStatus;
   }

   /**
    * Retrieve the deploy status from /opt/status/webapp.status.
    * If this file doesn't exist or has no content, return off.
    * For Windows environment, always return on.
    *
    * @return the latest server status
    */
   @Override
   public String getWebAppStatus() {
      return m_webAppStatus;
   }
}
