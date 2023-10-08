import sharedLib
import SwiftUI

struct HomeScreenView: View {
    @ObservedObject var viewState: HomeScreenState
    @State var navStack: [MainNavStackScreens] = []

    init() {
        viewState = HomeScreenState()
        viewState.checkUserRegistered()
    }

    var body: some View {
        NavigationStack(path: $navStack) {
//            HomeScreenNoPetsView(user: User(id: "", name: "Tester"), navStack: $navStack)
            HomeScreenPetsListView(user: User(id: "", name: "Tester"), pets: [
                PetWithInteractions(id: "", petType: .cat, breed: "No breed", name: "Test cat", avatar: "ic_cat", userId: "123", birthday: Kotlinx_datetimeLocalDate(year: 2020, month: Kotlinx_datetimeMonth.august, dayOfMonth: 1), isSterilized: false, gender: .female, chipNumber: "", dateCreated: Kotlinx_datetimeLocalDate(year: 2020, month: Kotlinx_datetimeMonth.august, dayOfMonth: 1), interactions: [:]),
                PetWithInteractions(id: "", petType: .cat, breed: "No breed", name: "Test cat", avatar: "ic_cat", userId: "123", birthday: Kotlinx_datetimeLocalDate(year: 2020, month: Kotlinx_datetimeMonth.august, dayOfMonth: 1), isSterilized: false, gender: .female, chipNumber: "", dateCreated: Kotlinx_datetimeLocalDate(year: 2020, month: Kotlinx_datetimeMonth.august, dayOfMonth: 1), interactions: [:]),
                PetWithInteractions(id: "", petType: .cat, breed: "No breed", name: "Test cat", avatar: "ic_cat", userId: "123", birthday: Kotlinx_datetimeLocalDate(year: 2020, month: Kotlinx_datetimeMonth.august, dayOfMonth: 1), isSterilized: false, gender: .female, chipNumber: "", dateCreated: Kotlinx_datetimeLocalDate(year: 2020, month: Kotlinx_datetimeMonth.august, dayOfMonth: 1), interactions: [:])
            ], navStack: $navStack)
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
