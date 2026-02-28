pluginManagement {
    repositories {
        // 阿里云 Google 插件镜像
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        // 阿里云 Gradle 插件镜像
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        // 阿里云公共镜像 (包含部分中央库内容)
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // 阿里云 Google 镜像 (加速 AndroidX, Google Play Services 等)
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        // 阿里云公共镜像 (加速 Maven Central)
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        google()
        mavenCentral()
    }
}

rootProject.name = "Video-Community"
include(":app")
include(":bili_sdk")
include(":common_ui_core")
include(":feature_annotations")
