---
pageClass: MobileWikiHomePage2
stepsClass: WikiHomePageStepDefinitions2
featureName: WikiHomeButtons2
packagePages: pages
packageSteps: cucumber.stepdefinitions
---

# Intent
Generate files for a native mobile login flow. Use By locators (no PageFactory).
Target both Android and iOS apps.

# Output files (names fixed)
- pages/MobileWikiHomePage.java
- cucumber/stepdefinitions/WikiHomePageStepDefinitions.java
- features/WikiHomeButtons.feature

# Steps to model
1. User launches mobile app
2. User enters username and password
3. User clicks on signin
4. User verifies whether login is successful

# Locators (logical ids you can refine later)
username_input
password_input
signin_button
// login success banner text is "Login successful"
