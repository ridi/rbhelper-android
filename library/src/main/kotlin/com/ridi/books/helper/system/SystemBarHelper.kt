package com.ridi.books.helper.system

import android.content.Context
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration
import com.ridi.books.helper.view.bool

/**
 * Created by kering on 2016. 10. 14..
 */

object SystemBarHelper {
    private fun Context.getSystemBarHeight(resourceName: String): Int {
        val resourceId = resources.getIdentifier(resourceName, "dimen", "android")
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId)
        } else {
            return 0
        }
    }

    @JvmStatic
    fun getStatusBarHeight(context: Context) = context.getSystemBarHeight("status_bar_height")

    @JvmStatic
    fun getNavigationBarHeight(context: Context) = context.getSystemBarHeight("navigation_bar_height")

    @JvmStatic
    fun isNavigationBarOnScreen(context: Context): Boolean {
        val id = context.resources.getIdentifier("config_showNavigationBar", "bool", "android")
        if (id > 0) {
            return context.bool(id)
        } else {
            return ViewConfiguration.get(context).hasPermanentMenuKey().not()
                    && KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK).not()
        }
    }
}
