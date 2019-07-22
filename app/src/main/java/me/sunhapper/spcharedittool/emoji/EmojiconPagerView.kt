package me.sunhapper.spcharedittool.emoji

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import me.sunhapper.spcharedittool.R
import java.util.*
import kotlin.math.max

class EmojiconPagerView @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet? = null) : ViewPager(mContext, attrs) {
    private lateinit var groupEntities: List<EmojiconGroupEntity>
    private lateinit var pagerAdapter: PagerAdapter
    private val emojiconRows = 3
    private var emojiconColumns = 7
    private var firstGroupPageSize: Int = 0
    private var maxPageCount: Int = 0
    private var previousPagerPosition: Int = 0
    private var pagerViewListener: EaseEmojiconPagerViewListener? = null
    private lateinit var viewpages: MutableList<View>


    fun init(emojiconGroupList: List<EmojiconGroupEntity>, emijiconColumns: Int) {
        this.groupEntities = emojiconGroupList
        this.emojiconColumns = emijiconColumns

        viewpages = ArrayList()
        for (i in groupEntities.indices) {
            val group = groupEntities[i]
            val gridViews = getGroupGridViews(group)
            if (i == 0) {
                firstGroupPageSize = gridViews.size
            }
            maxPageCount = max(gridViews.size, maxPageCount)
            viewpages.addAll(gridViews)
        }

        pagerAdapter = EmojiconPagerAdapter(viewpages)
        adapter = pagerAdapter
        addOnPageChangeListener(EmojiPagerChangeListener())
        pagerViewListener?.onPagerViewInited(maxPageCount, firstGroupPageSize)
    }

    fun setPagerViewListener(pagerViewListener: EaseEmojiconPagerViewListener) {
        this.pagerViewListener = pagerViewListener
    }


    /**
     * 设置当前表情组位置
     */
    fun setGroupPostion(position: Int) {
        if (adapter != null && position >= 0 && position < groupEntities.size) {
            var count = 0
            for (i in 0 until position) {
                count += getPageSize(groupEntities[i])
            }
            currentItem = count
        }
    }

    /**
     * 获取表情组的gridview list
     */
    private fun getGroupGridViews(groupEntity: EmojiconGroupEntity): List<View> {
        val defaultGifEmojiList = groupEntity.defaultGifEmojiList
        val itemSize = emojiconColumns * emojiconRows - 1
        val totalSize = defaultGifEmojiList.size
        val pageSize = if (totalSize % itemSize == 0) totalSize / itemSize else totalSize / itemSize + 1
        val views = ArrayList<View>()
        for (i in 0 until pageSize) {
            val view = View.inflate(mContext, R.layout.common_emoj_expression_gridview, null)
            val gv = view.findViewById<GridView>(R.id.gridview)
            gv.numColumns = emojiconColumns
            val list = ArrayList<Emoji>()
            if (i != pageSize - 1) {
                list.addAll(defaultGifEmojiList.subList(i * itemSize, (i + 1) * itemSize))
            } else {
                list.addAll(defaultGifEmojiList.subList(i * itemSize, totalSize))
            }
            val deleteIcon = DeleteEmoji()
            list.add(deleteIcon)
            val gridAdapter = EmojiconGridAdapter(mContext, 1, list)
            gv.adapter = gridAdapter
            gv.onItemClickListener = OnItemClickListener { _, _, position, _ ->
                val emoji = gridAdapter.getItem(position)
                pagerViewListener?.onExpressionClicked(emoji!!)
            }

            views.add(view)
        }
        return views
    }


    /**
     * 添加表情组
     */
    fun addEmojiconGroup(groupEntity: EmojiconGroupEntity, notifyDataChange: Boolean) {
        val pageSize = getPageSize(groupEntity)
        if (pageSize > maxPageCount) {
            maxPageCount = pageSize
            pagerViewListener?.onGroupMaxPageSizeChanged(maxPageCount)
        }
        viewpages.addAll(getGroupGridViews(groupEntity))
        if (notifyDataChange) {
            pagerAdapter.notifyDataSetChanged()
        }
    }

    /**
     * 移除表情组
     */
    fun removeEmojiconGroup(position: Int) {
        if (position > groupEntities.size - 1) {
            return
        }
        pagerAdapter.notifyDataSetChanged()
    }


    private fun getPageSize(groupEntity: EmojiconGroupEntity): Int {
        val defaultGifEmojiList = groupEntity.defaultGifEmojiList
        val itemSize = emojiconColumns * emojiconRows - 1
        val totalSize = defaultGifEmojiList.size
        return if (totalSize % itemSize == 0) totalSize / itemSize else totalSize / itemSize + 1
    }

    private inner class EmojiPagerChangeListener : OnPageChangeListener {

        override fun onPageSelected(position: Int) {
            var endSize = 0
            var groupPosition = 0
            for (groupEntity in groupEntities) {
                val groupPageSize = getPageSize(groupEntity)
                //选中的position在当前遍历的group里
                if (endSize + groupPageSize > position) {
                    //前面的group切换过来的
                    if (previousPagerPosition - endSize < 0) {
                        pagerViewListener?.onGroupPositionChanged(groupPosition, groupPageSize)
                        pagerViewListener?.onGroupPagePostionChangedTo(0)
                        break
                    }
                    //后面的group切换过来的
                    if (previousPagerPosition - endSize >= groupPageSize) {
                        pagerViewListener?.onGroupPositionChanged(groupPosition, groupPageSize)
                        pagerViewListener?.onGroupPagePostionChangedTo(position - endSize)
                        break
                    }

                    //当前group的pager切换
                    pagerViewListener?.onGroupInnerPagePostionChanged(previousPagerPosition - endSize,
                            position - endSize)
                    break

                }
                groupPosition++
                endSize += groupPageSize
            }

            previousPagerPosition = position
        }

        override fun onPageScrollStateChanged(arg0: Int) {}

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
    }


    interface EaseEmojiconPagerViewListener {

        /**
         * pagerview初始化完毕
         *
         * @param groupMaxPageSize 最大表情组的page大小
         * @param firstGroupPageSize 第一组的page大小
         */
        fun onPagerViewInited(groupMaxPageSize: Int, firstGroupPageSize: Int)

        /**
         * 表情组位置变动(从一组表情组移动另一组)
         *
         * @param groupPosition 表情组位置
         * @param pagerSizeOfGroup 表情组里的pager的size
         */
        fun onGroupPositionChanged(groupPosition: Int, pagerSizeOfGroup: Int)

        /**
         * 表情组内的page位置变动
         */
        fun onGroupInnerPagePostionChanged(oldPosition: Int, newPosition: Int)

        /**
         * 从别的表情组切过来的page位置变动
         */
        fun onGroupPagePostionChangedTo(position: Int)

        /**
         * 表情组最大pager数变化
         */
        fun onGroupMaxPageSizeChanged(maxCount: Int)


        fun onExpressionClicked(emoji: Emoji)

    }
}
