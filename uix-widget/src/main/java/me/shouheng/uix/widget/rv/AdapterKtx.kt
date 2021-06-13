@file:JvmName("AdapterUtils")
package me.shouheng.uix.widget.rv

import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.view.View
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import java.lang.IllegalArgumentException

@DslMarker
annotation class AdapterDSL

@AdapterDSL
class AdapterBuilder <T> {
    @LayoutRes var layout: Int? = null
    var converter: (helper: BaseViewHolder, item: T) -> Unit =  { _,_ ->  }
    var data: List<T> = emptyList()
}

/** Create an adapter by DSL. */
inline fun <T> createAdapter(
    init: AdapterBuilder<T>.() -> Unit
): Adapter<T> {
    val builder = AdapterBuilder<T>().apply(init)
    if (builder.layout == null)
        throw IllegalArgumentException("item layout is required!")
    return getAdapter(builder.layout!!, builder.converter, builder.data)
}

/**
 * Get a quick adapter.
 *
 * @param itemLayout the item layout
 * @param converter  how the data is set to its layout
 * @param data       the data
 */
fun <T> getAdapter(
    @LayoutRes itemLayout:Int,
    converter: (helper: BaseViewHolder, item: T) -> Unit,
    data: List<T>
): Adapter<T> = Adapter(itemLayout, converter, data)

/** The adapter with single type. */
class Adapter<T>(
    @LayoutRes private val layout: Int,
    private val converter: (helper: BaseViewHolder, item: T) -> Unit,
    val list: List<T>
): BaseQuickAdapter<T, BaseViewHolder>(layout, list) {
    override fun convert(helper: BaseViewHolder, item: T) {
        converter(helper, item)
    }
}

/** Make given view gone if satisfy given condition defined by [goneIf]. */
fun BaseViewHolder.goneIf(@IdRes id: Int, goneIf: Boolean) {
    this.getView<View>(id).visibility = if (goneIf) View.GONE else View.VISIBLE
}

/** Add click listener to views group. */
fun BaseViewHolder.addOnClickListeners(@IdRes vararg ids: Int) {
    ids.forEach { addOnClickListener(it) }
}

/**
 * Get a multiple item type adapter. The first parameter is a list of triple with
 * the first element view type, second element the layout of type, third element
 * the converter for item.
 */
fun <T : MultiItemEntity> getMultiItemAdapter(
    converters: Map<Int, Pair<Int, (helper: BaseViewHolder, item: T) -> Unit>>,
    data: List<T>
): MultiItemAdapter<T> = MultiItemAdapter(converters, data)

/** Multiple item adapter. */
class MultiItemAdapter<T : MultiItemEntity>(
    converters: Map<Int, Pair<Int, (helper: BaseViewHolder, item: T) -> Unit>>,
    list: List<T>
): BaseMultiItemQuickAdapter<T, BaseViewHolder>(list) {

    private val map = mutableMapOf<Int, (helper: BaseViewHolder, item: T) -> Unit>()

    init {
        converters.forEach {
            val itemType = it.key
            val itemLayout = it.value.first
            val converter = it.value.second
            addItemType(itemType, itemLayout)
            map[itemType] = converter
        }
    }

    override fun convert(helper: BaseViewHolder, item: T) {
        map[item.itemType]?.invoke(helper, item)
    }
}
