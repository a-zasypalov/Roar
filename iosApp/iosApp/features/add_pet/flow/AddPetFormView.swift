import sharedLib
import SwiftUI

struct AddPetFormView: View {
    var petType: PetType
    var petAvatar: String
    let onCreatePetButtonClicked: (AddPetDataScreenContract.EventAddPetButtonClicked) -> Void

    @State private var name = ""
    @State private var chipNumber = ""
    @State private var breeds = ["No breed", "British Shorthair", "Siberian"]
    @State private var selectedBreed = "No breed"
    @State private var genders = ["Male", "Female"]
    @State private var selectedGender = "Male"
    @State private var petIsSterilized = false
    @State private var birthDate = Date.now

    var body: some View {
        VStack {
            Form {
                Section {
                    VStack {
                        Image(petAvatar)
                            .resizable()
                            .scaledToFit()
                            .frame(height: 120)
                    }.padding()
                        .background(.regularMaterial)
                        .clipShape(.buttonBorder)
                        .shadow(color: Color.black.opacity(0.2), radius: 4)
                }
                .padding(.top)
                .expandedH()
                .listRowBackground(Color.clear)
                .listRowSeparator(.hidden)
                
                Section {
                    TextField(text: $name) {
                        Text("Name")
                    }
                    
                    Picker("Breed", selection: $selectedBreed) {
                        ForEach(breeds, id: \.self) {
                            Text($0)
                        }
                    }
                    .pickerStyle(.navigationLink)
                    
                    Picker("Gender", selection: $selectedGender) {
                        ForEach(genders, id: \.self) {
                            Text($0)
                        }
                    }
                    .pickerStyle(.menu)
                    
                    DatePicker(selection: $birthDate, in: ...Date.now, displayedComponents: .date) {
                        Text("Birthday")
                    }.datePickerStyle(.compact)
                    
                    TextField(text: $chipNumber) {
                        Text("Chip number")
                    }
                    
                    Toggle("Pet is sterilized", isOn: $petIsSterilized)
                }
            }
        }
        .safeAreaInset(edge: .bottom) {
            Button(action: {
                let event = AddPetDataScreenContract.EventAddPetButtonClicked(
                    petType: petType.name,
                    avatar: petAvatar,
                    breed: selectedBreed,
                    name: name,
                    birthday: birthDate.toShared(),
                    gender: selectedGender,
                    chipNumber: chipNumber,
                    isSterilized: petIsSterilized
                )
                
                onCreatePetButtonClicked(event)
            }) {
                Text("Add pet")
                    .mainActionButtonStyle()
            }
            .padding()
            .buttonStyle(.borderedProminent)
        }
        .navigationLargeTitle(title: "Pet's card")
        .expanded()
        .screenBackground(color: Color(.systemGroupedBackground))
    }
}

#Preview {
    NavigationView {
        AddPetFormView(petType: .cat, petAvatar: "ic_cat_48") { _ in }
    }
}
