import sharedLib
import SwiftUI

struct OnboardingScreenView: View {
    let TABS_NUMBER = 3
    
    @ObservedObject var state: OnboardingScreenState
    @State private var selectedTab = 1
    let authCallback: () -> Void
    
    init(authCallback: @escaping () -> Void) {
        state = OnboardingScreenState()
        self.authCallback = authCallback
    }
    
    var body: some View {
        NavigationView {
            VStack {
                TabView(selection: $selectedTab) {
                    OnboardingPageView(title: "Roar", subtitle: "Pet's care project")
                        .tag(1)
                    
                    OnboardingPageView(title: "Care reminders", subtitle: "Sometimes pets aren't talkative about their needs")
                        .tag(2)
                    
                    OnboardingPageView(title: "Community", subtitle: "Let's make life better for pets together!")
                        .tag(3)
                }
                .tabViewStyle(.page)
                .indexViewStyle(.page(backgroundDisplayMode: .always))
                .animation(.easeInOut, value: selectedTab)
                .transition(.slide)
                
                if selectedTab == TABS_NUMBER {
                    NavigationLink(destination: AuthScreenView(authCallback: authCallback)) {
                        Text("Start")
                            .mainActionButtonStyle()
                            .navigationBarBackButtonHidden()
                    }
                    .padding()
                    .buttonStyle(.borderedProminent)
                    .onAppear(perform: { state.completeOnboarding() })
                } else {
                    Button(action: { selectedTab += 1 }) {
                        Text("Continue")
                            .mainActionButtonStyle()
                    }
                    .padding()
                    .buttonStyle(.borderedProminent)
                }
            }
        }
    }
}

struct OnboardingPageView: View {
    let title: String
    let subtitle: String
    
    var body: some View {
        VStack(alignment: .center) {
            Text(title)
                .font(.largeTitle)
                .fontWeight(.bold)
            Text(subtitle)
                .font(.title3)
                .multilineTextAlignment(.center)
        }
    }
}

#Preview {
    OnboardingScreenView(authCallback: {})
}
