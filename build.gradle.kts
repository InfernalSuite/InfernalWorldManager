plugins {
    `java-library`
}

buildscript {
    repositories {
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("io.freefair.gradle:lombok-plugin:6.3.0")
    }
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "io.freefair.lombok")

    repositories {
        mavenLocal()
        mavenCentral()

        maven("https://repo.aikar.co/content/groups/aikar/")
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://repo.codemc.io/repository/nms/")
        maven("https://repo.rapture.pw/repository/maven-releases/")
        maven("https://repo.glaremasters.me/repository/concuncan/")
    }

    dependencies {
        implementation("org.checkerframework:checker-qual:3.21.2")
        implementation("co.aikar:locales:1.0-SNAPSHOT")
        implementation("io.github.jglrxavpok.hephaistos:common:2.4.1")
        compileOnly("org.jetbrains:annotations:22.0.0")
        compileOnly("org.projectlombok:lombok:1.18.22")
    }

    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    tasks.withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }

    tasks.withType<ProcessResources> {
        filteringCharset = Charsets.UTF_8.name()
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

}