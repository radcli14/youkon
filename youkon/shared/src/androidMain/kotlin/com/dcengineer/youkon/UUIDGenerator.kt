package com.dcengineer.youkon

import java.util.UUID

actual class UUIDGenerator actual constructor() {
    actual fun generateUUID(): String {
        return UUID.randomUUID().toString()
    }
}