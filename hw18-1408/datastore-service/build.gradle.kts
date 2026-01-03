plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.otus.java.pro"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation ("com.google.code.findbugs:jsr305:3.0.2")
    implementation("org.postgresql:r2dbc-postgresql:1.0.2.RELEASE")
    implementation("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")

    runtimeOnly("org.flywaydb:flyway-database-postgresql")
}