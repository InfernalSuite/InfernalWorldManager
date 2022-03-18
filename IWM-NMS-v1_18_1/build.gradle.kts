
plugins {
    id("io.papermc.paperweight.userdev") version "1.3.5"
}

dependencies {
    paperDevBundle("1.18.1-R0.1-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.20-M1")
    compileOnly(project(":IWM-API"))
    compileOnly(project(":IWM-Common"))
}

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

description = "IWM-NMS-v1_18_1"
