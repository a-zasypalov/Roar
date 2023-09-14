import sharedLib
import SwiftUI

struct AuthScreenView: View {
    @ObservedObject var state: AuthScreenState
    
    init(authCallback: @escaping () -> Void) {
        state = AuthScreenState(authCallback: authCallback)
    }
    
    var body: some View {
        VStack {
            Text("Authorization Screen")
                .padding()
            
            Spacer()
            
            Button(action: { state.signIn() }) {
                Text("Sign up with Google")
                    .mainActionButtonStyle()
            }
            .padding()
            .buttonStyle(.borderedProminent)
        }
        .navigationBarBackButtonHidden()
    }
}

struct AuthScreenView_Previews: PreviewProvider {
    static var previews: some View {
        AuthScreenView(authCallback: {})
    }
}
