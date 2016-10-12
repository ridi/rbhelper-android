package com.ridi.books.helper.view

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Interpolator
import android.view.animation.TranslateAnimation

private fun Animation.setup(duration: Int, fillAfter: Boolean,
                            interpolator: Interpolator?, listener: SimpleAnimationListener?): Animation {
    this.duration = (if (AnimationHelper.animationEnabled) duration else 0).toLong()
    this.fillAfter = fillAfter
    interpolator?.let { this.interpolator = it }
    setAnimationListener(listener)
    return this
}

@JvmOverloads
fun View?.alphaAnimation(from: Float, to: Float, duration: Int,
                         fillAfter: Boolean, interpolator: Interpolator? = null) =
        this?.startAnimation(AnimationHelper.makeAlphaAnimation(from, to, duration, fillAfter, interpolator, null))

@JvmOverloads
fun View?.translateAnimation(fromX: Float, toX: Float, fromY: Float, toY: Float,
                             duration: Int, fillAfter: Boolean, interpolator: Interpolator? = null) =
        this?.startAnimation(AnimationHelper.makeTranslateAnimation(fromX, toX, fromY, toY, duration, fillAfter, interpolator, null))

/**
 * Created by mspark on 2015. 2. 25..
 */
object AnimationHelper {
    var animationEnabled = true

    @JvmStatic
    @JvmOverloads
    fun makeAlphaAnimation(from: Float, to: Float, duration: Int, fillAfter: Boolean,
                           interpolator: Interpolator? = null,
                           listener: SimpleAnimationListener? = null) =
            AlphaAnimation(from, to).setup(duration, fillAfter, interpolator, listener)

    @JvmStatic
    @JvmOverloads
    fun makeTranslateAnimation(fromX: Float, toX: Float, fromY: Float, toY: Float,
                               duration: Int, fillAfter: Boolean,
                               interpolator: Interpolator? = null,
                               listener: SimpleAnimationListener? = null) =
            TranslateAnimation(fromX, toX, fromY, toY).setup(duration, fillAfter, interpolator, listener)
}

abstract class SimpleAnimationListener : Animation.AnimationListener {
    override fun onAnimationStart(animation: Animation) {
    }

    override fun onAnimationEnd(animation: Animation) {
    }

    override fun onAnimationRepeat(animation: Animation) {
    }
}
