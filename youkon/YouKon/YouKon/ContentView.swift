import SwiftUI
import shared

struct ContentView: View {

	var body: some View {
        VStack {
            HStack {
                Image("Icon")
                    .resizable()
                    .frame(width: 48, height: 48)
                    
                Text("YouKon")
                    .font(.largeTitle)
                    .fontWeight(.heavy)
                    .foregroundColor(Color.white)
            }
            Text(UserData().name)
            Spacer()
        }
        .background(
            Image("Background")
        )
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
