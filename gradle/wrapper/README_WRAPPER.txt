Gradle Wrapper — one missing binary file
=========================================

This project is configured to use Gradle 8.7 via the wrapper. The wrapper
configuration (gradle-wrapper.properties) is present, but the wrapper's binary
JAR (gradle-wrapper.jar) and the gradlew / gradlew.bat launcher scripts are NOT
included here, because they are binary/generated artifacts that could not be
produced in the environment that generated this project.

This is harmless and takes seconds to fix. Choose ONE:

Option A — Open in Android Studio (recommended)
  Just open the project. Android Studio detects the wrapper properties and
  regenerates gradle-wrapper.jar and the gradlew scripts automatically, then
  syncs.

Option B — Regenerate from a local Gradle install
  If you have Gradle installed, run from the project root:

      gradle wrapper --gradle-version 8.7

  That writes gradle/wrapper/gradle-wrapper.jar, gradlew, and gradlew.bat.
  After that, the usual `./gradlew assembleDebug` / `./gradlew bundleRelease`
  commands work.

Nothing else about the build is affected — all Gradle build scripts, the
manifest, resources, and source code are complete.
