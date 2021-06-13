package me.shouheng.uix.widget.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.Px
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import me.shouheng.uix.common.anno.BeautyDialogDSL
import me.shouheng.uix.common.anno.DialogPosition
import me.shouheng.uix.common.anno.DialogPosition.Companion.POS_BOTTOM
import me.shouheng.uix.common.anno.DialogPosition.Companion.POS_CENTER
import me.shouheng.uix.common.anno.DialogPosition.Companion.POS_TOP
import me.shouheng.uix.common.anno.DialogStyle
import me.shouheng.uix.common.anno.DialogStyle.Companion.STYLE_HALF
import me.shouheng.uix.common.anno.DialogStyle.Companion.STYLE_MATCH
import me.shouheng.uix.common.anno.DialogStyle.Companion.STYLE_TWO_THIRD
import me.shouheng.uix.common.anno.DialogStyle.Companion.STYLE_WRAP
import me.shouheng.uix.common.utils.UImage
import me.shouheng.uix.common.utils.URes
import me.shouheng.uix.common.utils.UView
import me.shouheng.uix.widget.R
import me.shouheng.uix.widget.dialog.content.IDialogContent
import me.shouheng.uix.widget.dialog.footer.IDialogFooter
import me.shouheng.uix.widget.dialog.title.IDialogTitle

/**
 * 对话框
 *
 * 示例程序：
 *
 * @sample
 *
 * ```java
 *     BeautyDialog.Builder()
 *             .setDialogTitle(SimpleTitle.Builder()
 *                     .setTitle("普通编辑对话框")
 *                     .build())
 *             .setDialogContent(SimpleEditor.Builder()
 *                     .setClearDrawable(ResUtils.getDrawable(R.drawable.ic_cancel_black_24dp))
 *                     .build())
 *             .setDialogBottom(SimpleFooter.Builder()
 *                     .setBottomStyle(BUTTON_THREE)
 *                     .setLeftText("Left")
 *                     .setMiddleText("Middle")
 *                     .setRightText("Right")
 *                     .build())
 *             .build().show(supportFragmentManager, "editor")
 * ```
 *
 * @author <a href="mailto:shouheng2015@gmail.com">WngShhng</a>
 * @version 2019-10-12 18:35
 */
class BeautyDialog : DialogFragment() {

    private var iDialogTitle: IDialogTitle? = null
    private var iDialogContent: IDialogContent? = null
    private var iDialogFooter: IDialogFooter? = null

    private var dialogPosition: Int = POS_CENTER
    private var dialogStyle: Int = STYLE_MATCH
    var dialogDarkStyle: Boolean = false
        private set

    private var outCancelable = GlobalConfig.outCancelable
    private var backCancelable = GlobalConfig.backCancelable
    private var customBackground = false

    private var onDismissListener: ((dialog: BeautyDialog) -> Unit)? = null
    private var onShowListener: ((dialog: BeautyDialog) -> Unit)? = null

    private var fixedHeight = 0
    private var dialogMargin = GlobalConfig.margin
    private var dialogBackground: Drawable? = null
    var dialogCornerRadius = GlobalConfig.cornerRadius
        private set

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val theme = when(dialogStyle) {
            STYLE_WRAP -> R.style.BeautyDialogWrap
            STYLE_HALF -> R.style.BeautyDialogHalf
            STYLE_TWO_THIRD -> R.style.BeautyDialogTwoThird
            else -> R.style.BeautyDialog
        }
        val dialog = AlertDialog.Builder(context!!, theme).create()

        val content = View.inflate(context, R.layout.uix_dialog_layout, null)
        if (customBackground) {
            content.background = dialogBackground
        } else {
            val dialogColor = if (dialogDarkStyle) GlobalConfig.darkBGColor else GlobalConfig.lightBGColor
            content.background = UImage.getGradientDrawable(dialogColor, dialogCornerRadius.toFloat())
        }
        val llTitle = content.findViewById<LinearLayout>(R.id.ll_title)
        val llContent = content.findViewById<LinearLayout>(R.id.ll_content)
        val llFooter = content.findViewById<LinearLayout>(R.id.ll_footer)

