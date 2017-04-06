package com.datagre.framework.foundation.internals.logger;

import com.datagre.framework.foundation.spi.Logger;

public class DefaultLogger implements Logger {
   @Override
   public void log(Throwable ex) {
      ex.printStackTrace();
   }

   @Override
   public void log(String message, String... args) {
      System.out.println(String.format("[framework-foundation] " + message, args));
   }
}
