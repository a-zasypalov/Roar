import sharedLib
import SwiftUI

struct HomeScreenPetsListView: View {
    let user: User
    let pets: [PetWithInteractions]
    @State private var addPetPath: [AddPetFlowScreens] = []

    var body: some View {
        NavigationStack(path: $addPetPath) {
            Spacer()
            Text("Hey, \(user.name). You have \(pets.count) pet!")
                .padding()
            ForEach(pets, id: \.id) { pet in
                Text(pet.name)
            }
            Spacer()

            AddPetFlowView(addPetPath: $addPetPath)
        }
    }
}
