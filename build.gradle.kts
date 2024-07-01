import java.util.*

val mainKotlinClass = "com.javapda.coffeemachine.CoffeeMachineKt"
val theJdkVersion = 17

plugins {
    kotlin("jvm") version "1.9.23"
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.javapda"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(theJdkVersion)
}
// Make the build task depend on shadowJar
tasks.named("build") {
    dependsOn("shadowJar")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = mainKotlinClass
        attributes["Jdk-Version"] = theJdkVersion
        attributes["Author"] = "John Kroubalkian"
        attributes["Build-Date"] = Date()
    }
}
application {
    mainClass.set(mainKotlinClass)
}
