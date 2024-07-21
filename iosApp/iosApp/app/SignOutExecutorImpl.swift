import sharedLib
import FirebaseAuth
import UserNotifications

class SignOutExecutorImpl: SignOutExecutor {
    func signOut() {
        do {
            UNUserNotificationCenter.current().removeAllPendingNotificationRequests()
            try Auth.auth().signOut()
        } catch {
            print(error.localizedDescription)
        }
    }
}
