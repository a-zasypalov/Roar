import SwiftUI
import sharedLib


class ThemeChangerIOS: ThemeChanger, ObservableObject {
    @Published var key = UUID()

    func applyTheme() {
        key = UUID()
    }

    func activateIcon(icon: AppIcon) {
        
    }
}
