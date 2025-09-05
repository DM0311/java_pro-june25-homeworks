plugins {
    id("java")
}

group = "ru.otus.java.pro"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly ("org.projectlombok:lombok:1.18.30")
    annotationProcessor ("org.projectlombok:lombok:1.18.30")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.27.3")


}

tasks.test {
    useJUnitPlatform()
}