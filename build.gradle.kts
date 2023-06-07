import com.modrinth.minotaur.TaskModrinthSyncBody
import net.darkhax.curseforgegradle.TaskPublishCurseForge
import org.spongepowered.asm.gradle.plugins.MixinExtension
import java.time.LocalDateTime

val mod_id: String by extra
val mappings_channel: String by extra
val mc_version: String by extra
val forge_version: String by extra
val registrate_version: String by extra
val create_version: String by extra
val flywheel_version: String by extra
val repository: String by extra
val mod_version: String by extra
val mod_name: String by extra
val mod_author: String by extra
val release_type: String by extra
val modrinth_project_id: String by extra
val curseforge_project_id: String by extra
val origins_version: String by extra
val caelus_version: String by extra
val pehkui_version: String by extra
val apoli_version: String by extra
val jei_version: String by extra

val localEnv = file(".env").takeIf { it.exists() }?.readLines()?.associate {
    val (key, value) = it.split("=")
    key to value
} ?: emptyMap()

val env = System.getenv() + localEnv
val isCI = env["CI"] == "true"

buildscript {
    dependencies {
        classpath("org.spongepowered:mixingradle:0.7-SNAPSHOT")
    }
}

plugins {
    id("maven-publish")
    id("net.minecraftforge.gradle") version ("5.1.+")
    id("net.darkhax.curseforgegradle") version ("1.0.8")
    id("com.modrinth.minotaur") version ("2.+")
}

apply(plugin = "net.minecraftforge.gradle")
apply(plugin = "java")
apply(plugin = "org.spongepowered.mixin")

configure<MixinExtension> {
    add(sourceSets.main.get(), "${mod_id}.refmap.json")
    config("${mod_id}.mixins.json")
}

val artifactGroup = "com.possible_triangle"
base {
    archivesName.set("$mod_id-$mod_version")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withSourcesJar()
}

minecraft {
    mappings(mappings_channel, mc_version)

    runs {
        create("client") {
            workingDirectory(project.file("run"))
        }

        create("server") {
            workingDirectory(project.file("run/server"))
        }

        create("data") {
            workingDirectory(project.file("run"))

            args(
                "--mod",
                mod_id,
                "--all",
                "--output",
                file("src/generated/resources/"),
                "--existing",
                file("src/main/resources")
            )
        }

        forEach { config ->
            config.property("forge.logging.console.level", "debug")

            config.arg("-mixin.config=create.mixins.json")
            config.arg("-mixin.config=flywheel.mixins.json")
            config.arg("-mixin.config=${mod_id}.mixins.json")
            config.property("mixin.env.remapRefMap", "true")
            config.property("mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg")

            config.mods {
                create(mod_id) {
                    source(sourceSets.main.get())
                }
            }
        }
    }
}

sourceSets["main"].resources.srcDir("src/generated/resources/")

repositories {
    mavenCentral()
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
        url = uri("https://cursemaven.com")
        content {
            includeGroup("curse.maven")
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
    minecraft("net.minecraftforge:forge:${mc_version}-${forge_version}")

    implementation(fg.deobf("top.theillusivec4.caelus:caelus-forge:${caelus_version}"))
    implementation(fg.deobf("curse.maven:origins-474438:${origins_version}"))

    implementation(fg.deobf("com.github.Virtuoel:Pehkui:${pehkui_version}-${mc_version}-forge"))

    //implementation fg.deobf("com.github.apace100:apoli:${apoli_version}")
    compileOnly(fg.deobf(fileTree("libs")))

    implementation(fg.deobf("com.simibubi.create:create-${mc_version}:${create_version}:slim"))
    implementation(fg.deobf("com.jozufozu.flywheel:flywheel-forge-${mc_version}:${flywheel_version}"))
    implementation(fg.deobf("com.tterrag.registrate:Registrate:${registrate_version}"))

    if (!isCI) {
        runtimeOnly(fg.deobf("mezz.jei:jei-${mc_version}-forge:${jei_version}"))
    }

    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
}

tasks.withType<Jar> {
    from(rootProject.file("LICENSE")) {
        rename { "${it}_${mod_id}" }
    }

    manifest {
        attributes(
            mapOf(
                "Specification-Title" to mod_id,
                "Specification-Vendor" to "examplemodsareus",
                "Specification-Version" to "1",
                "Implementation-Title" to project.name,
                "Implementation-Version" to mod_version,
                "Implementation-Vendor" to "examplemodsareus",
                "Implementation-Timestamp" to LocalDateTime.now().toString(),
                "MixinConfigs" to "${mod_id}.mixins.json",
            )
        )
    }
}

tasks.jar {
    finalizedBy("reobfJar")
}

tasks.withType<ProcessResources> {
    // this will ensure that this task is redone when the versions change.
    inputs.property("version", version)

    filesMatching(listOf("META-INF/mods.toml", "pack.mcmeta", "${mod_id}.mixins.json")) {
        expand(
            mapOf(
                "mod_version" to mod_version,
                "mod_name" to mod_name,
                "mod_id" to mod_id,
                "mod_author" to mod_author,
                "repository" to repository,
            )
        )
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/${repository}")
            version = version
            credentials {
                username = env["GITHUB_ACTOR"]
                password = env["GITHUB_TOKEN"]
            }
        }
    }
    publications {
        create<MavenPublication>("gpr") {
            groupId = artifactGroup
            artifactId = "${mod_id}-fabric"
            version = mod_version
            from(components["java"])
        }
    }
}

env["CURSEFORGE_TOKEN"]?.let { token ->
    tasks.register<TaskPublishCurseForge>("curseforge") {
        dependsOn(tasks.jar)

        apiToken = token

        upload(curseforge_project_id, tasks.jar.get().archiveFile).apply {
            changelogType = "html"
            changelog = env["CHANGELOG"]
            releaseType = release_type
            addModLoader("Forge")
            addGameVersion(mc_version)
            displayName = "Version $mod_version"

            addRelation("origins-forge", "requiredDependency")
            addRelation("create", "optionalDependency")
        }
    }
}

env["MODRINTH_TOKEN"]?.let { modrinthToken ->
    modrinth {
        token.set(modrinthToken)
        projectId.set(modrinth_project_id)
        versionNumber.set(mod_version)
        versionName.set("Version $mod_version")
        changelog.set(env["CHANGELOG"])
        gameVersions.set(listOf(mc_version))
        loaders.set(listOf("forge"))
        versionType.set(release_type)
        uploadFile.set(tasks.jar.get())
        syncBodyFrom.set(project.file("README.md").readText())
        dependencies {
            optional.project("LNytGWDc")
        }
    }
}

tasks.named("modrinth") {
    dependsOn(tasks.withType<TaskModrinthSyncBody>())
}
