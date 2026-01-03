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
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.webjars:webjars-locator-core")
    implementation ("com.google.code.findbugs:jsr305:3.0.2")
    implementation ("org.webjars:sockjs-client:1.5.1")
    implementation ("org.webjars:stomp-websocket:2.3.4")
    implementation ("org.webjars:bootstrap:5.2.3")
}