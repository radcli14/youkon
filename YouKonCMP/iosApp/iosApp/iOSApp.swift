import SwiftUI

@main
struct iOSApp: App {
	var body: some Scene {
		WindowGroup {
            // To build the Compose Multiplatform version, uncomment this line
			//ContentView()
            
            // To build the SwiftUI version, uncomment this line
            YouKonView()
		}
	}
}
