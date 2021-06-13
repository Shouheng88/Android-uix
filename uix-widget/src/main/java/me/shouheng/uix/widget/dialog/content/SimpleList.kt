package me.shouheng.uix.widget.dialog.content

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.LinearLayoutManager
import me.shouheng.uix.common.anno.BeautyDialogDSL
import me.shouheng.uix.common.bean.TextStyleBean
import me.shouheng.uix.widget.R
import me.shouheng.uix.widget.databinding.UixDialogContentListSimpleBinding
import me.shouheng.uix.widget.dialog.BeautyDialog
import me.shouheng.uix.widget.rv.getAdapter
import me.shouheng.uix.widget.rv.goneIf
import me.shouheng.uix.widget.text.NormalTextView

/**
 * Simple list dialog content
 *
 * @author <a href="mailto:shouheng2015@gmail.com">WngShhng</a>
 * @version 2019-10-15 19:06
 */
class SimpleList private constructor(): ViewBindingDialogContent<UixDialogContentListSimpleBinding>() {

    private lateinit var dialog: BeautyDialog

    private var list: List<Item> = emptyList()
    private var showIcon = true
    private var itemClickListener: ((dialog: BeautyDialog, item: Item) -> Unit)? = null
    private var textStyle = GlobalConfig.textStyle

    override fun doCreateView(ctx: Context) {
        val adapter = getAdapter(R.layout.uix_dialog_content_list_simple_item, { helper, item ->
            val tv = helper.getView<NormalTextView>(R.id.tv)
            tv.text = item.content
            tv.setStyle(textStyle, GlobalConfig.textStyle)
            item.gravity?.let { tv.gravity = it }
            item.icon?.let { helper.setImageDrawable(R.id.iv, it) }
            helper.goneIf(R.id.iv, !showIcon)
        }, list)
        binding.rv.adapter = adapter
        binding.rv.layoutManager = LinearLayoutManager(ctx)
        adapter.setOnItemClickListener { _, _, pos -> itemClickListener?.invoke(dialog, adapter.data[pos]) }
    }

    override fun setDialog(dialog: BeautyDialog) {
        this.dialog = dialog
    }

    @BeautyDialogDSL
    class Builder {
        var list: List<Item> = emptyList()
        var showIcon = true
        var textStyle = GlobalConfig.textStyle
        var onItemSelected: ((dialog: BeautyDialog, item: Item) -> Unit)? = null

        fun setList(list: List<Item>): Builder {
            this.list = list
            return this
        }

        fun setTextStyle(textStyle: TextStyleBean): Builder {
            this.textStyle = textStyle
            return this
        }

        fun setShowIcon(showIcon: Boolean): Builder {
            this.showIcon = showIcon
            return this
        }

        fun setOnItemClickListener(itemClickListener: (dialog: BeautyDialog, item: Item) -> Unit): Builder {
            this.onItemSelected = itemClickListener
            return this
        }

        fun build(): SimpleList {
            val simpleList = SimpleList()
            simpleList.list = list
            simpleList.textStyle = textStyle
            simpleList.showIcon = showIcon
            simpleList.itemClickListener = onItemSelected
            return simpleList
        }
    }

    object GlobalConfig {
        var textStyle = TextStyleBean()
    }

    data class Item(val id: Int, var content: CharSequence?, var icon: Drawable?, var gravity: Int? = null)

    companion object {

        fun get(items: List<Item>): SimpleList = Builder().setList(items).build()

        fun builder(): Builder = Builder()
    }
}

/** Create a simple list by DSL. */
inline fun simpleList(init: SimpleList.Builder.() -> Unit): SimpleList =
    SimpleList.Builder()
        .apply(init)
        .build()
