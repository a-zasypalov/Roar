import sharedLib
import SwiftUI

struct ContentView: View {
    @State var navStack = NavigationPath()

    var body: some View {
        NavigationStack(path: $navStack) {
            HomeScreenView(navStack: $navStack)
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
