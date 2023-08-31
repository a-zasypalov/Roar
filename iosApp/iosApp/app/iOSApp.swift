import SwiftUI
import sharedLib
import Firebase

@main
struct iOSApp: App {
    
    init() {
        KoinKt.doInitKoin()
        FirebaseApp.configure()
    }
    
	var body: some Scene {
		WindowGroup {
            // TODO: Naigation wrapper
            ContentView()
		}
	}
}
