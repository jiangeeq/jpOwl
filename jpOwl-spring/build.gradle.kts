plugins {
    id("java")
}

group = "com.youpeng.jpowl"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {
    implementation(project(":jpOwl-core"))
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.7.18")
    
    // 如果需要通过HTTP访问actuator端点
    compileOnly("org.springframework.boot:spring-boot-starter-web:2.7.18")
} 