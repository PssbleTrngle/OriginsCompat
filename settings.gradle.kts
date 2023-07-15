val mod_name: String by extra

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven { url = uri("https://maven.architectury.dev/") }
        maven { url = uri("https://maven.minecraftforge.net/") }
        maven { url = uri("https://repo.spongepowered.org/repository/maven-public/") }

        System.getenv()["LOCAL_MAVEN"]?.let { localMaven ->
            maven { url = uri(localMaven) }
        }
    }
}

rootProject.name = mod_name
include("api", "common", "forge", "fabric")