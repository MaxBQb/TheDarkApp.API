import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.0"
    kotlin("kapt") version "1.6.10"
    kotlin("plugin.spring") version "1.6.0"
}

group = "lab.maxb"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11


repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.7")
    implementation("org.springdoc:springdoc-openapi-security:1.6.7")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.7")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("javax.xml.bind:jaxb-api")
    implementation("com.google.firebase:firebase-admin:8.1.0")
    implementation("org.springframework.boot:spring-boot-starter-web:2.6.7")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.6.7")
    runtimeOnly("org.postgresql:postgresql:42.3.4")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.7")

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


