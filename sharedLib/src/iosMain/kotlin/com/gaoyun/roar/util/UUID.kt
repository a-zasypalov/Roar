package com.gaoyun.roar.util

import platform.Foundation.NSUUID

actual fun randomUUID(): String = NSUUID().UUIDString()