import sharedLib
import SwiftUI

@main
struct iOSApp: App
{
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    @Environment(\.scenePhase) private var scenePhase

    let provider = sharedLib.KoinProvider()
    var synchronisationScheduler: SynchronisationSchedulerIOS? = nil
    @ObservedObject var themeChanger = ThemeChangerIOS()

    init()
    {
        synchronisationScheduler = SynchronisationSchedulerIOS(provider: provider)
        guard let synchronisationScheduler else { fatalError("Init error") }

        let appDeclaration = sharedLib.iOSAppDeclaration(
            registrationLauncher: RegistrationLauncherIos(),
            synchronisationApi: SynchronisationApiIOS(provider: provider),
            synchronisationScheduler: synchronisationScheduler,
            themeChanger: themeChanger,
            notificationScheduler: NotificationSchedulerIOS(provider: provider),
            signOutExecutor: SignOutExecutorImpl(),
            emailSender: EmailSenderImpl(), 
            deleteRemoteAccountExecutor: DeleteRemoteAccountExecutorImpl(provider: provider)
        )

        KoinKt.doInitKoin(appDeclaration: appDeclaration)
    }

    var body: some Scene
    {
        WindowGroup
        {
            ContentView()
                .id(themeChanger.key)
                .onChange(of: scenePhase)
                { newPhase in
                    if newPhase == .background
                    {
                        synchronisationScheduler?.scheduleSynchronisation(dispatchTime: DispatchTime.now())
                    }
                }
        }
    }
}
