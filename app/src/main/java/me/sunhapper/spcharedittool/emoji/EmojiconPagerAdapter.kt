package me.sunhapper.spcharedittool.emoji

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

class EmojiconPagerAdapter(private val views: List<View>) : PagerAdapter() {

    override fun getCount(): Int {
        return views.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(views[position])
        return views[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(views[position])

    }

}
