plugins {
    java
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.glaremasters.me/repository/concuncan/")
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    implementation("com.flowpowered:flow-nbt:2.0.0")
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
}