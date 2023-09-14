import SwiftUI
import sharedLib

struct HomeScreenView: View {
    
    @ObservedObject var viewState: HomeScreenState
    
    init() {
        viewState = HomeScreenState()
        viewState.checkUserRegistered()
    }
    
    var body: some View {
        NavigationView {
            if(viewState.screenState.isLoading) {
                ProgressView()
            } else if let user = viewState.screenState.user {
                if(viewState.screenState.pets.isEmpty) {
                    HomeScreenNoPetsView(user: user)
                }
            } else {
                ProgressView()
            }
        }.popover(isPresented: $viewState.presentOnboarding) {
            OnboardingScreenView(authCallback: viewState.checkUserRegistered)
                .interactiveDismissDisabled()
        }
        .popover(isPresented: $viewState.presentAuthorization) {
            AuthScreenView(authCallback: viewState.checkUserRegistered)
                .interactiveDismissDisabled()
        }
    }
}
