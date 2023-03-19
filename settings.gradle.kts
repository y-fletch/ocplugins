rootProject.name = "OCPlugins"

include(":occore")
include(":ocgarden")
include(":ocrift")
include(":ocbloods")
include(":ocsepulchre")
include(":occlicker")

for (project in rootProject.children) {
    project.apply {
        projectDir = file(name)
        buildFileName = "$name.gradle.kts"

        require(projectDir.isDirectory) { "Project '${project.path} must have a $projectDir directory" }
        require(buildFile.isFile) { "Project '${project.path} must have a $buildFile build script" }
    }
}
