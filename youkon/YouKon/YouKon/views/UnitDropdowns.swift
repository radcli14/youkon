import SwiftUI
import shared


struct FromDropdown: View {
    @Binding var measurement: shared.Measurement
    @State private var isExpanded = false
    @State var fieldValue: MeasurementUnit = .meters

    var body: some View {
        Picker("", selection: $fieldValue) {
            UnitDropdownMenuItems(
                units: kotlinToSwiftArray(measurement.unit.allUnits)
            ) { unit in
                isExpanded = false
                onClick(unit)
            }
        }
        .frame(width: 112)
    }

    private func onClick(_ unit: MeasurementUnit?) {
        if let unit = unit {
            measurement.unit = unit
        }
    }
}

struct ToDropdown: View {
    var equivalentUnits: [MeasurementUnit]
    @State private var isExpanded = false
    @State var fieldValue: MeasurementUnit = .meters

    var body: some View {
        Picker("", selection: $fieldValue) {
            UnitDropdownMenuItems(
                units: equivalentUnits
            ) { unit in
                isExpanded = false
                //onClick(unit)
            }
        }
        .frame(width: 112)
    }
}

struct UnitDropdownButtonColumn: View {
    var firstLine: String
    var secondLine: String

    var body: some View {
        VStack(alignment: .leading, spacing: 2) {
            Text(firstLine)
                .font(.caption)
            Text(secondLine)
                .font(.caption2)
        }
    }
}

struct UnitDropdownMenuItems: View {
    var units: [MeasurementUnit]
    var onSelect: (MeasurementUnit) -> Void

    var body: some View {
        ForEach(units, id: \.self) { unit in
            Button(action: { onSelect(unit) }) {
                Text(String(describing: unit))
            }
            .buttonStyle(BorderlessButtonStyle())
        }
    }
}
