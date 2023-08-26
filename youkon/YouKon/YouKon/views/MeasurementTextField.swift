import SwiftUI
import shared

struct MeasurementTextField: View {
    @Binding var measurement: shared.Measurement
    let updateMeasurement: () -> Void
    
    @State private var text = ""

    var body: some View {
        
        // Create the binding so that the updateMeasurement closure will be called whenever the text changes
        let boundText = Binding<String>(get: {
            text
        }, set: { newText in
            if let value = Double(newText) {
                measurement.value = value
                text = newText
            } else if newText.isEmpty {
                measurement.value = 0.0
                text = newText
            }
            updateMeasurement()
        })
        
        // Create the numeric text field that uses the binding
        TextField("Value", text: boundText)
            .keyboardType(.decimalPad)
            .textFieldStyle(RoundedBorderTextFieldStyle())
            .frame(width: 96)
    }
}
