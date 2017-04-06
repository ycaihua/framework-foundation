package com.datagre.framework.foundation.spi;

import com.datagre.framework.foundation.spi.provider.Provider;

public interface ProviderManager {
   public String getProperty(String name, String defaultValue);

   public <T extends Provider> T provider(Class<T> clazz);
}
