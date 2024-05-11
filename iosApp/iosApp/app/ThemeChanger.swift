import SwiftUI
import sharedLib


class ThemeChangerIOS: ThemeChanger, ObservableObject {
    @Published var key = UUID()

    func applyTheme() {
        key = UUID()
    }

    func activateIcon(icon: AppIcon) {
        Task {
            switch icon {
                case AppIcon.paw:
                    await UIApplication.shared.setAlternateIconName("IconPaw"){ error in
                        if let error = error {
                            print(error.localizedDescription)
                        } else {
                            print("Success!")
                        }
                    }
                default:
                    await UIApplication.shared.setAlternateIconName(nil){ error in
                        if let error = error {
                            print(error.localizedDescription)
                        } else {
                            print("Success!")
                        }
                    }
            }
        }
    }
}
