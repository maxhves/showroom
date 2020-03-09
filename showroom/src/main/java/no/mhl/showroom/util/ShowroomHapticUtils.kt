package no.mhl.showroom.util

import android.view.HapticFeedbackConstants
import android.view.View

/**
 * This utility function is used exclusively to physically denote we have reached the end of
 * a data-set via [View.performHapticFeedback]. This function simply cuts out some boilerplate code.
 */
fun View.performEndOfDataSetHaptic() {
    isHapticFeedbackEnabled = true
    performHapticFeedback(
        HapticFeedbackConstants.VIRTUAL_KEY,
        HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
    )
}