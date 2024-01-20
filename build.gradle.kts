// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    kotlin("jvm") version "1.9.21" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
}


buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath (kotlin("gradle-plugin", version = "1.9.21"))
        classpath ("com.android.tools.build:gradle:8.2.1")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    }
}