import SwiftUI

struct AddPetSetupView: View {
    @Binding var petName: String
    @Binding var petAvatar: String
    let onSetupFinish: () -> Void

    var body: some View {
        VStack {
            
            Spacer()

            VStack {
                Image(petAvatar)
                    .resizable()
                    .scaledToFit()
                    .frame(height: 120)
            }
            .padding()
            .background(.regularMaterial)
            .clipShape(.buttonBorder)
            .shadow(color: Color.black.opacity(0.2), radius: 4)

            Text("New pet added!")
                .font(.largeTitle)
                .padding(.top)

            Text("We are welcoming \(petName)")

            Spacer()

        }.safeAreaInset(edge: .bottom) {
            Button(action: onSetupFinish) {
                Text("Finish")
                    .mainActionButtonStyle()
            }
            .padding()
            .buttonStyle(.borderedProminent)
        }
        .expanded()
        .screenBackground(color: Color(.systemGroupedBackground))
        .navigationBarBackButtonHidden()
    }
}

#Preview {
    AddPetSetupView(petName: .constant("Varya"), petAvatar: .constant("ic_cat")) {}
}
