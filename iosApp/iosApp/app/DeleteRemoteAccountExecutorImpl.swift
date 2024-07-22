import FirebaseAuth
import FirebaseStorage
import sharedLib

class DeleteRemoteAccountExecutorImpl: DeleteRemoteAccountExecutor
{
    let currentUserPreferenceKey = PreferencesKeys().CURRENT_USER_ID
    let provider: KoinProvider

    init(provider: KoinProvider) {
        self.provider = provider
    }


    func deleteAccount()
    {
        let storageRef = Storage.storage().reference()
        guard let userId = provider.preferences.getString(key: currentUserPreferenceKey)
        else { return }

        storageRef.child("sync_data/\(userId).json").delete{ _ in }
        Auth.auth().currentUser?.delete()
    }
}
