import SwiftUI
import shared


struct UnitDropdown: View {
    @Binding var availableUnits: [MeasurementUnit]
    var headerText = "From"
    let onClick: (MeasurementUnit) -> Void
    
    @State var unit: MeasurementUnit = .meters

    var body: some View {
        Menu {
            Picker("To", selection: $unit) {
                menuItems
            }
        } label: {
            menuButton
        }
        .id(unit)
        .onChange(of: unit) { newUnit in
            onClick(newUnit)
        }
        .frame(width: 112)
        .background(RoundedRectangle(cornerRadius: 4).foregroundColor(.indigo))
        .tint(.white)
    }
    
    @ViewBuilder
    var menuItems: some View {
        ForEach(availableUnits, id: \.self) { unit in
            Text(unit.toString)
        }
    }
    
    @ViewBuilder
    var menuButton: some View {
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
