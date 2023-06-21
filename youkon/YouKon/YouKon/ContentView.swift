import SwiftUI
import shared

struct ContentView: View {

	var body: some View {
        VStack(spacing: 24) {
            Header()
            //Text(UserData().name)
            QuickConvertCard()
            ProjectsCard()
            Spacer()
        }
        .background(
            Image("Background")
        )
        .padding(12)
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
