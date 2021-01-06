import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val swaggerVersion = "3.0.0"
val springBootVersion = "2.4.1"
val kotlinVersion = "1.4.21-2"
val jacksonVersion = "2.11.4"

plugins {
    id("org.springframework.boot") version "2.4.1"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    id("org.jetbrains.kotlin.plugin.noarg")
    kotlin("jvm")
    kotlin("plugin.spring")
}

group = "nl.opinity"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:${springBootVersion}")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:${springBootVersion}")
    testImplementation("org.springframework.boot:spring-boot-starter-test:${springBootVersion}")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:${springBootVersion}")
    implementation("io.springfox:springfox-boot-starter:${swaggerVersion}")
    implementation("io.springfox:springfox-swagger-ui:${swaggerVersion}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}")
    implementation("com.fasterxml.jackson.core:jackson-databind:${jacksonVersion}")
    implementation("org.apache.poi:poi-ooxml:4.1.2")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:2.2.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

noArg {
    annotation("javax.persistence.Entity")
}