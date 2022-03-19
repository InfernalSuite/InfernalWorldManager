
pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

rootProject.name = "InfernalWorldManager"
include(":IWM-Classmodifier")
include(":IWM-Common")
include(":IWM-API")
include(":ASWM-Migrater")
include(":IWM-Common:loader-utils")
include(":IWM-NMS-v1_18_1")
include("IWM-NMS-v1_18_2")
