@file:JvmName("MultiTypeAdapterUtils")
package me.shouheng.uix.widget.rv

import android.support.annotation.LayoutRes
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import me.shouheng.utils.stability.L

/** Converter builder of DSL. */
@AdapterDSL
class MultiItemAdapterTypeBuilder<T> {
    @LayoutRes var layout: Int? = null
    var converter: (helper: BaseViewHolder, item: T) -> Unit =  { _, _ ->  }
}

/** Adapter builder of DSL. */
@AdapterDSL
class MultiItemAdapterBuilder<T : MultiTypeEntity> {
    private val converters = mutableMapOf<Int, Pair<Int, (helper: BaseViewHolder, item: T) -> Unit>>()
    var data: List<T> = emptyList()

    fun getConverters(): Map<Int, Pair<Int, (helper: BaseViewHolder, item: T) -> Unit>> = converters

    fun <V : T> addType(
        type: Class<V>,
        init: MultiItemAdapterTypeBuilder<V>.() -> Unit
    ) {
        val builder = MultiItemAdapterTypeBuilder<V>()
        builder.apply(init)
        if (builder.layout != null) {
            converters[type.hashCode()] = Pair(builder.layout!!, {
                helper, item: T -> builder.converter.invoke(helper, item as V)
            })
        } else {
            L.d("Invalid type information: layout is required!")
        }
    }
}

/** Create a multi-type adapter by DSL. */
inline fun <T : MultiTypeEntity> multiTypeAdapter(
    init: MultiItemAdapterBuilder<T>.() -> Unit
): MultiItemAdapter<T> {
    val builder = MultiItemAdapterBuilder<T>()
    builder.apply(init)
    return getMultiItemAdapter(builder.getConverters(), builder.data)
}

/** The multi type entity wrapper for multi type adapter. */
interface MultiTypeEntity: MultiItemEntity {

    /** The multi type entity use the hash code of class type as view type. */
    override fun getItemType(): Int = javaClass.hashCode()
}
