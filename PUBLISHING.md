# Publishing to Maven Central Repository

This guide explains how to publish the Sud GIP Auth Java SDK to Maven Central Repository.

## Prerequisites

### 1. Sonatype OSSRH Account

1. Create a Sonatype JIRA account at https://issues.sonatype.org/
2. Create a new project ticket to request access to publish under `com.sudtech` groupId
3. Wait for approval from Sonatype team

### 2. GPG Key Setup

1. Install GPG:
   ```bash
   # macOS
   brew install gnupg
   
   # Ubuntu/Debian
   sudo apt-get install gnupg
   ```

2. Generate a GPG key pair:
   ```bash
   gpg --gen-key
   ```
   
3. List your keys to get the key ID:
   ```bash
   gpg --list-secret-keys --keyid-format LONG
   ```
   
4. Export and upload your public key to key servers:
   ```bash
   gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID
   gpg --keyserver keys.openpgp.org --send-keys YOUR_KEY_ID
   ```

### 3. Maven Configuration

1. Copy `settings.xml.template` to `~/.m2/settings.xml`:
   ```bash
   cp settings.xml.template ~/.m2/settings.xml
   ```

2. Edit `~/.m2/settings.xml` and replace the placeholders:
   - `YOUR_SONATYPE_USERNAME`: Your Sonatype JIRA username
   - `YOUR_SONATYPE_PASSWORD`: Your Sonatype JIRA password
   - `YOUR_GPG_KEY_ID`: Your GPG key ID (8-character short form)
   - `YOUR_GPG_PASSPHRASE`: Your GPG key passphrase

## Publishing Process

### Option 1: Using the Deployment Script (Recommended)

1. Run the deployment script:
   ```bash
   ./deploy.sh
   ```

2. The script will:
   - Perform pre-deployment checks
   - Clean and compile the project
   - Run all tests
   - Package with sources and javadoc
   - Sign artifacts with GPG
   - Deploy to OSSRH staging repository

### Option 2: Manual Deployment

1. Clean and test:
   ```bash
   mvn clean test
   ```

2. Deploy to staging repository:
   ```bash
   mvn clean deploy -P release
   ```

## Post-Deployment Steps

1. **Check Staging Repository**:
   - Go to https://s01.oss.sonatype.org/
   - Login with your Sonatype credentials
   - Navigate to "Staging Repositories"
   - Find your staging repository (usually named like `comsudtech-XXXX`)

2. **Release Process**:
   - If `autoReleaseAfterClose` is enabled in pom.xml, the release will happen automatically
   - If not, you need to manually "Close" and then "Release" the staging repository
   - The release process can take 10-30 minutes

3. **Verify Publication**:
   - Check Maven Central: https://search.maven.org/
   - Search for `com.sudtech:sud-gip-auth-java`
   - It may take 1-2 hours for the artifact to appear in search results

## Version Management

### Release Versions

For release versions (e.g., `1.0.0`, `1.1.0`):

1. Update version in `pom.xml`:
   ```xml
   <version>1.0.0</version>
   ```

2. Create and push a git tag:
   ```bash
   git tag -a v1.0.0 -m "Release version 1.0.0"
   git push origin v1.0.0
   ```

3. Deploy using the release profile:
   ```bash
   mvn clean deploy -P release
   ```

### Snapshot Versions

For development versions (e.g., `1.1.0-SNAPSHOT`):

1. Update version in `pom.xml`:
   ```xml
   <version>1.1.0-SNAPSHOT</version>
   ```

2. Deploy without the release profile:
   ```bash
   mvn clean deploy
   ```

## Troubleshooting

### Common Issues

1. **GPG Signing Fails**:
   - Ensure GPG is installed and configured
   - Check that your GPG key is not expired
   - Verify the key ID and passphrase in settings.xml

2. **Authentication Fails**:
   - Verify Sonatype credentials in settings.xml
   - Ensure your JIRA account has permission for the groupId

3. **Staging Repository Not Found**:
   - Check if the deployment completed successfully
   - Look for error messages in Maven output
   - Verify the nexusUrl in pom.xml

4. **Release Fails**:
   - Ensure all required files are present (jar, sources, javadoc)
   - Check that all artifacts are properly signed
   - Verify POM metadata is complete

### Getting Help

- Sonatype OSSRH Guide: https://central.sonatype.org/publish/publish-guide/
- Maven GPG Plugin: https://maven.apache.org/plugins/maven-gpg-plugin/
- Nexus Staging Plugin: https://github.com/sonatype/nexus-maven-plugins

## Security Notes

1. **Never commit credentials**: Keep your `settings.xml` file secure and never commit it to version control
2. **GPG Key Security**: Store your GPG private key securely and use a strong passphrase
3. **Token Usage**: Consider using tokens instead of passwords for Sonatype authentication
4. **Environment Variables**: You can use environment variables in settings.xml for better security:
   ```xml
   <username>${env.SONATYPE_USERNAME}</username>
   <password>${env.SONATYPE_PASSWORD}</password>
   ```

## Automation

For CI/CD pipelines, consider:

1. Using GitHub Actions or similar CI/CD tools
2. Storing secrets in encrypted environment variables
3. Automating releases based on git tags
4. Setting up automated snapshot deployments from main branch

Example GitHub Actions workflow can be found in `.github/workflows/` directory (if available).