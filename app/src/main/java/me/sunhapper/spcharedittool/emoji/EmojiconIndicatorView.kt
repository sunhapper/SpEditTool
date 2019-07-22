package me.sunhapper.spcharedittool.emoji

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import me.sunhapper.spcharedittool.R
import me.sunhapper.spcharedittool.util.Utils
import java.util.*


class EmojiconIndicatorView @JvmOverloads
constructor(private val mContext: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
    : LinearLayout(mContext, attrs, defStyle) {

    private var selectedBitmap: Bitmap = BitmapFactory
            .decodeResource(mContext.resources, R.drawable.common_emoj_dot_selected)
    private var unselectedBitmap: Bitmap = BitmapFactory
            .decodeResource(mContext.resources, R.drawable.common_emoj_dot_unselected)

    private var dotViews: MutableList<ImageView> = ArrayList()

    private var dotHeight = Utils.dp2px(context, 12f)

    init {
        gravity = Gravity.CENTER_HORIZONTAL
    }

    fun init(count: Int) {
        for (i in 0 until count) {
            val rl = RelativeLayout(mContext)
            val params = LayoutParams(dotHeight, dotHeight)
            val layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
            val imageView = ImageView(mContext)

            if (i == 0) {
                imageView.setImageBitmap(selectedBitmap)
                rl.addView(imageView, layoutParams)
            } else {
                imageView.setImageBitmap(unselectedBitmap)
                rl.addView(imageView, layoutParams)
            }
            this.addView(rl, params)
            dotViews.add(imageView)
        }
    }

    fun updateIndicator(count: Int) {
        for (i in dotViews.indices) {
            if (i >= count) {
                dotViews[i].visibility = View.GONE
                (dotViews[i].parent as View).visibility = View.GONE
            } else {
                dotViews[i].visibility = View.VISIBLE
                (dotViews[i].parent as View).visibility = View.VISIBLE
            }
        }
        if (count > dotViews.size) {
            val diff = count - dotViews.size
            for (i in 0 until diff) {
                val rl = RelativeLayout(mContext)
                val params = LayoutParams(dotHeight, dotHeight)
                val layoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
                val imageView = ImageView(mContext)
                imageView.setImageBitmap(unselectedBitmap)
                rl.addView(imageView, layoutParams)
                rl.visibility = View.GONE
                imageView.visibility = View.GONE
                this.addView(rl, params)
                dotViews.add(imageView)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        selectedBitmap.recycle()
        unselectedBitmap.recycle()
    }

    fun selectTo(position: Int) {
        for (iv in dotViews) {
            iv.setImageBitmap(unselectedBitmap)
        }
        dotViews[position].setImageBitmap(selectedBitmap)
    }


    fun selectTo(startPosition: Int, targetPostion: Int) {
        val startView = dotViews[startPosition]
        val targetView = dotViews[targetPostion]
        startView.setImageBitmap(unselectedBitmap)
        targetView.setImageBitmap(selectedBitmap)
    }

}   
