import SwiftUI
import shared

struct FromDropdown: View {
    @Binding var measurement: shared.Measurement
    let onClick: (MeasurementUnit) -> Void
    
    @State private var isExpanded = false
    @State var fieldValue: MeasurementUnit = .meters

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            Text("From")
                .font(.caption)
                .foregroundColor(.white)
                .padding(.leading, 8).padding(.top, 4)
            Picker("", selection: $fieldValue) {
                UnitDropdownMenuItems(
                    units: kotlinToSwiftArray(measurement.unit.allUnits)
                )
            }
            .onChange(of: fieldValue) { unit in
                isExpanded = false
                onClick(unit)
            }
        }
        .frame(width: 112)
        .background(RoundedRectangle(cornerRadius: 4).foregroundColor(.indigo))
        .tint(.white)
    }
}

struct ToDropdown: View {
    @Binding var equivalentUnits: [MeasurementUnit]
    let onClick: (MeasurementUnit) -> Void
    
    @State private var isExpanded = false
    @State var fieldValue: MeasurementUnit = .meters

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            Text("To")
                .font(.caption)
                .foregroundColor(.white)
                .padding(.leading, 8).padding(.top, 4)
            Picker("", selection: $fieldValue) {
                UnitDropdownMenuItems(
                    units: equivalentUnits
                )
            }
            .onChange(of: fieldValue) { unit in
                isExpanded = false
                onClick(unit)
            }
        }
        .frame(width: 112)
        .background(RoundedRectangle(cornerRadius: 4).foregroundColor(.indigo))
        .tint(.white)
    }
}

 
struct UnitDropdownMenuItems: View {
    var units: [MeasurementUnit]

    var body: some View {
        ForEach(units, id: \.self) { unit in
            Text(String(describing: unit)).foregroundColor(Color.purple)
        }
    }
}
