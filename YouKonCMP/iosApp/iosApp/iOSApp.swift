import SwiftUI
//import Firebase

@main
struct iOSApp: App {
    /*
    init(){
      FirebaseApp.configure()
    }
     */
	var body: some Scene {
		WindowGroup {
            // To build the Compose Multiplatform version, uncomment this line
			ContentView()
            
            // To build the SwiftUI version, uncomment this line
            //YouKonView()
		}
	}
}
