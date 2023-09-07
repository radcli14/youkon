import SwiftUI
import shared


struct UnitDropdown: View {
    @Binding var unit: YkUnit
    @Binding var availableUnits: [YkUnit]
    var headerText = "From"
    let onClick: (YkUnit) -> Void

    var body: some View {
        Menu {
            Picker(headerText, selection: $unit) {
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
