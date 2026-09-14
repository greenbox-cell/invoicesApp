#!/bin/sh
# POSIX Gradle wrapper. JVM options are passed as separate args (no nested quotes),
# so Linux CI shells (dash) do not treat "-Xmx64m" as a class name.
set -eu

APP_HOME=$(CDPATH= cd -- "$(dirname "$0")" && pwd)

if [ -n "${JAVA_HOME:-}" ]; then
  JAVACMD="$JAVA_HOME/bin/java"
else
  JAVACMD="java"
fi

exec "$JAVACMD" \
  -Xmx64m \
  -Xms64m \
  -Dfile.encoding=UTF-8 \
  -Dorg.gradle.appname=gradlew \
  -classpath "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" \
  org.gradle.wrapper.GradleWrapperMain \
  "$@"
