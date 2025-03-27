pluginManagement {
    repositories {
        gradlePluginPortal()
        maven { url = uri("https://maven.architectury.dev/") }
        maven { url = uri("https://maven.minecraftforge.net/") }
        maven { url = uri("https://maven.neoforged.net/releases/") }
        maven { url = uri("https://repo.spongepowered.org/repository/maven-public/") }
    }
}

include("api", "common", "forge", "fabric")