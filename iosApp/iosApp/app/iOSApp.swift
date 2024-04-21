import SwiftUI
import sharedLib

@main
struct iOSApp: App {

    init() {
        KoinKt.doInitKoin(registrationLauncher: RegistrationLauncherIos())
    }
    
	var body: some Scene {
		WindowGroup {
          ContentView()
		}
	}
}

class RegistrationLauncherIos : sharedLib.RegistrationLauncher {

    func launcher(registrationSuccessfulCallback: (String, String) -> Void) -> () -> Void {
        return {}
    }

}
