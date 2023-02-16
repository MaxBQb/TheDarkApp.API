import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.8.10"
    kotlin("kapt") version "1.8.10"
    kotlin("plugin.spring") version "1.8.10"
}

group = "lab.maxb"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8


repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.11")
    implementation("org.springdoc:springdoc-openapi-security:1.6.11")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.11")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("javax.xml.bind:jaxb-api")
    implementation("com.google.firebase:firebase-admin:9.0.0")
    implementation("com.vladmihalcea:hibernate-types-52:2.19.2")
    implementation("org.springframework.boot:spring-boot-starter-web:2.7.4")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.7.4")
    runtimeOnly("org.postgresql:postgresql:42.5.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.4")

    val configurationProcessor ="org.springframework.boot:spring-boot-configuration-processor"
    kapt(configurationProcessor)
    kaptTest(configurationProcessor)
    annotationProcessor(configurationProcessor)
}

kapt {
    annotationProcessor("org.springframework.boot.configurationprocessor.ConfigurationMetadataAnnotationProcessor")
}

tasks {
    withType<KotlinCompile> {
        dependsOn(processResources)
    }

    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }

    getByName<Jar>("jar") {
        enabled = false
    }
}


val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    languageVersion = "1.8"
}