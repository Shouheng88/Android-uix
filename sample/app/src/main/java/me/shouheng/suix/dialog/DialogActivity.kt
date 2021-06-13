package me.shouheng.suix.dialog

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import com.chad.library.adapter.base.BaseViewHolder
import me.shouheng.suix.R
import me.shouheng.suix.databinding.ActivityDialogBinding
import me.shouheng.uix.common.anno.AddressSelectLevel.Companion.LEVEL_AREA
import me.shouheng.uix.common.anno.BottomButtonPosition.Companion.isLeft
import me.shouheng.uix.common.anno.BottomButtonPosition.Companion.isRight
import me.shouheng.uix.common.anno.BottomButtonStyle.Companion.BUTTON_STYLE_DOUBLE
import me.shouheng.uix.common.anno.BottomButtonStyle.Companion.BUTTON_STYLE_SINGLE
import me.shouheng.uix.common.anno.BottomButtonStyle.Companion.BUTTON_STYLE_TRIPLE
import me.shouheng.uix.common.anno.DialogPosition.Companion.POS_BOTTOM
import me.shouheng.uix.common.anno.DialogPosition.Companion.POS_TOP
import me.shouheng.uix.common.anno.DialogStyle.Companion.STYLE_HALF
import me.shouheng.uix.common.anno.DialogStyle.Companion.STYLE_TWO_THIRD
import me.shouheng.uix.common.anno.DialogStyle.Companion.STYLE_WRAP
import me.shouheng.uix.common.anno.EmptyViewState
import me.shouheng.uix.common.anno.LoadingStyle
import me.shouheng.uix.common.bean.TextStyleBean
import me.shouheng.uix.common.bean.textStyle
import me.shouheng.uix.pages.rate.RatingManager
import me.shouheng.uix.widget.dialog.BeautyDialog
import me.shouheng.uix.widget.dialog.content.*
import me.shouheng.uix.widget.dialog.createDialog
import me.shouheng.uix.widget.dialog.footer.SimpleFooter
import me.shouheng.uix.widget.dialog.footer.simpleFooter
import me.shouheng.uix.widget.dialog.showDialog
import me.shouheng.uix.widget.dialog.title.SimpleTitle
import me.shouheng.uix.widget.dialog.title.simpleTitle
import me.shouheng.uix.widget.image.CircleImageView
import me.shouheng.uix.widget.rv.emptyView
import me.shouheng.uix.widget.rv.getAdapter
import me.shouheng.uix.widget.rv.onItemDebouncedClick
import me.shouheng.uix.widget.text.NormalTextView
import me.shouheng.utils.app.ResUtils
import me.shouheng.utils.ktx.colorOf
import me.shouheng.utils.ktx.dp2px
import me.shouheng.utils.ktx.drawableOf
import me.shouheng.utils.ktx.onDebouncedClick
import me.shouheng.utils.ui.ViewUtils
import me.shouheng.vmlib.base.CommonActivity
import me.shouheng.vmlib.comn.EmptyViewModel

/**
 * 对话框示例
 *
 * @author <a href="mailto:shouheng2015@gmail.com">WngShhng</a>
 * @version 2019-10-13 15:21
 */
class DialogActivity : CommonActivity<EmptyViewModel, ActivityDialogBinding>() {

    override fun getLayoutResId(): Int = R.layout.activity_dialog

    private var customList: CustomList? = null

