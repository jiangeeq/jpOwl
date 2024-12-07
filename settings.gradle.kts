rootProject.name = "jpOwl"

include("jpOwl-core")
include("jpOwl-extension-alert")
include("jpOwl-extension-logging")
include("jpOwl-extension-output")
include("jpOwl-spring")
include("jpOwl-spring-samples")
include("jpOwl-spring-boot-starter")
include("jpOwl-spring-boot-autoconfigure")
include("jpOwl-spring-boot-samples")
//
//pluginManagement {
//    repositories {
//        gradlePluginPortal()
//    }
//    resolutionStrategy {
//        eachPlugin {
//            if (requested.id.namespace == "org.jetbrains.kotlin") {
//                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
//            }
//        }
//    }
//}
