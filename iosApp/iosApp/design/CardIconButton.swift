import SwiftUI

struct CardIconButton: View {
    init(image: String, iconSize: CardIconButtonSize = .auto, tapAction: @escaping () -> Void) {
        self.image = image
        self.iconSize = iconSize
        self.tapAction = tapAction
    }
    
    let image: String
    let iconSize: CardIconButtonSize
    let tapAction: () -> Void
    
    var body: some View {
        let size: CGFloat = switch iconSize {
        case .auto: 0
        case .big: 160
        case .medium: 120
        case .small: 70
        case .custom(let size): size
        }
        
        Button(action: tapAction) {
            if size > 0 {
                Image(image)
                    .resizable()
                    .scaledToFit()
                    .frame(width: size)
            } else {
                Image(image)
                    .resizable()
                    .scaledToFit()
            }
        }
        .padding()
        .background(.regularMaterial)
        .clipShape(.buttonBorder)
        .shadow(color: Color.black.opacity(0.2), radius: 4)
    }
    
    enum CardIconButtonSize {
        case auto
        case big
        case medium
        case small
        case custom(size: CGFloat)
    }
}
