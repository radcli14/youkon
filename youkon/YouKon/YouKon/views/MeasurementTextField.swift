import SwiftUI
import shared


/// A custom styled `TextField` where the text tracks with the value of a `measurement`
/// - Parameter measurement: The `measurement` object which will have its value bound to the text
/// - Parameter updateMeasurement: An escaping closure that will notify the upstream view to update its data
struct MeasurementTextField: View {
    @Binding var measurement: YkMeasurement
    let updateMeasurement: () -> Void
    
    @State private var text: String // = ""

    /// Initialize with a measurement and an update method, will make the `text` state be equal to the `measurement.value` in string format
    init(measurement: Binding<YkMeasurement>, updateMeasurement: @escaping () -> Void) {
        self._measurement = measurement
        self.updateMeasurement = updateMeasurement
        _text = State(initialValue: "\(measurement.wrappedValue.value)")
    }
    
    var body: some View {
        // Create the binding so that the value is assured to be a valid number,
        // and the updateMeasurement closure will be called whenever the text changes
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
            //.textFieldStyle(RoundedBorderTextFieldStyle())
            .padding(EdgeInsets(top: 8, leading: 8, bottom: 8, trailing: 8))
            .background(Color.gray.opacity(0.2))
            .cornerRadius(8)
            .fontWeight(.bold)
    }
}
