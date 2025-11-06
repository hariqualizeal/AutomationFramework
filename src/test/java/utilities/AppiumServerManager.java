package utilities;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.io.File;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class AppiumServerManager {

    private static AppiumDriverLocalService service;
    private static int port = 4723; // default; will auto-pick if busy

    public static void start() {
        if (service != null && service.isRunning()) return;

        port = 4723;

        File node = resolveNodeExecutable();
        File appiumJS = resolveAppiumMainJs();

        if (node == null || appiumJS == null) {
            throw new RuntimeException("Could not resolve Node or Appium main.js. " +
                    "Set system properties -Dnode.path and -Dappium.js if auto-detect fails.");
        }

        Path logDir = Path.of("logs");
        try { Files.createDirectories(logDir); } catch (Exception ignored) {}

        String ts = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());
        File logFile = logDir.resolve("appium-" + ts + ".log").toFile();

        service = new AppiumServiceBuilder()
                .usingDriverExecutable(node)
                .withAppiumJS(appiumJS)
                .withIPAddress("127.0.0.1")
                .usingPort(port)
                // Common/handy flags
                .withArgument(GeneralServerFlag.BASEPATH, "/")              // Appium 2 default
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)           // replace old sessions
                .withArgument(GeneralServerFlag.LOG_LEVEL, "warn")
                .withTimeout(Duration.ofSeconds(90))
//                .withStartUpTimeOut(Duration.ofSeconds(90))// trace|debug|info|warn|error
                .withLogFile(logFile)
                .build();

        service.start();

        if (!service.isRunning()) {
            throw new RuntimeException("Failed to start Appium on port " + port);
        }
        System.out.println("[Appium] started at: " + service.getUrl());
    }

    public static void stop() {
        if (service != null) {
            service.stop();
            System.out.println("[Appium] stopped.");
        }
    }

    public static String getUrl() {
        if (service == null || !service.isRunning()) {
            throw new IllegalStateException("Appium is not running.");
        }
        return service.getUrl().toString();
    }

    // ---------- helpers ----------

    private static int getFreePortOr(int fallback) {
        try (ServerSocket s = new ServerSocket(0)) { return s.getLocalPort(); }
        catch (Exception e) { return fallback; }
    }

    /**
     * Auto-detect Node path. You can override with:
     *   -Dnode.path=C:/Program Files/nodejs/node.exe (Windows)
     *   -Dnode.path=/usr/local/bin/node (mac/linux)
     */
    private static File resolveNodeExecutable() {
        // user override
        String override = System.getProperty("node.path");
        if (override != null && !override.isBlank()) return new File(override);

        String os = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        if (os.contains("win")) {
            // Typical Windows install
            File node = new File("C:/Program Files/nodejs/node.exe");
            if (node.exists()) return node;
            // NVM or custom install? Try PATH fallback (not perfect, but helps)
            String path = System.getenv("PATH");
            if (path != null) {
                for (String p : path.split(";")) {
                    File cand = new File(p, "node.exe");
                    if (cand.exists()) return cand;
                }
            }
        } else {
            // mac/linux common locations
            String[] candidates = {
                    "/usr/local/bin/node",
                    "/opt/homebrew/bin/node",    // Apple Silicon Homebrew
                    "/usr/bin/node"
            };
            for (String c : candidates) if (new File(c).exists()) return new File(c);
        }
        return null;
    }

    /**
     * Auto-detect Appium main.js. You can override with:
     *   -Dappium.js=C:/Users/you/AppData/Roaming/npm/node_modules/appium/build/lib/main.js
     *   -Dappium.js=/usr/local/lib/node_modules/appium/build/lib/main.js
     */
    private static File resolveAppiumMainJs() {
        // user override
        String override = System.getProperty("appium.js");
        if (override != null && !override.isBlank()) return new File(override);

        String os = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        if (os.contains("win")) {
            String appData = System.getenv("APPDATA"); // e.g., C:\Users\you\AppData\Roaming
            if (appData != null) {
                File js = Path.of(appData, "npm", "node_modules", "appium", "build", "lib", "main.js").toFile();
                if (js.exists()) return js;
            }
        } else {
            // mac/linux common global npm locations
            String[] candidates = {
                    "/usr/local/lib/node_modules/appium/build/lib/main.js",
                    "/opt/homebrew/lib/node_modules/appium/build/lib/main.js",
                    "/usr/lib/node_modules/appium/build/lib/main.js"
            };
            for (String c : candidates) if (new File(c).exists()) return new File(c);
        }
        return null;
    }
}