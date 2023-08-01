val mc_version: String by extra
val origins_fabric_version: String by extra
val calio_fabric_version: String by extra
val apoli_fabric_version: String by extra
val create_fabric_version: String by extra
val fabric_api_version: String by extra
val jei_version: String by extra
val pehkui_version: String by extra
val cca_version: String by extra

fabric {
    enableMixins()

    dependOn(project(":api"))
    dependOn(project(":common"))
}

fun ExternalModuleDependency.withoutFabricApi() {
    exclude(group = "net.fabricmc.fabric-api")
}

dependencies {
    modImplementation("com.simibubi.create:create-fabric-${mc_version}:${create_fabric_version}")

    modCompileOnly("dev.onyxstudios.cardinal-components-api:cardinal-components-base:${cca_version}")

    modImplementation("com.github.apace100:calio:${calio_fabric_version}")
    modImplementation("com.github.apace100:apoli:${apoli_fabric_version}") {
        exclude(group = "com.github.DaFuqs")
    }

    modImplementation("com.github.apace100:origins-fabric:${origins_fabric_version}") {
        exclude(group = "com.github.apace100")
    }

    modImplementation("net.fabricmc.fabric-api:fabric-transfer-api-v1:${fabric_api_version}")

    modImplementation("com.github.Virtuoel:Pehkui:${pehkui_version}") {
        withoutFabricApi()
    }

    if (!env.isCI) {
        modRuntimeOnly("mezz.jei:jei-${mc_version}-fabric:${jei_version}")
    }
}