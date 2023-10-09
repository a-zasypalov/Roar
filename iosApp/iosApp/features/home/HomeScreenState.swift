import Firebase
import Foundation
import GoogleSignIn
import sharedLib

class HomeScreenState: ObservableObject {
    let viewModel = ViewModelProvider().homeScreenViewModel()
    let completeOnboardingUseCase = UseCaseProvider().completeOnboardingUseCase()

    @Published var screenState: HomeScreenContract.State
    @Published var presentAuthorization: Bool = false
    @Published var presentOnboarding: Bool = false

    init() {
        screenState = viewModel.setInitialState()
        viewModel.observeViewState { state in
            self.screenState = state
        }
        viewModel.observeEffect { effect in
            if effect == HomeScreenContract.EffectNavigationToUserRegistration() {
                if self.completeOnboardingUseCase.onboardingIsComplete() {
                    self.presentAuthorization = true
                } else {
                    self.presentOnboarding = true
                }
            }
        }
    }

    func checkUserRegistered() {
        presentAuthorization = false
        presentOnboarding = false
        viewModel.checkUserRegistered()
    }

    deinit {
        viewModel.dispose()
    }
}
