rootProject.name = "ocplugins"

include(":occore")
include(":ocbankskills")
include(":ocbarbfishing")
include(":ocblastfurnace")
include(":ocbloods")
include(":ocbwans")
include(":occlicker")
include(":occonstruction")
include(":ocgarden")
include(":ocgranite")
include(":ocnightmarezone")
include(":ocrift")
include(":ocsalamanders")
include(":ocsepulchre")
include(":octodt")

for (project in rootProject.children) {
    project.apply {
        projectDir = file(name)
        buildFileName = "$name.gradle.kts"

        require(projectDir.isDirectory) { "Project '${project.path} must have a $projectDir directory" }
        require(buildFile.isFile) { "Project '${project.path} must have a $buildFile build script" }
    }
}
