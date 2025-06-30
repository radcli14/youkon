package utilities

import platform.StoreKit.SKStoreReviewController

actual fun requestReview(activityContext: Any?) {
    SKStoreReviewController.requestReview()
}
