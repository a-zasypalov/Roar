import SwiftUI

public extension View
{
    @ViewBuilder
    func align(_ alignment: Alignment) -> some View
    {
        Group
        {
            switch alignment
            {
                // Basic alignments
                case .top: VStack(spacing: 0) { self; Spacer() }
                case .bottom: VStack(spacing: 0) { Spacer(); self }
                case .leading: HStack(spacing: 0) { self; Spacer() }
                case .trailing: HStack(spacing: 0) { Spacer(); self }
                // Mixed alignments
                case .topLeading: VStack(spacing: 0) { HStack(spacing: 0) { self; Spacer() }; Spacer() }
                case .topTrailing: VStack(spacing: 0) { HStack(spacing: 0) { Spacer(); self }; Spacer() }
                case .bottomLeading: VStack(spacing: 0) { Spacer(); HStack(spacing: 0) { self; Spacer() } }
                case .bottomTrailing: VStack(spacing: 0) { Spacer(); HStack(spacing: 0) { Spacer(); self } }
                // Center
                default: self
            }
        }
    }
}
