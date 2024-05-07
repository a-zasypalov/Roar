import BackgroundTasks
import sharedLib

class SynchronisationSchedulerIOS: SynchronisationScheduler {
    private let synchronizationQueue = DispatchQueue(label: "com.gaoyun.roar.syncQueue")
    private var isSynchronizationScheduled = false

    let provider: KoinProvider

    init(provider: KoinProvider) {
        self.provider = provider
    }

    func scheduleSynchronisation() {
        scheduleSynchronisation(dispatchTime: DispatchTime.now() + 30)
    }

    func scheduleSynchronisation(dispatchTime: DispatchTime) {
        synchronizationQueue.sync {
            guard !isSynchronizationScheduled else {
                print("Synchronization is already scheduled. Exiting.")
                return
            }
            isSynchronizationScheduled = true

            DispatchQueue.main.asyncAfter(deadline: dispatchTime) {
                self.performSynchronization()
                self.synchronizationQueue.sync {
                    self.isSynchronizationScheduled = false
                }
            }
        }
    }

    func performSynchronization() {
        provider.createBackupUseCase.createBackupToSync().watch { result in
            guard let backupToSync = result else {
                print("Error during creating a backup!")
                return
            }
            print("Sending Backup...")
            self.provider.synchronisationApi.sendBackup(backup: backupToSync as String)
        }
    }
}
