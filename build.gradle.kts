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
    id("com.possible-triangle.gradle") version("0.0.1")
}

forge {
    dataGen()
    enableMixins()
}

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
}

dependencies {
    modImplementation("top.theillusivec4.caelus:caelus-forge:${caelus_version}")
    modImplementation("curse.maven:origins-474438:${origins_version}")

    modImplementation("com.github.Virtuoel:Pehkui:${pehkui_version}-${mc_version}-forge")

    //implementation fg.deobf("com.github.apace100:apoli:${apoli_version}")
    compileOnly(fg.deobf(fileTree("libs")))

    modImplementation("com.simibubi.create:create-${mc_version}:${create_version}:slim")
    modImplementation("com.jozufozu.flywheel:flywheel-forge-${mc_version}:${flywheel_version}")
    modImplementation("com.tterrag.registrate:Registrate:${registrate_version}")

    modImplementation("maven.modrinth:appleskin:${appleskin_version}")
    modImplementation("maven.modrinth:farmers-delight:${farmers_delight_version}")

    if (!env.isCI) {
        modRuntimeOnly("mezz.jei:jei-${mc_version}-forge:${jei_version}")
    }
}

enablePublishing {
    repositories {
        githubPackages(project)
    }
}

uploadToCurseforge {
    dependencies {
        required("origins-forge")
        optional("create")
    }
}

uploadToModrinth {
    dependencies {
        optional("LNytGWDc")
    }

    syncBodyFromReadme()
}