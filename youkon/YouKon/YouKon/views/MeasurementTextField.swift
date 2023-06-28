import SwiftUI

struct MeasurementTextField: View {
    @Binding var measurement: Measurement
    @State private var text = ""

    var body: some View {
        TextField("", text: $text, onEditingChanged: { _ in }, onCommit: {
            if let value = Double(text) {
                measurement.value = value
            } else {
                measurement.value = 0.0
            }
        })
        .keyboardType(.numberPad)
        .textFieldStyle(RoundedBorderTextFieldStyle())
        .frame(width: 96)
    }
}
