val mc_version: String by extra
val registrate_version: String by extra
val create_version: String by extra
val flywheel_version: String by extra
val origins_version: String by extra
val caelus_version: String by extra
val pehkui_version: String by extra
val apoli_version: String by extra
val jei_version: String by extra
val appleskin_version: String by extra
val farmers_delight_version: String by extra

plugins {
    id("net.somethingcatchy.gradle") version("0.0.1")
}

subprojects {
    repositories {
        mavenCentral()
        curseMaven()
        modrinthMaven()
        
        maven {
            url = uri("https://repo.spongepowered.org/repository/maven-public/")
            content {
                includeGroup("org.spongepowered")
            }
        }
        maven {
            url = uri("https://maven.blamejared.com/")
            content {
                includeGroup("mezz.jei")
            }
        }
        maven {
            url = uri("https://maven.tterrag.com/")
            content {
                includeGroup("com.simibubi.create")
                includeGroup("com.jozufozu.flywheel")
                includeGroup("com.tterrag.registrate")
            }
        }
        maven {
            url = uri("https://maven.theillusivec4.top")
            content {
                includeGroup("top.theillusivec4.caelus")
                includeGroup("top.theillusivec4.curios")
            }
        }
        maven {
            url = uri("https://jitpack.io")
            content {
                includeGroup("com.github.Virtuoel")
                includeGroup("com.github.apace100")
            }
        }
        maven {
            url = uri("https://squiddev.cc/maven/")
            content {
                includeGroup("org.squiddev")
            }
        }

        apply(from = "https://raw.githubusercontent.com/PssbleTrngle/GradleHelper/main/repositories/create-fabric.build.kts")
    }

    enablePublishing {
        repositories {
            githubPackages(project)
        }
    }
}