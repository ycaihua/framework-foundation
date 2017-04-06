package com.datagre.framework.foundation.internals;

import com.datagre.framework.foundation.internals.logger.DefaultLogger;
import com.datagre.framework.foundation.spi.Logger;
import com.datagre.framework.foundation.spi.LoggerManager;

public class DefaultLoggerManager extends LoggerManager {
   private static final Logger logger = new DefaultLogger();

   @Override
   protected Logger logger() {
      return logger;
   }
}
