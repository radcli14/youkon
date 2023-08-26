import SwiftUI
import shared

// TODO: don't provide a measurement as an argument, handle that at a higher level, just provide the list of units
struct FromDropdown: View {
    @Binding var measurement: shared.Measurement
    let onClick: (MeasurementUnit) -> Void
    
    @State var unit: MeasurementUnit = .meters

    var body: some View {
        Menu {
            Picker("From", selection: $unit) {
                UnitDropdownMenuItems(units: kotlinToSwiftArray(measurement.unit.allUnits))
            }
        } label: {
            UnitDropdownMenuButton(headerText: "From", unit: $unit)
        }
        .id(unit)
        .onChange(of: unit) { newUnit in
            onClick(newUnit)
        }
        .frame(width: 112)
        .background(RoundedRectangle(cornerRadius: 4).foregroundColor(.indigo))
        .tint(.white)
    }
}

struct ToDropdown: View {
    @Binding var equivalentUnits: [MeasurementUnit]
    let onClick: (MeasurementUnit) -> Void
    
    @State var unit: MeasurementUnit = .meters

    var body: some View {
        Menu {
            Picker("To", selection: $unit) {
                UnitDropdownMenuItems(units: equivalentUnits)
            }
        } label: {
            UnitDropdownMenuButton(headerText: "To", unit: $unit)
        }
        .id(unit)
        .onChange(of: unit) { newUnit in
            onClick(newUnit)
        }
        .frame(width: 112)
        .background(RoundedRectangle(cornerRadius: 4).foregroundColor(.indigo))
        .tint(.white)
    }
}


struct UnitDropdownMenuButton: View {
    let headerText: String
    @Binding var unit: MeasurementUnit
    
    var body: some View {
        HStack {
            VStack(alignment: .leading) {
                Text(headerText)
                    .font(.caption2)
                Text(unit.toString)
                    .font(.caption)
            }
            Spacer()
        }
        .padding(4)
    }
}

 
struct UnitDropdownMenuItems: View {
    var units: [MeasurementUnit]

    var body: some View {
        ForEach(units, id: \.self) { unit in
            Text(unit.toString)
        }
    }
}

