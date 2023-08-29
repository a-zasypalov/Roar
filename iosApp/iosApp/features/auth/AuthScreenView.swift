import SwiftUI
import sharedLib

struct AuthScreenView: View {
    
    @ObservedObject var state: AuthScreenState
    
    init() {
        state = AuthScreenState()
    }
    
    var body: some View {
        Text("Hey")
    }
}
