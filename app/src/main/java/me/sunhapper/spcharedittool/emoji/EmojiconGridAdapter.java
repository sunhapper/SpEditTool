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

public class EmojiconGridAdapter extends ArrayAdapter<PngFileEmoji> {


  public EmojiconGridAdapter(Context context, int textViewResourceId, List<PngFileEmoji> objects) {
    super(context, textViewResourceId, objects);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {

      convertView = View.inflate(getContext(), R.layout.common_emoj_row_expression, null);

    }

    ImageView imageView = convertView.findViewById(R.id.iv_expression);
    Emoji pngFileEmoji = getItem(position);
    if (pngFileEmoji.isDeleteIcon()) {
      imageView.setImageResource(R.drawable.common_emoj_delete_expression);
    } else {
      Glide.with(getContext())
          .load(pngFileEmoji.getRes())
          .apply(new RequestOptions().placeholder(R.drawable.common_emoj_smile_default))
          .into(imageView);
    }

    return convertView;
  }

}
