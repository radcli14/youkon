package com.dcsim.youkon

/// Hold's a user generated project containing multiple variables with units
class Project {
    var name = "New Project"
    var description = ""
    var measurements = mutableListOf<Measurement>()
    var images = mutableListOf<String>()
}

fun testProject() : Project {
    val project = Project()
    project.name = "Test Project"
    project.description = "A bunch of things"
    project.measurements.add(testMeasurement())
    return project
}