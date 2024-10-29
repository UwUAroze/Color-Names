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
    api("com.github.ajalt.colormath:colormath:3.6.0")
}

tasks.test {
    useJUnitPlatform()
}

java {
    withJavadocJar()
    withSourcesJar()
}

kotlin {
    jvmToolchain(21)
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
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
