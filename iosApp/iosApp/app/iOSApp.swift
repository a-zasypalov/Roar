import SwiftUI
import sharedLib

@main
struct iOSApp: App {
    
    init() {
        KoinKt.doInitKoin()
    }
    
	var body: some Scene {
		WindowGroup {
          ContentView()
		}
	}
}
