import BackgroundTasks
import sharedLib

class SynchronisationSchedulerIOS: SynchronisationScheduler {
    private let synchronizationQueue = DispatchQueue(label: "com.gaoyun.roar.syncQueue")
    private var isSynchronizationScheduled = false

    let provider: KoinProvider
    var syncTimeout: Int {
        #if DEBUG
        5
        #else
        30
        #endif
    }

    init(provider: KoinProvider) {
        self.provider = provider
    }

    func scheduleSynchronisation() {
        scheduleSynchronisation(dispatchTime: DispatchTime.now() + syncTimeout)
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
