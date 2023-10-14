import sharedLib
import SwiftUI

struct HomeScreenView: View {
    @ObservedObject var viewState: HomeScreenState
    @Binding var navStack: NavigationPath

    init(navStack: Binding<NavigationPath>) {
        _navStack = navStack
        viewState = HomeScreenState()
    }

    var body: some View {
        Group {
            if viewState.screenState.isLoading {
                ProgressView()
            } else if let user = viewState.screenState.user {
                if viewState.screenState.pets.isEmpty {
                    HomeScreenNoPetsView(user: user, navStack: $navStack)
                } else {
                    HomeScreenPetsListView(user: user, pets: viewState.screenState.pets, navStack: $navStack)
                        .navigationDestination(for: PetScreen.self) { link in
                            PetScreenView(pet: link.pet, navStack: $navStack)
                        }
                }
            } else {
                ProgressView()
            }
        }
        .onAppear {
            viewState.checkUserRegistered()
        }
        .popover(isPresented: $viewState.presentOnboarding) {
            OnboardingScreenView(authCallback: viewState.checkUserRegistered)
                .interactiveDismissDisabled()
        }
        .popover(isPresented: $viewState.presentAuthorization) {
            AuthScreenView(authCallback: viewState.checkUserRegistered)
                .interactiveDismissDisabled()
        }
    }
}
