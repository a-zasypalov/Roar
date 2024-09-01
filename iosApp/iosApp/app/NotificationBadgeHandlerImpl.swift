import sharedLib
import UIKit

class NotificationBadgeHandlerImpl: NotificationBadgeHandler {
    func setShowBadge(count: Int32) {
        UIApplication.shared.applicationIconBadgeNumber = Int(count)
    }
}
