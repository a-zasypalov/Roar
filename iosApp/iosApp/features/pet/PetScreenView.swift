import sharedLib
import SwiftUI

struct PetScreenView: View {
    @ObservedObject var viewState: PetScreenState

    init(pet: PetWithInteractions, navStack: Binding<NavigationPath>) {
        viewState = PetScreenState(pet: pet, navStack: navStack)
    }

    var body: some View {
        let genderLabel = if viewState.petPreview.gender == Gender.male {
            "Male"
        } else {
            "Female"
        }
        
        let sterilizedLabel = if viewState.petPreview.isSterilized {
            "sterilized"
        } else {
            "not sterilized"
        }
        
        ZStack(alignment: .bottomTrailing) {
            ScrollView {
                VStack(alignment: .leading) {
                    HStack(alignment: .center) {
                        Image(viewState.petPreview.avatar)
                            .resizable()
                            .scaledToFit()
                            .frame(height: 80)
                            .padding(.vertical, 24.0)
                            .padding(.leading, 20.0)
                        
                        VStack(alignment: .center, spacing: 8.0) {
                            Image(systemName: "birthday.cake.fill")
                                .resizable()
                                .scaledToFit()
                                .frame(height: 20)
                            
                            Image(systemName: "wallet.pass.fill")
                                .resizable()
                                .scaledToFit()
                                .frame(height: 20)
                            
                            Image(systemName: "pawprint.fill")
                                .resizable()
                                .scaledToFit()
                                .frame(height: 20)
                            
                            if !viewState.petPreview.chipNumber.isEmpty {
                                Image(systemName: "cpu.fill")
                                    .resizable()
                                    .scaledToFit()
                                    .frame(height: 20)
                            }
                            
                        }.padding(.leading, 12.0)
                        
                        VStack(alignment: .leading, spacing: 8.0) {
                            Text(viewState.petPreview.birthday.toSwift().formatted(date: .long, time: .omitted))
                                .lineLimit(1)
                                .truncationMode(.tail)
                            
                            Text("\(genderLabel), \(sterilizedLabel)")
                                .lineLimit(1)
                                .truncationMode(.tail)
                            
                            Text(viewState.petPreview.breed)
                                .lineLimit(1)
                                .truncationMode(.tail)
                            
                            if !viewState.petPreview.chipNumber.isEmpty {
                                Text(viewState.petPreview.chipNumber)
                                    .lineLimit(1)
                                    .truncationMode(.tail)
                            }
                        }.padding(.leading, 4.0)
                        
                        Spacer()
                    }
                    .expandedH()
                    .cardBackground()
                    .padding()
                    
                    if !viewState.screenState.interactions.isEmpty {
                        Text("Reminders")
                            .font(.title)
                            .fontWeight(.medium)
                            .align(.leading)
                            .expandedH()
                            .padding()
                        
//                        let arr = viewState.screenState.interactions.keys.sorted(by: { a, b in
//                            viewState.screenState.interactions[a].reminders.min(by: {
//                                $0.dateTime.toSwift() < $1.dateTime.toSwift()
//                            })?.dateTime.toSwift() < viewState.screenState.interactions[b].reminders.min(by: {
//                                $0.dateTime.toSwift() < $1.dateTime.toSwift()
//                            })?.dateTime.toSwift()
//                        })
                        
                        ForEach(Array(viewState.screenState.interactions.keys), id: \.self) { key in
                            
                            Section(header: Text(key.name).padding(.horizontal)) {
                                let interactions = viewState.screenState.interactions[key]
//                                    .sorted(by: {$0.reminders.min(by: {$0.dateTime.toSwift() < $1.dateTime.toSwift()}) < $1.reminders.min(by: {$0.dateTime.toSwift() < $1.dateTime.toSwift()})})
                                
                                
                                ForEach(interactions ?? [], id: \.self) { interaction in
                                    HStack(alignment: .center) {
                                        Image(systemName: "circle")
                                            .imageScale(.large)
                                            .foregroundColor(.accentColor)
                                            .padding(.leading)
                                        Text(interaction.name)
                                            .font(.body)
                                        Spacer()
                                    }
                                    .padding(.vertical, 8)
                                    .expandedH()
                                    .cardBackground()
                                    .padding(.horizontal)
                                    .onTapGesture {
                                        viewState.openInteraction(interaction: interaction)
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer()
                    
                    Button("Delete Pet", role: .destructive) {
                        viewState.presentDeletePetDialog.toggle()
                    }
                    .expandedH()
                    .padding(8.0)
                }
            }
            
            HStack(alignment: .bottom) {
                Spacer()
                Button(action: {}) {
                    Text("Add Reminder")
                        .floatingActionButtonStyle()
                }
                .padding()
                .buttonStyle(.borderedProminent)
            }
        }
        .toolbar {
            ToolbarItemGroup(placement: .primaryAction) {
                Button(action: { viewState.presentEditPet.toggle() }) {
                    Image(systemName: "pencil")
                }
            }
        }
        .navigationLargeTitle(title: viewState.petPreview.name)
        .expanded()
        .screenBackground(color: Color(.systemGroupedBackground))
        .popover(isPresented: $viewState.presentEditPet) {}
        .confirmationDialog("Do you really want to delete \(viewState.petPreview.name)?", isPresented: $viewState.presentDeletePetDialog) {
            Button("Delete", role: .destructive) {
                viewState.deletePet()
            }
        } message: {
            Text("Delete \(viewState.petPreview.name.trimmingCharacters(in: .whitespacesAndNewlines))? You cannot undo this action")
        }
    }
}

#Preview {
    NavigationView {
        PetScreenView(
            pet: PetWithInteractions(id: "", petType: .cat, breed: "No breed", name: "Test cat", avatar: "ic_cat", userId: "123", birthday: Kotlinx_datetimeLocalDate(year: 2020, month: Kotlinx_datetimeMonth.august, dayOfMonth: 1), isSterilized: false, gender: .female, chipNumber: "123456789012345", dateCreated: Kotlinx_datetimeLocalDate(year: 2020, month: Kotlinx_datetimeMonth.august, dayOfMonth: 1), interactions: [InteractionGroup.health: [InteractionWithReminders(
                id: "",
                templateId: "",
                petId: "",
                type: InteractionType.custom,
                name: "Interaction Name",
                group: InteractionGroup.care,
                repeatConfig: InteractionRepeatConfig(repeatsEveryNumber: 1, repeatsEveryPeriod: .day, repeatsEveryPeriodOn: "", ends: "", active: false),
                remindConfig: InteractionRemindConfig(remindBeforeNumber: 1, repeatsEveryPeriod: .day),
                isActive: true,
                notes: "",
                reminders: [
                    Reminder(
                        id: "",
                        interactionId: "",
                        dateTime: Date().toShared(),
                        isCompleted: false,
                        notificationJobId: nil
                    )
                ]
            )
            ]]),
            navStack: .constant(NavigationPath())
        )
    }
}
