plugins {
    `maven-publish`
    kotlin("jvm") version "2.0.20"
}

group = "me.aroze"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.ajalt.colormath:colormath:3.6.0")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "me.aroze"
            artifactId = "Color-Names"
            version = "1.0-SNAPSHOT"
        }
    }
}

kotlin {
    jvmToolchain(21)
}
