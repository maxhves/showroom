package no.finn.granite.util

import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class GraniteActivityUtils {

    // region Static Functions and Properties
    companion object {

        /**
         * In the case of an app that is having issues regarding the status bar this function can
         * be used to setup an existing activity to allow the status bar to behave nicely.
         * @param activity the existing activity that GraniteImageGallery will be implemented into
         */
        fun setupExistingActivityForImageGallery(activity: AppCompatActivity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                activity.window.attributes.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }

            activity.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

    }
    // endregion

}