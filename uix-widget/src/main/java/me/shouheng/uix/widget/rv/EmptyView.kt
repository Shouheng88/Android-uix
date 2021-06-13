package me.shouheng.uix.widget.rv

import android.content.Context
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import me.shouheng.uix.common.anno.BeautyDialogDSL
import me.shouheng.uix.common.anno.EmptyViewState
import me.shouheng.uix.common.anno.LoadingStyle
import me.shouheng.uix.widget.R

/**
 * 列表为空控件的一个实现类
 *
 * @author <a href="mailto:shouheng2015@gmail.com">WngShhng</a>
 * @version 2019-10-22 22:21
 */
class EmptyView : FrameLayout, IEmptyView {

    @LoadingStyle
    var loadingStyle: Int = LoadingStyle.STYLE_ANDROID
        set(value) {
            field = value
            isAndroidStyle = loadingStyle == LoadingStyle.STYLE_ANDROID
            ivLoading.visibility = if (isAndroidStyle) View.GONE else View.VISIBLE
            pb.visibility = if (isAndroidStyle) View.VISIBLE else View.GONE
        }
    @EmptyViewState
    var emptyViewState: Int = EmptyViewState.STATE_LOADING
        set(value) {
            field = value
            isLoading = emptyViewState == EmptyViewState.STATE_LOADING
            loading.visibility = if (isLoading) View.VISIBLE else View.GONE
            empty.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    var emptyImage: Int? = null
        set(value) {
            field = value
            if (emptyImage != null) ivEmpty.setImageResource(emptyImage!!)
            else ivEmpty.visibility = View.GONE
        }
    var emptyTitle: String? = null
        set(value) {
            field = value
            tvEmptyTitle.text = value
        }
    @ColorInt
    var emptyTitleColor: Int? = null
        set(value) {
            field = value
            if (value != null) tvEmptyTitle.setTextColor(value)
        }
    var emptyDetails: String? = null
        set(value) {
            field = value
            tvEmptyDetail.text = value
        }
    @ColorInt
    var emptyDetailsColor: Int? = null
        set(value) {
            field = value
            if (value != null) tvEmptyDetail.setTextColor(value)
        }
    var emptyLoadingTips: String? = null
        set(value) {
            field = value
            tvLoading.text = value
        }
    @ColorInt
    var emptyLoadingTipsColor: Int? = null
        set(value) {
            field = value
            if (value != null) tvLoading.setTextColor(value)
        }

    private lateinit var loading: View
    private lateinit var empty: View
    private lateinit var ivLoading: ImageView
    private lateinit var tvLoading: TextView
    private lateinit var pb: ProgressBar
    private lateinit var ivEmpty: ImageView
    private lateinit var tvEmptyTitle: TextView
    private lateinit var tvEmptyDetail: TextView

    private var isLoading: Boolean = false
    private var isAndroidStyle: Boolean = true

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.uix_layout_empty_view,this, true)

