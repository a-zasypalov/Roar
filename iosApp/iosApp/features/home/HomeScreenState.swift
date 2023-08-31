import Foundation
import sharedLib

class HomeScreenState: ObservableObject {
    
    let viewModel = ViewModelProvider().homeScreenViewModel()
    let completeOnboardingUseCase = UseCaseProvider().completeOnboardingUseCase()
    
    @Published var state: HomeScreenContract.State
    @Published var presentOnboarding = false
    

    init() {
        state = viewModel.setInitialState()
        viewModel.observeViewState { state in
            self.state = state
        }
    }
    
    func checkUserRegistered() {
        viewModel.observeEffect { effect in
            if effect == HomeScreenContract.EffectNavigationToUserRegistration() {
                self.presentOnboarding = !self.completeOnboardingUseCase.onboardingIsComplete()
            }
        }
        viewModel.checkUserRegistered()
    }

    deinit {
        viewModel.dispose()
    }
    
}
