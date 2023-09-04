package com.dcsim.youkon

/// Hold's a user generated project containing multiple variables with units
class Project {
    var name = "New Project"
    var about = ""
    var measurements = mutableListOf<Measurement>()
    var images = mutableListOf<String>()

    fun addMeasurement(
        value: Double = 0.0,
        unit: MeasurementUnit = MeasurementUnit.METERS,
        name: String = "New Measurement",
        about: String = ""
    ) {
        measurements.add(Measurement(value, unit, name, about))
    }
}

enum class ProjectExpansionLevel {
    COMPACT, STATIC, EDITABLE
}

fun wembyProject() : Project {
    val project = Project()
    project.name = "Victor Wembenyama"
    project.about = "Stats of the craziest NBA prospect"
    project.addMeasurement(wembyHeight.value, wembyHeight.unit, wembyHeight.name, wembyHeight.about)
    return project
}

fun spaceProject() : Project {
    val project = Project()
    project.name = "Space Shuttle"
    project.about = "Mass props of the Space Shuttle"
    project.addMeasurement(shuttleWeight.value, shuttleWeight.unit, shuttleWeight.name, shuttleWeight.about)
    return project
}