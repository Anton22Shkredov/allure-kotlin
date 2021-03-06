plugins {
    java
    kotlin("jvm") version Versions.kotlin
}

buildscript {
    repositories {
        mavenCentral()
        google()
        jcenter()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}")
        classpath("org.jetbrains.kotlin:kotlin-android-extensions:${Versions.kotlin}")
        classpath("com.android.tools.build:gradle:${Versions.Android.gradlePlugin}")
        classpath("com.github.dcendents:android-maven-gradle-plugin:${Versions.Android.mavenPlugin}")
    }
}

allprojects {
    group = "io.qameta.allure"
    version = "1.0.31-SNAPSHOT"

    repositories {
        mavenCentral()
        maven(url = "https://kotlin.bintray.com/kotlinx")
        google()
        jcenter()
    }
}

val gradleScriptDir by extra("${rootProject.projectDir}/gradle")

configure(subprojects
              .filter { !it.name.contains("android") }
              .filter { it.parent?.name != "samples" }) {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(from = "$gradleScriptDir/maven-publish.gradle")

    dependencies {
        implementation(kotlin("stdlib", Versions.kotlin))
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_6
        targetCompatibility = JavaVersion.VERSION_1_6
    }

    tasks.test {
        systemProperty("allure.model.indentOutput", "true")
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    tasks.processTestResources {
        filesMatching("**/allure.properties") {
            filter {
                it.replace("#project.description#", project.description ?: project.name)
            }
        }
    }
}