package me.sunhapper.spcharedittool.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sunhapper.spedittool.util.GifTextUtil;
import java.util.ArrayList;
import java.util.List;
import me.sunhapper.spcharedittool.R;
import me.sunhapper.spcharedittool.emoji.DefaultGifEmoji;
import me.sunhapper.spcharedittool.emoji.EmojiManager;
import me.sunhapper.spcharedittool.emoji.EmojiManager.OnUnzipSuccessListener;
import me.sunhapper.spcharedittool.span.EqualHeightSpan;

/**
 * Created by sunha on 2018/2/7 0007.
 */

public class RecyclerDemoActivity extends AppCompatActivity {

  private static final String TAG = "RecyclerDemoActivity";
  private RecyclerView gifRecyclerView;
  private List<CharSequence> charSequences = new ArrayList<>();
  private Adapter adapter = new Adapter();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recycler_demo);
    gifRecyclerView = findViewById(R.id.gif_recyclerView);
    gifRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    gifRecyclerView.setAdapter(adapter);
    EmojiManager.getInstance().getDefaultEmojiData(new OnUnzipSuccessListener() {
      @Override
      public void onUnzipSuccess(DefaultGifEmoji[] defaultGifEmojis) {
        Log.i(TAG, "onUnzipSuccess: 0");
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        for (DefaultGifEmoji defaultGifEmoji : defaultGifEmojis) {
          Drawable gifDrawable = EmojiManager.getInstance()
              .getDrawableByEmoji(RecyclerDemoActivity.this, defaultGifEmoji);
          ImageSpan imageSpan = new EqualHeightSpan(gifDrawable);
          spannableStringBuilder
              .append(defaultGifEmoji.getEmojiText(), imageSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
          charSequences.add(new SpannableString(spannableStringBuilder));
        }
        Log.i(TAG, "onUnzipSuccess: ");
        adapter.notifyDataSetChanged();
      }
    });

  }

  class Adapter extends RecyclerView.Adapter<ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.layout_recycler_demo, parent, false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      CharSequence charSequence = charSequences.get(position);
      Log.i(TAG, "onBindViewHolder:start" + position);
      GifTextUtil.setTextWithReuseDrawable(holder.textView, charSequence);
      Log.i(TAG, "onBindViewHolder:  end" + position);
    }

    @Override
    public int getItemCount() {
      return charSequences.size();
    }
  }


  class ViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;

    public ViewHolder(View itemView) {
      super(itemView);
      textView = itemView.findViewById(R.id.textView);
    }
  }

}
