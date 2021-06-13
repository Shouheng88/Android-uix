package me.shouheng.uix.widget.dialog.content

import android.content.Context
import android.graphics.Typeface
import android.support.annotation.ColorInt
import android.support.annotation.Size
import android.view.Gravity
import me.shouheng.uix.common.anno.BeautyDialogDSL
import me.shouheng.uix.widget.databinding.UixDialogContentSimpleBinding

/**
 * Simple dialog content for text
 *
 * @author <a href="mailto:shouheng2015@gmail.com">WngShhng</a>
 * @version 2019-10-13 09:46
 */
class SimpleContent private constructor(): ViewBindingDialogContent<UixDialogContentSimpleBinding>() {

    private var content: CharSequence? = null
    @ColorInt private var contentColor: Int? = GlobalConfig.contentTextColor
    @Size private var contentSize: Float = GlobalConfig.contentTextSize
    private var typeFace: Int = GlobalConfig.contentTypeFace
    private var gravity: Int = GlobalConfig.contentGravity

    override fun doCreateView(ctx: Context) {
        binding.tv.text = content
        binding.tv.textSize = contentSize
        binding.tv.gravity = gravity
        binding.tv.setTypeface(null, typeFace)
        contentColor?.let { binding.tv.setTextColor(it) }
    }

    @BeautyDialogDSL
    class Builder {
        var content: CharSequence? = null
        @ColorInt var contentColor: Int? = GlobalConfig.contentTextColor
        @Size var contentSize: Float = GlobalConfig.contentTextSize
        var typeFace: Int = GlobalConfig.contentTypeFace
        var gravity: Int = GlobalConfig.contentGravity

        fun setContent(content: CharSequence): Builder {
            this.content = content
            return this
        }

        fun setContentColor(@ColorInt contentColor: Int): Builder {
            this.contentColor = contentColor
            return this
        }

        fun setContentSize(contentSize: Float): Builder {
            this.contentSize = contentSize
            return this
        }

        fun setContentTypeface(typeFace: Int): Builder {
            this.typeFace = typeFace
            return this
        }

        fun setGravity(gravity: Int): Builder {
            this.gravity = gravity
            return this
        }

        fun build(): SimpleContent {
            val simpleContent = SimpleContent()
            simpleContent.content = content
            simpleContent.contentColor = contentColor
            simpleContent.contentSize = contentSize
            simpleContent.typeFace = typeFace
            simpleContent.gravity = gravity
            return simpleContent
        }
    }

    companion object {

        fun get(title: CharSequence): SimpleContent = Builder().setContent(title).build()

        fun builder(): Builder = Builder()
    }

    object GlobalConfig {
        @ColorInt var contentTextColor: Int?      = null
        @Size var contentTextSize: Float          = 16f
        var contentTypeFace: Int                  = Typeface.NORMAL
        var contentGravity: Int                   = Gravity.CENTER
    }
}

/** Create a simple content by DSL. */
inline fun simpleContent(init: SimpleContent.Builder.() -> Unit): SimpleContent =
    SimpleContent.Builder()
        .apply(init)
        .build()
