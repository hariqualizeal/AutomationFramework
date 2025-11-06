import { readFileSync, writeFileSync, mkdirSync, existsSync } from "fs";
import { resolve, join, isAbsolute } from "path";

type Platform = "android" | "ios";

export type GenerateOptions = {
  promptPath: string;
  platform: Platform;

  // Output roots (project-relative); defaults match Option A
  javaRoot?: string;   // default: "src/test/java"
  resRoot?: string;    // default: "src/test/resources"

  // Optional overrides (CLI flags). If absent, we infer from prompt.
  pageClass?: string;
  stepsClass?: string;
  featureName?: string;
  packagePages?: string; // default: "pages"
  packageSteps?: string; // default: "cucumber.stepdefinitions"
};

/** Ensure dir exists like `mkdir -p`. */
function ensureDir(dir: string): void {
  if (!existsSync(dir)) mkdirSync(dir, { recursive: true });
}

/** Resolve prompt path robustly when running from /mcp (Maven workingDirectory). */
function resolvePromptPath(input: string): string {
  if (/^[A-Za-z]:[\\/]/.test(input) || isAbsolute(input)) return input; // absolute
  const cand1 = resolve(process.cwd(), input);            if (existsSync(cand1)) return cand1;
  const cand2 = resolve(process.cwd(), "..", input);      if (existsSync(cand2)) return cand2;
  return cand2; // best-guess to project root
}

/** Minimal front-matter parser:
 *  ---
 *  pageClass: MobileWikiHomePage
 *  stepsClass: WikiHomePageStepDefinitions
 *  featureName: WikiHomeButtons
 *  packagePages: pages
 *  packageSteps: cucumber.stepdefinitions
 *  ---
 */
function parseFrontMatter(text: string): Partial<GenerateOptions> {
  const fmMatch = text.match(/^---\s*([\s\S]*?)\s*---\s*/);
  if (!fmMatch) return {};
  const body = fmMatch[1];
  const out: Partial<GenerateOptions> = {};
  for (const line of body.split(/\r?\n/)) {
    const m = line.match(/^\s*([A-Za-z][A-Za-z0-9_]*)\s*:\s*(.+?)\s*$/);
    if (m) {
      const k = m[1] as keyof GenerateOptions;
      const v = m[2].trim();
      (out as any)[k] = v;
    }
  }
  return out;
}

/** Fallback inference from Gherkin:
 *  - featureName from first "Feature: XXX"
 */
function inferFromFeature(text: string): Partial<GenerateOptions> {
  const m = text.match(/^\s*Feature:\s*(.+)\s*$/m);
  return m ? { featureName: sanitizeName(m[1], "Feature") } : {};
}

function sanitizeName(s: string, fallback: string): string {
  const core = s.trim().replace(/[^\w]+/g, " ").trim();
  return core ? core.replace(/\s+/, " ") : fallback;
}

export function generate(opts: GenerateOptions) {
  // Defaults (Option A)
  const projectRoot = resolve(process.cwd(), "..");
  const javaRoot = opts.javaRoot ?? "src/test/java";
  const resRoot  = opts.resRoot  ?? "src/test/resources";
  const pkgPages = opts.packagePages ?? "pages";
  const pkgSteps = opts.packageSteps ?? "cucumber.stepdefinitions";

  // 1) Load prompt
  const promptAbs = resolvePromptPath(opts.promptPath);
  console.log("📂 Reading prompt:", promptAbs);
  const prompt = readFileSync(promptAbs, "utf8");

  // 2) Dynamic naming: front-matter > CLI flags > inference > safe defaults
  const fm = parseFrontMatter(prompt);
  const inferred = inferFromFeature(prompt);

  const pageClass   = (opts.pageClass   ?? (fm.pageClass   as string) ?? "MobileWikiHomePage").replace(/[^\w]/g, "");
  const stepsClass  = (opts.stepsClass  ?? (fm.stepsClass  as string) ?? "WikiHomePageStepDefinitions").replace(/[^\w]/g, "");
  const featureName = (opts.featureName ?? (fm.featureName as string) ?? (inferred.featureName || "WikiHomeButtons")).replace(/[^\w]/g, "");

  // 3) Compute output dirs under the project root (NOT in mcp/)
  const pageDir    = resolve(projectRoot, javaRoot, ...pkgPages.split("."));
  const stepsDir   = resolve(projectRoot, javaRoot, ...pkgSteps.split("."));
  const featureDir = resolve(projectRoot, resRoot, "features");
  [pageDir, stepsDir, featureDir].forEach(ensureDir);

  // 4) Filenames
  const pageFile    = join(pageDir, `${pageClass}.java`);
  const stepsFile   = join(stepsDir, `${stepsClass}.java`);
  const featureFile = join(featureDir, `${featureName}.feature`);

  // 5) Templates (By locators; no PageFactory)
  const pageContent = `package ${pkgPages};

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ${pageClass} {
    private final WebDriver driver;

    // TODO: swap to real ${opts.platform.toUpperCase()} locators (id/xpath/accessibility) after inspection
    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By signInButton  = By.id("signIn");

    public ${pageClass}(WebDriver driver) {
        this.driver = driver;
    }

    public void enterUsername(String username) {
        driver.findElement(usernameField).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickSignIn() {
        driver.findElement(signInButton).click();
    }
}
`;

  const stepsContent = `package ${pkgSteps};

import io.cucumber.java.en.*;
import ${pkgPages}.${pageClass};
import org.openqa.selenium.WebDriver;

public class ${stepsClass} {

    private WebDriver driver; // TODO: wire your ThreadLocal/Appium driver
    private ${pageClass} homePage;

    @Given("User launches mobile app")
    public void user_launches_mobile_app() {
        // driver = ThreadLocalDriver.getDriver(); // e.g., your existing utility
        homePage = new ${pageClass}(driver);
    }

    @When("User enters username and password")
    public void user_enters_username_and_password() {
        homePage.enterUsername("abc@gmail.com");
        homePage.enterPassword("password@123");
    }

    @And("User clicks on signin")
    public void user_clicks_on_signin() {
        homePage.clickSignIn();
    }

    @Then("User verifies whether login is successful")
    public void user_verifies_login_success() {
        // TODO: Assert on a post-login element
    }
}
`;

  const featureContent = `Feature: Verify user login

  Scenario: Successful login
    Given User launches mobile app
    When User enters username and password
    And User clicks on signin
    Then User verifies whether login is successful
`;

  // 6) Write files
  writeFileSync(pageFile, pageContent, "utf8");
  writeFileSync(stepsFile, stepsContent, "utf8");
  writeFileSync(featureFile, featureContent, "utf8");

  console.log("✅ Generated (project-root):");
  console.log(" - " + pageFile);
  console.log(" - " + stepsFile);
  console.log(" - " + featureFile);

  return {
    status: "success",
    platform: opts.platform,
    promptPreview: prompt.slice(0, 160),
    files: { pageFile, stepsFile, featureFile }
  };
}
