pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/"
        )
    }
}

rootProject.name = "InfernalWorldManager"


include("common", "plugin", "api", "classmodifier")
