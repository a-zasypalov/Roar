import Foundation
import sharedLib
import GoogleSignIn
import Firebase

class HomeScreenState: ObservableObject {
    
    let viewModel = ViewModelProvider().homeScreenViewModel()
    let completeOnboardingUseCase = UseCaseProvider().completeOnboardingUseCase()
    
    @Published var state: HomeScreenContract.State
    @Published var presentAuthorization: Bool = false
    @Published var presentOnboarding: Bool = false
    

    init() {
        state = viewModel.setInitialState()
        viewModel.observeViewState { state in
            self.state = state
        }
        viewModel.observeEffect { effect in
            if effect == HomeScreenContract.EffectNavigationToUserRegistration() {
                if(self.completeOnboardingUseCase.onboardingIsComplete()) {
                    self.presentAuthorization = true
                } else {
                    self.presentOnboarding = true
                }
            }
        }
    }
    
    func checkUserRegistered() {
        viewModel.checkUserRegistered()
    }

    deinit {
        viewModel.dispose()
    }
    
}
