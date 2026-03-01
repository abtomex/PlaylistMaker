package dom.dima.practicum.playlistmaker.utils

import android.content.Context
import android.util.TypedValue

class Useful {
    companion object {
        fun dpToPx(dp: Float, context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
            ).toInt()
        }

        fun itemsText(count: Int, variant1: String, variant2: String, variant3: String): String {
            if (count in 11 .. 14) return variant3
            return when (count % 10) {
                1 -> variant1
                2, 3, 4 -> variant2
                else -> variant3
            }
        }
    }
}