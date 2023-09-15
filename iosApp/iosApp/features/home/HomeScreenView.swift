import sharedLib
import SwiftUI

struct HomeScreenView: View {
    @ObservedObject var viewState: HomeScreenState
    
    init() {
        viewState = HomeScreenState()
        viewState.checkUserRegistered()
    }
    
    var body: some View {
        NavigationView {
            HomeScreenNoPetsView(user: User(id: "", name: "Tester"))
//            if viewState.screenState.isLoading {
//                ProgressView()
//            } else if let user = viewState.screenState.user {
//                if viewState.screenState.pets.isEmpty {
//                    HomeScreenNoPetsView(user: user)
//                } else {
//                    HomeScreenPetsListView(user: user, pets: viewState.screenState.pets)
//                }
//            } else {
//                ProgressView()
//            }
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