        iDialogTitle?.setDialog(this)
        iDialogContent?.setDialog(this)
        iDialogFooter?.setDialog(this)
        iDialogTitle?.setDialogContent(iDialogContent)
        iDialogTitle?.setDialogFooter(iDialogFooter)
        iDialogContent?.setDialogTitle(iDialogTitle)
        iDialogContent?.setDialogFooter(iDialogFooter)
        iDialogFooter?.setDialogTitle(iDialogTitle)
        iDialogFooter?.setDialogContent(iDialogContent)

        if (iDialogTitle != null) {
            val titleView = iDialogTitle!!.getView(context!!)
            llTitle.addView(titleView)
            val lp = titleView.layoutParams
            lp.height = WRAP_CONTENT
            titleView.layoutParams = lp
        } else {
            llTitle.visibility = View.GONE
        }
        if (iDialogContent != null) {
            val contentView = iDialogContent!!.getView(context!!)
            llContent.addView(contentView)
            val lp = contentView.layoutParams
            lp.height = if (fixedHeight == 0) MATCH_PARENT else fixedHeight
            contentView.layoutParams = lp
        } else {
            llContent.visibility = View.GONE
        }
        if (iDialogFooter != null) {
            val footerView = iDialogFooter!!.getView(context!!)
            llFooter.addView(footerView)
            val lp = footerView.layoutParams
            lp.height = WRAP_CONTENT
            footerView.layoutParams = lp
        } else {
            llFooter.visibility = View.GONE
        }

        when(dialogPosition) {
            POS_TOP -> {
                dialog.window?.setGravity(Gravity.TOP)
                dialog.window?.setWindowAnimations(R.style.DialogTopAnimation)
            }
            POS_CENTER -> {
                dialog.window?.setGravity(Gravity.CENTER)
                dialog.window?.setWindowAnimations(R.style.DialogCenterAnimation)
            }
            POS_BOTTOM -> {
                dialog.window?.setGravity(Gravity.BOTTOM)
                dialog.window?.setWindowAnimations(R.style.DialogBottomAnimation)
            }
        }

        // fix 2020-07-05 : invalid
        // dialog.setOnDismissListener { onDismissListener?.onOnDismiss(this) }
        dialog.setOnShowListener { onShowListener?.invoke(this) }

