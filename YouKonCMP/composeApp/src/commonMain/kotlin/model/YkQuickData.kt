package model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class YkQuickData(
    var value: Double,
    var unit: YkUnit,
    var targetUnit: YkUnit
) {
    val measurement get() = YkMeasurement(value, unit, id = "Quick-Convert")
    val convertedText: String get() = measurement.unitAndConversion(targetUnit)
    val equivalentUnits: Array<YkUnit> get() = unit.equivalentUnits()
    val asJsonString: String get() = Json { prettyPrint = true }.encodeToString(this)

    companion object {
        fun fromJsonString(jsonString: String): YkQuickData {
            return Json.decodeFromString<YkQuickData>(jsonString)
        }
    }
}