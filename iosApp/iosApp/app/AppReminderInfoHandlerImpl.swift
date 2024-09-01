import sharedLib
import UIKit

class AppReminderInfoHandlerImpl: AppReminderInfoHandler {
    func setShowBadge(count: Int32) {
        UIApplication.shared.applicationIconBadgeNumber = Int(count)
    }
}
