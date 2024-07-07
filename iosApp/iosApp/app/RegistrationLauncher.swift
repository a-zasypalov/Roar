import sharedLib
import Firebase
import GoogleSignIn
import GoogleSignInSwift
import CryptoKit
import AuthenticationServices

class RegistrationLauncherIos : sharedLib.RegistrationLauncherApple {
    private var coordinator = SignInWithAppleCoordinator()

    func launcherApple(registrationSuccessfulCallback: @escaping (String, String) -> Void) -> () -> Void {
        return {
            self.coordinator.registrationSuccessfulCallback = { uid, email in
                registrationSuccessfulCallback(uid, email)
            }
            self.coordinator.handleSignInWithApple()
        }
    }

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

class SignInWithAppleCoordinator: NSObject, ASAuthorizationControllerDelegate, ASAuthorizationControllerPresentationContextProviding {

    var registrationSuccessfulCallback: ((String, String) -> Void)?

    func handleSignInWithApple() {
        let request = ASAuthorizationAppleIDProvider().createRequest()
        request.requestedScopes = [.fullName, .email]

        let authorizationController = ASAuthorizationController(authorizationRequests: [request])
        authorizationController.delegate = self
        authorizationController.presentationContextProvider = self
        authorizationController.performRequests()
    }

    func authorizationController(controller: ASAuthorizationController, didCompleteWithAuthorization authorization: ASAuthorization) {
        if let appleIDCredential = authorization.credential as? ASAuthorizationAppleIDCredential {
            guard let appleIDToken = appleIDCredential.identityToken else {
                print("Unable to fetch identity token")
                return
            }

            guard let idTokenString = String(data: appleIDToken, encoding: .utf8) else {
                print("Unable to serialize token string from data: \(appleIDToken.debugDescription)")
                return
            }

            let credential = OAuthProvider.credential(withProviderID: "apple.com", idToken: idTokenString, rawNonce: nil)

            Auth.auth().signIn(with: credential) { (authResult, error) in
                if let error = error {
                    print("Error authenticating: \(error.localizedDescription)")
                    return
                }

                // User is signed in to Firebase with Apple.
                if let user = authResult?.user {
                    let uid = user.uid
                    let email = user.email ?? "No Email"
                    self.registrationSuccessfulCallback?(uid, email)
                }
            }
        }
    }

    func authorizationController(controller: ASAuthorizationController, didCompleteWithError error: Error) {
        print("Sign in with Apple errored: \(error.localizedDescription)")
    }

    func presentationAnchor(for controller: ASAuthorizationController) -> ASPresentationAnchor {
        return UIApplication.shared.windows.first!
    }
}
