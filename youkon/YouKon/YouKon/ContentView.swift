import SwiftUI
import shared

struct ContentView: View {
    @Environment(\.colorScheme) var colorScheme
    
	var body: some View {
        VStack(spacing: 24) {
            Header()
            QuickConvertCard()
            ProjectsCard()
            //Spacer()
        }
        .background(
            Image("Background")
                .brightness(colorScheme == .dark ? -0.3 : 0.3)
        )
        .padding(12)
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
