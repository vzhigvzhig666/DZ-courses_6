plugins {
    kotlin("jvm") version "1.9.20"
    id("io.qameta.allure") version "4.3.0"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("org.xerial:sqlite-jdbc:3.47.2.0")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
    testImplementation("io.mockk:mockk:1.13.13")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    testImplementation("com.squareup.okhttp3:okhttp:4.12.0")
}

tasks.test {
    useJUnitPlatform()
}

allure {
    report {
        dependsOnTests.set(true)
    }
}

tasks.register<Sync>("exportAllureReport") {
    dependsOn("allureReport")
    from(layout.buildDirectory.dir("reports/allure-report/allureReport"))
    into(layout.projectDirectory.dir("reports"))
}
