import sharedLib
import Firebase
import FirebaseStorage

class SynchronisationApiIOS: SynchronisationApi {

    let currentUserPreferenceKey = PreferencesKeys().CURRENT_USER_ID
    let provider: KoinProvider

    init(provider: KoinProvider) {
        self.provider = provider
    }

    func sendBackup(backup: String) {
        let storageRef = Storage.storage().reference()
        guard let userId = provider.preferences.getString(key: currentUserPreferenceKey)
        else { return }

        storageRef.child("sync_data/\(userId).json")
            .putData(Data(backup.utf8)) { (metadata, error) in
                if let error {
                    print("Sync failed! \n\(error)")
                } else {
                    print("Sync succeed!")
                }
            }
    }

    func retrieveBackup(onFinish: ((KotlinBoolean) -> Void)?) async throws {
        let storageRef = Storage.storage().reference()
        guard let userId = provider.preferences.getString(key: currentUserPreferenceKey)
        else { return }

        let syncUseCase = provider.synchronisationUseCase

        storageRef.child("sync_data/\(userId).json")
            .getData(maxSize: Int64.max) { data, error in
                if let error {
                    print("Sync failed! \n\(error)")
                    if let onFinish { onFinish(false) }
                } else {
                    guard let backup = data else {
                        print("No sync data")
                        return
                    }
                    syncUseCase.sync(backup: KotlinByteArray.from(data: backup)).watch { result in
                        if let onFinish { onFinish(result ?? false) }
                        print("Synced")
                    }
                }
            }
    }
}

extension KotlinByteArray {
    static func from(data: Data) -> KotlinByteArray {
        let swiftByteArray = [UInt8](data)
        return swiftByteArray
            .map(Int8.init(bitPattern:))
            .enumerated()
            .reduce(into: KotlinByteArray(size: Int32(swiftByteArray.count))) { result, row in
                result.set(index: Int32(row.offset), value: row.element)
            }
    }
}
