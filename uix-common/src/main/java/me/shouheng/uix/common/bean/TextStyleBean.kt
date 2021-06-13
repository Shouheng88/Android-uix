package me.shouheng.uix.common.bean

import android.support.annotation.ColorInt
import android.support.annotation.Size
import me.shouheng.uix.common.anno.BeautyDialogDSL
import java.io.Serializable

data class TextStyleBean(
    @ColorInt var textColor: Int? = null,
    @Size var textSize: Float? = null,
    var typeFace: Int? = null,
    var gravity: Int?  = null
): Serializable

@BeautyDialogDSL
class TextStyleBeanBuilder {
    @ColorInt var color: Int? = null
    @Size var size: Float? = null
    var typeFace: Int? = null
    var gravity: Int? = null
}

fun textStyle(init: TextStyleBeanBuilder.() -> Unit): TextStyleBean {
    val builder = TextStyleBeanBuilder()
    builder.init()
    return TextStyleBean(
        builder.color,
        builder.size,
        builder.typeFace,
        builder.gravity
    )
}
