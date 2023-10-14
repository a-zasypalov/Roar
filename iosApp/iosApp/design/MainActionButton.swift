import Foundation
import SwiftUI

extension Text {
    func mainActionButtonStyle() -> some View {
        frame(maxWidth: .infinity)
            .padding(.horizontal)
            .padding(.vertical, 12)
    }

    func floatingActionButtonStyle() -> some View {
        fontWeight(.medium)
            .padding(.horizontal)
            .padding(.vertical, 12)
    }
}
