#!/bin/bash

# Sud GIP Auth Java SDK - Maven Central Repository Deployment Script
# This script helps deploy the SDK to Maven Central Repository

set -e

echo "Sud GIP Auth Java SDK - Deployment Script"
echo "==========================================="

# Check if GPG is installed
if ! command -v gpg &> /dev/null; then
    echo "Error: GPG is not installed. Please install GPG first."
    echo "On macOS: brew install gnupg"
    echo "On Ubuntu: sudo apt-get install gnupg"
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven is not installed. Please install Maven first."
    exit 1
fi

# Check if settings.xml exists
if [ ! -f "$HOME/.m2/settings.xml" ]; then
    echo "Warning: Maven settings.xml not found at $HOME/.m2/settings.xml"
    echo "Please copy and configure settings.xml.template to $HOME/.m2/settings.xml"
    echo "Make sure to replace placeholders with your actual credentials."
    exit 1
fi

echo "Pre-deployment checks passed!"
echo ""

# Clean and compile
echo "Step 1: Cleaning and compiling..."
mvn clean compile

# Run tests
echo "Step 2: Running tests..."
mvn test

# Package with sources and javadoc
echo "Step 3: Packaging with sources and javadoc..."
mvn package

# Deploy to Maven Central
echo "Step 4: Deploying to Maven Central..."
echo "This will sign artifacts with GPG and upload to OSSRH staging repository."
read -p "Are you sure you want to continue? (y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    mvn clean deploy -P release
    echo ""
    echo "Deployment completed successfully!"
    echo "Please check the staging repository at: https://s01.oss.sonatype.org/"
    echo "If autoReleaseAfterClose is disabled, you need to manually release the staging repository."
else
    echo "Deployment cancelled."
fi

echo "Done!"