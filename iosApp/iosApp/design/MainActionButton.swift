import Foundation
import SwiftUI

extension Text {
    func mainActionButtonStyle() -> some View {
        self.frame(maxWidth: .infinity)
            .padding(.horizontal)
            .padding(.vertical, 12)
    }
}