    override fun doCreateView(savedInstanceState: Bundle?) {
        val builder = createDialogBuilder()
        binding.btnNoBg.setOnClickListener {
            showDialog("normal") {
                style = STYLE_WRAP
                dialogContent = UpgradeContent()
                background = null
                onDismiss { toast("Dismissed") }
                onShow { toast("Showed") }
            }
        }
        binding.btnNoBgOneThird.onDebouncedClick {
            builder.setDialogStyle(STYLE_TWO_THIRD)
                .build().show(supportFragmentManager, "normal")
        }
        binding.btnNoBgHalf.onDebouncedClick {
            builder.setDialogStyle(STYLE_HALF)
                .build().show(supportFragmentManager, "normal")
        }
        binding.btnNormal.setOnClickListener {
            BeautyDialog.Builder()
                .setDialogStyle(STYLE_WRAP)
                .setDialogTitle(SimpleTitle.Builder()
                    .setTitle("测试标题 [RED|18f]")
                    .setTitleStyle(TextStyleBean().apply {
                        textSize = 18f
                        textColor = Color.RED
                    })
                    .build())
                .setDialogContent(MultipleContent())
                .setDialogBottom(SimpleFooter.Builder()
                    .setBottomStyle(BUTTON_STYLE_SINGLE)
                    .setMiddleText("OK")
                    .setMiddleTextStyle(TextStyleBean().apply {
                        textColor = Color.RED
                        typeFace = Typeface.BOLD
                    })
                    .setOnClickListener { dialog, _, _, _ ->
                        dialog.dismiss()
                    }.build())
                .build().show(supportFragmentManager, "normal")
        }
        onDebouncedClick(binding.btnNormalTop) {
            createDialog {
                position = POS_TOP
                dialogTitle = simpleTitle {
                    title = "测试标题 [BOLD|LEFT]"
                    titleStyle = textStyle {
                        gravity = Gravity.START
                        typeFace = Typeface.BOLD
                    }
                }
                dialogContent = MultipleContent()
                dialogFooter = SampleFooter()
            }.show(supportFragmentManager, "normal_top")
        }
        onDebouncedClick(binding.btnNormalBottom) {
            createDialog {
                dark = true
                position = POS_BOTTOM
                dialogTitle = simpleTitle {
                    title = "测试标题 [WHITE]"
                    titleStyle = textStyle {
                        color = Color.WHITE
                    }
                }
                dialogContent = MultipleContent()
                dialogFooter = simpleFooter {
                    style = BUTTON_STYLE_TRIPLE
                    leftText = "左"
                    middleText = "中"
                    rightText = "右"
                    leftTextStyle = textStyle {
                        color = Color.WHITE
                        size = 14f
                    }
                    middleTextStyle = textStyle {
                        color = Color.WHITE
                        size = 16f
                    }
                    rightTextStyle = textStyle {
                        color = Color.WHITE
                        size = 18f
                    }
                }
            }.show(supportFragmentManager, "normal_bottom")
        }
        onDebouncedClick(binding.btnEditorNormal) {
            // The DSL styled way to create dialog.
            showDialog("editor") {
                style = STYLE_HALF
                cornerRadius = 4f.dp2px()
                dialogTitle = simpleTitle {
                    title = "普通编辑对话框 [无限制]"
                }
                dialogContent = simpleEditor {
                    clearDrawable = drawableOf(R.drawable.ic_cancel_black_24dp)
                }
                dialogFooter = simpleFooter {
                    style = BUTTON_STYLE_TRIPLE
                    leftText = "Left"
                    middleText = "Middle"
                    rightText = "Right"
                    rightTextStyle = textStyle {
                        color = Color.RED
                    }
                    onLeft = { _, _, _ -> toast("Left") }
                    onMiddle = { _, _, _ -> toast("Middle") }
                    onRight = { _, _, _ -> toast("Right") }
                }
            }
        }
        binding.btnEditorNumeric.setOnClickListener {
            BeautyDialog.Builder()
                .setDialogStyle(STYLE_TWO_THIRD)
                .setDialogTitle(SimpleTitle.get("编辑对话框（数字|单行|长度10）"))
                .setDialogContent(SimpleEditor.Builder()
                    .setSingleLine(true)
                    .setNumeric(true)
                    .setContent("10086")
                    .setHint("在这里输入数字...")
                    .setBottomLineColor(Color.LTGRAY)
                    .setMaxLength(10)
                    .build())
                .setDialogBottom(SimpleFooter.Builder()
                    .setBottomStyle(BUTTON_STYLE_DOUBLE)
                    .setLeftText("取消")
                    .setRightText("确定")
                    .setDividerColor(Color.LTGRAY)
                    .setRightTextStyle(TextStyleBean().apply {
                        textColor = Color.GRAY
                    })
                    .setRightTextStyle(TextStyleBean().apply {
                        textColor = Color.RED
                    })
                    .setOnClickListener { dialog, position, _, content ->
                        if (isLeft(position)) dialog.dismiss()
                        else if (isRight(position)) {
                            toast((content as SimpleEditor).getContent())
                        }
                    }.build())
                .build().show(supportFragmentManager, "editor")
        }
        binding.btnListNormal.onDebouncedClick {
            showDialog("list") {
                position = POS_BOTTOM
                dialogTitle = simpleTitle {
                    title = "简单的列表"
                }
                dialogContent = simpleList {
                    textStyle = textStyle {
                        gravity = Gravity.CENTER
                        size = 14f
                        color = Color.BLACK
                        typeFace = Typeface.BOLD
                    }
                    showIcon = true
                    list = getSimpleListData()
                    onItemSelected = { dialog, item ->
                        toast("${item.id} : ${item.content}")
                        dialog.dismiss()
                    }
                }
            }
        }
        binding.btnAddress.onDebouncedClick {
            showDialog("list") {
                position = POS_BOTTOM
                margin = 8f.dp2px()
                dialogTitle = simpleTitle {
                    title = "地址对话框"
                }
                dialogContent = addressContent {
                    maxLevel = LEVEL_AREA
                    onSelected = { d, p, c, a ->
                        toast("$p - $c - $a")
                        d.dismiss()
                    }
                }
            }
        }
        binding.btnContent.onDebouncedClick {
            showDialog("list") {
                style = STYLE_TWO_THIRD
                outCancelable = true
                position = POS_BOTTOM
                dialogTitle = simpleTitle {
                    title = "简单内容对话框"
                }
                dialogContent = simpleContent {
                    content = "君不見黃河之水天上來，奔流到海不復回。" +
                            " 君不見高堂明鏡悲白髮，朝如青絲暮成雪。 " +
                            "人生得意須盡歡，莫使金樽空對月。 " +
                            "天生我材必有用，千金散盡還復來。" +
                            " 烹羊宰牛且爲樂，會須一飲三百杯。" +
                            " 岑夫子，丹丘生。將進酒，杯莫停。" +
                            " 與君歌一曲，請君爲我側耳聽。 " +
                            "鐘鼓饌玉不足貴，但願長醉不願醒。 " +
                            "古來聖賢皆寂寞，惟有飲者留其名。" +
                            " 陳王昔時宴平樂，斗酒十千恣讙謔。" +
                            " 主人何為言少錢？徑須沽取對君酌。 " +
                            "五花馬，千金裘。呼兒將出換美酒，與爾同銷萬古愁。"
                }
            }
        }
        binding.btnCustomList.onDebouncedClick { createCustomList() }
        binding.btnNotCancelable.onDebouncedClick {
            showDialog("not cancelable") {
                style = STYLE_WRAP
                outCancelable = false
                backCancelable = false
                background = null
                dialogContent = UpgradeContent()
            }
        }
        binding.btnRateIntro.onDebouncedClick {
            RatingManager.marketTitle = "测试修改标题"
            RatingManager.rateApp(STYLE_WRAP, {
                toast("跳转到反馈页")
            }, {
                toast("跳转到应用市场评分")
            }, supportFragmentManager)
        }
        binding.btnSimpleGrid.onDebouncedClick {
            showDialog("grid") {
                margin = 0
                position = POS_BOTTOM
                dialogTitle = simpleTitle {
                    title = "简单的网格"
                }
                dialogContent = simpleGrid(
                    R.layout.item_tool_color,
                    { helper: BaseViewHolder, item: Int ->
                        helper.getView<CircleImageView>(R.id.iv).setFillingCircleColor(item)
                    }
                ) {
                    list = getSimpleGridData()
                    onItemSelected = { _: BeautyDialog, item: Int ->
                        toast("$item")
                    }
                    spanCount = 5
                }
            }
        }
    }

