package me.shouheng.uix.widget.dialog.content

import android.content.Context
import android.view.View
import me.shouheng.uix.common.anno.AddressSelectLevel
import me.shouheng.uix.common.anno.AddressSelectLevel.Companion.LEVEL_AREA
import me.shouheng.uix.common.anno.AddressSelectLevel.Companion.LEVEL_CITY
import me.shouheng.uix.common.anno.BeautyDialogDSL
import me.shouheng.uix.common.bean.AddressBean
import me.shouheng.uix.common.utils.UBiz
import me.shouheng.uix.widget.R
import me.shouheng.uix.widget.databinding.UixDialogContentAddressBinding
import me.shouheng.uix.widget.dialog.BeautyDialog
import me.shouheng.uix.widget.rv.getAdapter
import me.shouheng.uix.widget.rv.onItemDebouncedClick
import me.shouheng.utils.ktx.gone
import me.shouheng.utils.ktx.onDebouncedClick
import me.shouheng.utils.ktx.visible

/**
 * Address pick dialog content
 *
 * @author <a href="mailto:shouheng2015@gmail.com">WngShhng</a>
 * @version 2019-10-05 10:44
 */
class AddressContent: ViewBindingDialogContent<UixDialogContentAddressBinding>() {

    private lateinit var dialog: BeautyDialog
    private val rvList = mutableListOf<View>()

    @AddressSelectLevel private var maxLevel: Int = LEVEL_CITY
    private var listener: ((
        dialog: BeautyDialog,
        province: String,
        city: String?,
        area: String?
    ) -> Unit)? = null

    override fun doCreateView(ctx: Context) {
        val list = UBiz.getAddressList()

        val pAdapter = getAdapter(R.layout.uix_dialog_content_address_item, { helper, item ->
            helper.setText(R.id.tv, item.name)
        }, list)
        val cityAdapter = getAdapter(R.layout.uix_dialog_content_address_item, { helper, item: AddressBean.CityBean ->
            helper.setText(R.id.tv, item.name)
        }, emptyList())
        val areaAdapter = getAdapter(R.layout.uix_dialog_content_address_item, { helper, item: String ->
            helper.setText(R.id.tv, item)
        }, emptyList())

        rvList.addAll(listOf(binding.rvProvince, binding.rvCity, binding.rvArea))

        binding.rvProvince.adapter = pAdapter
        binding.rvCity.adapter = cityAdapter
        binding.rvArea.adapter = areaAdapter

        switchToRv(binding.rvProvince)

        pAdapter.onItemDebouncedClick { _, _, pos ->
            val province = list[pos].name
            val cityBeans = list[pos].city
            cityAdapter.setNewData(cityBeans)
            binding.tvProvince.text = province
            switchToRv(binding.rvCity)
            cityAdapter.onItemDebouncedClick { _, _, cityPos ->
                val city = cityBeans!![cityPos].name
                val areaBeans = cityBeans[cityPos].area
                areaAdapter.setNewData(areaBeans)
                binding.tvCity.text = city
                switchToRv(binding.rvArea)
                if (maxLevel == LEVEL_CITY) {
                    listener?.invoke(dialog, province!!, city, null)
                }
                areaAdapter.onItemDebouncedClick { _, _, areaPos ->
                    val area = areaBeans!![areaPos]
                    binding.tvArea.text = area
                    if (maxLevel == LEVEL_AREA) {
                        listener?.invoke(dialog, province!!, city, area)
                    }
                }
            }
        }

        binding.tvProvince.onDebouncedClick {
            binding.tvCity.text = ""
            binding.tvArea.text = ""
            switchToRv(binding.rvProvince)
        }
        binding.tvCity.onDebouncedClick {
            binding.tvArea.text = ""
            switchToRv(binding.rvCity)
        }
        binding.tvArea.onDebouncedClick { switchToRv(binding.rvArea) }
    }

    private fun switchToRv(rv: View) {
        rv.visible()
        rvList.filter { it != rv }.forEach { it.gone() }
    }

    override fun setDialog(dialog: BeautyDialog) {
        this.dialog = dialog
    }

    fun getSelection(): Triple<String?, String?, String?> {
        return Triple<String?, String?, String?>(
            binding.tvProvince.text.toString(),
            binding.tvCity.text.toString(),
            binding.tvArea.text.toString())
    }

    @BeautyDialogDSL
    class Builder {
        @AddressSelectLevel var maxLevel: Int = LEVEL_CITY

        var onSelected: ((
            dialog: BeautyDialog,
            province: String,
            city: String?,
            area: String?
        ) -> Unit)? = null

        fun setMaxLevel(@AddressSelectLevel maxLevel: Int): Builder {
            this.maxLevel = maxLevel
            return this
        }

        fun setOnAddressSelectedListener(
            listener: (
                dialog: BeautyDialog,
                province: String,
                city: String?,
                area: String?
            ) -> Unit
        ): Builder {
            this.onSelected = listener
            return this
        }

        fun build(): AddressContent {
            val content = AddressContent()
            content.maxLevel = maxLevel
            content.listener = onSelected
            return content
        }
    }
}

/** Create address content by DSL. */
inline fun addressContent(init: AddressContent.Builder.() -> Unit): AddressContent {
    val builder = AddressContent.Builder()
    builder.init()
    return builder.build()
}
