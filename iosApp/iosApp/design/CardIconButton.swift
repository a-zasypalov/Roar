import SwiftUI

struct CardIconButton: View {
    let image: String
    let iconSize: CardIconButtonSize
    let tapAction: () -> Void
    
    var body: some View {
        let size: CGFloat = switch iconSize {
        case .big: 160
        case .medium: 120
        case .small: 80
        case .custom(let size): size
        }
        
        Button(action: tapAction) {
            Image(image)
                .resizable()
                .scaledToFit()
                .frame(width: size)
        }
        .padding()
        .background(Color.white)
        .clipShape(.buttonBorder)
        .shadow(color: Color.black.opacity(0.2), radius: 4)
    }
    
    enum CardIconButtonSize {
        case big
        case medium
        case small
        case custom(size: CGFloat)
    }
}
