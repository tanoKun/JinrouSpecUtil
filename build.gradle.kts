import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    // Add a dependency on the Kotlin Gradle plugin, so that convention plugins can apply it.
    implementation(libs.kotlinGradlePlugin)
    implementation(project("app"))
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.github.johnrengelman.shadow")

    group ="com.github.tanokun"
    version = "1.0"

    kotlin {
        jvmToolchain(17)
    }

    tasks.withType<Test> {
        testLogging {
            events(PASSED, FAILED, SKIPPED)
            showStandardStreams = true
        }

        useJUnitPlatform()
    }

    dependencies {
        testImplementation(kotlin("test"))
    }
}
tasks.named("shadowJar") {
    dependsOn(subprojects.map { it.tasks.named("test") })
}

tasks.shadowJar {
    archiveBaseName.set("spectatorUtil")
    archiveVersion.set("1.0")

    mergeServiceFiles()
}

bukkit {
    main = "com.github.tanokun.spec.SpectatorUtil"

    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    apiVersion = "1.18"
    authors = listOf("tanoKun")

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
