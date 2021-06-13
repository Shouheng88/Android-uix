package me.shouheng.uix.widget.dialog.title

import android.content.Context
import android.view.Gravity
import me.shouheng.uix.common.bean.TextStyleBean
import me.shouheng.uix.widget.databinding.UixDialogTitleSimpleBinding
import me.shouheng.uix.common.anno.BeautyDialogDSL

/**
 * Simple dialog title
 *
 * @author <a href="mailto:shouheng2015@gmail.com">WngShhng</a>
 * @version 2019-10-13 09:46
 */
class SimpleTitle private constructor(): ViewBindingDialogTitle<UixDialogTitleSimpleBinding>() {

    private var title: CharSequence? = null
    private var titleStyle: TextStyleBean = GlobalConfig.titleStyle

    override fun doCreateView(ctx: Context) {
        binding.tv.text = title
        binding.tv.setStyle(titleStyle, GlobalConfig.titleStyle)
    }

    @BeautyDialogDSL
    class Builder {
        var title: CharSequence? = null
        var titleStyle: TextStyleBean = GlobalConfig.titleStyle

        fun setTitle(title: CharSequence): Builder {
            this.title = title
            return this
        }

        fun setTitleStyle(titleStyle: TextStyleBean): Builder {
            this.titleStyle = titleStyle
            return this
        }

        fun build(): SimpleTitle {
            val simpleTitle = SimpleTitle()
            simpleTitle.title = title
            simpleTitle.titleStyle = titleStyle
            return simpleTitle
        }
    }

    object GlobalConfig {
        var titleStyle = TextStyleBean().apply { gravity=Gravity.CENTER }
    }

    companion object {

        fun get(title: CharSequence): SimpleTitle = Builder().setTitle(title).build()

        fun builder(): Builder = Builder()
    }
}

/** Create a simple title dialog title by DSL. */
inline fun simpleTitle(init: SimpleTitle.Builder.() -> Unit): SimpleTitle {
    val builder = SimpleTitle.Builder()
    builder.init()
    return builder.build()
}