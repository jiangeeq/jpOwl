plugins {
    id("java")
}

group = "org.youpeng"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":jpOwl-core"))
    
    // 日志框架依赖都设置为compileOnly，由使用方决定使用哪个实现
    compileOnly("ch.qos.logback:logback-classic:1.5.6")
    compileOnly("org.apache.logging.log4j:log4j-core:2.20.0")
    compileOnly("org.slf4j:slf4j-api:2.0.9")
} 