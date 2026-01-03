plugins {
    id("java")
}

group = "ru.otus.java.pro"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
//    implementation ("com.google.code.findbugs:jsr305")
    implementation("org.webjars:webjars-locator-core")
//    implementation("org.webjars:sockjs-client")
//    implementation("org.webjars:stomp-websocket")
//    implementation("org.webjars:bootstrap")

    implementation ("com.google.code.findbugs:jsr305:3.0.2")
    implementation ("org.webjars:sockjs-client:1.5.1")
    implementation ("org.webjars:stomp-websocket:2.3.4")
    implementation ("org.webjars:bootstrap:5.2.3")
}