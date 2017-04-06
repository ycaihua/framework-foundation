package com.datagre.framework.foundation.spi;

import com.datagre.framework.foundation.internals.NullLoggerManager;
import com.datagre.framework.foundation.internals.ServiceBootstrap;

public abstract class LoggerManager {
   private static Object lock = new Object();
   private static LoggerManager s_manager;

   static {
      getManager();
   }

   private static LoggerManager getManager() {
      try {
         if (s_manager == null) {
            synchronized (lock) {
               if (s_manager == null) {
                  s_manager = ServiceBootstrap.loadFirst(LoggerManager.class);
               }
            }
         }

         return s_manager;
      } catch (Throwable ex) {
         s_manager = new NullLoggerManager();
         ex.printStackTrace();
         return s_manager;
      }
   }

   protected abstract Logger logger();

   public static Logger getLogger() {
      try {
         return getManager().logger();
      } catch (Throwable ex) {
         ex.printStackTrace();
         return NullLoggerManager.logger;
      }
   }
}
