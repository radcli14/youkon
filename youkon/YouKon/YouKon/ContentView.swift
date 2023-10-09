import SwiftUI
import shared

struct ContentView: View {
    @Environment(\.colorScheme) var colorScheme
    
    @StateObject var contentViewController = ContentViewController()
    
	var body: some View {
        VStack(spacing: 0) {
            Header()
            QuickConvertCard()
            ProjectsCard(
                with: contentViewController.projectsCardController
            )
        }
        .accentColor(.indigo)
        .onTapGesture {
            hideKeyboard()
        }
        .toolbar {
            ToolbarItem(placement: .keyboard) {
                Button(action: hideKeyboard) {
                    Image(systemName: "chevron.down")
                }
            }
        }
        .background(
            Image("Background")
                .brightness(colorScheme == .dark ? -0.3 : 0.3)
        )
        .sheet(isPresented: $contentViewController.isEditingProject) {
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
        .onChange(of: contentViewController.isEditingProject) { _ in
            contentViewController.saveUserToJson()
        }
        .accentColor(.indigo)
        .environmentObject(contentViewController)
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
