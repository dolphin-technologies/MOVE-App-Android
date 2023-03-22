
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven(
            url = "https://dolphin.jfrog.io/artifactory/move-sdk-libs-release"
        )
    }
}
rootProject.name = "Move"

include(":app")
