package com.ridi.books.helper

import android.app.Activity
import android.app.Dialog
import android.app.Fragment
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.*
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by mspark on 2015. 6. 25..
 */
fun ViewGroup.inflate(@LayoutRes resId: Int): View = LayoutInflater.from(context).inflate(resId, this)

@Suppress("UNCHECKED_CAST")
fun <T : View> View.find(@IdRes viewId: Int) = findViewById(viewId) as T

@Suppress("UNCHECKED_CAST")
fun <T : View> Fragment.find(@IdRes viewId: Int) = view.findViewById(viewId) as T

@Suppress("UNCHECKED_CAST")
fun <T : View> Activity.find(@IdRes viewId: Int) = findViewById(viewId) as T

@Suppress("UNCHECKED_CAST")
fun <T : View> Dialog.find(@IdRes viewId: Int) = findViewById(viewId) as T

@Suppress("UNCHECKED_CAST")
fun <T : View> Fragment.findLazy(@IdRes viewId: Int) = lazy { find<T>(viewId) }

@Suppress("UNCHECKED_CAST")
fun <T : View> Activity.findLazy(@IdRes viewId: Int) = lazy { find<T>(viewId) }

@Suppress("UNCHECKED_CAST")
fun <T : View> Dialog.findLazy(@IdRes viewId: Int) = lazy { find<T>(viewId) }

@Suppress("UNCHECKED_CAST")
fun <T : View> View.findLazy(@IdRes viewId: Int) = lazy { find<T>(viewId) }

@Dimension fun Context.dip(value: Int) = dip(value.toFloat())

@Dimension fun View.dip(value: Int) = context.dip(value)

@Dimension fun Context.dip(value: Float) = Math.round(value * resources.displayMetrics.density)

@Dimension fun View.dip(value: Float) = context.dip(value)

@Dimension fun Context.dimen(@DimenRes resId: Int) = resources.getDimensionPixelSize(resId)

@Dimension fun View.dimen(@DimenRes resId: Int) = context.dimen(resId)

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

fun View.setBackgroundCompat(background: Drawable?) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        this.background = background
    } else {
        @Suppress("DEPRECATION")
        setBackgroundDrawable(background)
    }
}

// For view hierarchy inspection
fun ViewGroup.logChildren() {
    fun ViewGroup.logChildrenRecursively(level: Int) {
        RBLog.d(javaClass, id.toString() + " - ViewGroup at level " + level)
        for (i in 0..childCount - 1) {
            val child = getChildAt(i)
            when (child) {
                is ViewGroup -> child.logChildrenRecursively(level + 1)
                else -> RBLog.d(child.javaClass, child.id.toString())
            }
        }
    }
    logChildrenRecursively(0)
}
