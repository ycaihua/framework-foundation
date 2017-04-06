package com.datagre.framework.foundation.internals.provider;

import com.datagre.framework.foundation.internals.parser.Parsers;
import com.datagre.framework.foundation.spi.provider.Provider;

import java.util.Date;
import java.util.Locale;

public abstract class AbstractProvider implements Provider {
   @Override
   public int getIntProperty(String name, int defaultValue) {
      try {
         String value = getProperty(name, null);

         if (value != null) {
            return Integer.parseInt(value);
         }
      } catch (Throwable e) {
         // ignore it
      }

      return defaultValue;
   }

   @Override
   public long getLongProperty(String name, long defaultValue) {
      try {
         String value = getProperty(name, null);

         if (value != null) {
            return Long.parseLong(value);
         }
      } catch (Throwable e) {
         // ignore it
      }

      return defaultValue;
   }

   @Override
   public short getShortProperty(String name, short defaultValue) {
      try {
         String value = getProperty(name, null);

         if (value != null) {
            return Short.parseShort(value);
         }
      } catch (Throwable e) {
         // ignore it
      }

      return defaultValue;
   }

   @Override
   public float getFloatProperty(String name, float defaultValue) {
      try {
         String value = getProperty(name, null);

         if (value != null) {
            return Float.parseFloat(value);
         }
      } catch (Throwable e) {
         // ignore it
      }

      return defaultValue;
   }

   @Override
   public double getDoubleProperty(String name, double defaultValue) {
      try {
         String value = getProperty(name, null);

         if (value != null) {
            return Double.parseDouble(value);
         }
      } catch (Throwable e) {
         // ignore it
      }

      return defaultValue;
   }

   @Override
   public byte getByteProperty(String name, byte defaultValue) {
      try {
         String value = getProperty(name, null);

         if (value != null) {
            return Byte.parseByte(value);
         }
      } catch (Throwable e) {
         // ignore it
      }

      return defaultValue;
   }

   @Override
   public boolean getBooleanProperty(String name, boolean defaultValue) {
      try {
         String value = getProperty(name, null);

         if (value != null) {
            return Boolean.parseBoolean(value);
         }
      } catch (Throwable e) {
         // ignore it
      }

      return defaultValue;
   }

   @Override
   public <T extends Enum<T>> T getEnumProperty(String name, Class<T> enumType, T defaultValue) {
      try {
         String value = getProperty(name, null);

         if (value != null) {
            return Enum.valueOf(enumType, value);
         }
      } catch (Throwable e) {
         // ignore it
      }

      return defaultValue;
   }

   @Override
   public Date getDateProperty(String name, Date defaultValue) {
      try {
         String value = getProperty(name, null);

         if (value != null) {
            return Parsers.forDate().parse(value);
         }
      } catch (Throwable e) {
         // ignore it
      }

      return defaultValue;
   }

   @Override
   public Date getDateProperty(String name, String format, Date defaultValue) {
      try {
         String value = getProperty(name, null);

         if (value != null) {
            return Parsers.forDate().parse(value, format);
         }
      } catch (Throwable e) {
         // ignore it
      }

      return defaultValue;
   }

   @Override
   public Date getDateProperty(String name, String format, Locale locale, Date defaultValue) {
      try {
         String value = getProperty(name, null);

         if (value != null) {
            return Parsers.forDate().parse(value, format, locale);
         }
      } catch (Throwable e) {
         // ignore it
      }

      return defaultValue;
   }

   @Override
   public long getDurationProperty(String name, long defaultValue) {
      try {
         String value = getProperty(name, null);

         if (value != null) {
            return Parsers.forDuration().parseToMillis(value);
         }
      } catch (Throwable e) {
         // ignore it
      }

      return defaultValue;
   }
}
