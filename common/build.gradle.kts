val mc_version: String by extra
val create_fabric_version: String by extra

plugins {
    id("dev.architectury.loom") version ("1.2-SNAPSHOT")
}

common {
    applyVanillaGradle = false

    dependOn(project(":api"))
}

dependencies {
    "minecraft"("com.mojang:minecraft:${mc_version}")
    "mappings"(loom.officialMojangMappings())

    modCompileOnly("com.simibubi.create:create-fabric-${mc_version}:${create_fabric_version}")
}

// Needed for some reason
tasks.register("prepareWorkspace")