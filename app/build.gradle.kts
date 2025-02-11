repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.xenondevs.xyz/releases")
    }
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.paperApi)
    implementation(libs.invUIKotlin)
    implementation(libs.invUI)

    testImplementation(kotlin("test"))
    testImplementation(libs.slf4j)
    testImplementation(libs.bukkitMock)
}