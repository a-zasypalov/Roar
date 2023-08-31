import Foundation
import sharedLib
import GoogleSignIn
import Firebase

class AuthScreenState: ObservableObject {
    
    let viewModel = ViewModelProvider().registerUserViewModel()
    
    @Published private var state: RegisterUserScreenContract.State
    let authCallback: () -> Void
    
    init(authCallback: @escaping () -> Void) {
        self.authCallback = authCallback
        
        viewModel.observeEffect { effect in
            
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
        
        Auth.auth().signIn(with: credential) { (_, error) in
            if let error = error {
                print(error.localizedDescription)
            } else {
                self.authCallback()
            }
        }
    }
    
    deinit {
        viewModel.dispose()
    }
    
}
