import sharedLib
import FirebaseAuth

class SignOutExecutorImpl: SignOutExecutor {
    func signOut() {
        do {
            try Auth.auth().signOut()
        } catch {
            print(error.localizedDescription)
        }
    }
}
