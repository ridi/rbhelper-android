package com.ridi.books.helper.view

import android.animation.Animator

abstract class SimpleAnimatorListener: Animator.AnimatorListener {
    override fun onAnimationRepeat(animation: Animator?) {
        // no-op
    }

    override fun onAnimationEnd(animation: Animator?) {
        // no-op
    }

    override fun onAnimationCancel(animation: Animator?) {
        // no-op
    }

    override fun onAnimationStart(animation: Animator?) {
        // no-op
    }
}
