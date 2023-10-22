import sharedLib
import SwiftUI

struct InteractionScreenView: View {
    @ObservedObject var viewState: InteractionScreenState

    init(interaction: InteractionWithReminders, navStack: Binding<NavigationPath>) {
        viewState = InteractionScreenState(interaction: interaction, navStack: navStack)
    }

    var body: some View {
        ScrollView {
            VStack(alignment: .leading) {
                HStack(alignment: .center) {
                    VStack(alignment: .leading, spacing: 8.0) {
                        HStack {
                            Image("drops")
                                .resizable()
                                .scaledToFit()
                                .frame(width: 30, height: 25)
                            Text(viewState.interactionPreview.group.name.localizedLowercase.capitalized)
                                .padding(.leading, 8)
                        }
                        .padding(.top)
                        .padding(.horizontal)

                        HStack {
                            Image(systemName: "checkmark.seal")
                                .resizable()
                                .scaledToFit()
                                .frame(width: 30, height: 25)
                            Text("Active")
                                .padding(.leading, 8)
                        }
                        .padding(.top, 6)
                        .padding(.horizontal)

                        HStack {
                            Image(systemName: "bell.fill")
                                .resizable()
                                .scaledToFit()
                                .frame(width: 30, height: 25)
                            Text("Remind 1 day before")
                                .padding(.leading, 8)
                        }
                        .padding(.top, 6)
                        .padding(.horizontal)

                        HStack(alignment: .top) {
                            Image(systemName: "repeat")
                                .resizable()
                                .scaledToFit()
                                .frame(width: 30, height: 25)
                            Text("Repeats every 3 months on the same day, never ends")
                                .padding(.leading, 8)
                        }
                        .padding(.top, 6)
                        .padding(.horizontal)
                        .padding(.bottom)
                    }
                    .padding(.leading, 4.0)

                    Spacer()
                }
                .expandedH()
                .cardBackground()
                .padding()

                TextField("Notes", text: $viewState.notes, axis: .vertical)
                    .textFieldStyle(.roundedBorder)
                    .padding()

                let nextReminders = viewState.screenState.interaction?.reminders.filter { reminder in
                    !reminder.isCompleted
                } ?? []

                let historyReminders = viewState.screenState.interaction?.reminders.filter { reminder in
                    reminder.isCompleted
                } ?? []

                if !nextReminders.isEmpty {
                    Text("Next")
                        .font(.title)
                        .fontWeight(.medium)
                        .align(.leading)
                        .expandedH()
                        .padding()

                    ForEach(Array(nextReminders), id: \.self) { reminder in
                        Section {
                            HStack(alignment: .center) {
                                Image(systemName: "circle")
                                    .imageScale(.large)
                                    .foregroundColor(.accentColor)
                                    .padding(.leading)
                                Text(reminder.dateTime.toSwift().formatted(date: .long, time: .omitted))
                                    .font(.body)
                                Spacer()
                            }
                            .padding(.vertical, 8)
                            .expandedH()
                            .cardBackground()
                            .padding(.horizontal)
                        }
                    }
                }

                if !historyReminders.isEmpty {
                    Text("Next")
                        .font(.title)
                        .fontWeight(.medium)
                        .align(.leading)
                        .expandedH()
                        .padding()
                    
                    ForEach(Array(historyReminders), id: \.self) { reminder in
                        Section {
                            HStack(alignment: .center) {
                                Image(systemName: "checkmark.circle.fill")
                                    .imageScale(.large)
                                    .foregroundColor(.accentColor)
                                    .padding(.leading)
                                Text(reminder.dateTime.toSwift().formatted(date: .long, time: .omitted))
                                    .font(.body)
                                Spacer()
                            }
                            .padding(.vertical, 8)
                            .expandedH()
                            .cardBackground()
                            .padding(.horizontal)
                        }
                    }
                }
                
                Button("Delete reminder", role: .destructive) {
                    viewState.presentDeleteInteractionDialog.toggle()
                }
                .expandedH()
                .padding(8.0)
            }
        }
        .toolbar {
            ToolbarItemGroup(placement: .primaryAction) {
                Button(action: { viewState.presentEditInteraction.toggle() }) {
                    Image(systemName: "pencil")
                }
            }
        }
        .navigationLargeTitle(title: viewState.interactionPreview.name)
        .expanded()
        .screenBackground(color: Color(.systemGroupedBackground))
        .popover(isPresented: $viewState.presentEditInteraction) {}
        .confirmationDialog("Do you really want to delete \(viewState.interactionPreview.name)?", isPresented: $viewState.presentDeleteInteractionDialog) {
            Button("Delete", role: .destructive) {}
        } message: {
            Text("Delete \(viewState.interactionPreview.name.trimmingCharacters(in: .whitespacesAndNewlines))? You cannot undo this action")
        }
    }
}

#Preview {
    NavigationView {
        InteractionScreenView(
            interaction: InteractionWithReminders(
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
            ),
            navStack: .constant(NavigationPath())
        )
    }
}
