#!/usr/bin/env node
import { generate, type GenerateOptions } from "./generator.js";

function getArg(key: string, fallback?: string): string | undefined {
  const i = process.argv.indexOf(`--${key}`);
  return i !== -1 && process.argv[i + 1] ? process.argv[i + 1] : fallback;
}

const promptPath = getArg("prompt", "../src/test/resources/utilities/mcp/login.prompt.md")!;
const platformArg = (getArg("platform", "android") || "android").toLowerCase();
const platform = (platformArg === "ios" ? "ios" : "android") as "android" | "ios";

// Optionally override names/packages from CLI
const pageClass    = getArg("pageClass");
const stepsClass   = getArg("stepsClass");
const featureName  = getArg("featureName");
const packagePages = getArg("packagePages");
const packageSteps = getArg("packageSteps");

// Optional output roots (rarely needed—defaults are Option A)
const javaRoot = getArg("javaRoot", "src/test/java");
const resRoot  = getArg("resRoot",  "src/test/resources");

const opts: GenerateOptions = {
  promptPath,
  platform,
  javaRoot,
  resRoot,
  pageClass,
  stepsClass,
  featureName,
  packagePages,
  packageSteps
};

const result = generate(opts);

console.log("Generated:");
console.log(`  Page:    ${result.files.pageFile}`);
console.log(`  Steps:   ${result.files.stepsFile}`);
console.log(`  Feature: ${result.files.featureFile}`);
console.log(`\nStatus: ${result.status} | Platform: ${result.platform}`);
