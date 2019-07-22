package me.sunhapper.spcharedittool.emoji

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import me.sunhapper.spcharedittool.R

class EmojiconGridAdapter(context: Context, textViewResourceId: Int, objects: List<Emoji>) : ArrayAdapter<Emoji>(context, textViewResourceId, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = View.inflate(context, R.layout.common_emoj_row_expression, null)
        }
        val imageView: ImageView = view!!.findViewById(R.id.iv_expression)
        val emoji = getItem(position)
        EmojiManager.displayImage(imageView, emoji!!)
        return view
    }


}