        loading = findViewById(R.id.ll_loading)
        empty = findViewById(R.id.ll_empty)
        ivLoading = findViewById(R.id.iv_loading)
        tvLoading = findViewById(R.id.tv_loading)
        pb = findViewById(R.id.pb)
        ivEmpty = findViewById(R.id.iv_empty)
        tvEmptyTitle = findViewById(R.id.tv_empty_title)
        tvEmptyDetail = findViewById(R.id.tv_empty_detail)

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.EmptyView)
            loadingStyle = typedArray.getInt(R.styleable.EmptyView_empty_loading_style, LoadingStyle.STYLE_ANDROID)
            emptyViewState = typedArray.getInt(R.styleable.EmptyView_empty_state, EmptyViewState.STATE_LOADING)

            if (typedArray.hasValue(R.styleable.EmptyView_empty_image)) {
                emptyImage = typedArray.getResourceId(R.styleable.EmptyView_empty_image, -1)
            }

            emptyTitle = typedArray.getString(R.styleable.EmptyView_empty_title)
            emptyDetails = typedArray.getString(R.styleable.EmptyView_empty_detail)
            emptyLoadingTips = typedArray.getString(R.styleable.EmptyView_empty_loading_tips)

            if (typedArray.hasValue(R.styleable.EmptyView_empty_title_text_color))
                emptyTitleColor = typedArray.getColor(R.styleable.EmptyView_empty_title_text_color, 0)
            if (typedArray.hasValue(R.styleable.EmptyView_empty_detail_text_color))
                emptyDetailsColor = typedArray.getColor(R.styleable.EmptyView_empty_detail_text_color, 0)
            if (typedArray.hasValue(R.styleable.EmptyView_empty_loading_tips_text_color))
                emptyLoadingTipsColor = typedArray.getColor(R.styleable.EmptyView_empty_loading_tips_text_color, 0)
            typedArray.recycle()
        }

        tvLoading.text = emptyLoadingTips
        if (emptyLoadingTipsColor != null) tvLoading.setTextColor(emptyLoadingTipsColor!!)
        tvEmptyTitle.text = emptyTitle
        if (emptyTitleColor != null) tvEmptyTitle.setTextColor(emptyTitleColor!!)
        tvEmptyDetail.text = emptyDetails
        if (emptyDetailsColor != null) tvEmptyDetail.setTextColor(emptyDetailsColor!!)

        isAndroidStyle = loadingStyle == LoadingStyle.STYLE_ANDROID
        ivLoading.visibility = if (isAndroidStyle) View.GONE else View.VISIBLE
        pb.visibility = if (isAndroidStyle) View.VISIBLE else View.GONE
        val hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.uix_dialog_loading)
        ivLoading.startAnimation(hyperspaceJumpAnimation)

        isLoading = emptyViewState == EmptyViewState.STATE_LOADING
        loading.visibility = if (isLoading) View.VISIBLE else View.GONE
        empty.visibility = if (isLoading) View.GONE else View.VISIBLE

        if (emptyImage != null) ivEmpty.setImageResource(emptyImage!!)
        else ivEmpty.visibility = View.GONE
    }

    override fun showLoading() {
        isLoading = true
        loading.visibility = if (isLoading) View.VISIBLE else View.GONE
        empty.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    override fun showEmpty() {
        isLoading = false
        loading.visibility = if (isLoading) View.VISIBLE else View.GONE
        empty.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    override fun show() {
        this.visibility = View.VISIBLE
    }

    override fun hide() {
        this.visibility = View.GONE
    }

    override fun getView(): View = this

    @BeautyDialogDSL
    class Builder(private var context: Context) {
        @LoadingStyle var style: Int = LoadingStyle.STYLE_ANDROID
        @EmptyViewState var state: Int = EmptyViewState.STATE_LOADING
        var emptyImage: Int? = null
        var emptyTitle: String? = null
        @ColorInt var emptyTitleColor: Int? = null
        var emptyDetails: String? = null
        @ColorInt var emptyDetailsColor: Int? = null
        var loadingTips: String? = null
        @ColorInt var loadingTipsColor: Int? = null

        fun setLoadingStyle(@LoadingStyle loadingStyle: Int): Builder {
            this.style = loadingStyle
            return this
        }

        fun setEmptyViewState(@LoadingStyle emptyViewState: Int): Builder {
            this.state = emptyViewState
            return this
        }

        fun setEmptyImage(emptyImage: Int): Builder {
            this.emptyImage = emptyImage
            return this
        }

        fun setEmptyTitle(emptyTitle: String): Builder {
            this.emptyTitle = emptyTitle
            return this
        }

        fun setEmptyTitleColor(@ColorInt emptyTitleColor: Int): Builder {
            this.emptyTitleColor = emptyTitleColor
            return this
        }

        fun setEmptyDetails(emptyDetails: String): Builder {
            this.emptyDetails = emptyDetails
            return this
        }

        fun setEmptyDetailsColor(@ColorInt emptyDetailsColor: Int): Builder {
            this.emptyDetailsColor = emptyDetailsColor
            return this
        }

        fun setEmptyLoadingTips(emptyLoadingTips: String): Builder {
            this.loadingTips = emptyLoadingTips
            return this
        }

        fun setEmptyLoadingTipsColor(@ColorInt emptyLoadingTipsColor: Int): Builder {
            this.loadingTipsColor = emptyLoadingTipsColor
            return this
        }

        fun build(): EmptyView {
            val emptyView = EmptyView(context)
            emptyView.loadingStyle = style
            emptyView.emptyViewState = state
            emptyView.emptyImage = emptyImage
            emptyView.emptyTitle = emptyTitle
            emptyView.emptyTitleColor = emptyTitleColor
            emptyView.emptyDetails = emptyDetails
            emptyView.emptyDetailsColor = emptyDetailsColor
            emptyView.emptyLoadingTips = loadingTips
            emptyView.emptyLoadingTipsColor = loadingTipsColor
            return emptyView
        }
    }
}

/** Create an empty view by DSL. */
inline fun emptyView(context: Context, init: EmptyView.Builder.() -> Unit): EmptyView {
    val builder = EmptyView.Builder(context)
    builder.init()
    return builder.build()
}
