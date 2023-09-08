package com.dcsim.youkon

/// Hold's a user generated project containing multiple variables with units
class YkProject {
    var name = "New Project"
    var about = ""
    var measurements = mutableListOf<YkMeasurement>()
    var images = mutableListOf<String>()

    fun addMeasurement(
        value: Double = 0.0,
        unit: YkUnit = YkUnit.METERS,
        name: String = "New Measurement",
        about: String = ""
    ) {
        measurements.add(YkMeasurement(value, unit, name, about))
    }
}

enum class ProjectExpansionLevel {
    COMPACT, STATIC, EDITABLE
}

fun wembyProject() : YkProject {
    val project = YkProject()
    project.name = "Victor Wembenyama"
    project.about = "Stats of the craziest NBA prospect"
    project.addMeasurement(wembyHeight.value, wembyHeight.unit, wembyHeight.name, wembyHeight.about)
    return project
}

fun spaceProject() : YkProject {
    val project = YkProject()
    project.name = "Space Shuttle"
    project.about = "Mass props of the Space Shuttle"
    project.addMeasurement(shuttleWeight.value, shuttleWeight.unit, shuttleWeight.name, shuttleWeight.about)
    return project
}