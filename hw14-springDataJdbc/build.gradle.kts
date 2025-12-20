plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.otus.java.pro"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation("ch.qos.logback:logback-classic")
    implementation("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")


}

tasks.test {
    useJUnitPlatform()
}