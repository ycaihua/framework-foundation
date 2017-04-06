package com.datagre.framework.foundation.spi.provider;

/**
 * Provider for status related properties
 */
public interface StatusProvider extends Provider{
   /**
    * Retrieve the server status, possible values are: on, off, etc
    */
   public String getServerStatus();

   /**
    * Retrieve the web app status, possible values are: on, off, etc
    */
   public String getWebAppStatus();
}
