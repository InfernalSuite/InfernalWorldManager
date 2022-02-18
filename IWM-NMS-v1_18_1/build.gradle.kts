/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    java
    `maven-publish`
    id("io.papermc.paperweight.userdev") version "1.3.4"
}

repositories {
    mavenLocal()
    maven {
        url = uri("target/dependencies")
    }
    maven {
        url = uri("https://repo.aikar.co/content/groups/aikar/")
    }
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
    maven {
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
}

dependencies {
    paperDevBundle("1.18.1-R0.1-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")

    implementation("org.checkerframework:checker-qual:3.21.2")
    implementation("co.aikar:locales:1.0-SNAPSHOT")
    implementation("io.github.jglrxavpok.hephaistos:common:2.4.1")
    compileOnly("org.jetbrains:annotations:22.0.0")
    compileOnly("org.projectlombok:lombok:1.18.22")
    compileOnly("com.infernalsuite:IWM-API:0.0.1-ALPHA")
    compileOnly("com.infernalsuite:IWM-Common:0.0.1-ALPHA")
    compileOnly("org.jetbrains:annotations:22.0.0")
    compileOnly("org.projectlombok:lombok:1.18.22")
}

description = "IWM-NMS-v1_18_1"

tasks {
    assemble {
        dependsOn(reobfJar)
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    compileJava {
        options.release.set(17)
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}
