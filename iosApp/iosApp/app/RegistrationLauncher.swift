import sharedLib
import Firebase
import GoogleSignIn
import GoogleSignInSwift

class RegistrationLauncherIos : sharedLib.RegistrationLauncher {
    func launcher(registrationSuccessfulCallback: @escaping (String, String) -> Void) -> () -> Void {
        return {
            guard let clientID = FirebaseApp.app()?.options.clientID else {
                fatalError("No client ID found in Firebase configuration")
            }

            let config = GIDConfiguration(clientID: clientID)
            GIDSignIn.sharedInstance.configuration = config

            guard let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
                  let window = windowScene.windows.first,
                  let rootViewController = window.rootViewController else {
                print("No ViewController for Google Sign-In")
                return
            }

            GIDSignIn.sharedInstance.signIn(withPresenting: rootViewController, completion: {userAuth, error in
                if let error = error {
                    print(error.localizedDescription)
                } else {
                    guard let userAuth else {
                        print("Authentication error - no user")
                        return
                    }
                    let user = userAuth.user
                    guard let idToken = user.idToken else {
                        print("Authentication error - no token")
                        return
                    }
                    let accessToken = user.accessToken
                    let credential = GoogleAuthProvider.credential(withIDToken: idToken.tokenString, accessToken: accessToken.tokenString)
                    Auth.auth().signIn(with: credential) { result, error in
                        guard let firebaseUser = result?.user else {
                            print("Authentication error: \(error?.localizedDescription ?? "unknown")")
                            return
                        }
                        print("User \(firebaseUser.uid) signed in with email \(firebaseUser.email ?? "unknown")")
                        registrationSuccessfulCallback(firebaseUser.displayName ?? "Username", firebaseUser.uid)
                    }
                }
            })
        }
    }

}
