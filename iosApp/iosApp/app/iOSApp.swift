import SwiftUI
import sharedLib
import Firebase
import GoogleSignIn

class AppDelegate: NSObject, UIApplicationDelegate {
  
    func application(_ application: UIApplication, 
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool
    {
        FirebaseApp.configure()
        return true
    }

    func application(_ app: UIApplication,
                     open url: URL,
                     options: [UIApplication.OpenURLOptionsKey: Any] = [:]) -> Bool 
    {
        return GIDSignIn.sharedInstance.handle(url)
    }
}

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    init() {
        KoinKt.doInitKoin(registrationLauncher: RegistrationLauncherIos())
    }
    
	var body: some Scene {
		WindowGroup {
          ContentView()
		}
	}
}
