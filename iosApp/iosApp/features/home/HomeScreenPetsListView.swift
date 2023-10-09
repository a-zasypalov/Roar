import sharedLib
import SwiftUI

struct HomeScreenPetsListView: View {
    let user: User
    let pets: [PetWithInteractions]
    @Binding var navStack: NavigationPath

    @State var userProfileOpen = false

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
                                        
                    ForEach(pet.interactions.values.flatMap { i in i }, id: \.self) { key in
                        RemindersListRowView(isCompleted: false, name: "Reminder")
//                        if key != pet.interactions.last.key {
                            Divider()
//                        }
                    }
                }
                .padding(.vertical)
            }
            .onTapGesture {
                navStack.append(PetScreen(pet: pet))
            }
        }
        .listStyle(.insetGrouped)
        .toolbar {
            ToolbarItemGroup(placement: .primaryAction) {
                AddPetFlowView(navStack: $navStack) {
                    Image(systemName: "plus")
                }

                Button(action: { userProfileOpen.toggle() }) {
                    Image(systemName: "person.circle")
                }
            }
        }
        .navigationLargeTitle(title: "Roar")
        .screenBackground(color: Color(.systemGroupedBackground))
        .popover(isPresented: $userProfileOpen) {}
    }
}

struct RemindersListRowView: View {
    @State var isCompleted: Bool
    let name: String

    var body: some View {
        HStack(alignment: .center) {
            Image(systemName: isCompleted
                ? "largecircle.fill.circle"
                : "circle")
                .imageScale(.large)
                .foregroundColor(.accentColor)
            Text(name)
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
            navStack: .constant(NavigationPath())
        )
    }
}
