plugins {
    id("java")
}

group = "org.youpeng"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // 可选依赖
    compileOnly(project(":jpOwl-extension-alert"))

    implementation(project(":jpOwl-spring-boot-autoconfigure"))
}