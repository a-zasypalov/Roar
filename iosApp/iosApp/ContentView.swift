import SwiftUI
import sharedLib

struct ContentView: View {
    let vm = HomeScreenViewModel()
    
    var body: some View {
        Text("Hey")
    }
    
    func testF() {
        print(vm)
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
