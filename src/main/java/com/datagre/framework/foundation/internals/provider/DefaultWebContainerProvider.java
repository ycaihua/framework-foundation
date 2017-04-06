package com.datagre.framework.foundation.internals.provider;

import com.datagre.framework.foundation.internals.Utils;
import com.datagre.framework.foundation.spi.Logger;
import com.datagre.framework.foundation.spi.LoggerManager;
import com.datagre.framework.foundation.spi.provider.Provider;
import com.datagre.framework.foundation.spi.provider.WebContainerProvider;

import javax.servlet.ServletContext;

public class DefaultWebContainerProvider extends AbstractProvider implements WebContainerProvider {
   private static final Logger logger = LoggerManager.getLogger();
   private static final String HTTP_PORT_PROPERTY = "port.http.server";
   private static final String HTTP_PORT_ENV = "SERVER_HTTP_PORT";
   public static final int DEFAULT_HTTP_PORT = 0;
   private boolean m_hasServletContext = false;
   private String m_contextPath;
   private int m_httpPort = DEFAULT_HTTP_PORT;
   private boolean isHttpPortSet = false;

   @Override
   public void initialize() {
      try {
         initHttpPort();
      } catch (Exception ex) {
         logger.log(ex);
      }
   }

   @Override
   public synchronized void initialize(ServletContext context) {
      if (context == null) {
         throw new IllegalArgumentException("ServletContext passed to DefaultWebContainerProvider is null, invalid.");
      }

      m_contextPath = context.getContextPath();
      m_hasServletContext = true;
   }

   @Override
   public int getHttpPort() {
      return m_httpPort;
   }

   @Override
   public boolean isHttpPortSet() {
      return isHttpPortSet;
   }

   @Override
   public String getContextPath() {
      if (!m_hasServletContext) {
         throw new IllegalStateException("ServletContext is not available. Please call Foundation.web().initialize(servletContext) first to pass me a ServletContext before calling getContextPath().");
      }
      return m_contextPath;
   }

   @Override
   public String getProperty(String name, String defaultValue) {
      if ("context.path".equalsIgnoreCase(name)) {
         String val = getContextPath();
         return val == null ? defaultValue : val;
      } else if (HTTP_PORT_PROPERTY.equalsIgnoreCase(name)) {
         String val = String.valueOf(getHttpPort());
         return val == null ? defaultValue : val;
      } else {
         return defaultValue;
      }
   }

   @Override
   public Class<? extends Provider> getType() {
      return WebContainerProvider.class;
   }

   private void initHttpPort() {
      // 1. Try to get http port from JVM system property "port.http.server", set by TARS.
      String httpPortStr = System.getProperty(HTTP_PORT_PROPERTY);
      if (!Utils.isBlank(httpPortStr)) {
         m_httpPort = parseIntegerQuietly(httpPortStr.trim(), DEFAULT_HTTP_PORT);
         logger.log("http port is set to [" + m_httpPort + "] by JVM system property '" + HTTP_PORT_PROPERTY + "' raw value (" + httpPortStr + ").");
         isHttpPortSet = true;
         return;
      } else {
         logger.log("http port is not available from JVM system property '" + HTTP_PORT_PROPERTY);
      }

      httpPortStr = System.getenv(HTTP_PORT_ENV);
      if (!Utils.isBlank(httpPortStr)) {
         m_httpPort = parseIntegerQuietly(httpPortStr.trim(), DEFAULT_HTTP_PORT);
         logger.log("http port is set to [" + m_httpPort + "] by OS environment variable '" + HTTP_PORT_ENV + "'. raw value (" + httpPortStr + ").");
         isHttpPortSet = true;
         return;
      } else {
         logger.log("http port is not available from OS environment variable '" + HTTP_PORT_ENV + ". Default http port [" + DEFAULT_HTTP_PORT + "] is used.");
         m_httpPort = DEFAULT_HTTP_PORT;
      }
   }

   private int parseIntegerQuietly(String str, int defaultValue) {
      try {
         return Integer.valueOf(str);
      } catch (NumberFormatException nfe) {
         logger.log("NumberFormatException happens when converting [" + str + "] to int. Default value [" + defaultValue + "] will be used instead.");
         logger.log(nfe);
         return defaultValue;
      }
   }

   @Override
   public String toString() {
      return "context path [" + getContextPath() + "] http port [" + getHttpPort() + "] (DefaultWebContainerProvider)";
   }
}
