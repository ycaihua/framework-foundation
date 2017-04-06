package com.datagre.framework.foundation.spi.provider;

import java.io.InputStream;

/**
 * Provider for application related properties
 */
public interface ApplicationProvider extends Provider {
   /**
    * @return the application's app id
    */
   public String getAppId();

   /**
    * @return the application's runtime requirement for jdk version
    */
   public String getJdkVersion();

   /**
    * @return whether the application's app id is set or not
    */
   public boolean isAppIdSet();

   /**
    * @return whether the application's app id is conflicted with the one provided by build system
    */
   public boolean isAppIdConflicted();

   /**
    * Initialize the application provider with the specified input stream
    */
   public void initialize(InputStream in);
}
