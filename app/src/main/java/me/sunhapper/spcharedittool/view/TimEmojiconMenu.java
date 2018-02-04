package me.sunhapper.spcharedittool.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.sunhapper.spcharedittool.R;
import me.sunhapper.spcharedittool.util.FileUtil;
import me.sunhapper.spcharedittool.util.PreferenceUtil;
import me.sunhapper.spcharedittool.view.TimEmojiconPagerView.EaseEmojiconPagerViewListener;
import me.sunhapper.spcharedittool.view.TimEmojiconScrollTabBar.EaseScrollTabBarItemClickListener;

/**
 * 表情图片控件
 */
public class TimEmojiconMenu extends TimEmojiconMenuBase {

  private int emojiconColumns;
  private int bigEmojiconColumns;
  private final int defaultBigColumns = 4;
  private final int defaultColumns = 7;
  private TimEmojiconScrollTabBar tabBar;
  private TimEmojiconIndicatorView indicatorView;
  private TimEmojiconPagerView pagerView;

  private List<TimEmojiconGroupEntity> emojiconGroupList = new ArrayList<TimEmojiconGroupEntity>();


  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public TimEmojiconMenu(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, attrs);
  }

  public TimEmojiconMenu(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public TimEmojiconMenu(Context context) {
    super(context);
    init(context, null);
  }

  private void init(Context context, AttributeSet attrs) {
    LayoutInflater.from(context).inflate(R.layout.common_emoj_widget_emojicon, this);
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TimEmojiconMenu);
    emojiconColumns = ta.getInt(R.styleable.TimEmojiconMenu_emojiconColumns, defaultColumns);
    bigEmojiconColumns = ta.getInt(R.styleable.TimEmojiconMenu_bigEmojiconRows, defaultBigColumns);
    ta.recycle();

    pagerView = findViewById(R.id.pager_view);
    indicatorView = findViewById(R.id.indicator_view);
    tabBar = findViewById(R.id.tab_bar);
  boolean unziped=  PreferenceUtil.getEmojiInitResult(getContext());
  if (unziped){
    initDefault();
  }else {
    new Thread(new Runnable() {
      @Override
      public void run() {
        boolean unzipResult = FileUtil
            .unzipFromAssets(getContext(), FileUtil.getEmojiDir(getContext()), "emoji");
        PreferenceUtil.setEmojiInitResult(getContext(),unzipResult);
        if (unzipResult){
          post(new Runnable() {
            @Override
            public void run() {
              initDefault();
            }
          });

        }
      }
    }).start();
  }


  }


  public void setSendBtnListener(OnClickListener sendBtnListener) {
    tabBar.setSendBtnListener(sendBtnListener);
  }

  private void initDefault() {
    emojiconGroupList = new ArrayList<>();
    emojiconGroupList.add(new TimEmojiconGroupEntity(R.drawable.common_emoj_smile,
        Arrays.asList(EmojiManager.getInstance().createData(getContext()))));
    for (TimEmojiconGroupEntity groupEntity : emojiconGroupList) {
      tabBar.addTab(groupEntity.getIcon());
    }

    pagerView.setPagerViewListener(new EmojiconPagerViewListener());
    pagerView.init(emojiconGroupList, emojiconColumns, bigEmojiconColumns);

    tabBar.setTabBarItemClickListener(new EaseScrollTabBarItemClickListener() {

      @Override
      public void onItemClick(int position) {
        pagerView.setGroupPostion(position);
      }
    });

  }


  /**
   * 添加表情组
   */
  public void addEmojiconGroup(TimEmojiconGroupEntity groupEntity) {
    emojiconGroupList.add(groupEntity);
    pagerView.addEmojiconGroup(groupEntity, true);
    tabBar.addTab(groupEntity.getIcon());
  }

  /**
   * 添加一系列表情组
   */
  public void addEmojiconGroup(List<TimEmojiconGroupEntity> groupEntitieList) {
    for (int i = 0; i < groupEntitieList.size(); i++) {
      TimEmojiconGroupEntity groupEntity = groupEntitieList.get(i);
      emojiconGroupList.add(groupEntity);
      pagerView.addEmojiconGroup(groupEntity, i == groupEntitieList.size() - 1 ? true : false);
      tabBar.addTab(groupEntity.getIcon());
    }

  }

  /**
   * 移除表情组
   */
  public void removeEmojiconGroup(int position) {
    emojiconGroupList.remove(position);
    pagerView.removeEmojiconGroup(position);
    tabBar.removeTab(position);
  }

  public void setTabBarVisibility(int visibility) {
    tabBar.setVisibility(visibility);
  }


  private class EmojiconPagerViewListener implements EaseEmojiconPagerViewListener {

    @Override
    public void onPagerViewInited(int groupMaxPageSize, int firstGroupPageSize) {
      indicatorView.init(groupMaxPageSize);
      indicatorView.updateIndicator(firstGroupPageSize);
      tabBar.selectedTo(0);
    }

    @Override
    public void onGroupPositionChanged(int groupPosition, int pagerSizeOfGroup) {
      indicatorView.updateIndicator(pagerSizeOfGroup);
      tabBar.selectedTo(groupPosition);
    }

    @Override
    public void onGroupInnerPagePostionChanged(int oldPosition, int newPosition) {
      indicatorView.selectTo(oldPosition, newPosition);
    }

    @Override
    public void onGroupPagePostionChangedTo(int position) {
      indicatorView.selectTo(position);
    }

    @Override
    public void onGroupMaxPageSizeChanged(int maxCount) {
      indicatorView.updateIndicator(maxCount);
    }

    @Override
    public void onDeleteImageClicked() {
      if (listener != null) {
        listener.onDeleteImageClicked();
      }
    }

    @Override
    public void onExpressionClicked(TimEmojicon emojicon) {
      if (listener != null) {
        listener.onExpressionClicked(emojicon);
      }
    }

  }

}
