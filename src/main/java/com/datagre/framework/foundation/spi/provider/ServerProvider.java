package com.datagre.framework.foundation.spi.provider;

import com.datagre.framework.foundation.EnvFamily;

import java.io.IOException;
import java.io.InputStream;

/**
 * Provider for server related properties
 */
public interface ServerProvider extends Provider {
   /**
    * @return current environment or {@code null} if not set
    */
   public String getEnvType();

   /**
    * @return whether current environment is set or not
    */
   public boolean isEnvTypeSet();

   /**
    * @return current sub environment or {@code null} if not set
    */
   public String getSubEnv();

   /**
    * @return whether current sub environment is set or not
    */
   public boolean isSubEnvSet();

   /**
    * @return current data center or {@code null} if not set
    */
   public String getDataCenter();

   /**
    * @return whether data center is set or not
    */
   public boolean isDataCenterSet();

   /**
    * @return whether current environment is tooling environment
    */
   public boolean isTooling();

   /**
    * @return whether current environment is pci environment
    */
   public boolean isPci();

   /**
    * EnvFamily is an abstraction of environments.<br/>
    * For example: FAT, FWS, LPT and DEV will be categorized as one EnvFamily: EnvFamily.FAT.
    *
    * @return the EnvFamily abstraction of current environment
    */
   public EnvFamily getEnvFamily();

   /**
    * As with the EnvFamily, each EnvFamily could have different clusters.<br/>
    * For example: env LPT is considered as a cluster of EnvFamily.FAT: lpt
    *
    * @return the cluster name, should be lowercase except for those well known idc names(SHAOY, SHAJQ, SHAFQ)<br/>
    * Typical return values are:
    * <ul>
    * <li>fat01(fat sub envs are in the format: fatxxx)</li>
    * <li>lpt01(lpt sub envs are in the format: lptxxx)</li>
    * <li>fat</li>
    * <li>lpt</li>
    * <li>fws</li>
    * <li>dev</li>
    * <li>uat</li>
    * <li>pro</li>
    * <li>SHAOY</li>
    * <li>SHAJQ</li>
    * <li>SHAFQ</li>
    * </ul>
    */
   public String getClusterName();

   /**
    * Initialize server provider with the specified input stream
    *
    * @throws IOException
    */
   public void initialize(InputStream in) throws IOException;
}
