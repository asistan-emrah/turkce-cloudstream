#!/bin/sh
export JAVA_HOME=${JAVA_HOME:-/usr/lib/jvm/java-11-openjdk}
exec java -Dorg.gradle.appname=gradlew -classpath gradle/wrapper/gradle-wrapper.jar org.gradle.wrapper.GradleWrapperMain "$@"
