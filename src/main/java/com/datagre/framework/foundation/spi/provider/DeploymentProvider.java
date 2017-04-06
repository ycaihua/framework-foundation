package com.datagre.framework.foundation.spi.provider;

import java.io.IOException;
import java.io.InputStream;

/**
 * Provider for deployment related properties
 */
public interface DeploymentProvider extends Provider {
   /**
    * Initialize the deployment provider with the specified input stream
    */
   public void initialize(InputStream in) throws IOException;
}
