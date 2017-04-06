package com.datagre.framework.foundation;

import com.datagre.framework.foundation.internals.NullProviderManager;
import com.datagre.framework.foundation.internals.ServiceBootstrap;
import com.datagre.framework.foundation.spi.Logger;
import com.datagre.framework.foundation.spi.LoggerManager;
import com.datagre.framework.foundation.spi.ProviderManager;
import com.datagre.framework.foundation.spi.provider.*;

public abstract class Foundation {
   private static final Logger logger = LoggerManager.getLogger();
   private static Object lock = new Object();

   private static ProviderManager s_manager;

   // yj.huang Encourage early initialization and fail early if it happens.
   static {
      getManager();
   }

   private static ProviderManager getManager() {
      try {
         if (s_manager == null) {
            // yj.huang Double locking to make sure only one thread initializes ProviderManager.
            synchronized (lock) {
               if (s_manager == null) {
                  s_manager = ServiceBootstrap.loadFirst(ProviderManager.class);
               }
            }
         }

         return s_manager;
      } catch (Throwable ex) {
         s_manager = new NullProviderManager();
         logger.log(ex);
         return s_manager;
      }
   }

   public static String getProperty(String name, String defaultValue) {
      try {
         return getManager().getProperty(name, defaultValue);
      } catch (Exception ex) {
         logger.log(ex);
         return defaultValue;
      }
   }

   public static NetworkProvider net() {
      try {
         return getManager().provider(NetworkProvider.class);
      } catch (Exception ex) {
         logger.log(ex);
         return NullProviderManager.provider;
      }
   }

   public static WebContainerProvider web() {
      try {
         return getManager().provider(WebContainerProvider.class);
      } catch (Exception ex) {
         logger.log(ex);
         return NullProviderManager.provider;
      }
   }

   public static ServerProvider server() {
      try {
         return getManager().provider(ServerProvider.class);
      } catch (Exception ex) {
         logger.log(ex);
         return NullProviderManager.provider;
      }
   }

   public static ApplicationProvider app() {
      try {
         return getManager().provider(ApplicationProvider.class);
      } catch (Exception ex) {
         logger.log(ex);
         return NullProviderManager.provider;
      }
   }

   public static DeploymentProvider deploy() {
      try {
         return getManager().provider(DeploymentProvider.class);
      } catch (Exception ex) {
         logger.log(ex);
         return NullProviderManager.provider;
      }
   }

   public static BuildProvider build() {
      try {
         return getManager().provider(BuildProvider.class);
      } catch (Exception ex) {
         logger.log(ex);
         return NullProviderManager.provider;
      }
   }

   public static StatusProvider status() {
      try {
         return getManager().provider(StatusProvider.class);
      } catch (Exception ex) {
         logger.log(ex);
         return NullProviderManager.provider;
      }
   }

   public static String asString() {
      try {
         return getManager().toString();
      } catch (Exception ex) {
         logger.log(ex);
         return NullProviderManager.provider.toString();
      }
   }
}
