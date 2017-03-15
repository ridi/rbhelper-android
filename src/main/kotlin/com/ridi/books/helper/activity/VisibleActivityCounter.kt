package com.ridi.books.helper.activity

import android.app.Activity

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
