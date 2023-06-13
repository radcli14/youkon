import SwiftUI
import shared

struct ContentView: View {
	//let greet = Greeting().greet()

	var body: some View {
        VStack {
            HStack {
                Image("Icon")
                    .resizable()
                    .frame(width: 48, height: 48)
                    
                Text("YouKon")
                    .font(.largeTitle)
                    .fontWeight(.black)
            }
            Spacer()
        }
        .background(
            Image("Background")
                        //.resizable()
        )
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
