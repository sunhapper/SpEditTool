package me.sunhapper.spcharedittool.emoji;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.List;
import me.sunhapper.spcharedittool.R;

public class EmojiconGridAdapter extends ArrayAdapter<Emojicon> {



  public EmojiconGridAdapter(Context context, int textViewResourceId, List<Emojicon> objects) {
    super(context, textViewResourceId, objects);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {

        convertView = View.inflate(getContext(), R.layout.common_emoj_row_expression, null);

    }

    ImageView imageView =  convertView.findViewById(R.id.iv_expression);
    Emojicon emojicon = getItem(position);
    if (EmojiManager.DELETE_KEY.equals(emojicon.getEmojiText())) {
      imageView.setImageResource(R.drawable.common_emoj_delete_expression);
    } else {
      if (emojicon.getIcon() != 0) {
        imageView.setImageResource(emojicon.getIcon());
      } else if (emojicon.getIconPath() != null) {
        Glide.with(getContext())
            .load(emojicon.getIconPath())
            .apply(new RequestOptions().placeholder(R.drawable.common_emoj_smile_default))
            .into(imageView);
      } else if (emojicon.getIconSet() != null) {
        Glide.with(getContext())
            .load(emojicon.getIconSet().iconPng)
            .apply(new RequestOptions().placeholder(R.drawable.common_emoj_smile_default))
            .into(imageView);
      }
    }

    return convertView;
  }

}
