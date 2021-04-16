package no.mhl.showroom.util

import android.content.res.Resources


/**
 * Used for returning an Int that is ordinal into a zero index origin, by simply minusing 1 from it.
 */
val Int.indexOrigin
    get() = this - 1

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()