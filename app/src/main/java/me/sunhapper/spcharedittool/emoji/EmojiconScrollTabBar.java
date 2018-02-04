package me.sunhapper.spcharedittool.emoji;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import me.sunhapper.spcharedittool.R;
import me.sunhapper.spcharedittool.util.Utils;

public class EmojiconScrollTabBar extends RelativeLayout {

  private Context context;
  private HorizontalScrollView scrollView;
  private LinearLayout tabContainer;
  private TextView sendBtn;

  private List<ImageView> tabList = new ArrayList<ImageView>();
  private EaseScrollTabBarItemClickListener itemClickListener;
  private OnClickListener sendBtnListener;

  private int tabWidth = 60;

  public EmojiconScrollTabBar(Context context) {
    this(context, null);
  }

  public EmojiconScrollTabBar(Context context, AttributeSet attrs, int defStyle) {
    this(context, attrs);
  }

  public EmojiconScrollTabBar(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    this.context = context;
    LayoutInflater.from(context).inflate(R.layout.common_emoj_widget_emojicon_tab_bar, this);

    scrollView =  findViewById(R.id.scroll_view);
    tabContainer =  findViewById(R.id.tab_container);
    sendBtn = findViewById(R.id.send_btn);
    sendBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (sendBtnListener != null) {
          sendBtnListener.onClick(v);
        }
      }
    });
  }

  /**
   * 添加tab
   */
  public void addTab(int icon) {
    View tabView = View.inflate(context, R.layout.common_emoj_scroll_tab_item, null);
    ImageView imageView = (ImageView) tabView.findViewById(R.id.iv_icon);
    imageView.setImageResource(icon);
    LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(
        Utils.dp2px(context, tabWidth), LayoutParams.MATCH_PARENT);
    imageView.setLayoutParams(imgParams);
    tabContainer.addView(tabView);
    tabList.add(imageView);
    final int position = tabList.size() - 1;
    imageView.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        if (itemClickListener != null) {
          itemClickListener.onItemClick(position);
        }
      }
    });
  }

  public void setSendBtnListener(OnClickListener sendBtnListener) {
    this.sendBtnListener = sendBtnListener;
  }

  /**
   * 移除tab
   */
  public void removeTab(int position) {
    tabContainer.removeViewAt(position);
    tabList.remove(position);
  }

  public void selectedTo(int position) {
    scrollTo(position);
    for (int i = 0; i < tabList.size(); i++) {
      if (position == i) {
        tabList.get(i).setBackgroundColor(getResources().getColor(R.color.emojicon_tab_selected));
      } else {
        tabList.get(i).setBackgroundColor(getResources().getColor(R.color.emojicon_tab_nomal));
      }
    }
  }

  private void scrollTo(final int position) {
    int childCount = tabContainer.getChildCount();
    if (position < childCount) {
      scrollView.post(new Runnable() {
        @Override
        public void run() {
          int mScrollX = tabContainer.getScrollX();
          int childX = (int) ViewCompat.getX(tabContainer.getChildAt(position));

          if (childX < mScrollX) {
            scrollView.scrollTo(childX, 0);
            return;
          }

          int childWidth = (int) tabContainer.getChildAt(position).getWidth();
          int hsvWidth = scrollView.getWidth();
          int childRight = childX + childWidth;
          int scrollRight = mScrollX + hsvWidth;

          if (childRight > scrollRight) {
            scrollView.scrollTo(childRight - scrollRight, 0);
            return;
          }
        }
      });
    }
  }


  public void setTabBarItemClickListener(EaseScrollTabBarItemClickListener itemClickListener) {
    this.itemClickListener = itemClickListener;
  }


  public interface EaseScrollTabBarItemClickListener {

    void onItemClick(int position);
  }


}
