package me.sunhapper.spcharedittool.emoji;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

public class EmojiconPagerAdapter extends PagerAdapter {

  private List<View> views;

  public EmojiconPagerAdapter(List<View> views) {
    this.views = views;
  }

  @Override
  public int getCount() {
    return views.size();
  }

  @Override
  public boolean isViewFromObject(View arg0, Object arg1) {
    return arg0 == arg1;
  }

  @Override
  public Object instantiateItem(ViewGroup arg0, int arg1) {
    arg0.addView(views.get(arg1));
    return views.get(arg1);
  }

  @Override
  public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
    arg0.removeView(views.get(arg1));

  }

}
