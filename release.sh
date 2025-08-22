#!/bin/bash

# Exit immediately if a command exits with a non-zero status.
set -e

# --- Configuration ---
APK_PATH="app/build/outputs/apk/debug/app-debug.apk"

# --- Helper functions ---
function check_deps() {
  if ! command -v gh &> /dev/null; then
    echo "GitHub CLI (gh) could not be found. Please install it to continue."
    exit 1
  fi
  if [ -z "$GITHUB_TOKEN" ]; then
    echo "GITHUB_TOKEN environment variable is not set. Please set it to your GitHub personal access token."
    exit 1
  fi
}

function build_apk() {
  echo "Building the APK..."
  export HOME=$(realpath ~)
  export ANDROID_SDK_ROOT="$HOME/Android/sdk"
  export ANDROID_HOME="$ANDROID_SDK_ROOT"
  export PATH="$PATH:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin"
  export PATH="$PATH:$ANDROID_SDK_ROOT/platform-tools"
  export PATH="$PATH:$ANDROID_SDK_ROOT/build-tools/35.0.1"
  gradle assembleDebug
  echo "Build complete."
}

function create_release() {
  local version_tag=$1
  echo "Creating GitHub release for tag $version_tag..."
  gh release create "$version_tag" \
    --title "Release $version_tag" \
    --notes "Release for version $version_tag." \
    "$APK_PATH"
  echo "Release created successfully."
}

# --- Main script ---
function main() {
  if [ -z "$1" ]; then
    echo "Usage: $0 <version-tag>"
    echo "Example: $0 v1.0.0"
    exit 1
  fi

  local version_tag=$1

  check_deps
  build_apk
  create_release "$version_tag"
}

main "$@"
