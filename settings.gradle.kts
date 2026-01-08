pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "PlayDemo"
include(":app")
include(":lib-base")
include(":lib-common")
include(":lib-storage")
include(":lib-crash")
include(":lib-log")
include(":feature-brvah")
include(":feature-dialog")
 