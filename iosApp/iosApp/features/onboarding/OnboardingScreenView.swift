import SwiftUI
import sharedLib

struct OnboardingScreenView: View {
    
    let TABS_NUMBER = 3
    
    @ObservedObject var state: OnboardingScreenState
    @State private var selectedTab = 1
    
    init() {
        state = OnboardingScreenState()
    }
    
    var body: some View {
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
            
            Button(action: {
                if selectedTab == TABS_NUMBER {
                    //next screen
                } else {
                    selectedTab+=1
                }
            }) {
                Text("Continue")
                    .frame(maxWidth: .infinity)
                    .padding()
                
            }
            .padding()
            .buttonStyle(.borderedProminent)
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

struct OnboardingScreenView_Previews: PreviewProvider {
    static var previews: some View {
        OnboardingScreenView()
    }
}
