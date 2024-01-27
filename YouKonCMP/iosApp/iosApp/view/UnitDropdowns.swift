import SwiftUI
import ComposeApp


struct UnitDropdown: View {
    @Binding var unit: YkUnit
    @Binding var availableUnits: [YkUnit]
    var headerText: String? = nil
    let onClick: (YkUnit) -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            if let headerText {
                Text(headerText)
                    .font(.caption2)
                    .padding(.leading, 8)
                    .foregroundColor(.accentColor)
            }
            menu
        }
    }
    
    @ViewBuilder
    var menu: some View {
        Menu {
            Picker(headerText ?? "From", selection: $unit) {
                menuItems
            }
        } label: {
            menuButton
        }
        .id(unit)
        .onChange(of: unit) { newUnit in
            onClick(newUnit)
        }
        .background(RoundedRectangle(cornerRadius: 8).foregroundColor(.accentColor))
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
            Text(unit.toString)
                .font(.body)
                .multilineTextAlignment(.leading)
            Spacer()
        }
        .padding(8)
    }
}
