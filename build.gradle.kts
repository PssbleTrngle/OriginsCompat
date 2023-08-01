plugins {
    id("net.somethingcatchy.gradle") version("0.0.4")
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
                includeGroup("com.github.DaFuqs")
            }
        }
        maven {
            url = uri("https://squiddev.cc/maven/")
            content {
                includeGroup("org.squiddev")
            }
        }
        maven {
            url = uri("https://maven.shedaniel.me/")
            content {
                includeGroup("me.shedaniel.cloth")
            }
        }
        maven {
            url = uri("https://maven.ladysnake.org/releases")
            content {
                includeGroup("dev.onyxstudios.cardinal-components-api")
                includeGroup("io.github.ladysnake")
            }
        }
        maven {
            url = uri("https://maven.terraformersmc.com/releases/")
            content {
                includeGroup("com.terraformersmc")
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