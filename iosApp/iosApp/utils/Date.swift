import sharedLib

extension sharedLib.Kotlinx_datetimeLocalDateTime: SwiftTypeConvertible
{
    public func toSwift() -> Date
    {
        DateComponents(
            calendar: Calendar(identifier: .iso8601),
            year: Int(year),
            month: Int(monthNumber),
            day: Int(dayOfMonth),
            hour: Int(hour),
            minute: Int(minute),
            second: Int(second),
            nanosecond: Int(nanosecond)
        )
        .date!
    }
}

extension sharedLib.Kotlinx_datetimeLocalDate: SwiftTypeConvertible
{
    public func toSwift() -> Date
    {
        DateComponents(
            calendar: Calendar(identifier: .iso8601),
            year: Int(year),
            month: Int(monthNumber),
            day: Int(dayOfMonth)
        )
        .date!
    }
}

public extension Date
{
    func toShared() -> sharedLib.Kotlinx_datetimeLocalDateTime
    {
        let components = Calendar(identifier: .iso8601)
            .dateComponents([.day, .month, .year, .hour, .minute, .second, .nanosecond], from: self)

        return sharedLib.Kotlinx_datetimeLocalDateTime(
            year: Int32(components.year!),
            monthNumber: Int32(components.month!),
            dayOfMonth: Int32(components.day!),
            hour: Int32(components.hour!),
            minute: Int32(components.minute!),
            second: Int32(components.second!),
            nanosecond: Int32(components.nanosecond ?? 0)
        )
    }
}

public extension Date
{
    func toShared() -> sharedLib.Kotlinx_datetimeLocalDate
    {
        let components = Calendar(identifier: .iso8601)
            .dateComponents([.day, .month, .year], from: self)

        return sharedLib.Kotlinx_datetimeLocalDate(
            year: Int32(components.year!),
            monthNumber: Int32(components.month!),
            dayOfMonth: Int32(components.day!)
        )
    }
}
