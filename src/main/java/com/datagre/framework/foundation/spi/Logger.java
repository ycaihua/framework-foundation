package com.datagre.framework.foundation.spi;

public interface Logger {
   public void log(Throwable ex);

   public void log(String message, String... args);
}
