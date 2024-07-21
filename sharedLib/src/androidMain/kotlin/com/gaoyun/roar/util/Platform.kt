package com.gaoyun.roar.util

import android.os.Build

actual object Platform {
    actual val name = PlatformNames.Android
    actual val supportsDynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}