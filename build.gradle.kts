import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

val projectVersion = "1.0.3"

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.21"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

kotlin {
    jvmToolchain(17)
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.xenondevs.xyz/releases")
    }
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.21")
    compileOnly("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")
    implementation("xyz.xenondevs.invui:invui-kotlin:1.44")
    implementation("xyz.xenondevs.invui:invui:1.44")

    testImplementation(kotlin("test"))
}

tasks.withType<Test> {
    testLogging {
        events(PASSED, FAILED, SKIPPED)
        showStandardStreams = true
    }

    useJUnitPlatform()
}

tasks.named("shadowJar") {
    dependsOn(subprojects.map { it.tasks.named("test") })
}

tasks.shadowJar {
    archiveBaseName.set("spectatorUtil")
    archiveVersion.set(projectVersion)

    mergeServiceFiles()
}

bukkit {
    main = "com.github.tanokun.spec.SpectatorUtil"

    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    apiVersion = "1.18"
    authors = listOf("tanoKun")
    version = projectVersion

    commands {
        register("spec") {
            permission = "com.tanokun.github.spec"
        }
    }

    permissions {
        register("com.tanokun.github.spec") {
            default = BukkitPluginDescription.Permission.Default.OP
        }
    }
}
