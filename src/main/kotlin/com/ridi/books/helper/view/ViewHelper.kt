package com.ridi.books.helper.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.TypedValue
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.annotation.AnyRes
import androidx.annotation.AttrRes
import androidx.annotation.BoolRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.IntegerRes
import androidx.annotation.LayoutRes
import androidx.annotation.Px
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ridi.books.helper.Log
import com.ridi.books.helper.annotation.Dp
import kotlin.math.roundToInt

fun ViewGroup.inflate(@LayoutRes resId: Int): View = LayoutInflater.from(context).inflate(resId, this)

@Suppress("UNCHECKED_CAST")
fun <T : View?> View.find(@IdRes viewId: Int) = findViewById<T>(viewId) as T

@Suppress("UNCHECKED_CAST")
fun <T : View?> Fragment.find(@IdRes viewId: Int) = view!!.findViewById<T>(viewId) as T

@Suppress("UNCHECKED_CAST")
fun <T : View?> Activity.find(@IdRes viewId: Int) = findViewById<T>(viewId) as T

@Suppress("UNCHECKED_CAST")
fun <T : View?> Dialog.find(@IdRes viewId: Int) = findViewById<T>(viewId) as T

@Suppress("UNCHECKED_CAST")
fun <T : View?> Activity.findLazy(@IdRes viewId: Int) = lazy { find<T>(viewId) }

@Suppress("UNCHECKED_CAST")
fun <T : View?> Dialog.findLazy(@IdRes viewId: Int) = lazy { find<T>(viewId) }

@Suppress("UNCHECKED_CAST")
fun <T : View?> View.findLazy(@IdRes viewId: Int) = lazy { find<T>(viewId) }

@Suppress("UNCHECKED_CAST")
fun <T : View?> Fragment.findLazy(@IdRes viewId: Int) = lazy { find<T>(viewId) }

@Px fun Context.dip(@Dp value: Int) = dip(value.toFloat())

@Px fun View.dip(@Dp value: Int) = context.dip(value)

@Px fun Context.dip(@Dp value: Float) = dip(value, false).toInt()

@Px fun View.dip(@Dp value: Float) = context.dip(value)

fun Context.dip(@Dp value: Int, accurate: Boolean) = dip(value.toFloat(), accurate)

fun View.dip(@Dp value: Int, accurate: Boolean) = context.dip(value.toFloat(), accurate)

fun Context.dip(@Dp value: Float, accurate: Boolean): Float {
    val result = value * resources.displayMetrics.density
    return if (accurate) result else result.roundToInt().toFloat()
}

fun View.dip(@Dp value: Float, sharp: Boolean) = context.dip(value, sharp)

@Dp fun Context.px(@Px value: Int) = px(value.toFloat())

@Dp fun View.px(@Px value: Int) = context.px(value)

@Dp fun Context.px(@Px value: Float) = px(value, false).toInt()

@Dp fun View.px(@Px value: Float) = context.px(value)

@Dp fun Context.px(@Px value: Int, accurate: Boolean) = px(value.toFloat(), accurate)

@Dp fun View.px(@Px value: Int, accurate: Boolean) = context.px(value.toFloat(), accurate)

@Dp fun Context.px(@Px value: Float, accurate: Boolean): Float {
    val result = value / resources.displayMetrics.density
    return if (accurate) result else result.roundToInt().toFloat()
}

@Dp fun View.px(@Px value: Float, sharp: Boolean) = context.px(value, sharp)

@Px fun Context.dimen(@DimenRes resId: Int) = resources.getDimensionPixelSize(resId)

@Px fun View.dimen(@DimenRes resId: Int) = context.dimen(resId)

@ColorInt fun Context.color(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)

@ColorInt fun View.color(@ColorRes resId: Int) = context.color(resId)

fun Context.drawable(@DrawableRes resId: Int) = ContextCompat.getDrawable(this, resId)!!

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

private fun Context.getSystemBarHeight(resourceName: String): Int {
    val resourceId = resources.getIdentifier(resourceName, "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else {
        0
    }
}

fun Context.getStatusBarHeight() = getSystemBarHeight("status_bar_height")

fun Context.getNavigationBarHeight() = getSystemBarHeight("navigation_bar_height")

fun Context.isNavigationBarOnScreen(): Boolean {
    val id = resources.getIdentifier("config_showNavigationBar", "bool", "android")
    return if (id > 0) {
        bool(id)
    } else {
        ViewConfiguration.get(this).hasPermanentMenuKey().not() &&
            KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK).not()
    }
}

// For view hierarchy inspection
fun ViewGroup.logChildren() {
    fun ViewGroup.logChildrenRecursively(level: Int) {
        Log.d(javaClass, id.toString() + " - ViewGroup at level " + level)
        (0 until childCount)
            .map { getChildAt(it) }
            .forEach { child ->
                when (child) {
                    is ViewGroup -> child.logChildrenRecursively(level + 1)
                    else -> Log.d(child.javaClass, "${child.id}")
                }
            }
    }
    logChildrenRecursively(0)
}

fun View.drawToBitmap(isHighQuality: Boolean = true): Bitmap? {
    if (width == 0 || height == 0) return null

    val bitmap = Bitmap.createBitmap(
        width, height,
        if (isHighQuality) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
    ).apply {
        density = Bitmap.DENSITY_NONE
    }
    draw(Canvas(bitmap))
    return bitmap
}
