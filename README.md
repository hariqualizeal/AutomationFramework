# MCP based Automation Testing Framework

A robust **Selenium + Appium + MCP integrated Test Automation Framework** for Web and Mobile applications, supporting execution on **Local Devices, Cloud (BrowserStack)**, and integration with **Jenkins, Jira, and Zephyr**.

---

## ✅ Features
- 🌐 **Web Automation** (Local & Cloud)
- 📱 **Mobile Automation (Android/iOS)** (Local & Cloud) using Appium
- ** Integrated with MCP(Model Context Protocol) server for Page classes, Step Definitions and Feature files creation
- ⚡ **Parallel execution**
- 🧪 **Cucumber BDD**
- ☁️ **Cloud Execution** support (BrowserStack)
- ✅ **Integrations:**
  - **Jenkins** (CI/CD)
  - **Jira & Zephyr Squad Cloud** (Test Management)
- 📊 **Reporting Support** (Extent Reports / Cucumber HTML)

---

## 📁 Project Structure (Example)
```
src
└── test
    ├── java
    │   └── cucumber
    │       └── stepdefinitions
    │       └── testRunners
    │   └── pages
    │   └── utilities
    └── resources
        └── features
        └── testngxml
pom.xml
README.md
```

---

## 💻 Prerequisites
| Tool | Version |
|------|---------|
| Java | 17 (OpenJDK) |
| Maven | 3.6+ |
| Appium | 3.1.0 |
| Android Studio | Latest |
| IntelliJ IDEA | Recommended |
| Git | Latest |

---

## ▶ Running Tests Locally

### ✅ Start Appium Server
```bash
appium --address 127.0.0.1 --port 4723
```

### ✅ Execute Tests
| Command | Description                                          |
|---------|------------------------------------------------------|
| `mvn clean test -PSequentialLocalWeb` | Run web tests locally on browser                     |
| `mvn clean test -PSequentialLocalAndroid` | Run mobile tests locally on connected Android device |

### ✅ Update Cucumber Test Runner
- Modify tags in `src/test/java/cucumber/testRunners/*Runner.java`
- Update thread count in TestNG XML (`testng/testng*.xml`)

---

## ☁ Running Tests on BrowserStack

### ✅ BrowserStack Execution Flow:
1. Upload your app using REST API or Web UI  
2. Add BrowserStack credentials in `config.properties` *(Do not commit credentials!)*  
3. Run using:
```bash
mvn clean test -PSequentialCloudWeb
```

---

## ⚙ Maven Commands
| Description | Command |
|-------------|---------|
| Run with profile | `mvn clean test -P<profile>` |

---

## ⚙ Environment Setup

### 🖥 System Variables (Windows)
| Variable | Value |
|----------|-------|
| ANDROID_HOME | `C:\Users\<user>\AppData\Local\Android\Sdk` |

---

## 🚀 Jenkins CI/CD Setup
- #### Step1: installing maven in Jenkins Agent
  - MAVEN_HOME with C:\Program Files\apache-maven-3.6.1-bin\apache-maven-3.6.1
  - M2_HOME with C:\Program Files\apache-maven-3.6.1-bin\apache-maven-3.6.1
  - JAVA_HOME with C:\Program Files\Java\jdk-17.0.2
  - Path with C:\Program Files\Java\jdk-17.0.2\bin
  - Path with C:\Program Files\apache-maven-3.6.1-bin\apache-maven-3.6.1\bin
- #### Step2: installing maven plugins in Jenkins
  - Go to "Manage Jenkins" -> "Manage Plugins" -> click "Available" tab -> enter "maven" in search box.
  - In search results select Maven Integration plugin and click on Download Now And Install After Restart button
- #### Step3: Maven and JDK configuration in Global Tool Configuration in Jenkins
  - #### If Step1 is done Step3 is not needed and vice versa
  - Navigate to Global Tool Configuration
  - Scroll down and click on Add Maven
  - Provide name as something like Maven3, Select install automaticall checkbox, select version in the Install From Apache dropdown and Save
- #### Step4: Creating jenkins job using pipeline

---

## 🔌 ADB Commands
| Command                                                            | Description |
|--------------------------------------------------------------------|-------------|
| `adb devices`                                                      | List connected devices |
| `adb -s <device_id> install C:\Users\user\Downloads\<appname>.apk` | Install APK on device |
| `adb uninstall <package>`                                          | Uninstall app |
- With Android Studio ADB will be installed automatically.
- To enable developer options: About Phone->Software Information-> Tap Build Number multiple times
- Navigate to Developer options and turn on USB debugging
- Navigate to C:\Users\user\AppData\Local\Android\Sdk\platform-tools in command prompt and type ``adb``

---

## 🐙 Git & GitHub Setup

### Generate SSH Key
```bash
ssh-keygen -t rsa
```

### Push Project
```bash
git add .
git commit -m "commit message"
git push origin main
```

---

## 🛠 Jira & Zephyr Integration
- ZephyrSquadCloud Rest API link1: https://support.smartbear.com/zephyr-squad-cloud/docs/api/index.html
- ZephyrSquadCloud Rest API link2 (interactive api): https://zephyrsquad.docs.apiary.io/#reference/execution/update-execution/update-execution
- ZephyrSquadCloud Access key and Zephyr Secret key:
  - To generate ZephyrSquadCloud Access key and Secret key refer https://support.smartbear.com/zephyr-squad-cloud/docs/api/api-keys.html
- Jira Account ID:
  - Its is available in the URL after clicking on the profile link in jira.
- Jira basic authorization token: follow below steps to generate basic authorization token for jira:
  - Step1: Get Jira API key from Jira account settings
  - Step2: Generate base64 encoded string of "JiraEmailID:JiraApiKey", append Basic keyword and use it as Authorization header.
    - To generate base64 encoded string: www.base64encode.org
    - Example: "Authorization: Basic base64-encoded-string"
    - For detailed info follow https://developer.atlassian.com/cloud/jira/platform/basic-auth-for-rest-apis/
- Link to download Jar for interacting with Jira and ZephyrSquadCloud APIs: https://support.smartbear.com/zephyr-squad-cloud/docs/api/jwt-token.html
---

## ✅ Best Practices
✔ Do not push credentials to GitHub  
✔ Use `.gitignore` to exclude logs, apk, target/, credentials  
✔ Maintain separate `config.properties` per environment  
✔ Use Maven profiles for `dev`, `qa`, `androidLocal`, `browserstack`  

---

Feel free to contribute or raise issues!

---