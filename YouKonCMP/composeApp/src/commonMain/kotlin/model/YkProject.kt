package model
import UUIDGenerator
import kotlinx.serialization.*
import kotlinx.serialization.json.*

/// Hold's a user generated project containing multiple variables with units
@Serializable
data class YkProject(
    var name: String = "New Project",
    var about: String = "",
    var measurements: MutableList<YkMeasurement> = mutableListOf(),
    var images: MutableList<String> = mutableListOf(),
    val id: String = UUIDGenerator().generateUUID(),
    var userId: String = "",
    var isPublic: Boolean = false
) {

    constructor() : this(
        name = "New Project",
        about = "",
        measurements = mutableListOf(),
        images = mutableListOf()
    )

    fun asJsonString(): String {
        return Json{ prettyPrint = true }.encodeToString(this)
    }

    /// Add a new measurement with user specifications for value, unit, name, and about
    fun addMeasurement(
        value: Double = 0.0,
        unit: YkUnit = YkUnit.METERS,
        name: String = "New Measurement",
        about: String = "Description"
    ) {
        measurements.add(YkMeasurement(value, unit, name, about))
    }

    /// Remove the specified `YkMeasurement`, if it exits in the `measurements` list
    fun removeMeasurement(measurement: YkMeasurement) {
        measurements.indexOfFirst { measurement == it }.let { idx ->
            measurements.removeAt(idx)
        }
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