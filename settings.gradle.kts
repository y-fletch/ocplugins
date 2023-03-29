rootProject.name = "ocplugins"

include(":occore")
include(":ocgarden")
include(":ocrift")
include(":ocbloods")
include(":ocsepulchre")
include(":ocbarbfishing")
include(":occlicker")
include(":ocbarbfishing")

for (project in rootProject.children) {
    project.apply {
        projectDir = file(name)
        buildFileName = "$name.gradle.kts"

        require(projectDir.isDirectory) { "Project '${project.path} must have a $projectDir directory" }
        require(buildFile.isFile) { "Project '${project.path} must have a $buildFile build script" }
    }
}
