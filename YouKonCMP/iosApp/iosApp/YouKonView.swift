import SwiftUI
import ComposeApp

/// The main app content includingm a `Header`, `QuickConvertCard`, and `ProjectsCard`.
/// Includes a background image, a sheet for editing a project, gestures, and toolbar itesms.
struct YouKonView: View {
    @Environment(\.colorScheme) var colorScheme
    
    @StateObject var contentViewController = ContentViewController()
    
	var body: some View {
        mainContentStack
            .background(
                backgroundImage
            )
            .sheet(isPresented: $contentViewController.isEditingProject) {
                projectEditingSheet
            }
            .onChange(of: contentViewController.isEditingProject) { _ in
                contentViewController.saveUserToJson()
            }
            .onTapGesture {
                hideKeyboard()
            }
            .toolbar {
                ToolbarItem(placement: .keyboard) {
                    hideKeyboardButton
                }
            }
            .accentColor(.indigo)
            .environmentObject(contentViewController)
	}
    
    /// The stack of a `Header`, `QuickConvertCard`, and `ProjectsCard`
    @ViewBuilder
    private var mainContentStack: some View {
        VStack(spacing: 0) {
            Header()
            QuickConvertCard()
            ProjectsCard(
                with: contentViewController.projectsCardController
            )
        }
    }
    
    /// The toolbar item to close the keyboard
    @ViewBuilder
    private var hideKeyboardButton: some View {
        Button(action: hideKeyboard) {
            Image(systemName: "chevron.down")
        }
    }
    
    /// The image of the mountains in the background, expanded to fill the screen
    @ViewBuilder
    private var backgroundImage: some View {
        GeometryReader { geo in
            Image("Background")
                .resizable()
                .aspectRatio(contentMode: .fill)
                .frame(width: geo.size.width, height: geo.size.height)
                .clipped()
                .brightness(colorScheme == .dark ? -0.3 : 0.3)
        }
        .edgesIgnoringSafeArea(.all)
    }
    
    /// When the user taps on the values in a `ProjectView` this opens the sheet for editing it
    @ViewBuilder
    private var projectEditingSheet: some View {
        VStack {
            if let project = contentViewController.project {
                ProjectView(project: project, editing: true)
                    .padding(16)
            }
            Spacer()
        }
        .accentColor(.indigo)
        .presentationDetents([.medium, .large])
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
