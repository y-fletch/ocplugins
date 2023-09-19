import ProjectVersions.unethicaliteVersion

buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    `java-library`
    checkstyle
    kotlin("jvm") version "1.6.21"
}

project.extra["GithubUrl"] = "https://github.com/y-fletch/ocplugins"
project.extra["GithubUserName"] = "y-fletch"
project.extra["GithubRepoName"] = "ocplugins"

apply<BootstrapPlugin>()

allprojects {
    group = "net.unethicalite"

    project.extra["PluginProvider"] = "y-fletch"
    project.extra["ProjectSupportUrl"] = "https://github.com/y-fletch/ocplugins"
    project.extra["PluginLicense"] = "3-Clause BSD License"

    apply<JavaPlugin>()
    apply(plugin = "java-library")
    apply(plugin = "kotlin")
    apply(plugin = "checkstyle")

    repositories {
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        annotationProcessor(Libraries.lombok)
        annotationProcessor(Libraries.pf4j)

        compileOnly("net.unethicalite:http-api:$unethicaliteVersion")
        compileOnly("net.unethicalite:runelite-api:$unethicaliteVersion")
        compileOnly("net.unethicalite:runelite-client:$unethicaliteVersion")
        compileOnly("net.unethicalite.rs:runescape-api:$unethicaliteVersion")

        compileOnly(Libraries.guice)
        compileOnly(Libraries.javax)
        compileOnly(Libraries.lombok)
        compileOnly(Libraries.pf4j)
        compileOnly(Libraries.apacheCommonsText)
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        withType<AbstractArchiveTask> {
            isPreserveFileTimestamps = false
            isReproducibleFileOrder = true
            dirMode = 493
            fileMode = 420
        }

        compileKotlin {
            kotlinOptions.jvmTarget = "11"
        }

        register<Copy>("copyDeps") {
            into("./build/deps/")
            from(configurations["runtimeClasspath"])
        }
    }
}