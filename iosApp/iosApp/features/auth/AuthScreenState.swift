import Foundation
import sharedLib

class AuthScreenState: ObservableObject {
    
    let viewModel = ViewModelProvider().registerUserViewModel()
    
    @Published private var state: RegisterUserScreenContract.State

    init() {
        viewModel.observeEffect { effect in

        }

        state = viewModel.setInitialState()
        viewModel.observeViewState { state in
            self.state = state
        }
    }

    deinit {
        viewModel.dispose()
    }
    
}
