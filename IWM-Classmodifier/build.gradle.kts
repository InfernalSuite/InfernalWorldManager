
plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

dependencies {
    implementation("org.javassist:javassist:3.28.0-GA")
    implementation("org.yaml:snakeyaml:1.29")
    compileOnly(project(":IWM-API"))
}

sourceSets {
    main {
        resources {
            include("**/*")
        }
    }
}

tasks {
    jar {
        manifest {
            attributes["Premain-Class"] = "com.infernalsuite.iwm.clsm.NMSTransformer"
        }
    }

    shadowJar {
        archiveClassifier.set("")
    }

    assemble {
        dependsOn(shadowJar)
    }
}

description = "IWM-Classmodifier"
