package my.edu.tarumt.ecolution.reward

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Reward(
    @StringRes val stringResourceId: Int,
    @DrawableRes val imageResourceId: Int
)
