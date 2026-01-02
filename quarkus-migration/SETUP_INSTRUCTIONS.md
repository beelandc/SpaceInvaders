# Quarkus Space Invaders - Environment Setup Instructions

## Prerequisites

Before we begin developing the Quarkus Space Invaders application, you need to install the following dependencies:

### 1. Java Development Kit (JDK)
**Required Version:** Java 17 or higher (Java 21 LTS recommended)

**Check if installed:**
```bash
java -version
```

**Installation Options:**

**macOS (using Homebrew):**
```bash
# Install OpenJDK 21
brew install openjdk@21

# Link it to your system
sudo ln -sfn /opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-21.jdk

# Verify installation
java -version
```

**Alternative - Using SDKMAN (Recommended for managing multiple Java versions):**
```bash
# Install SDKMAN
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# Install Java 21
sdk install java 21.0.1-tem

# Verify
java -version
```

---

### 2. Maven
**Required Version:** Maven 3.8.1 or higher

**Check if installed:**
```bash
mvn -version
```

**Installation (macOS using Homebrew):**
```bash
brew install maven

# Verify
mvn -version
```

**Alternative - Using SDKMAN:**
```bash
sdk install maven
```

---

### 3. Quarkus CLI (Optional but Recommended)
The Quarkus CLI makes project creation and development easier.

**Installation (macOS using Homebrew):**
```bash
brew install quarkusio/tap/quarkus

# Verify
quarkus --version
```

**Alternative - Using SDKMAN:**
```bash
sdk install quarkus
```

---

### 4. IDE Setup (Recommended)
Choose one of the following:

**Option A: IntelliJ IDEA (Recommended)**
- Download from: https://www.jetbrains.com/idea/download/
- Community Edition is sufficient
- Install Quarkus plugin: Preferences → Plugins → Search "Quarkus" → Install

**Option B: Visual Studio Code**
```bash
# Install VS Code
brew install --cask visual-studio-code

# Install extensions (run after opening VS Code)
code --install-extension redhat.java
code --install-extension vscjava.vscode-java-pack
code --install-extension redhat.vscode-quarkus
```

---

### 5. Native Build Tools (Optional - for GraalVM Native Image)
Only needed if you want to compile to native executable later.

**GraalVM Installation:**
```bash
# Using SDKMAN
sdk install java 21.0.1-graalce

# Verify
java -version
```

---

## Verification Checklist

Please run these commands and confirm each one works:

### ✅ Step 1: Verify Java
```bash
java -version
```
**Expected output:** Should show Java 17 or higher (21 recommended)

### ✅ Step 2: Verify Maven
```bash
mvn -version
```
**Expected output:** Should show Maven 3.8.1 or higher

### ✅ Step 3: Verify Quarkus CLI (Optional)
```bash
quarkus --version
```
**Expected output:** Should show Quarkus CLI version

### ✅ Step 4: Test Maven Repository Access
```bash
mvn help:evaluate -Dexpression=settings.localRepository -q -DforceStdout
```
**Expected output:** Should show your local Maven repository path (usually ~/.m2/repository)

---

## Next Steps

Once you have confirmed all prerequisites are installed:

1. Reply with "✅ All prerequisites installed" 
2. I will then create the Quarkus project structure
3. We'll set up the initial LWJGL dependencies
4. Begin implementing the game engine foundation

---

## Troubleshooting

### Issue: Java version conflicts
If you have multiple Java versions:
```bash
# List all Java versions (macOS)
/usr/libexec/java_home -V

# Set JAVA_HOME temporarily
export JAVA_HOME=$(/usr/libexec/java_home -v 21)

# Or add to ~/.zshrc or ~/.bash_profile for permanent
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 21)' >> ~/.zshrc
```

### Issue: Maven not found
```bash
# Check if Maven is in PATH
echo $PATH | grep maven

# If not, add to ~/.zshrc or ~/.bash_profile
export PATH="/opt/homebrew/bin:$PATH"
```

### Issue: Permission denied
```bash
# Fix Maven permissions
sudo chown -R $(whoami) ~/.m2
```

---

## Additional Resources

- Quarkus Getting Started: https://quarkus.io/guides/getting-started
- LWJGL Documentation: https://www.lwjgl.org/guide
- Maven Documentation: https://maven.apache.org/guides/
