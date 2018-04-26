package com.configlinker.tests.loaders;

import com.configlinker.ConfigSet;
import com.configlinker.ConfigSetFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;


class LoadersTest
{
  @Test
  void test_ClasspathLoader()
  {
    Set<Class<?>> configClasses = new HashSet<>();
    configClasses.add(LoadFromClasspath.class);
    ConfigSet configs = ConfigSetFactory.create(configClasses);
  
    LoadFromClasspath loadFromClasspath = configs.getConfigObject(LoadFromClasspath.class);
    Assertions.assertEquals("classpath_config.properties", loadFromClasspath.getConfigName());
  }

  @Test
  void test_PropertyFileLoader()
  {
    Set<Class<?>> configClasses = new HashSet<>();
    configClasses.add(LoadFromFile.class);
    ConfigSet configs = ConfigSetFactory.create(configClasses);
  
    LoadFromFile loadFromFile = configs.getConfigObject(LoadFromFile.class);
    Assertions.assertEquals("workdir_config.properties", loadFromFile.getConfigName());
  }
  
  @Test
  void test_HttpLoader()
  {
    Set<Class<?>> configClasses = new HashSet<>();
    configClasses.add(LoadFromHttp.class);
    ConfigSet configs = ConfigSetFactory.create(configClasses);
  
    LoadFromHttp loadFromHttp = configs.getConfigObject(LoadFromHttp.class);
    Assertions.assertEquals("http_config.properties", loadFromHttp.getConfigName());
  }

  @Disabled
  @Test
  void test_ConfigLinkerLoader()
  {
    Assertions.fail("Not implemented.");
  }
}
