package me.shouheng.suix.widget

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import me.shouheng.suix.R
import me.shouheng.suix.databinding.ActivityLayoutListBinding
import me.shouheng.suix.dialog.getSimpleGridData
import me.shouheng.uix.widget.button.SwitchButton
import me.shouheng.uix.widget.image.CircleImageView
import me.shouheng.uix.widget.rv.MultiItemAdapter
import me.shouheng.uix.widget.rv.MultiTypeEntity
import me.shouheng.uix.widget.rv.createAdapter
import me.shouheng.uix.widget.rv.multiTypeAdapter
import me.shouheng.vmlib.base.CommonActivity
import me.shouheng.vmlib.comn.EmptyViewModel

/** List layout sample page. */
class ListLayoutActivity : CommonActivity<EmptyViewModel, ActivityLayoutListBinding>() {

    override fun getLayoutResId(): Int = R.layout.activity_layout_list

    override fun doCreateView(savedInstanceState: Bundle?) {
        binding.v.bind(binding.rv)
        binding.rv.setEmptyView(binding.ev)
        val adapter = generateAdapter()
        binding.rv.adapter = adapter
        adapter.setNewData(mockData())
    }

    private fun generateAdapter(): MultiItemAdapter<IListSampleItem> {
        return multiTypeAdapter {
            addType(ListSampleItemRV::class.java) {
                layout = R.layout.layout_rv
                converter = { helper, item ->
                    val rv = helper.getView<RecyclerView>(R.id.rv)
                    rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    rv.adapter = createAdapter<Int> {
                        layout = R.layout.item_color
                        converter = { helper, item ->
                            helper.getView<CircleImageView>(R.id.iv).setFillingCircleColor(item)
                        }
                        data = item.data
                    }
                }
            }
            addType(ListSampleItemRVGrid::class.java) {
                layout = R.layout.layout_rv
                converter = { helper, item ->
                    val rv = helper.getView<RecyclerView>(R.id.rv)
                    rv.layoutManager = GridLayoutManager(context, 4)
                    rv.adapter = createAdapter<Int> {
                        layout = R.layout.item_tool_color
                        converter = { helper, item ->
                            helper.getView<CircleImageView>(R.id.iv).setFillingCircleColor(item)
                        }
                        data = item.data
                    }
                }
            }
            addType(ListSampleItemV::class.java) {
                layout = R.layout.uix_item_setting_text
                converter = { helper, item ->
                    helper.setText(R.id.tv_title, item.title)
                    helper.setText(R.id.tv_foot, item.foot)
                }
            }
            addType(ListSampleItemSwitch::class.java) {
                layout = R.layout.uix_item_setting_switch
                converter = { helper, item ->
                    helper.setText(R.id.tv_title, item.title)
                    val sb = helper.getView(R.id.sb) as SwitchButton
                    sb.isChecked = item.checked
                }
            }
            addType(ListSampleItemCard::class.java) {
                layout = R.layout.layout_scene_card_item
            }
        }
    }

    private fun mockData(): List<IListSampleItem> {
        return listOf(
            ListSampleItemCard(),
            ListSampleItemRVGrid(),
            ListSampleItemV("The title x1", "The foot x1"),
            ListSampleItemV("The title x2", "The foot x2"),
            ListSampleItemV("The title x3", "The foot x3"),
            ListSampleItemV("The title x4", "The foot x4"),
            ListSampleItemRV(),
            ListSampleItemCard(),
            ListSampleItemSwitch("The title x5", false),
            ListSampleItemSwitch("The title x6", true),
            ListSampleItemCard(),
            ListSampleItemSwitch("The title x7", false),
            ListSampleItemSwitch("The title x8", true),
            ListSampleItemCard(),
            ListSampleItemRVGrid()
        )
    }
}

interface IListSampleItem: MultiTypeEntity

class ListSampleItemRV: IListSampleItem {
    var data: List<Int> = getSimpleGridData()
}

class ListSampleItemRVGrid: IListSampleItem {
    var data: List<Int> = getSimpleGridData()
}

data class ListSampleItemV(
    var title: String = "",
    var foot: String = ""
): IListSampleItem

data class ListSampleItemSwitch(
    var title: String = "",
    var checked: Boolean = false
): IListSampleItem

class ListSampleItemCard: IListSampleItem
