import SwiftUI

struct FromDropdown: View {
    @Binding var measurement: Measurement
    @State private var isExpanded = false

    var body: some View {
        Button(action: { isExpanded.toggle() }) {
            UnitDropdownButtonColumn(firstLine: "From", secondLine: measurement.unit.rawValue)
        }
        .buttonStyle(BorderlessButtonStyle())
        .frame(width: 112)
        .overlay(
            Group {
                if isExpanded {
                    UnitDropdownMenuItems(units: measurement.allUnits) { unit in
                        isExpanded = false
                        onClick(unit)
                    }
                    .padding(.top, 8)
                }
            }
        )
    }

    private func onClick(_ unit: Measurement.Unit?) {
        if let unit = unit {
            measurement.unit = unit
        }
    }
}

struct ToDropdown: View {
    var equivalentUnits: [Measurement.Unit]
    @State private var isExpanded = false

    var body: some View {
        Button(action: { isExpanded.toggle() }) {
            UnitDropdownButtonColumn(firstLine: "To", secondLine: equivalentUnits.first?.rawValue ?? "")
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
    var units: [Measurement.Unit]
    var onSelect: (Measurement.Unit) -> Void

    var body: some View {
        ForEach(units, id: \.self) { unit in
            Button(action: { onSelect(unit) }) {
                Text(unit.rawValue)
            }
            .buttonStyle(BorderlessButtonStyle())
        }
    }
}
