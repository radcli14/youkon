import SwiftUI
import shared

struct ContentView: View {
    @Environment(\.colorScheme) var colorScheme
    
	var body: some View {
        VStack(spacing: 0) {
            Header()
            QuickConvertCard()
            ProjectsCard()
        }
        .background(
            Image("Background")
                .brightness(colorScheme == .dark ? -0.3 : 0.3)
        )
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
