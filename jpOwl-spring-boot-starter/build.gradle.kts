plugins {
    id("java")
}

group = "org.youpeng"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api(project(":jpOwl-core"))
    api(project(":jpOwl-spring-boot-autoconfigure"))
    
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework:spring-webmvc")

    implementation("org.springframework.boot:spring-boot-configuration-processor")
}