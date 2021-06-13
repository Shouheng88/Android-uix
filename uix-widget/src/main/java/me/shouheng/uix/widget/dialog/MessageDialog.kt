package me.shouheng.uix.widget.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import me.shouheng.uix.common.anno.LoadingStyle
import me.shouheng.uix.common.bean.TextStyleBean
import me.shouheng.uix.common.utils.UImage
import me.shouheng.uix.common.utils.URes
import me.shouheng.uix.common.utils.UView
import me.shouheng.uix.widget.R
import me.shouheng.uix.widget.text.NormalTextView

/**
 * 加载对话框
 *
 * @author [WngShhng](mailto:shouheng2015@gmail.com)
 * @version 2020-01-12 17:56
 */
class MessageDialog constructor(builder: Builder) {

    private val message: CharSequence?
    private var messageStyle: TextStyleBean = GlobalConfig.textStyle
    @LoadingStyle
    private val loadingStyle: Int
    private val cancelable: Boolean
    private val loading: Boolean
    private val icon: Drawable?
    @ColorInt
    private val bgColor: Int
    private val borderRadius: Int

    init {
        message = builder.message
        loadingStyle = builder.style
        cancelable = builder.cancelable
        loading = builder.loading
        icon = builder.icon
        bgColor = builder.bgColor
        borderRadius = builder.borderRadius
    }

    private fun build(context: Context): Dialog {
        val v = LayoutInflater.from(context).inflate(R.layout.uix_message_dialog, null)
        val ll = v.findViewById(R.id.loading_dialog_view) as LinearLayout
        val img = v.findViewById(R.id.img) as ImageView
        val pb = v.findViewById(R.id.pb) as ProgressBar
        val tv = v.findViewById(R.id.tipTextView) as NormalTextView

        ll.background = UImage.getGradientDrawable(bgColor, borderRadius.toFloat())

        if (icon != null) img.setImageDrawable(icon)
        else img.visibility = View.GONE

        pb.visibility = if (loading) View.VISIBLE else View.GONE
        if (loadingStyle == LoadingStyle.STYLE_IOS) {
            pb.indeterminateDrawable = URes.getDrawable(R.drawable.uix_anim_loading)
        }

        tv.text = message
        tv.setStyle(messageStyle, GlobalConfig.textStyle)

        val dlg = Dialog(context, if (cancelable) R.style.Dialog_Loading_Cancelable else R.style.Dialog_Loading)
        dlg.setCancelable(cancelable)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        dlg.setContentView(ll, params)
        return dlg
    }

    class Builder {
        var message: CharSequence?      = null
        var messageStyle: TextStyleBean = GlobalConfig.textStyle
        var style: Int                  = GlobalConfig.loadingStyle
        var cancelable: Boolean         = GlobalConfig.cancelable
        var icon: Drawable?             = null
        var bgColor: Int                = GlobalConfig.bgColor
        var loading: Boolean            = GlobalConfig.loading
        var borderRadius: Int           = GlobalConfig.bgBorderRadius

        fun withMessage(param: CharSequence): Builder {
            message = param
            return this
        }

        fun withMessageStyle(style: TextStyleBean): Builder {
            messageStyle = style
            return this
        }

        fun withLoadingStyle(@LoadingStyle param: Int): Builder {
            style = param
            return this
        }

        fun withCancelable(param: Boolean): Builder {
            cancelable = param
            return this
        }

        fun withLoading(param: Boolean): Builder {
            loading = param
            return this
        }

        fun withIcon(param: Drawable?): Builder {
            icon = param
            return this
        }

        fun withBgColor(@ColorInt param: Int): Builder {
            bgColor = param
            return this
        }

        fun withBorderRadius(param: Int): Builder {
            borderRadius = param
            return this
        }

        fun build(context: Context): Dialog {
            return MessageDialog(this).build(context)
        }
    }

    companion object {

        fun showLoading(
                context: Context,
                msg: String,
                cancelable: Boolean = GlobalConfig.cancelable,
                isAnimation: Boolean = GlobalConfig.loading,
                icon: Drawable? = null,
                @LoadingStyle loadingStyle: Int = GlobalConfig.loadingStyle
        ): Dialog = Builder()
                .withMessage(msg)
                .withCancelable(cancelable)
                .withLoading(isAnimation)
                .withIcon(icon)
                .withLoadingStyle(loadingStyle)
                .build(context)

        fun showLoading(
                context: Context,
                @StringRes msgResId: Int,
                cancelable: Boolean = GlobalConfig.cancelable,
                isAnimation: Boolean = GlobalConfig.loading,
                @DrawableRes iconResId: Int? = null,
                @LoadingStyle loadingStyle: Int = GlobalConfig.loadingStyle
        ): Dialog = Builder()
                .withMessage(URes.getString(msgResId))
                .withCancelable(cancelable)
                .withLoading(isAnimation)
                .withLoadingStyle(loadingStyle)
                .apply {
                    if (iconResId != null) {
                        this.withIcon(URes.getDrawable(iconResId))
                    }
                }
                .build(context)

        fun builder(): Builder = Builder()

        fun builder(
                msg: String,
                cancelable: Boolean = GlobalConfig.cancelable,
                loading: Boolean = GlobalConfig.loading,
                icon: Drawable? = null,
                @LoadingStyle loadingStyle: Int = GlobalConfig.loadingStyle
        ): Builder = Builder()
                .withMessage(msg)
                .withCancelable(cancelable)
                .withLoading(loading)
                .withIcon(icon)
                .withLoadingStyle(loadingStyle)

        fun builder(
                @StringRes msgResId: Int,
                cancelable: Boolean = GlobalConfig.cancelable,
                loading: Boolean = GlobalConfig.loading,
                @DrawableRes iconResId: Int? = null,
                @LoadingStyle loadingStyle: Int = GlobalConfig.loadingStyle
        ): Builder = Builder()
                .withMessage(URes.getString(msgResId))
                .withCancelable(cancelable)
                .withLoading(loading)
                .withLoadingStyle(loadingStyle)
                .apply {
                    if (iconResId != null) {
                        this.withIcon(URes.getDrawable(iconResId))
                    }
                }

        fun hide(dialog: Dialog?) {
            if (dialog != null && dialog.isShowing) dialog.dismiss()
        }
    }

    object GlobalConfig {
        var textStyle                        = TextStyleBean()
        @LoadingStyle
        var loadingStyle: Int                = LoadingStyle.STYLE_IOS
        var cancelable: Boolean              = true
        var loading: Boolean                 = true
        var bgColor: Int                     = Color.parseColor("#C0000000")
        var bgBorderRadius: Int            = UView.dp2px(8f)
    }
}

/** Create a message dialog by DSL. */
inline fun messageDialog(
    context: Context,
    init: MessageDialog.Builder.() -> Unit
): Dialog =
    MessageDialog.Builder()
        .also(init)
        .build(context)

/** Create a message dialog by DSL. */
@JvmName("showMessageDialog")
inline fun Context.showMessage(
    init: MessageDialog.Builder.() -> Unit
): Dialog =
    MessageDialog.Builder()
        .also(init)
        .build(this)
        .apply { show() }
