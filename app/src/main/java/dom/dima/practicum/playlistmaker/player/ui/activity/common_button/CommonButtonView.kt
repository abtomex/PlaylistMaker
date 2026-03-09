package dom.dima.practicum.playlistmaker.player.ui.activity.common_button

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import dom.dima.practicum.playlistmaker.R

class CommonButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var buttonDrawable: Drawable? = null

    var isPlaying: Boolean = false
        set(value) {
            field = value
            updateButtonIcon()
            invalidate()
        }

    init {
        val size = resources.getDimensionPixelSize(R.dimen.common_button_size)

        if (layoutParams == null) {
            layoutParams = ViewGroup.LayoutParams(size, size)
        } else {
            layoutParams?.width = size
            layoutParams?.height = size
        }

        updateButtonIcon()

        isClickable = true
        isFocusable = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val buttonSize = resources.getDimensionPixelSize(
            R.dimen.common_button_size
        )
        setMeasuredDimension(buttonSize, buttonSize)
    }

    private fun updateButtonIcon() {
        buttonDrawable = ContextCompat.getDrawable(
            context,
            if (isPlaying) R.drawable.button_pause else R.drawable.button_play
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        buttonDrawable?.let { drawable ->
            drawable.setBounds(
                paddingLeft,
                paddingTop,
                width - paddingRight,
                height - paddingBottom
            )
            drawable.draw(canvas)
        }
    }

    fun setButtonImage(@DrawableRes resId: Int) {
        buttonDrawable = ContextCompat.getDrawable(context, resId)
        invalidate()
    }
}