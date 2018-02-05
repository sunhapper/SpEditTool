package me.sunhapper.spcharedittool.emoji;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import java.util.List;
import me.sunhapper.spcharedittool.R;

public class EmojiconGridAdapter extends ArrayAdapter<Emoji> {


  public EmojiconGridAdapter(Context context, int textViewResourceId, List<Emoji> objects) {
    super(context, textViewResourceId, objects);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {

      convertView = View.inflate(getContext(), R.layout.common_emoj_row_expression, null);

    }

    ImageView imageView = convertView.findViewById(R.id.iv_expression);
    Emoji emoji = getItem(position);
    EmojiManager.getInstance().displayImage(imageView, emoji);
    return convertView;
  }



}
