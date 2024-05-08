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
    @Environment(\.scenePhase) private var scenePhase

    let provider = sharedLib.KoinProvider()
    var synchronisationScheduler: SynchronisationSchedulerIOS? = nil
    @ObservedObject var themeChanger = ThemeChangerIOS()


    init() {
        synchronisationScheduler = SynchronisationSchedulerIOS(provider: provider)
        guard let synchronisationScheduler else { fatalError("Init error") }


        let appDeclaration = sharedLib.iOSAppDeclaration(
            registrationLauncher: RegistrationLauncherIos(),
            synchronisationApi: SynchronisationApiIOS(provider: provider),
            synchronisationScheduler: synchronisationScheduler,
            themeChanger: themeChanger
        )

        KoinKt.doInitKoin(appDeclaration: appDeclaration)
    }

	var body: some Scene {
		WindowGroup {
            ContentView()
                .id(themeChanger.key)
                .onChange(of: scenePhase) { newPhase in
                    if newPhase == .background {
                        synchronisationScheduler?.scheduleSynchronisation(dispatchTime: DispatchTime.now())
                    }
                }
		}
	}
}
