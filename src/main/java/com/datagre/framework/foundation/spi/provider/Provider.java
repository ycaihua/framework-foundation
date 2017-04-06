package com.datagre.framework.foundation.spi.provider;

import java.util.Date;
import java.util.Locale;

public interface Provider {
   /**
    * @return the current provider's type
    */
   public Class<? extends Provider> getType();

   /**
    * Return the property value with the given name, or {@code defaultValue} if the name doesn't exist.
    *
    * @param name         the property name
    * @param defaultValue the default value when name is not found or any error occurred
    * @return the property value
    */
   public String getProperty(String name, String defaultValue);

   /**
    * Return the int property value with the given name, or {@code defaultValue} if the name doesn't exist.
    *
    * @param name         the property name
    * @param defaultValue the default value when name is not found or any error occurred
    * @return the property value
    */
   public int getIntProperty(String name, int defaultValue);

   /**
    * Return the long property value with the given name, or {@code defaultValue} if the name doesn't exist.
    *
    * @param name         the property name
    * @param defaultValue the default value when name is not found or any error occurred
    * @return the property value
    */
   public long getLongProperty(String name, long defaultValue);

   /**
    * Return the short property value with the given name, or {@code defaultValue} if the name doesn't exist.
    *
    * @param name         the property name
    * @param defaultValue the default value when name is not found or any error occurred
    * @return the property value
    */
   public short getShortProperty(String name, short defaultValue);

   /**
    * Return the float property value with the given name, or {@code defaultValue} if the name doesn't exist.
    *
    * @param name         the property name
    * @param defaultValue the default value when name is not found or any error occurred
    * @return the property value
    */
   public float getFloatProperty(String name, float defaultValue);

   /**
    * Return the double property value with the given name, or {@code defaultValue} if the name doesn't exist.
    *
    * @param name         the property name
    * @param defaultValue the default value when name is not found or any error occurred
    * @return the property value
    */
   public double getDoubleProperty(String name, double defaultValue);

   /**
    * Return the byte property value with the given name, or {@code defaultValue} if the name doesn't exist.
    *
    * @param name         the property name
    * @param defaultValue the default value when name is not found or any error occurred
    * @return the property value
    */
   public byte getByteProperty(String name, byte defaultValue);

   /**
    * Return the boolean property value with the given name, or {@code defaultValue} if the name doesn't exist.
    *
    * @param name         the property name
    * @param defaultValue the default value when name is not found or any error occurred
    * @return the property value
    */
   public boolean getBooleanProperty(String name, boolean defaultValue);

   /**
    * Return the Date property value with the given name, or {@code defaultValue} if the name doesn't exist.
    * Will try to parse the date with Locale.US and formats as follows: yyyy-MM-dd HH:mm:ss.SSS, yyyy-MM-dd HH:mm:ss and yyyy-MM-dd
    *
    * @param name         the property name
    * @param defaultValue the default value when name is not found or any error occurred
    * @return the property value
    */
   public Date getDateProperty(String name, Date defaultValue);

   /**
    * Return the Date property value with the given name, or {@code defaultValue} if the name doesn't exist.
    * Will parse the date with the format specified and Locale.US
    *
    * @param name         the property name
    * @param format       the date format, see {@link java.text.SimpleDateFormat} for more information
    * @param defaultValue the default value when name is not found or any error occurred
    * @return the property value
    */
   public Date getDateProperty(String name, String format, Date defaultValue);

   /**
    * Return the Date property value with the given name, or {@code defaultValue} if the name doesn't exist.
    *
    * @param name         the property name
    * @param format       the date format, see {@link java.text.SimpleDateFormat} for more information
    * @param locale       the locale to use
    * @param defaultValue the default value when name is not found or any error occurred
    * @return the property value
    */
   public Date getDateProperty(String name, String format, Locale locale, Date defaultValue);

   /**
    * Return the Enum property value with the given name, or {@code defaultValue} if the name doesn't exist.
    *
    * @param name         the property name
    * @param enumType     the enum class
    * @param defaultValue the default value when name is not found or any error occurred
    * @param <T>          the enum
    * @return the property value
    */
   public <T extends Enum<T>> T getEnumProperty(String name, Class<T> enumType, T defaultValue);

   /**
    * Return the duration property value(in milliseconds) with the given name, or {@code defaultValue} if the name doesn't exist.
    * Please note the format should comply with the follow example (case insensitive).
    * Examples:
    * <pre>
    *    "123MS"          -- parses as "123 milliseconds"
    *    "20S"            -- parses as "20 seconds"
    *    "15M"            -- parses as "15 minutes" (where a minute is 60 seconds)
    *    "10H"            -- parses as "10 hours" (where an hour is 3600 seconds)
    *    "2D"             -- parses as "2 days" (where a day is 24 hours or 86400 seconds)
    *    "2D3H4M5S123MS"  -- parses as "2 days, 3 hours, 4 minutes, 5 seconds and 123 milliseconds"
    * </pre>
    *
    * @param name         the property name
    * @param defaultValue the default value when name is not found or any error occurred
    * @return the parsed property value(in milliseconds)
    */
   public long getDurationProperty(String name, long defaultValue);

   /**
    * Initialize the provider
    */
   public void initialize();
}
