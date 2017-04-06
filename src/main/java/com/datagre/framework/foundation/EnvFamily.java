package com.datagre.framework.foundation;

/**
 * EnvFamily is an abstraction of environments.<br/>
 * For example: FAT, FWS, LPT and DEV will be categorized as one EnvFamily: EnvFamily.FAT.
 */
public enum EnvFamily {
   // Assume you are working at the beach with no network access
   LOCAL("Local Development environment"),

   FAT("Feature Acceptance Test environment"),

   UAT("User Acceptance Test environment"),

   PRO("Production environment");

   private final String m_description;

   EnvFamily(String description) {
      m_description = description;
   }

   public boolean isLocal() {
      return this == LOCAL;
   }

   public boolean isFAT() {
      return this == FAT;
   }

   public boolean isUAT() {
      return this == UAT;
   }

   public boolean isPRO() {
      return this == PRO;
   }

   public String getName() {
      return name();
   }

   public String getDescription() {
      return m_description;
   }

   public static EnvFamily getByName(String name, EnvFamily defaultValue) {
      if (name != null) {
         name = name.trim();
         for (EnvFamily value : values()) {
            if (value.name().equalsIgnoreCase(name)) {
               return value;
            }
         }
         //FAT mappings
         if (name.equalsIgnoreCase("LPT") ||
            name.equalsIgnoreCase("FWS") ||
            name.equalsIgnoreCase("DEV")) {
            return EnvFamily.FAT;
         }
      }

      return defaultValue;
   }
}
