package com.ridi.books.helper.view

import android.app.Activity
import android.app.Dialog
import android.app.Fragment
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.*
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.*
import com.ridi.books.helper.Log
import com.ridi.books.helper.annotation.Dp

/**
 * Created by mspark on 2015. 6. 25..
 */
fun ViewGroup.inflate(@LayoutRes resId: Int): View = LayoutInflater.from(context).inflate(resId, this)

inline fun <reified T : View> View.find(@IdRes viewId: Int) = findViewById(viewId) as T

inline fun <reified T : View> Fragment.find(@IdRes viewId: Int) = view.findViewById(viewId) as T

inline fun <reified T : View> Activity.find(@IdRes viewId: Int) = findViewById(viewId) as T

inline fun <reified T : View> Dialog.find(@IdRes viewId: Int) = findViewById(viewId) as T

inline fun <reified T : View> Fragment.findLazy(@IdRes viewId: Int) = lazy { find<T>(viewId) }

inline fun <reified T : View> Activity.findLazy(@IdRes viewId: Int) = lazy { find<T>(viewId) }

inline fun <reified T : View> Dialog.findLazy(@IdRes viewId: Int) = lazy { find<T>(viewId) }

inline fun <reified T : View> View.findLazy(@IdRes viewId: Int) = lazy { find<T>(viewId) }

@Px fun Context.dip(@Dp value: Int) = dip(value.toFloat())

@Px fun View.dip(@Dp value: Int) = context.dip(value)

@Px fun Context.dip(@Dp value: Float) = Math.round(value * resources.displayMetrics.density)

@Px fun View.dip(@Dp value: Float) = context.dip(value)

@Px fun Context.dimen(@DimenRes resId: Int) = resources.getDimensionPixelSize(resId)

@Px fun View.dimen(@DimenRes resId: Int) = context.dimen(resId)

@ColorInt fun Context.color(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)

@ColorInt fun View.color(@ColorRes resId: Int) = context.color(resId)

fun Context.drawable(@DrawableRes resId: Int) = ContextCompat.getDrawable(this, resId)

fun View.drawable(@DrawableRes resId: Int) = context.drawable(resId)

@AnyRes fun Context.attr(@AttrRes attrId: Int) = when (attrId) {
    0 -> 0
    else -> {
        val typedValue = TypedValue()
        theme.resolveAttribute(attrId, typedValue, true)
        typedValue.resourceId
    }
}

@AnyRes fun View.attr(@AttrRes resId: Int) = context.attr(resId)

fun Context.bool(@BoolRes resId: Int) = resources.getBoolean(resId)

fun View.bool(@BoolRes resId: Int) = context.bool(resId)

fun Context.integer(@IntegerRes resId: Int) = resources.getInteger(resId)

fun View.integer(@IntegerRes resId: Int) = context.integer(resId)

fun View.setBackgroundCompat(background: Drawable?) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        this.background = background
    } else {
        @Suppress("DEPRECATION")
        setBackgroundDrawable(background)
    }
}

private fun Context.getSystemBarHeight(resourceName: String): Int {
    val resourceId = resources.getIdentifier(resourceName, "dimen", "android")
    if (resourceId > 0) {
        return resources.getDimensionPixelSize(resourceId)
    } else {
        return 0
    }
}

fun Context.getStatusBarHeight() = getSystemBarHeight("status_bar_height")

fun Context.getNavigationBarHeight() = getSystemBarHeight("navigation_bar_height")

fun Context.isNavigationBarOnScreen(): Boolean {
    val id = resources.getIdentifier("config_showNavigationBar", "bool", "android")
    if (id > 0) {
        return bool(id)
    } else {
        return ViewConfiguration.get(this).hasPermanentMenuKey().not()
                && KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK).not()
    }
}

// For view hierarchy inspection
fun ViewGroup.logChildren() {
    fun ViewGroup.logChildrenRecursively(level: Int) {
        Log.d(javaClass, id.toString() + " - ViewGroup at level " + level)
        for (i in 0..childCount - 1) {
            val child = getChildAt(i)
            when (child) {
                is ViewGroup -> child.logChildrenRecursively(level + 1)
                else -> Log.d(child.javaClass, child.id.toString())
            }
        }
    }
    logChildrenRecursively(0)
}
