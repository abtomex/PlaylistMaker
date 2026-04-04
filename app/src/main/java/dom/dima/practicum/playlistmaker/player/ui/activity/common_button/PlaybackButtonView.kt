package dom.dima.practicum.playlistmaker.player.ui.activity.common_button

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import dom.dima.practicum.playlistmaker.R

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = R.style.DefaultCommonButtonStyle
) : View(context, attrs, defStyleAttr, defStyleRes) {

    lateinit var commonButtonListener: () -> Unit

    private var toPlayCommandView: Drawable? = null
    private var toPauseCommandView: Drawable? = null
    enum class ButtonState {
        TO_PLAY, TO_PAUSE
    }

    var state: ButtonState = ButtonState.TO_PLAY
        set(value) {
            field = value
            updateButtonIcon()
            invalidate()
        }

    private var buttonDrawable: Drawable? = null

    init {
        val size = resources.getDimensionPixelSize(R.dimen.common_button_size)
        layoutParams = ViewGroup.LayoutParams(size, size)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CommonButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            toPlayCommandView = getDrawable(R.styleable.CommonButtonView_toPlayCommandView)
            toPauseCommandView = getDrawable(R.styleable.CommonButtonView_toPauseCommandView)
            val stateValue = getInt(R.styleable.CommonButtonView_state, 0)
            state = when (stateValue) {
                0 -> ButtonState.TO_PLAY
                else -> ButtonState.TO_PAUSE
            }

            recycle()
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
        buttonDrawable =
            when (state) {
                ButtonState.TO_PLAY -> toPlayCommandView
                ButtonState.TO_PAUSE -> toPauseCommandView
            }
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

    fun setPlaying() {
        state = ButtonState.TO_PAUSE
    }

    fun setPaused() {
        state = ButtonState.TO_PLAY
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }
            MotionEvent.ACTION_UP -> {
                commonButtonListener.invoke()
                return true
            }
        }
        return super.onTouchEvent(event)
    }





}