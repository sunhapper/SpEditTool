package me.sunhapper.spcharedittool.emoji

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.common_emoj_widget_emojicon.view.*
import me.sunhapper.spcharedittool.R
import me.sunhapper.spcharedittool.emoji.EmojiManager.OnUnzipSuccessListener
import me.sunhapper.spcharedittool.emoji.EmojiconPagerView.EaseEmojiconPagerViewListener
import me.sunhapper.spcharedittool.emoji.EmojiconScrollTabBar.EaseScrollTabBarItemClickListener
import java.util.*

/**
 * 表情图片控件
 */
class EmojiconMenu : EmojiconMenuBase {

    private var emojiconColumns: Int = 0
    private var emojiconGroupList: MutableList<EmojiconGroupEntity> = ArrayList()


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.common_emoj_widget_emojicon, this)
        val ta = context.obtainStyledAttributes(attrs, R.styleable.EmojiconMenu)
        emojiconColumns = ta.getInt(R.styleable.EmojiconMenu_emojiconColumns, DEFAULT_COLUMNS)
        ta.recycle()

        EmojiManager.getDefaultEmojiData(object : OnUnzipSuccessListener {
            override fun onUnzipSuccess(defaultGifEmojis: List<DefaultGifEmoji>) {
                initDefault(defaultGifEmojis)
            }
        })


    }


    private fun initDefault(defaultGifEmojis: List<DefaultGifEmoji>) {
        emojiconGroupList = ArrayList()
        emojiconGroupList.add(EmojiconGroupEntity(R.drawable.common_emoj_smile,
                defaultGifEmojis))
        for (groupEntity in emojiconGroupList) {
            tab_bar.addTab(groupEntity.icon)
        }

        pager_view.setPagerViewListener(EmojiconPagerViewListener())
        pager_view.init(emojiconGroupList, emojiconColumns)

        tab_bar.setTabBarItemClickListener(object : EaseScrollTabBarItemClickListener {

            override fun onItemClick(position: Int) {
                pager_view.setGroupPostion(position)
            }
        })

    }


    /**
     * 添加表情组
     */
    fun addEmojiconGroup(groupEntity: EmojiconGroupEntity) {
        emojiconGroupList.add(groupEntity)
        pager_view.addEmojiconGroup(groupEntity, true)
        tab_bar.addTab(groupEntity.icon)
    }

    /**
     * 添加一系列表情组
     */
    fun addEmojiconGroup(groupEntitieList: List<EmojiconGroupEntity>) {
        for (i in groupEntitieList.indices) {
            val groupEntity = groupEntitieList[i]
            emojiconGroupList.add(groupEntity)
            pager_view.addEmojiconGroup(groupEntity, if (i == groupEntitieList.size - 1) true else false)
            tab_bar.addTab(groupEntity.icon)
        }

    }

    /**
     * 移除表情组
     */
    fun removeEmojiconGroup(position: Int) {
        emojiconGroupList.removeAt(position)
        pager_view.removeEmojiconGroup(position)
        tab_bar.removeTab(position)
    }

    fun setTabBarVisibility(visibility: Int) {
        tab_bar.visibility = visibility
    }


    private inner class EmojiconPagerViewListener : EaseEmojiconPagerViewListener {
        override fun onExpressionClicked(emoji: Emoji) {
            listener?.invoke(emoji)
        }

        override fun onPagerViewInited(groupMaxPageSize: Int, firstGroupPageSize: Int) {
            indicator_view.init(groupMaxPageSize)
            indicator_view.updateIndicator(firstGroupPageSize)
            tab_bar.selectedTo(0)
        }

        override fun onGroupPositionChanged(groupPosition: Int, pagerSizeOfGroup: Int) {
            indicator_view.updateIndicator(pagerSizeOfGroup)
            tab_bar.selectedTo(groupPosition)
        }

        override fun onGroupInnerPagePostionChanged(oldPosition: Int, newPosition: Int) {
            indicator_view.selectTo(oldPosition, newPosition)
        }

        override fun onGroupPagePostionChangedTo(position: Int) {
            indicator_view.selectTo(position)
        }

        override fun onGroupMaxPageSizeChanged(maxCount: Int) {
            indicator_view.updateIndicator(maxCount)
        }


    }

    companion object {
        private val DEFAULT_COLUMNS = 7
    }

}
