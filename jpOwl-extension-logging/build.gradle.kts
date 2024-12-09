plugins {
    id("java")
}

group = "com.youpeng.jpowl"
version = "1.0.0-SNAPSHOT"


dependencies {
    implementation(project(":jpOwl-core"))
    implementation(libs.disruptor)
    implementation(libs.slf4j.api)

    implementation(libs.logback.classic)
    compileOnly(libs.logback.core)

    // 4. 添加 Log4j2 到 SLF4J 的桥接器，将 Log4j2 的日志转发到 SLF4J（即 Logback）
    // implementation("org.apache.logging.log4j:log4j-to-slf4j:${libs.versions.log4j2.get()}")
    // // Log4j2
    compileOnly(libs.log4j2.api)
    compileOnly(libs.log4j2.core)
    compileOnly(libs.log4j2.slf4j)


    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    // 跳过测试
    enabled = false
}

// 配置编码
tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

// 配置 Jar 任务
tasks.withType<Jar>().configureEach {
    manifest {
        attributes["Implementation-Title"] = project.name
        attributes["Implementation-Version"] = project.version
    }
}

// 禁用不需要的任务报告
tasks.withType<GenerateModuleMetadata> {
    enabled = false
}