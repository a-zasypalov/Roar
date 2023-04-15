import SwiftUI
import sharedLib

//struct ContentView: UIViewControllerRepresentable {
//    func makeUIViewController(context: Context) -> UIViewController {
//        Main_iosKt.MainViewController()
//    }
//
//    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
//}

struct ContentView: View {
    let vm = HomeScreenViewModel()
    
    var body: some View {
        Text("Hey")
    }
    
    func testF() {
        print(vm)
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
            .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
	}
}