    private fun createCustomList() {
        val adapter = getAdapter(R.layout.uix_dialog_content_list_simple_item, { helper, item: SimpleList.Item ->
            val tv = helper.getView<NormalTextView>(me.shouheng.uix.widget.R.id.tv)
            tv.text = item.content
            item.gravity?.let { tv.gravity = it }
            item.icon?.let { helper.setImageDrawable(me.shouheng.uix.widget.R.id.iv, it) }
        }, emptyList())
        val ev = emptyView(context) {
            style = LoadingStyle.STYLE_IOS
            state = EmptyViewState.STATE_LOADING
            loadingTips = "loading"
            loadingTipsColor = Color.BLUE
        }
        customList = customList(context) {
            this.adapter = adapter
            this.emptyView = ev
        }
        adapter.onItemDebouncedClick { _, _, pos ->
            val item = adapter.data[pos]
            toast("${item.id} : ${item.content}")
            customList?.getDialog()?.dismiss()
        }
        createDialog {
            position = POS_BOTTOM
            height = ViewUtils.getScreenHeight()/2
            outCancelable = true
            dialogTitle = simpleTitle {
                title = "自定义列表对话框"
            }
            dialogContent = customList
        }.show(supportFragmentManager, "custom-list")
        // 先显示对话框再加载数据的情形
        Handler().postDelayed({
            ev.hide()
            adapter.setNewData(getCustomListData())
        }, 3000)
    }

