package me.sunhapper.spcharedittool.emoji

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.common_emoj_widget_emojicon_tab_bar.view.*
import me.sunhapper.spcharedittool.R
import me.sunhapper.spcharedittool.util.Utils
import java.util.*

class EmojiconScrollTabBar @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
    : RelativeLayout(mContext, attrs, defStyle) {


    private val tabList = ArrayList<ImageView>()
    private var itemClickListener: EaseScrollTabBarItemClickListener? = null

    private val tabWidth = 60f

    init {
        LayoutInflater.from(mContext).inflate(R.layout.common_emoj_widget_emojicon_tab_bar, this)
    }

    /**
     * 添加tab
     */
    fun addTab(icon: Int) {
        val tabView = View.inflate(mContext, R.layout.common_emoj_scroll_tab_item, null)
        val imageView: ImageView = tabView.findViewById(R.id.iv_icon)
        imageView.setImageResource(icon)
        val imgParams = LinearLayout.LayoutParams(
                Utils.dp2px(mContext, tabWidth), LayoutParams.MATCH_PARENT)
        imageView.layoutParams = imgParams
        tab_container.addView(tabView)
        tabList.add(imageView)
        val position = tabList.size - 1
        imageView.setOnClickListener {
            itemClickListener?.onItemClick(position)
        }
    }


    /**
     * 移除tab
     */
    fun removeTab(position: Int) {
        tab_container.removeViewAt(position)
        tabList.removeAt(position)
    }

    fun selectedTo(position: Int) {
        scrollTo(position)
        for (i in tabList.indices) {
            if (position == i) {
                tabList[i].setBackgroundColor(ContextCompat.getColor(mContext, R.color.emojicon_tab_selected))
            } else {
                tabList[i].setBackgroundColor(ContextCompat.getColor(mContext, R.color.emojicon_tab_nomal))
            }
        }
    }

    private fun scrollTo(position: Int) {
        val childCount = tab_container.childCount
        if (position < childCount) {
            scroll_view.post(Runnable {
                val mScrollX = tab_container.scrollX
                val childX = tab_container.getChildAt(position).x.toInt()

                if (childX < mScrollX) {
                    scroll_view.scrollTo(childX, 0)
                    return@Runnable
                }

                val childWidth = tab_container.getChildAt(position).width
                val hsvWidth = scroll_view.width
                val childRight = childX + childWidth
                val scrollRight = mScrollX + hsvWidth

                if (childRight > scrollRight) {
                    scroll_view.scrollTo(childRight - scrollRight, 0)
                    return@Runnable
                }
            })
        }
    }


    fun setTabBarItemClickListener(itemClickListener: EaseScrollTabBarItemClickListener) {
        this.itemClickListener = itemClickListener
    }


    interface EaseScrollTabBarItemClickListener {

        fun onItemClick(position: Int)
    }


}
