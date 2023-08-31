import SwiftUI
import sharedLib

struct HomeScreenView: View {
    
    @ObservedObject var state: HomeScreenState

    init() {
        state = HomeScreenState()
        state.checkUserRegistered()
    }
    
    var body: some View {
        ProgressView()
            .popover(isPresented: $state.presentOnboarding) {
                OnboardingScreenView(authCallback: { state.presentOnboarding = false })
                    .interactiveDismissDisabled()
            }
    }
}
