import SwiftUI

extension View {
    func expanded() -> some View {
        frame(maxWidth: .infinity, maxHeight: .infinity)
    }

    func screenBackground(color: Color) -> some View {
        background(color.frame(maxWidth: .infinity, maxHeight: .infinity).ignoresSafeArea())
    }
    
    func navigationLargeTitle(title: String) -> some View {
        navigationTitle(title).navigationBarTitleDisplayMode(.large)
    }
}
