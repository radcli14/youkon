import SwiftUI
import shared


struct FromDropdown: View {
    @Binding var measurement: shared.Measurement
    @State private var isExpanded = false

    var body: some View {
        Button(action: { isExpanded.toggle() }) {
            UnitDropdownButtonColumn(firstLine: "From", secondLine: String(describing: measurement.unit))
        }
        .buttonStyle(BorderlessButtonStyle())
        .frame(width: 112)
        .overlay(
            Group {
                if isExpanded {
                    UnitDropdownMenuItems(
                        units: kotlinToSwiftArray(measurement.allUnits)
                    ) { unit in
                        isExpanded = false
                        onClick(unit)
                    }
                    .padding(.top, 8)
                }
            }
        )
    }

    private func onClick(_ unit: shared.Measurement.Unit?) {
        if let unit = unit {
            measurement.unit = unit
        }
    }
}

struct ToDropdown: View {
    var equivalentUnits: [shared.Measurement.Unit]
    @State private var isExpanded = false

    var body: some View {
        Button(action: { isExpanded.toggle() }) {
            UnitDropdownButtonColumn(firstLine: "To", secondLine: String(describing: equivalentUnits.first))
        }
        .buttonStyle(BorderlessButtonStyle())
        .frame(width: 112)
        .overlay(
            Group {
                if isExpanded {
                    UnitDropdownMenuItems(units: equivalentUnits) { _ in
                        isExpanded = false
                    }
                    .padding(.top, 8)
                }
            }
        )
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
    var units: [shared.Measurement.Unit]
    var onSelect: (shared.Measurement.Unit) -> Void

    var body: some View {
        ForEach(units, id: \.self) { unit in
            Button(action: { onSelect(unit) }) {
                Text(String(describing: unit))
            }
            .buttonStyle(BorderlessButtonStyle())
        }
    }
}
