package me.shouheng.uix.widget.dialog.content

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import me.shouheng.uix.widget.databinding.UixDialogContentEditSimpleBinding
import me.shouheng.uix.common.anno.BeautyDialogDSL
import me.shouheng.uix.widget.dialog.footer.IDialogFooter
import me.shouheng.uix.widget.dialog.title.IDialogTitle
import me.shouheng.utils.ktx.gone

/**
 * Simple dialog editor content
 *
 * @author <a href="mailto:shouheng2015@gmail.com">WngShhng</a>
 * @version 2019-10-14 11:24
 */
class SimpleEditor private constructor(): ViewBindingDialogContent<UixDialogContentEditSimpleBinding>(), TextWatcher {

    private var content: String? = null
    private var hint: String? = null
    private var textColor: Int? = null
    private var hintTextColor: Int? = null
    private var lengthTextColor: Int? = null
    private var bottomLineColor: Int? = null
    private var textSize: Float = 16f
    private var singleLine = false
    private var numeric = false
    private var inputRegex: String? = null
    private var maxLength: Int? = null
    private var maxLines: Int? = null
    private var showLength = true
    private var clearDrawable: Drawable? = null

    private var dialogTitle: IDialogTitle? = null
    private var dialogFooter: IDialogFooter? = null

    override fun doCreateView(ctx: Context) {
        binding.et.addTextChangedListener(this)
        binding.et.setText(content)
        binding.et.hint = hint
        textColor?.let { binding.et.setTextColor(it) }
        hintTextColor?.let { binding.et.setHintTextColor(it) }
        lengthTextColor?.let { binding.tv.setTextColor(it) }
        bottomLineColor?.let { binding.v.setBackgroundColor(it) }
        binding.et.textSize = textSize
        binding.et.setSingleLine(singleLine)
        if (numeric) binding.et.addInputType(EditorInfo.TYPE_CLASS_NUMBER)
        inputRegex?.let { binding.et.inputRegex = inputRegex }
        maxLength?.let { binding.et.addFilters(InputFilter.LengthFilter(it)) }
        maxLines?.let { binding.et.maxLines = it }
        if (!showLength) binding.tv.gone()
        clearDrawable?.let { binding.et.setClearDrawable(it) }
    }

    fun getContent(): Editable? = binding.et.text

    override fun afterTextChanged(s: Editable?) {
        if (showLength) {
            val text = if (maxLength != null)
                "${binding.et.text?.length?:0}/${maxLength}"
            else "${binding.et.text?.length?:0}"
            binding.tv.text = text
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /*noop*/ }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { /*noop*/ }

    override fun setDialogTitle(dialogTitle: IDialogTitle?) {
        this.dialogTitle = dialogTitle
    }

    override fun setDialogFooter(dialogFooter: IDialogFooter?) {
        this.dialogFooter = dialogFooter
    }

    @BeautyDialogDSL
    class Builder {
        var content: String? = null
        var hint: String? = null
        var textColor: Int? = null
        var hintTextColor: Int? = null
        var lengthTextColor: Int? = null
        var bottomLineColor: Int? = null
        var textSize: Float = 16f
        var singleLine = false
        var numeric = false
        var inputRegex: String? = null
        var maxLength: Int? = null
        var maxLines: Int? = null
        var showLength = true
        var clearDrawable: Drawable? = null

        fun setContent(content: String): Builder {
            this.content = content
            return this
        }

        fun setHint(hint: String): Builder {
            this.hint = hint
            return this
        }

        fun setTextColor(textColor: Int): Builder {
            this.textColor = textColor
            return this
        }

        fun setHintTextColor(hintTextColor: Int): Builder {
            this.hintTextColor = hintTextColor
            return this
        }

        fun setClearDrawable(clearDrawable: Drawable): Builder {
            this.clearDrawable = clearDrawable
            return this
        }

        fun setLengthTextColor(lengthTextColor: Int): Builder {
            this.lengthTextColor = lengthTextColor
            return this
        }

        fun setBottomLineColor(bottomLineColor: Int): Builder {
            this.bottomLineColor = bottomLineColor
            return this
        }

        fun setTextSize(textSize: Float): Builder {
            this.textSize = textSize
            return this
        }

        fun setSingleLine(singleLine: Boolean): Builder {
            this.singleLine = singleLine
            return this
        }

        fun setNumeric(numeric: Boolean): Builder {
            this.numeric = numeric
            return this
        }

        fun setInputRegex(inputRegex: String): Builder {
            this.inputRegex = inputRegex
            return this
        }

        fun setMaxLength(maxLength: Int): Builder {
            this.maxLength = maxLength
            return this
        }

        fun setMaxLines(maxLines: Int): Builder {
            this.maxLines = maxLines
            return this
        }

        fun setShowLength(showLength: Boolean): Builder {
            this.showLength = showLength
            return this
        }

        fun build(): SimpleEditor {
            val editor = SimpleEditor()
            editor.content = content
            editor.hint = hint
            editor.textColor = textColor
            editor.hintTextColor = hintTextColor
            editor.lengthTextColor = lengthTextColor
            editor.bottomLineColor = bottomLineColor
            editor.textSize = textSize
            editor.singleLine = singleLine
            editor.numeric = numeric
            editor.inputRegex = inputRegex
            editor.maxLength = maxLength
            editor.maxLines = maxLines
            editor.showLength = showLength
            editor.clearDrawable = clearDrawable
            return editor
        }
    }
}

/** Create a simple editor of DSL style. */
inline fun simpleEditor(init: SimpleEditor.Builder.() -> Unit): SimpleEditor {
    val builder = SimpleEditor.Builder()
    builder.init()
    return builder.build()
}
