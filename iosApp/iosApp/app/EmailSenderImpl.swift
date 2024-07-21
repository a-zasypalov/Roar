import sharedLib
import MessageUI

class EmailSenderImpl: NSObject, EmailSender, MFMailComposeViewControllerDelegate {

    func sendSupportEmail(to: String, subject: String) {
        if MFMailComposeViewController.canSendMail() {
            let window = UIApplication.shared.connectedScenes.first
            if let topController = (window as? UIWindowScene)?.windows.first?.rootViewController {
                let mail = MFMailComposeViewController()
                mail.mailComposeDelegate = self
                mail.setToRecipients([to])
                mail.setSubject(subject)
                topController.present(mail, animated: true)
            }
        } else {
            // show failure alert
        }
    }

    func mailComposeController(_ controller: MFMailComposeViewController, didFinishWith result: MFMailComposeResult, error: Error?) {
        controller.dismiss(animated: true, completion: nil)
    }
}