        dialog.setCanceledOnTouchOutside(outCancelable)
        dialog.setCancelable(backCancelable)
        dialog.setOnKeyListener { _, keyCode, _ ->
            if (!backCancelable && keyCode == KeyEvent.KEYCODE_BACK) {
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        dialog.setView(content, dialogMargin, dialogMargin, dialogMargin, dialogMargin)

        return dialog
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        onDismissListener?.invoke(this)
    }

    @BeautyDialogDSL
    class Builder {
        var dialogTitle: IDialogTitle? = null
        var dialogContent: IDialogContent? = null
        var dialogFooter: IDialogFooter? = null

        @DialogPosition var position: Int = POS_CENTER
        @DialogStyle var style: Int = STYLE_MATCH
        var dark: Boolean = false

        var outCancelable = GlobalConfig.outCancelable
        var backCancelable = GlobalConfig.backCancelable
        private var customBackground = false

        var onDismiss: ((dialog: BeautyDialog) -> Unit)? = null
        var onShow: ((dialog: BeautyDialog) -> Unit)? = null

        var height = 0
        var margin: Int = GlobalConfig.margin
        var background: Drawable? = null
            set(value) {
                field = value
                customBackground = true
            }
        var cornerRadius: Int = GlobalConfig.cornerRadius

        fun setDialogTitle(iDialogTitle: IDialogTitle): Builder {
            this.dialogTitle = iDialogTitle
            return this
        }

        fun setDialogContent(iDialogContent: IDialogContent): Builder {
            this.dialogContent = iDialogContent
            return this
        }

        fun setDialogBottom(iDialogFooter: IDialogFooter): Builder {
            this.dialogFooter = iDialogFooter
            return this
        }

        fun setDialogPosition(@DialogPosition dialogPosition: Int): Builder {
            this.position = dialogPosition
            return this
        }

        fun setDialogStyle(@DialogStyle dialogStyle: Int): Builder {
            this.style = dialogStyle
            return this
        }

        fun setOutCancelable(outCancelable: Boolean): Builder {
            this.outCancelable = outCancelable
            return this
        }

        fun setBackCancelable(backCancelable: Boolean): Builder {
            this.backCancelable = backCancelable
            return this
        }

        fun onDismiss(onDismissListener: (dialog: BeautyDialog) -> Unit): Builder {
            this.onDismiss = onDismissListener
            return this
        }

        fun onShow(onShowListener: (dialog: BeautyDialog) -> Unit): Builder {
            this.onShow = onShowListener
            return this
        }

        /** 对话框"内容"的固定高度，单位 px */
        fun setFixedHeight(@Px fixedHeight: Int): Builder {
            this.height = fixedHeight
            return this
        }

        /** 对话框边距，单位 px */
        fun setDialogMargin(dialogMargin: Int): Builder {
            this.margin = dialogMargin
            return this
        }

        fun setDarkDialog(darkDialog: Boolean): Builder {
            this.dark = darkDialog
            return this
        }

        /**
         * 设置对话框的背景：
         * 1. 如果不调用这个方法将根据主题使用默认的背景，否则将会直接使用设置的背景，即使是 null.
         * 2. 当传入的参数为 null 的时候对话框将不使用任何背景。
         */
        fun setDialogBackground(dialogBackground: Drawable?): Builder {
            this.background = dialogBackground
            customBackground = true
            return this
        }

        /** 对话框的默认背景的边角的大小，单位：px */
        fun setDialogCornerRadius(dialogCornerRadius: Int): Builder {
            this.cornerRadius = dialogCornerRadius
            return this
        }

        fun build(): BeautyDialog {
            val dialog = BeautyDialog()
            dialog.iDialogTitle = dialogTitle
            dialog.iDialogContent = dialogContent
            dialog.iDialogFooter = dialogFooter
            dialog.dialogPosition = position
            dialog.dialogStyle = style
            dialog.dialogDarkStyle = dark
            dialog.outCancelable = outCancelable
            dialog.backCancelable = backCancelable
            dialog.onDismissListener = onDismiss
            dialog.onShowListener = onShow
            dialog.fixedHeight = height
            dialog.dialogMargin = margin
            dialog.dialogBackground = background
            dialog.customBackground = customBackground
            dialog.dialogCornerRadius = cornerRadius
            return dialog
        }
    }

    object GlobalConfig {
        /** 对话框边距，单位 px */
        @Px var margin: Int                     = UView.dp2px(20f)
        /** 对话框圆角，单位 px */
        @Px var cornerRadius: Int               = UView.dp2px(15f)
        /** 对话框明主题背景色 */
        @ColorInt var lightBGColor: Int         = URes.getColor(R.color.uix_default_light_bg_color)
        /** 对话框暗主题背景色 */
        @ColorInt var darkBGColor: Int          = URes.getColor(R.color.uix_default_dark_bg_color)
        /** 点击对话框外部是否可以取消对话框的全局配置 */
        var outCancelable                       = true
        /** 点击返回按钮是否可以取消对话框的全局配置 */
        var backCancelable                      = true
    }
}

/** Create a beauty dialog by DSL style creator. */
inline fun createDialog(init: BeautyDialog.Builder.() -> Unit): BeautyDialog =
    BeautyDialog.Builder()
        .apply(init)
        .build()

/** Create and show dialog in activity. */
inline fun AppCompatActivity.showDialog(
    tag: String,
    init: BeautyDialog.Builder.() -> Unit
): BeautyDialog =
    BeautyDialog.Builder()
        .apply(init)
        .build()
        .apply { show(supportFragmentManager, tag) }

/** Create and show dialog in fragment. */
inline fun Fragment.showDialog(
    tag: String,
    init: BeautyDialog.Builder.() -> Unit
): BeautyDialog =
    BeautyDialog.Builder()
        .apply(init)
        .build()
        .apply { show(fragmentManager, tag) }
