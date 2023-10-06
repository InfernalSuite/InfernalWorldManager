dependencies {
    implementation(project(":IWM-API"))
    implementation("net.bytebuddy:byte-buddy:1.14.8")
    implementation("com.google.guava:guava:32.1.2-jre")
    implementation("net.kyori:event-api:3.0.0") {
        exclude(module = "checker-qual")
        exclude(module = "guava")
    }
    implementation("com.github.luben:zstd-jni:1.5.5-6")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.20-M1")
    implementation("org.spongepowered:configurate-core:4.1.2")
    compileOnly("org.apache.logging.log4j:log4j-api:2.20.0")
    compileOnly("org.slf4j:slf4j-api:2.0.9")
    compileOnly(project(":IWM-Common:loader-utils"))
}

description = "IWM-Common"
