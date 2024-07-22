import AppTrackingTransparency
import sharedLib
import SwiftUI
import UIKit

struct ComposeView: UIViewControllerRepresentable
{
    func makeUIViewController(context: Context) -> UIViewController
    {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View
{
    var body: some View
    {
        ComposeView()
            .ignoresSafeArea(.all)
            .onAppear(perform: askPermissions)
    }

    private func askPermissions()
    {
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .badge, .sound])
        { success, error in
            if success
            {
                print("All set!")
            }
            else if let error
            {
                print(error.localizedDescription)
            }
            askPrivacyChoices()
        }
    }

    private func askPrivacyChoices()
    {
        DispatchQueue.main.asyncAfter(deadline: .now() + 1.0)
        {
            ATTrackingManager.requestTrackingAuthorization(completionHandler: {_ in })
        }
    }
}
