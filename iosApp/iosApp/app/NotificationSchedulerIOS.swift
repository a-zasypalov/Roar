import UserNotifications
import sharedLib

class NotificationSchedulerIOS: NotificationScheduler {

    let provider: KoinProvider

    init(provider: KoinProvider) {
        self.provider = provider
    }

    func cancelNotification(id: String?) {
        guard let id = id else { return }
        UNUserNotificationCenter.current().removePendingNotificationRequests(withIdentifiers: [id])
    }
    
    func scheduleNotification(data: NotificationData) {
        guard let reminderData = data.item as? NotificationItemReminder else { return }
        provider.notificationContentMaker.make(itemId: reminderData.itemId) { notificationData, error in
            if let error = error {
                print("Notification scheduling failed (content making stage): \(String(describing: error))")
                return
            }
            guard let notificationData = notificationData else { return }

            let content = UNMutableNotificationContent()
            content.title = notificationData.title as String
            content.body = notificationData.content as String
            content.sound = UNNotificationSound.default

            let d = data.scheduled
            let formatter = DateFormatter()
            formatter.dateFormat = "yyyy/MM/dd HH:mm"
            guard let someDateTime = formatter.date(from: "\(d.year)/\(d.monthNumber)/\(d.dayOfMonth) \(d.hour):\(d.minute)") else { return }

            let triggerInterval = someDateTime.timeIntervalSinceNow
            let trigger = UNTimeIntervalNotificationTrigger(timeInterval: triggerInterval, repeats: false)
            let request = UNNotificationRequest(identifier: reminderData.workId, content: content, trigger: trigger)

            UNUserNotificationCenter.current().add(request) { error in
                print("Notification scheduling failed (scheduling stage): \(String(describing: error))")
            }
        }
    }
}
