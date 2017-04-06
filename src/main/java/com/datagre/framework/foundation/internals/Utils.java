package com.datagre.framework.foundation.internals;

import com.datagre.framework.foundation.spi.LoggerManager;

public class Utils {
   /**
    * @deprecated Use LoggerManager.getLogger().log(Throwable ex) instead
    */
   @Deprecated
   public static void log(Throwable ex) {
      LoggerManager.getLogger().log(ex);
   }

   /**
    * @deprecated Use LoggerManager.getLogger().log(String message, String... args) instead
    */
   @Deprecated
   public static void log(String message, String... args) {
      LoggerManager.getLogger().log(message, args);
   }

   public static boolean isBlank(String str) {
      if (str == null || str.length() == 0) {
         return true;
      }

      int length = str.length();
      for (int i = 0; i < length; i++) {
         char ch = str.charAt(i);

         if (!Character.isWhitespace(ch)) {
            return false;
         }
      }
      return true;
   }

   public static boolean isOSWindows() {
      String osName = System.getProperty("os.name");
      if (Utils.isBlank(osName)) {
         return false;
      }
      return osName.startsWith("Windows");
   }
}
