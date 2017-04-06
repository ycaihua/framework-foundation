package com.datagre.framework.foundation.spi.provider;

import javax.servlet.ServletContext;

/**
 * Provider for web container related properties
 */
public interface WebContainerProvider extends Provider {
   /**
    * @return the context path
    */
   public String getContextPath();

   /**
    * @return the http port used
    */
   public int getHttpPort();

   /**
    * @return whether the http port is set or not
    */
   public boolean isHttpPortSet();

   /**
    * Initialize with the specified servlet context
    */
   public void initialize(ServletContext context);
}
