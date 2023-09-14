import SwiftUI
import sharedLib

struct HomeScreenView: View {
    
    @ObservedObject var state: HomeScreenState
    
    init() {
        state = HomeScreenState()
        state.checkUserRegistered()
    }
    
    var body: some View {
        HStack {
            if(state.state.isLoading) {
                ProgressView()
            } else if let user = state.state.user {
                Text("Hey, \(user.name).\nYou have \(state.state.pets.count) pets!")
            } else {
                ProgressView()
            }
        }.popover(isPresented: $state.presentOnboarding) {
            OnboardingScreenView(authCallback: {
                state.presentOnboarding = false
                state.checkUserRegistered()
            }).interactiveDismissDisabled()
        }
        .popover(isPresented: $state.presentAuthorization) {
            AuthScreenView(authCallback: {
                state.presentAuthorization = false
                state.checkUserRegistered()
            }).interactiveDismissDisabled()
        }
    }
}
