package com.datagre.framework.foundation.internals;

import com.datagre.framework.foundation.internals.logger.NullLogger;
import com.datagre.framework.foundation.spi.Logger;
import com.datagre.framework.foundation.spi.LoggerManager;

public class NullLoggerManager extends LoggerManager {
   public static final Logger logger = new NullLogger();

   @Override
   protected Logger logger() {
      return logger;
   }
}
