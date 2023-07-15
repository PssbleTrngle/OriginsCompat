val mc_version: String by extra
val create_fabric_version: String by extra

fabric {
    enableMixins()

    dependOn(project(":api"))
    dependOn(project(":common"))
}

dependencies {
    modImplementation("com.simibubi.create:create-fabric-${mc_version}:${create_fabric_version}")
}