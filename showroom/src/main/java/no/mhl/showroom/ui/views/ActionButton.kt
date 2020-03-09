package no.mhl.showroom.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import no.mhl.showroom.R

class ActionButton(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    // region View Properties
    private val iconImageView by lazy { findViewById<ImageView>(R.id.icon) }
    private val textView by lazy { findViewById<TextView>(R.id.text) }
    // endregion

    // region Properties
    var onButtonPressed: (() -> Unit)? = null
    // endregion

    // region Initialisation
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_button_action, this)


        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ActionButton,
            0, 0).apply {
            try {
                setupView(
                    getString(R.styleable.ActionButton_text),
                    getResourceId(R.styleable.ActionButton_icon, 0)
                )
            } finally { recycle() }
        }
    }
    // endregion

    // region View Setup
    private fun setupView(text: String? = "", icon: Int) {
        iconImageView.setImageResource(icon)
        textView.text = text

        setOnClickListener { onButtonPressed?.invoke() }
    }
    // endregion

}