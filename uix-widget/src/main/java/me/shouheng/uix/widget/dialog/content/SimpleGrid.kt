package me.shouheng.uix.widget.dialog.content

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseViewHolder
import me.shouheng.uix.common.anno.BeautyDialogDSL
import me.shouheng.uix.widget.databinding.UixDialogContentListSimpleBinding
import me.shouheng.uix.widget.dialog.BeautyDialog
import me.shouheng.uix.widget.rv.getAdapter

class SimpleGrid<T> private constructor(
    private val itemLayout:Int,
    private val converter: (helper: BaseViewHolder, item: T) -> Unit
): ViewBindingDialogContent<UixDialogContentListSimpleBinding>() {

    private lateinit var dialog: BeautyDialog
    private var itemClickListener: ((dialog: BeautyDialog, item: T) -> Unit)? = null
    private var list: List<T> = emptyList()
    private var spanCount = 4

    override fun doCreateView(ctx: Context) {
        val adapter = getAdapter(itemLayout, converter, list)
        binding.rv.adapter = adapter
        binding.rv.layoutManager = GridLayoutManager(ctx, spanCount)
        adapter.setOnItemClickListener { _, _, pos -> itemClickListener?.invoke(dialog, adapter.data[pos]) }
    }

    override fun setDialog(dialog: BeautyDialog) {
        this.dialog = dialog
    }

    @BeautyDialogDSL
    class Builder<T> constructor(
        @LayoutRes val itemLayout:Int,
        private val converter: (helper: BaseViewHolder, item: T) -> Unit
    ) {
        var list: List<T> = emptyList()
        var onItemSelected: ((dialog: BeautyDialog, item: T) -> Unit)? = null
        var spanCount = 4

        fun setSpanCount(spanCount: Int): Builder<T> {
            this.spanCount = spanCount
            return this
        }

        fun setList(list: List<T>): Builder<T> {
            this.list = list
            return this
        }

        fun setOnItemClickListener(itemClickListener: (dialog: BeautyDialog, item: T) -> Unit): Builder<T> {
            this.onItemSelected = itemClickListener
            return this
        }

        fun build(): SimpleGrid<T> {
            val simpleList = SimpleGrid(itemLayout, converter)
            simpleList.list = list
            simpleList.spanCount = spanCount
            simpleList.itemClickListener = onItemSelected
            return simpleList
        }
    }
}

/** Create a simple grid by DSL. */
inline fun <T> simpleGrid(
    @LayoutRes layout:Int,
    noinline converter: (helper: BaseViewHolder, item: T) -> Unit,
    init: SimpleGrid.Builder<T>.() -> Unit
): SimpleGrid<T> =
    SimpleGrid.Builder(layout, converter)
        .also(init)
        .build()
