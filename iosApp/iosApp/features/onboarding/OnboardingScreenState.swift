import Foundation
import sharedLib

class OnboardingScreenState: ObservableObject {
    
    let viewModel = ViewModelProvider().onboardingViewModel()
    
    @Published var state: OnboardingScreenContract.State

    init() {
        viewModel.observeEffect { effect in
            
        }

        state = viewModel.setInitialState()
        viewModel.observeViewState { state in
            self.state = state
        }
    }
    
    func completeOnboarding() {
        viewModel.setEvent(event: OnboardingScreenContract.EventOnboardingComplete())
    }

    deinit {
        viewModel.dispose()
    }
    
}
