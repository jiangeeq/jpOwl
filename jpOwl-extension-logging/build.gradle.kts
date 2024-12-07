plugins {
    id("java")
    // 如果需要创建可执行 JAR，添加 application 插件
    // id("application")
}

group = "org.youpeng"
version = "1.0-SNAPSHOT"

// 指定 Java 版本
java {
    sourceCompatibility = JavaVersion.VERSION_1_8 // 或者你项目使用的 Java 版本
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.lmax:disruptor:3.4.4")

    implementation("ch.qos.logback:logback-classic:1.2.12"){
        exclude(group = "org.slf4j", module = "slf4j-api")
    }
    implementation("ch.qos.logback:logback-core:1.2.12")
    implementation("org.slf4j:slf4j-api:1.7.36")

    // 4. 添加 Log4j2 到 SLF4J 的桥接器，将 Log4j2 的日志转发到 SLF4J（即 Logback）
    // implementation("org.apache.logging.log4j:log4j-to-slf4j:${libs.versions.log4j2.get()}")
    // // Log4j2
     implementation(libs.log4j2.api)
     implementation(libs.log4j2.core)
     compileOnly(libs.log4j2.slf4j)


    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    // 跳过测试
//    enabled = false
}

// 如果使用 application 插件，需要指定主类
// application {
//     mainClass.set("com.youpeng.jpowl.Main")
// }

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