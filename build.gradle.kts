// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    kotlin("jvm") version "1.9.21" apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
}


buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath (kotlin("gradle-plugin", version = "1.9.21"))
        classpath ("com.android.tools.build:gradle:8.1.4")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    }
}