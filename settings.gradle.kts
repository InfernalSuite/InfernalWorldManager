
pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

rootProject.name = "InfernalWorldManager"
include(":IWM-Common")
include(":IWM-API")
include(":ASWM-Migrater")
include(":IWM-Common:loader-utils")
include("IWM-Paper")
include("IWM-Paper:iwm-api")
include("IWM-Paper:iwm-server")
include("IWM-Pufferfish")
include("IWM-Pufferfish:iwm-api")
include("IWM-Pufferfish:iwm-server")
