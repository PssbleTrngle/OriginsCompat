import net.minecraftforge.gradle.common.util.MinecraftExtension

val mc_version: String by extra
val registrate_version: String by extra
val create_forge_version: String by extra
val flywheel_version: String by extra
val origins_forge_version: String by extra
val caelus_version: String by extra
val pehkui_version: String by extra
val apoli_forge_version: String by extra
val calio_forge_version: String by extra
val jei_version: String by extra
val appleskin_version: String by extra
val farmers_delight_version: String by extra

forge {
    dataGen()
    enableMixins()
    
    dependOn(project(":api"))
    dependOn(project(":common"))
}

// TODO required for flywheel
configure<MinecraftExtension> {
    runs {
        forEach {
            it.property("production", "true")
        }
    }
}

dependencies {
    modImplementation("top.theillusivec4.caelus:caelus-forge:${caelus_version}")
    modImplementation("curse.maven:origins-474438:${origins_forge_version}")

    modImplementation("com.github.virtuoel:Pehkui:${pehkui_version}-${mc_version}-forge")

    //compileOnly(fg.deobf(rootProject.fileTree("libs")))

    modCompileOnly("curse.maven:apoli-975561:${apoli_forge_version}")
    modCompileOnly("curse.maven:calio-977891:${calio_forge_version}")

    modImplementation("com.simibubi.create:create-${mc_version}:${create_forge_version}:slim")
    modImplementation("com.jozufozu.flywheel:flywheel-forge-${mc_version}:${flywheel_version}")
    modImplementation("com.tterrag.registrate:Registrate:${registrate_version}")

    modImplementation("maven.modrinth:appleskin:${appleskin_version}")
    modImplementation("maven.modrinth:farmers-delight:${farmers_delight_version}")

    if (!env.isCI) {
        modRuntimeOnly("mezz.jei:jei-${mc_version}-forge:${jei_version}")
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