package utilities

import android.app.Activity
import android.content.Context
import com.google.android.play.core.review.ReviewManagerFactory

actual fun requestReview(activityContext: Any?) {
    if (activityContext is Activity) {
        val manager = ReviewManagerFactory.create(activityContext as Context)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                manager.launchReviewFlow(activityContext, reviewInfo)
            }
        }
    }
}
