package utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

  // volatile ensures visibility across threads
  private static volatile Properties prop;

  // Private constructor prevents instantiation
  private ConfigReader() {}

  public static Properties getConfig() {
    if (prop == null) {  // First check (no locking)
      synchronized (ConfigReader.class) {
        if (prop == null) {  // Second check (with lock)
          loadConfig();
        }
      }
    }
    return prop;
  }

  private static void loadConfig() {
    prop = new Properties();
    try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
      if (input == null) {
        throw new IOException("config.properties not found in classpath");
      }
      prop.load(input);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load configuration file", e);
    }
  }

  public static String get(String key) {
    return getConfig().getProperty(key);
  }
}
