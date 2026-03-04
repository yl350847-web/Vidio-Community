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
        // 阿里云 Google 镜像，只代理 Google 相关依赖
        maven {
            url = uri("https://maven.aliyun.com/repository/google")
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        // 阿里云公共镜像，代理 Maven Central 及大部分其他依赖
        maven {
            url = uri("https://maven.aliyun.com/repository/public")
            content {
                // 排除 Google 相关组，避免与上面的阿里云 Google 镜像冲突
                excludeGroupByRegex("com\\.android.*")
                excludeGroupByRegex("com\\.google.*")
                excludeGroupByRegex("androidx.*")
            }
        }
        // 官方 Google 仓库（后备）
        google()
        // 官方 Maven Central（后备）
        mavenCentral()
    }
}

rootProject.name = "Video-Community"
include(":app")
include(":bili_sdk")
include(":common_ui_core")
include(":feature_annotations")