import SwiftUI

struct DeferredLoadingIndicator: ViewModifier {
    var isLoading = false
    
    @State private var loadingIndicatorVisible = false
    @State private var deferredTask: Task<Void, Never>?
    
    func body(content: Content) -> some View {
        content
            .opacity(loadingIndicatorVisible ? 0 : 1)
            .overlay {
                if loadingIndicatorVisible { ProgressView() }
            }
            .onChange(of: isLoading, initial: true) { _, newValue in
                deferredTask?.cancel()
                
                guard newValue else {
                    loadingIndicatorVisible = false
                    return
                }
                
                deferredTask = Task {
                    try? await Task.sleep(for: .milliseconds(200))
                    
                    guard !Task.isCancelled else { return }
                    
                    if newValue {
                        loadingIndicatorVisible = true
                    }
                }
            }
            .animation(.easeInOut, value: loadingIndicatorVisible)
    }
}
