plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.5"
	id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "6.25.0"
}

group = "com.minimart.inventory"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
    // Web/API
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // Data
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")

    // Validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Serialization
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Reactive
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // Kotlin runtime
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // Observability
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Utilities
    implementation("org.apache.commons:commons-lang3:3.18.0")

    // API Docs
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.6.0")

    // Dev tools
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")

    // Testing - Integration
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mongodb")

    // Testing - Runtime
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

spotless {
    kotlin {
        ktfmt().googleStyle()
        target("src/**/*.kt", "src/**/*.kts")
    }
}

tasks.withType<Test> {
	useJUnitPlatform()
}
