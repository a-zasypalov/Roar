import Foundation
import sharedLib
import GoogleSignIn
import Firebase

class AuthScreenState: ObservableObject {
    
    let viewModel = ViewModelProvider().registerUserViewModel()
    
    @Published private var state: RegisterUserScreenContract.State
    
    init(authCallback: @escaping () -> Void) {
        viewModel.observeEffect { effect in
            if(effect is RegisterUserScreenContract.EffectNavigationToPetAdding) {
                authCallback()
            }
        }
        
        state = viewModel.setInitialState()
        viewModel.observeViewState { state in
            self.state = state
        }
    }
    
    func signIn() {
        if GIDSignIn.sharedInstance.hasPreviousSignIn() {
            GIDSignIn.sharedInstance.restorePreviousSignIn { [unowned self] user, error in
                authenticateUser(for: user, with: error)
            }
        } else {
            guard let clientID = FirebaseApp.app()?.options.clientID else { return }
                    
            let config = GIDConfiguration(clientID: clientID)
            GIDSignIn.sharedInstance.configuration = config
            
            guard let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene else { return }
            guard let rootViewController = windowScene.windows.first?.rootViewController else { return }
            
            GIDSignIn.sharedInstance.signIn(withPresenting: rootViewController) { [unowned self] user, error in
                authenticateUser(for: user?.user, with: error)
            }
        }
    }
    
    private func authenticateUser(for user: GIDGoogleUser?, with error: Error?) {
        if let error = error {
            print(error.localizedDescription)
            return
        }
        
        guard let idToken = user?.idToken?.tokenString else { return }
        guard let accessToken = user?.accessToken.tokenString else { return }
        
        let credential = GoogleAuthProvider.credential(withIDToken: idToken, accessToken: accessToken)
        
        Auth.auth().signIn(with: credential) { (authData, error) in
            if let error = error {
                print(error.localizedDescription)
            } else {
                guard let user = authData?.user else { return }
                
                self.viewModel.setEvent(
                    event: RegisterUserScreenContract.EventRegistrationSuccessful(
                        name: user.displayName ?? "User", //TODO: Localize default username
                        userId: user.uid
                    )
                )
            }
        }
    }
    
    deinit {
        viewModel.dispose()
    }
    
}
