import sharedLib
import SwiftUI

struct ContentView: View {
    var body: some View {
        // TODO: Naigation wrapper
        NavigationStack {
            HomeScreenView()
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
