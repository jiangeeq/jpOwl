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
    
    // 各种输出源的依赖设为compileOnly，由使用方决定使用哪些实现
    compileOnly("org.elasticsearch.client:elasticsearch-rest-high-level-client:7.17.12")
    compileOnly("org.mongodb:mongodb-driver-sync:4.11.1")
    
    // 通用依赖
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.3")
    implementation("io.netty:netty-buffer:4.1.68.Final")
} 