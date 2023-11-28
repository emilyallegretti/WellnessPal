package com.bignerdranch.android.wellnesspal//package com.bignerdranch.android.wellnesspal

import android.content.res.Resources
import android.provider.MediaStore.Images.Media.getBitmap
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher


class DrawableMatcher(val expectedId:Int) : TypeSafeMatcher<View?>() {


        protected override fun matchesSafely(target: View?): Boolean {
            if (target !is ImageView) {
                return false
            }
            val imageView = target
            if (expectedId < 0) {
                return imageView.drawable == null
            }
            val resources: Resources = target.getContext().resources
            val expectedDrawable = resources.getDrawable(expectedId,null) ?: return false
            val bitmap =imageView.drawable.toBitmap()
            val otherBitmap = expectedDrawable.toBitmap()
            return bitmap.sameAs(otherBitmap)
        }

        override fun describeTo(description: Description?) {
            if (description != null) {
                description.appendText("with drawable from resource id: ")
            };
            if (description != null) {
                description.appendValue(expectedId)
            };
//            if (resourceName != null) {
//                description.appendText("[");
//                description.appendText(resourceName);
//                description.appendText("]");
//            }
        }
    }
