import sharedLib
import UserNotifications

class NotificationSchedulerIOS: NotificationScheduler
{
    let provider: KoinProvider

    init(provider: KoinProvider)
    {
        self.provider = provider
    }

    func scheduledNotificationIds(completion: @escaping ([String]) -> Void) {
        UNUserNotificationCenter.current().getPendingNotificationRequests { notificationRequests in
            let ids = notificationRequests.map { request in request.identifier }
            completion(ids)
        }
    }

    func cancelNotification(id: String?)
    {
        guard let id else { return }
        cancelNotifications(ids: [id])
    }

    func cancelNotifications(ids: [String]) {
        UNUserNotificationCenter.current().removePendingNotificationRequests(withIdentifiers: ids)
    }

    func cancelAllNotifications()
    {
        UNUserNotificationCenter.current().removeAllPendingNotificationRequests()
    }

    func scheduleNotification(data: NotificationData)
    {
        guard let reminderData = data.item as? NotificationItemReminder else { return }
        provider.notificationContentMaker.make(itemId: reminderData.itemId)
        { notificationData, error in
            if let error
            {
                print("Notification scheduling failed (content making stage): \(String(describing: error))")
                return
            }
            guard let notificationData else { return }

            let content = UNMutableNotificationContent()
            content.title = notificationData.title as String
            content.body = notificationData.content as String
            content.sound = UNNotificationSound.default

            guard let d = convertLocalDateTimeToSwiftDate(localDateTime: data.scheduled)
            else { return }

            let oneHour: TimeInterval = 3_600

            let triggerInterval = if d.timeIntervalSinceNow > 0
            {
                d.timeIntervalSinceNow
            }
            else
            {
                oneHour
            }

            if triggerInterval > 0
            {
                let trigger = UNTimeIntervalNotificationTrigger(timeInterval: triggerInterval, repeats: false)
                let request = UNNotificationRequest(identifier: reminderData.workId, content: content, trigger: trigger)

                UNUserNotificationCenter.current().add(request)
                { error in
                    if let error
                    {
                        print("Notification scheduling failed (scheduling stage): \(String(describing: error))")
                    }
                    else
                    {
                        print("Notification scheduling succeed! (\(String(describing: trigger.nextTriggerDate()))")
                    }
                }
            }
        }
    }
}

func convertLocalDateTimeToSwiftDate(
    localDateTime: Kotlinx_datetimeLocalDateTime,
    timeZone: TimeZone = .current
) -> Date?
{
    // Extract components from the LocalDateTime
    let year = localDateTime.year
    let month = localDateTime.monthNumber
    let day = localDateTime.dayOfMonth
    let hour = localDateTime.hour
    let minute = localDateTime.minute
    let second = localDateTime.second
    let nanosecond = localDateTime.nanosecond

    // Create DateComponents
    var dateComponents = DateComponents()
    dateComponents.year = Int(year)
    dateComponents.month = Int(month)
    dateComponents.day = Int(day)
    dateComponents.hour = Int(hour)
    dateComponents.minute = Int(minute)
    dateComponents.second = Int(second)
    dateComponents.nanosecond = Int(nanosecond)
    dateComponents.timeZone = timeZone

    // Create a Calendar instance
    let calendar = Calendar.current

    // Convert DateComponents to Date
    return calendar.date(from: dateComponents)
}
