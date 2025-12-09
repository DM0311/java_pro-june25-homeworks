plugins {
    id("java")
}

group = "ru.otus.java.pro"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    implementation("ch.qos.logback:logback-classic")
    implementation("com.google.code.gson:gson")
    implementation("org.eclipse.jetty.ee10:jetty-ee10-servlet")
    implementation("org.eclipse.jetty:jetty-server")
    implementation("org.eclipse.jetty.ee10:jetty-ee10-webapp")
    implementation("org.eclipse.jetty:jetty-security")
    implementation("org.eclipse.jetty:jetty-http")
    implementation("org.eclipse.jetty:jetty-io")
    implementation("org.eclipse.jetty:jetty-util")
    implementation("org.freemarker:freemarker")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.mockito:mockito-junit-jupiter")
    implementation("org.hibernate.orm:hibernate-core")
    implementation("org.flywaydb:flyway-core")
    runtimeOnly("org.flywaydb:flyway-database-postgresql")

    implementation("org.postgresql:postgresql")
}

tasks.test {
    useJUnitPlatform()
}