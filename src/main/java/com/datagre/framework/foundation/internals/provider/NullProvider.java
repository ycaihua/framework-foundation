package com.datagre.framework.foundation.internals.provider;

import com.datagre.framework.foundation.EnvFamily;
import com.datagre.framework.foundation.spi.provider.*;

import javax.servlet.ServletContext;
import java.io.InputStream;
import java.util.Date;

public class NullProvider extends AbstractProvider implements ApplicationProvider, NetworkProvider, ServerProvider, WebContainerProvider, DeploymentProvider, BuildProvider, StatusProvider {
   @Override
   public Class<? extends Provider> getType() {
      return null;
   }

   @Override
   public void initialize(ServletContext context) {

   }

   @Override
   public String getProperty(String name, String defaultValue) {
      return defaultValue;
   }

   @Override
   public void initialize() {

   }

   @Override
   public String getAppId() {
      return null;
   }

   @Override
   public String getBuildId() {
      return null;
   }

   @Override
   public Date getBuildTime() {
      return null;
   }

   @Override
   public String getJdkVersion() {
      return null;
   }

   @Override
   public boolean isAppIdSet() {
      return false;
   }

   @Override
   public boolean isAppIdConflicted() {
      return false;
   }

   @Override
   public String getEnvType() {
      return null;
   }

   @Override
   public boolean isEnvTypeSet() {
      return false;
   }

   @Override
   public String getSubEnv() {
      return null;
   }

   @Override
   public boolean isSubEnvSet() {
      return false;
   }

   @Override
   public String getDataCenter() {
      return null;
   }

   @Override
   public boolean isDataCenterSet() {
      return false;
   }

   @Override
   public boolean isTooling() {
      return false;
   }

   @Override
   public boolean isPci() {
      return false;
   }

   @Override
   public EnvFamily getEnvFamily() {
      return null;
   }

   @Override
   public String getClusterName() {
      return null;
   }

   @Override
   public void initialize(InputStream in) {

   }

   @Override
   public String getHostAddress() {
      return null;
   }

   @Override
   public String getHostName() {
      return null;
   }

   @Override
   public String getContextPath() {
      return null;
   }

   @Override
   public int getHttpPort() {
      return DefaultWebContainerProvider.DEFAULT_HTTP_PORT;
   }

   @Override
   public boolean isHttpPortSet() {
      return false;
   }

   @Override
   public String getServerStatus() {
      return null;
   }

   @Override
   public String getWebAppStatus() {
      return null;
   }

   @Override
   public String toString() {
      return "(NullProvider)";
   }
}
