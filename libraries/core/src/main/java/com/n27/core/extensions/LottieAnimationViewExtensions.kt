package com.n27.core.extensions

import com.airbnb.lottie.LottieAnimationView

fun LottieAnimationView.playErrorAnimation() {
    playAnimation()

    addAnimatorUpdateListener {
        val progress = it.animatedFraction

        if (progress in 0.67F..0.68F) {
            removeAllUpdateListeners()
            pauseAnimation()
        }
    }
}
