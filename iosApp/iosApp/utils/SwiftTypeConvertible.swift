import Foundation

protocol SwiftTypeConvertible
{
    associatedtype SwiftType
    func toSwift() -> SwiftType
}
