package com.ridi.books.helper.activity

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * Created by kering on 15. 7. 27..
 */
abstract class SimpleActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}
}

open class VisibleActivityCounter : SimpleActivityLifecycleCallbacks() {
    /**
     * non-negative number of the visible activities
     */
    var visibleActivitiesCount = 0
        private set

    override fun onActivityResumed(activity: Activity) {
        if (isIgnoringActivity(activity).not()) {
            visibleActivitiesCount = Math.max(1, visibleActivitiesCount + 1)
            onVisibleActivitiesCountIncreased()
        }
    }

    override fun onActivityPaused(activity: Activity) {
        if (isIgnoringActivity(activity).not()) {
            visibleActivitiesCount = Math.max(0, visibleActivitiesCount - 1)
            onVisibleActivitiesCountDecreased()
        }
    }

    open fun onVisibleActivitiesCountIncreased() {}

    open fun onVisibleActivitiesCountDecreased() {}

    open fun isIgnoringActivity(activity: Activity) = false
}
