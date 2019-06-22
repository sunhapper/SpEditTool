package me.sunhapper.spcharedittool.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunhapper.gifdrawable.drawable.TextGifDrawable;
import com.sunhapper.glide.drawable.DrawableTarget;
import com.sunhapper.x.spedit.SpUtil;
import com.sunhapper.x.spedit.gif.drawable.ProxyDrawable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.sunhapper.spcharedittool.GlideApp;
import me.sunhapper.spcharedittool.R;
import pl.droidsonroids.gif.GifDrawable;

/**
 * Created by sunha on 2018/2/7 0007.
 */

public class RecyclerGlideDemoActivity extends AppCompatActivity {

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
    for (int i = 0; i < 100; i++) {
      GifDrawable gifDrawable = null;
      try {
        gifDrawable = new TextGifDrawable(getResources(), R.drawable.a);
        ProxyDrawable proxyDrawable = new ProxyDrawable();
        GlideApp.with(this)
                .load(
                        "http://5b0988e595225.cdn.sohucs.com/images/20170919/1ce5d4c52c24432e9304ef942b764d37.gif")
                .placeholder(gifDrawable)
                .into(new DrawableTarget(proxyDrawable));
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        Spannable spannable= SpUtil.createResizeGifDrawableSpan(proxyDrawable, "[c]");
        spannableStringBuilder.append(spannable).append(String.valueOf(i));
        charSequences.add(new SpannableString(spannableStringBuilder));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    adapter.notifyDataSetChanged();

  }

  class Adapter extends RecyclerView.Adapter<ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.layout_recycler_glide_demo, parent, false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      CharSequence charSequence = charSequences.get(position);
      Log.i(TAG, "onBindViewHolder:start" + position);
      if (position == 8) {
        Log.i(TAG, "onBindViewHolder: ");
      }
      holder.textView.setText(charSequence);
//      GifTextUtil.setTextWithReuseDrawable(holder.textView, charSequence, false);
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
