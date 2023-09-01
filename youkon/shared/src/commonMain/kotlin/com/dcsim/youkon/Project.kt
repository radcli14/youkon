package com.dcsim.youkon

/// Hold's a user generated project containing multiple variables with units
class Project {
    var name = "New Project"
    var about = ""
    var measurements = mutableListOf<Measurement>()
    var images = mutableListOf<String>()
}

enum class ProjectExpansionLevel {
    COMPACT, STATIC, EDITABLE
}

fun wembyProject() : Project {
    val project = Project()
    project.name = "Victor Wembenyama"
    project.about = "Stats of the craziest NBA prospect"
    project.measurements.add(wembyHeight)
    project.measurements.add(wembyWeight)
    return project
}

fun spaceProject() : Project {
    val project = Project()
    project.name = "Space Shuttle"
    project.about = "Mass props of the Space Shuttle"
    project.measurements.add(shuttleWeight)
    return project
}