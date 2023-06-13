package com.dcsim.youkon

/// Hold's a user generated project containing multiple variables with units
class Project {
    var name = "New Project"
    var description = ""
    var variables = mutableListOf<VariableWithUnits>()
    var images = mutableListOf<String>()
}

fun testProject() : Project {
    val project = Project()
    project.name = "Test Project"
    project.description = "A bunch of things"
    return project
}