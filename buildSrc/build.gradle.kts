plugins {
    `groovy`
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
    google()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())
}

gradlePlugin {
    plugins {
        create("buildInfo") {
            id = "com.suzhe.buildinfo"
            implementationClass = "com.suzhe.plugin.BuildInfoPlugin"
        }
    }
}
