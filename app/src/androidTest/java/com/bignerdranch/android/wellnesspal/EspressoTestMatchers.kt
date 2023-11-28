package com.bignerdranch.android.wellnesspal

import android.view.View
import org.hamcrest.Matcher

class EspressoTestMatchers {

    companion object {
        fun withDrawable(resourceId: Int): Matcher<View?>? {
            return DrawableMatcher(resourceId)
        }
    }

}