plugins {
  java
  id("org.springframework.boot") version "3.2.2"
  id("io.spring.dependency-management") version "1.1.4"

  id("org.jetbrains.kotlin.plugin.spring") version "1.9.22"
  id("org.jetbrains.kotlin.plugin.jpa") version "1.9.22"
  id("org.jetbrains.kotlin.jvm") version "1.9.22"
}

group = "kz.nearbygems.catalog"
version = "0.0.1"

java {
  sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("co.elastic.clients:elasticsearch-java:8.12.2")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.3")

  compileOnly("org.projectlombok:lombok")
  annotationProcessor("org.projectlombok:lombok")

  testImplementation("org.jetbrains.kotlin:kotlin-test")
  testImplementation("com.ninja-squad:springmockk:4.0.2")
  testImplementation("org.testcontainers:testcontainers:1.19.7")
  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude("org.junit.vintage", "junit-vintage-engine")
    exclude("org.mockito", "mockito.core")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
