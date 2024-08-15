import BackgroundTasks
import sharedLib

class SynchronisationSchedulerIOS: SynchronisationScheduler
{
    private let nightlySyncTaskIdentifier = "com.gaoyun.roar.nightlySync"
    private let synchronizationQueue = DispatchQueue(label: "com.gaoyun.roar.syncQueue")
    private var isSynchronizationScheduled = false

    let provider: KoinProvider

    init(provider: KoinProvider)
    {
        self.provider = provider

        // Register the background task
        BGTaskScheduler.shared.register(forTaskWithIdentifier: nightlySyncTaskIdentifier, using: nil)
        { task in
            provider.synchronisationApi.retrieveBackup(
                onFinish: { _ in },
                onAuthException: {},
                completionHandler: {
                    error in
                    print("Nightly sync completed")
                    if let error { print(error) }
                    self.scheduleSynchronisation()
                    task.setTaskCompleted(success: error == nil)
                }
            )
        }
    }

    func scheduleSynchronisation()
    {
        scheduleSynchronisation(dispatchTime: getSyncTime())
    }

    func scheduleSynchronisation(dispatchTime: DispatchTime)
    {
        synchronizationQueue.sync
        {
            guard !isSynchronizationScheduled
            else
            {
                print("Synchronization is already scheduled. Exiting.")
                return
            }
            isSynchronizationScheduled = true

            DispatchQueue.main.asyncAfter(deadline: dispatchTime)
            {
                self.performSynchronization()
                self.synchronizationQueue.sync
                {
                    self.isSynchronizationScheduled = false
                }
            }
        }
    }

    func scheduleNightlySynchronisation()
    {
        let request = BGAppRefreshTaskRequest(identifier: nightlySyncTaskIdentifier)
        request.earliestBeginDate = next3AM()

        do
        {
            try BGTaskScheduler.shared.submit(request)
        }
        catch
        {
            print("Could not schedule app refresh: \(error)")
        }
    }



    func performSynchronization()
    {
        provider.createBackupUseCase.createBackupToSync().watch
        { result in
            guard let backupToSync = result
            else
            {
                print("Error during creating a backup!")
                return
            }
            print("Sending Backup...")
            self.provider.synchronisationApi.sendBackup(backup: backupToSync as String)
        }
    }

    func next3AM() -> Date
    {
        let now = Date()
        var calendar = Calendar.current
        calendar.timeZone = TimeZone.current

        var components = calendar.dateComponents([.year, .month, .day], from: now)

        components.hour = 3
        components.minute = 0
        components.second = 0

        let today3AM = calendar.date(from: components)!

        if today3AM <= now
        {
            return calendar.date(byAdding: .day, value: 1, to: today3AM) ?? Date()
        }
        else
        {
            return today3AM
        }
    }

    private func getSyncTime() -> DispatchTime
    {
        #if DEBUG
        return DispatchTime.now() + 5
        #else
        return DispatchTime.now() + 30
        #endif
    }
}
