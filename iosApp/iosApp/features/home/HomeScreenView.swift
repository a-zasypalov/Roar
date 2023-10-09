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
            //            previewWithPets()
            //            previewWithoutPets()
            if viewState.screenState.isLoading {
                ProgressView()
            } else if let user = viewState.screenState.user {
                if viewState.screenState.pets.isEmpty {
                    HomeScreenNoPetsView(user: user, navStack: $navStack)
                } else {
                    HomeScreenPetsListView(user: user, pets: viewState.screenState.pets, navStack: $navStack)
                        .navigationDestination(for: PetScreen.self) { link in
                            PetScreenView(pet: link.pet)
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

extension HomeScreenView {
    func previewWithPets() -> HomeScreenPetsListView {
        return HomeScreenPetsListView(user: User(id: "", name: "Tester"), pets: [
            PetWithInteractions(id: "11", petType: .cat, breed: "No breed", name: "Test cat1", avatar: "ic_cat_3", userId: "123", birthday: Kotlinx_datetimeLocalDate(year: 2020, month: Kotlinx_datetimeMonth.august, dayOfMonth: 1), isSterilized: false, gender: .female, chipNumber: "11", dateCreated: Kotlinx_datetimeLocalDate(year: 2020, month: Kotlinx_datetimeMonth.august, dayOfMonth: 1), interactions: [:]),
            PetWithInteractions(id: "12", petType: .cat, breed: "No breed", name: "Test cat2", avatar: "ic_cat_35", userId: "123", birthday: Kotlinx_datetimeLocalDate(year: 2020, month: Kotlinx_datetimeMonth.august, dayOfMonth: 1), isSterilized: false, gender: .female, chipNumber: "12", dateCreated: Kotlinx_datetimeLocalDate(year: 2020, month: Kotlinx_datetimeMonth.august, dayOfMonth: 1), interactions: [:]),
            PetWithInteractions(id: "13", petType: .cat, breed: "No breed", name: "Test cat3", avatar: "ic_cat_48", userId: "123", birthday: Kotlinx_datetimeLocalDate(year: 2020, month: Kotlinx_datetimeMonth.august, dayOfMonth: 1), isSterilized: false, gender: .female, chipNumber: "13", dateCreated: Kotlinx_datetimeLocalDate(year: 2020, month: Kotlinx_datetimeMonth.august, dayOfMonth: 1), interactions: [:])
        ], navStack: $navStack)
    }

    func previewWithoutPets() -> HomeScreenNoPetsView {
        return HomeScreenNoPetsView(user: User(id: "", name: "Tester"), navStack: $navStack)
    }
}
