package no.mhl.showroom.util

import android.view.View
import android.widget.TextView

/**
 * Function to setup a description text
 * @param description the string of text that should be set as the description
 */
fun TextView.setDescription(description: String?) {
    if (description.isNullOrEmpty()) {
        visibility = View.GONE
    } else {
        visibility = View.VISIBLE
        text = description
    }
}

/**
 * Function to setup a count text
 * @param positon the current position aka the counter value
 * @param total the total size of the data set
 */
@SuppressWarnings("SetTextI18n")
fun TextView.setCount(positon: Int, total: Int) {
    text = "(${positon + 1}/$total)"
}