    private fun createDialogBuilder(): BeautyDialog.Builder {
        return BeautyDialog.Builder()
            .setDarkDialog(true)
            .setDialogPosition(POS_BOTTOM)
            .setDialogContent(MultipleContent())
            .setDialogTitle(
                simpleTitle {
                    title = "测试标题 [WHITE]"
                    titleStyle = textStyle {
                        color = Color.WHITE
                    }
                })
            .setDialogBottom(
                simpleFooter {
                    style = BUTTON_STYLE_TRIPLE
                    leftText = "左"
                    middleText = "中"
                    rightText = "右"
                    leftTextStyle = textStyle {
                        color = Color.WHITE
                        size = 14f
                    }
                    middleTextStyle = textStyle {
                        color = Color.WHITE
                        size = 16f
                    }
                    rightTextStyle = textStyle {
                        color = Color.WHITE
                        size = 18f
                    }
                })
    }

    private fun getSimpleListData(): List<SimpleList.Item> {
        return listOf(
            SimpleList.Item(
                0,
                "秦时明月汉时关，万里长征人未还。\n" + "但使龙城飞将在，不教胡马度阴山。",
                drawableOf(R.drawable.uix_eye_close_48)
            ),
            SimpleList.Item(
                1,
                "春眠不觉晓，处处闻啼鸟。\n" + "夜来风雨声，花落知多少。",
                drawableOf(R.drawable.uix_eye_open_48)),
            SimpleList.Item(
                2,
                "君自故乡来，应知故乡事。\n" + "来日绮窗前，寒梅著花未？",
                drawableOf(R.drawable.uix_close_black_24dp),
                Gravity.START),
            SimpleList.Item(
                3,
                "松下问童子，言师采药去。\n" + "只在此山中，云深不知处。",
                drawableOf(R.drawable.uix_loading),
                Gravity.END)
        )
    }

    private fun getCustomListData(): List<SimpleList.Item> {
        return listOf(
            SimpleList.Item(0, "第 1 项", ResUtils.getDrawable(R.drawable.uix_eye_close_48)),
            SimpleList.Item(1, "第 2 项", ResUtils.getDrawable(R.drawable.uix_eye_open_48)),
            SimpleList.Item(2, "第 3 项", ResUtils.getDrawable(R.drawable.uix_close_black_24dp)),
            SimpleList.Item(3, "第 4 项", ResUtils.getDrawable(R.drawable.uix_loading)),
            SimpleList.Item(4, "第 5 项", ResUtils.getDrawable(R.drawable.uix_eye_close_48)),
            SimpleList.Item(5, "第 6 项", ResUtils.getDrawable(R.drawable.uix_eye_open_48)),
            SimpleList.Item(6, "第 7 项", ResUtils.getDrawable(R.drawable.uix_close_black_24dp)),
            SimpleList.Item(7, "第 8 项", ResUtils.getDrawable(R.drawable.uix_loading)),
            SimpleList.Item(8, "第 9 项", ResUtils.getDrawable(R.drawable.uix_eye_close_48)),
            SimpleList.Item(9, "第 10 项", ResUtils.getDrawable(R.drawable.uix_eye_open_48)),
            SimpleList.Item(10, "第 11 项", ResUtils.getDrawable(R.drawable.uix_close_black_24dp)),
            SimpleList.Item(11, "第 12 项", ResUtils.getDrawable(R.drawable.uix_loading)),
            SimpleList.Item(12, "第 13 项", ResUtils.getDrawable(R.drawable.uix_eye_close_48)),
            SimpleList.Item(13, "第 14 项", ResUtils.getDrawable(R.drawable.uix_eye_open_48)),
            SimpleList.Item(14, "第 15 项", ResUtils.getDrawable(R.drawable.uix_close_black_24dp)),
            SimpleList.Item(15, "第 16 项", ResUtils.getDrawable(R.drawable.uix_loading))
        )
    }

    private fun getSimpleGridData(): List<Int> {
        return listOf(
            colorOf(R.color.tool_item_color_1),
            colorOf(R.color.tool_item_color_2),
            colorOf(R.color.tool_item_color_3),
            colorOf(R.color.tool_item_color_4),
            colorOf(R.color.tool_item_color_5),
            colorOf(R.color.tool_item_color_6),
            colorOf(R.color.tool_item_color_7),
            colorOf(R.color.tool_item_color_8),
            colorOf(R.color.tool_item_color_9),
            colorOf(R.color.tool_item_color_10),
            colorOf(R.color.tool_item_color_11),
            colorOf(R.color.tool_item_color_12)
        )
    }
}

inline fun onClick(v: View, crossinline onClicked: (v: View) -> Unit) {
    v.setOnClickListener {
        onClicked(it)
    }
}

inline fun onDebouncedClick(v: View, crossinline onClicked: (v: View) -> Unit) {
    v.onDebouncedClick {
        onClicked(it)
    }
}