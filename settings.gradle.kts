rootProject.name = "excel-mapper"

pluginManagement.resolutionStrategy.eachPlugin {
    if (requested.id.id.startsWith("org.jetbrains.kotlin.")) {
        useVersion("1.4.21-2")
    }
}
