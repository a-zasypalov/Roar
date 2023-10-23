import sharedLib
import SwiftUI

struct TemplatesListScreenView: View {
    let onTemplateChosen: (String) -> Void
    @Environment(\.colorScheme) var colorScheme

    init(petId: String, onTemplateChosen: @escaping (String) -> Void) {
        self.onTemplateChosen = onTemplateChosen
    }

    var body: some View {
        ScrollView {
            VStack(alignment: .leading) {
                ForEach(["Health", "Care", "Custom"], id: \.self) { category in
                    Section(
                        header: Text(category)
                            .padding(.horizontal)
                            .padding(.top, 8.0)
                            .font(.title2)
                            .fontWeight(.medium)
                    ) {
                        ForEach(["drops", "nails", "vaccine", "stethoscope"], id: \.self) { icon in
                            Button(action: { onTemplateChosen("") }) {
                                HStack(alignment: .center) {
                                    Image(icon)
                                        .renderingMode(.template)
                                        .resizable()
                                        .scaledToFit()
                                        .frame(height: 40)
                                        .foregroundColor(colorScheme == .dark ? .white : .black)
                                        .padding(.vertical)
                                        .padding(.leading, 20.0)

                                    VStack(alignment: .leading) {
                                        Text("Annual check up")
                                            .font(.title)
                                            .foregroundColor(colorScheme == .dark ? .white : .black)

                                        Text("Repeats 3 times a week")
                                            .font(.subheadline)
                                            .foregroundColor(colorScheme == .dark ? .white : .black)
                                    }
                                    .padding(.leading, 8.0)

                                    Spacer()
                                }
                            }
                            .expandedH()
                            .background(.regularMaterial)
                            .clipShape(.buttonBorder)
                            .shadow(color: Color.black.opacity(0.2), radius: 4)
                            .padding(.horizontal)
                        }
                    }
                }
            }
        }
        .navigationLargeTitle(title: "Templates")
        .expanded()
        .screenBackground(color: Color(.systemGroupedBackground))
    }
}

#Preview {
    NavigationView {
        TemplatesListScreenView(petId: "1", onTemplateChosen: { _ in })
    }
}
