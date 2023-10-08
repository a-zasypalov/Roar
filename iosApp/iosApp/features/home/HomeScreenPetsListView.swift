import sharedLib
import SwiftUI

struct HomeScreenPetsListView: View {
    let user: User
    let pets: [PetWithInteractions]
    @Binding var navStack: [MainNavStackScreens]
    
    @State private var petName = ""
    @State private var petType = PetType.cat
    @State private var avatar = "ic_cat"

    var body: some View {
        List(pets, id: \.id) { pet in
            Section {
                VStack(alignment: .leading) {
                    HStack(alignment: .center) {
                        Image(pet.avatar)
                            .resizable()
                            .scaledToFit()
                            .frame(maxHeight: 64)
                            .padding(.trailing, 6)

                        VStack(alignment: .leading) {
                            Text(pet.name)
                                .font(.title)
                                .fontWeight(.bold)

                            Text(pet.birthday.toSwift().formatted(date: .long, time: .omitted))
                                .font(.body)
                        }
                        .fixedSize()
                    }

                    Divider()
                        .padding(.top, 8)

                    RemindersListRowView(isCompleted: false)
                    Divider()
                    RemindersListRowView(isCompleted: false)
                    Divider()
                    RemindersListRowView(isCompleted: false)
                }
                .padding(.vertical)
            }
            .onTapGesture {
                navStack.append(.petScreen(pet: pet))
            }
        }
        .listStyle(.insetGrouped)
        .toolbar {
            ToolbarItemGroup(placement: .primaryAction) {
                AddPetFlowView(navStack: $navStack)
                Button(action: {}) {
                    Image(systemName: "person.circle")
                }
            }
        }
        .navigationLargeTitle(title: "Roar")
        .screenBackground(color: Color(.systemGroupedBackground))
        .navigationDestination(for: MainNavStackScreens.self) { screen in
            switch screen {
            case .petScreen(let pet): PetScreenView(navStack: $navStack, pet: pet)
            case .addPetFlowType: AddPetTypeView { petType in
                    self.petType = petType
                    navStack.append(.addPetFlowAvatar)
                }
            case .addPetFlowAvatar: AddPetAvatarView(petType: $petType) { avatar in
                    self.avatar = avatar
                    navStack.append(.addPetFlowForm)
                }
            case .addPetFlowForm: AddPetFormView(petType: $petType, petAvatar: $avatar) { name in
                    self.petName = name
                    navStack.append(.addPetFlowSetup)
                }
            case .addPetFlowSetup: AddPetSetupView(petName: $petName, petAvatar: $avatar) {
                    navStack.removeAll()
                }
            }
        }
    }
}

struct RemindersListRowView: View {
    @State var isCompleted: Bool

    var body: some View {
        HStack(alignment: .center) {
            Image(systemName: isCompleted
                ? "largecircle.fill.circle"
                : "circle")
                .imageScale(.large)
                .foregroundColor(.accentColor)
            Text("Reminder")
                .font(.body)
                .expandedH()
        }
        .padding(.vertical, 8)
        .fixedSize()
        .onTapGesture {
            isCompleted.toggle()
        }
    }
}

#Preview {
    NavigationView {
        HomeScreenPetsListView(
            user: User(id: "", name: "Tester"),
            pets: [
                PetWithInteractions(id: "", petType: .cat, breed: "No breed", name: "Test cat", avatar: "ic_cat", userId: "123", birthday: Kotlinx_datetimeLocalDate(year: 2020, month: Kotlinx_datetimeMonth.august, dayOfMonth: 1), isSterilized: false, gender: .female, chipNumber: "", dateCreated: Kotlinx_datetimeLocalDate(year: 2020, month: Kotlinx_datetimeMonth.august, dayOfMonth: 1), interactions: [:]),
                PetWithInteractions(id: "", petType: .cat, breed: "No breed", name: "Test cat", avatar: "ic_cat", userId: "123", birthday: Kotlinx_datetimeLocalDate(year: 2020, month: Kotlinx_datetimeMonth.august, dayOfMonth: 1), isSterilized: false, gender: .female, chipNumber: "", dateCreated: Kotlinx_datetimeLocalDate(year: 2020, month: Kotlinx_datetimeMonth.august, dayOfMonth: 1), interactions: [:]),
                PetWithInteractions(id: "", petType: .cat, breed: "No breed", name: "Test cat", avatar: "ic_cat", userId: "123", birthday: Kotlinx_datetimeLocalDate(year: 2020, month: Kotlinx_datetimeMonth.august, dayOfMonth: 1), isSterilized: false, gender: .female, chipNumber: "", dateCreated: Kotlinx_datetimeLocalDate(year: 2020, month: Kotlinx_datetimeMonth.august, dayOfMonth: 1), interactions: [:])
            ],
            navStack: .constant([])
        )
    }
}
