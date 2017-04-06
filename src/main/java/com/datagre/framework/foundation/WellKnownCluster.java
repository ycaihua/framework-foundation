package com.datagre.framework.foundation;

/**
 * Define the well known clusters here for reference
 */
public enum WellKnownCluster {
   FWS("fws", EnvFamily.FAT, "Web Service for Feature Acceptance Test(FAT), RESERVED for framework middleware etc."),

   LPT("lpt", EnvFamily.FAT, "for Load Performance Test purpose"),

   DEV("dev", EnvFamily.FAT, "for Development integration purpose"),

   SHAOY("SHAOY", EnvFamily.PRO, "Shanghai OuYang IDC"),

   SHAJQ("SHAJQ", EnvFamily.PRO, "Shanghai JinQiao IDC"),

   SHAFQ("SHAFQ", EnvFamily.PRO, "Shanghai FuQuan IDC"),

   ;

   private String m_name;
   private EnvFamily m_family;
   private String m_description;

   WellKnownCluster(String name, EnvFamily family, String description) {
      m_name = name;
      m_family = family;
      m_description = description;
   }

   public boolean isFWS() {
      return this == FWS;
   }

   public boolean isLPT() {
      return this == LPT;
   }

   public boolean isDEV() {
      return this == DEV;
   }

   public boolean isSHAOY() {
      return this == SHAOY;
   }

   public boolean isSHAJQ() {
      return this == SHAJQ;
   }

   public boolean isSHAFQ() {
      return this == SHAFQ;
   }

   public String getName() {
      return m_name;
   }

   public EnvFamily getFamily() {
      return m_family;
   }

   public String getDescription() {
      return m_description;
   }

   public static WellKnownCluster getByName(String name, WellKnownCluster defaultValue) {
      if (name != null) {
         name = name.trim();
         for (WellKnownCluster value : values()) {
            if (value.name().equalsIgnoreCase(name)) {
               return value;
            }
         }
      }

      return defaultValue;
   }
}
