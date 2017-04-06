package com.datagre.framework.foundation.internals.logger;

import com.datagre.framework.foundation.spi.Logger;

public class NullLogger implements Logger {
   @Override
   public void log(Throwable ex) {

   }

   @Override
   public void log(String message, String... args) {

   }
}
