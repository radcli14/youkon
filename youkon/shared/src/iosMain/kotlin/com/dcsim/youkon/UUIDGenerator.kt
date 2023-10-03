package com.dcsim.youkon

import platform.Foundation.NSUUID

actual class UUIDGenerator actual constructor() {
    actual fun generateUUID(): String {
        return NSUUID().UUIDString()
    }
}