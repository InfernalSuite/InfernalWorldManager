plugins {
    `java-library`
    id("io.freefair.lombok") version "8.3" apply false
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
        implementation("org.checkerframework:checker-qual:3.39.0")
        implementation("co.aikar:locales:1.0-SNAPSHOT")
        implementation("io.github.jglrxavpok.hephaistos:common:2.6.1")
        compileOnly("org.jetbrains:annotations:24.0.1")
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