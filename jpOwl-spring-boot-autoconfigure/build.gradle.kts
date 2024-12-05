plugins {
    id("java")
}

group = "org.youpeng"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.springframework.boot:spring-boot-autoconfigure:2.7.18")
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("ch.qos.logback:logback-core:1.5.6")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("org.springframework:spring-webmvc:5.3.31")
    implementation("javax.servlet:javax.servlet-api:4.0.1")
}

tasks.test {
    useJUnitPlatform()
}
