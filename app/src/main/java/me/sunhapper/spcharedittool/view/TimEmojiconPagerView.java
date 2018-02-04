package me.sunhapper.spcharedittool.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import java.util.ArrayList;
import java.util.List;
import me.sunhapper.spcharedittool.R;

public class TimEmojiconPagerView extends ViewPager {

  private Context context;
  private List<TimEmojiconGroupEntity> groupEntities;
  private List<TimEmojicon> totalEmojiconList = new ArrayList<TimEmojicon>();

  private PagerAdapter pagerAdapter;

  private int emojiconRows = 3;
  private int emojiconColumns = 7;

  private int bigEmojiconRows = 2;
  private int bigEmojiconColumns = 4;

  private int firstGroupPageSize;

  private int maxPageCount;
  private int previousPagerPosition;
  private EaseEmojiconPagerViewListener pagerViewListener;
  private List<View> viewpages;

  public TimEmojiconPagerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
  }

  public TimEmojiconPagerView(Context context) {
    this(context, null);
  }


  public void init(List<TimEmojiconGroupEntity> emojiconGroupList, int emijiconColumns,
      int bigEmojiconColumns) {
    if (emojiconGroupList == null) {
      throw new RuntimeException("emojiconGroupList is null");
    }

    this.groupEntities = emojiconGroupList;
    this.emojiconColumns = emijiconColumns;
    this.bigEmojiconColumns = bigEmojiconColumns;

    viewpages = new ArrayList<View>();
    for (int i = 0; i < groupEntities.size(); i++) {
      TimEmojiconGroupEntity group = groupEntities.get(i);
      List<TimEmojicon> groupEmojicons = group.getEmojiconList();
      totalEmojiconList.addAll(groupEmojicons);
      List<View> gridViews = getGroupGridViews(group);
      if (i == 0) {
        firstGroupPageSize = gridViews.size();
      }
      maxPageCount = Math.max(gridViews.size(), maxPageCount);
      viewpages.addAll(gridViews);
    }

    pagerAdapter = new EmojiconPagerAdapter(viewpages);
    setAdapter(pagerAdapter);
    addOnPageChangeListener(new EmojiPagerChangeListener());

    if (pagerViewListener != null) {
      pagerViewListener.onPagerViewInited(maxPageCount, firstGroupPageSize);
    }
  }

  public void setPagerViewListener(EaseEmojiconPagerViewListener pagerViewListener) {
    this.pagerViewListener = pagerViewListener;
  }


  /**
   * 设置当前表情组位置
   */
  public void setGroupPostion(int position) {
    if (getAdapter() != null && position >= 0 && position < groupEntities.size()) {
      int count = 0;
      for (int i = 0; i < position; i++) {
        count += getPageSize(groupEntities.get(i));
      }
      setCurrentItem(count);
    }
  }

  /**
   * 获取表情组的gridview list
   */
  public List<View> getGroupGridViews(TimEmojiconGroupEntity groupEntity) {
    List<TimEmojicon> emojiconList = groupEntity.getEmojiconList();
    int itemSize = emojiconColumns * emojiconRows - 1;
    int totalSize = emojiconList.size();
    int pageSize = totalSize % itemSize == 0 ? totalSize / itemSize : totalSize / itemSize + 1;
    List<View> views = new ArrayList<View>();
    for (int i = 0; i < pageSize; i++) {
      View view = View.inflate(context, R.layout.common_emoj_expression_gridview, null);
      GridView gv =  view.findViewById(R.id.gridview);
      gv.setNumColumns(emojiconColumns);
      List<TimEmojicon> list = new ArrayList<TimEmojicon>();
      if (i != pageSize - 1) {
        list.addAll(emojiconList.subList(i * itemSize, (i + 1) * itemSize));
      } else {
        list.addAll(emojiconList.subList(i * itemSize, totalSize));
      }
      TimEmojicon deleteIcon = new TimEmojicon();
      deleteIcon.setEmojiText(EmojiManager.DELETE_KEY);
      list.add(deleteIcon);
      final EmojiconGridAdapter gridAdapter = new EmojiconGridAdapter(context, 1, list);
      gv.setAdapter(gridAdapter);
      gv.setOnItemClickListener(new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          TimEmojicon emojicon = gridAdapter.getItem(position);
          if (pagerViewListener != null) {
            String emojiText = emojicon.getEmojiText();
            if (emojiText != null && emojiText.equals(EmojiManager.DELETE_KEY)) {
              pagerViewListener.onDeleteImageClicked();
            } else {
              pagerViewListener.onExpressionClicked(emojicon);
            }

          }

        }
      });

      views.add(view);
    }
    return views;
  }


  /**
   * 添加表情组
   */
  public void addEmojiconGroup(TimEmojiconGroupEntity groupEntity, boolean notifyDataChange) {
    int pageSize = getPageSize(groupEntity);
    if (pageSize > maxPageCount) {
      maxPageCount = pageSize;
      if (pagerViewListener != null && pagerAdapter != null) {
        pagerViewListener.onGroupMaxPageSizeChanged(maxPageCount);
      }
    }
    viewpages.addAll(getGroupGridViews(groupEntity));
    if (pagerAdapter != null && notifyDataChange) {
      pagerAdapter.notifyDataSetChanged();
    }
  }

  /**
   * 移除表情组
   */
  public void removeEmojiconGroup(int position) {
    if (position > groupEntities.size() - 1) {
      return;
    }
    if (pagerAdapter != null) {
      pagerAdapter.notifyDataSetChanged();
    }
  }


  private int getPageSize(TimEmojiconGroupEntity groupEntity) {
    List<TimEmojicon> emojiconList = groupEntity.getEmojiconList();
    int itemSize = emojiconColumns * emojiconRows - 1;
    int totalSize = emojiconList.size();
    return totalSize % itemSize == 0 ? totalSize / itemSize : totalSize / itemSize + 1;
  }

  private class EmojiPagerChangeListener implements OnPageChangeListener {

    @Override
    public void onPageSelected(int position) {
      int endSize = 0;
      int groupPosition = 0;
      for (TimEmojiconGroupEntity groupEntity : groupEntities) {
        int groupPageSize = getPageSize(groupEntity);
        //选中的position在当前遍历的group里
        if (endSize + groupPageSize > position) {
          //前面的group切换过来的
          if (previousPagerPosition - endSize < 0) {
            if (pagerViewListener != null) {
              pagerViewListener.onGroupPositionChanged(groupPosition, groupPageSize);
              pagerViewListener.onGroupPagePostionChangedTo(0);
            }
            break;
          }
          //后面的group切换过来的
          if (previousPagerPosition - endSize >= groupPageSize) {
            if (pagerViewListener != null) {
              pagerViewListener.onGroupPositionChanged(groupPosition, groupPageSize);
              pagerViewListener.onGroupPagePostionChangedTo(position - endSize);
            }
            break;
          }

          //当前group的pager切换
          if (pagerViewListener != null) {
            pagerViewListener.onGroupInnerPagePostionChanged(previousPagerPosition - endSize,
                position - endSize);
          }
          break;

        }
        groupPosition++;
        endSize += groupPageSize;
      }

      previousPagerPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }
  }


  public interface EaseEmojiconPagerViewListener {

    /**
     * pagerview初始化完毕
     *
     * @param groupMaxPageSize 最大表情组的page大小
     * @param firstGroupPageSize 第一组的page大小
     */
    void onPagerViewInited(int groupMaxPageSize, int firstGroupPageSize);

    /**
     * 表情组位置变动(从一组表情组移动另一组)
     *
     * @param groupPosition 表情组位置
     * @param pagerSizeOfGroup 表情组里的pager的size
     */
    void onGroupPositionChanged(int groupPosition, int pagerSizeOfGroup);

    /**
     * 表情组内的page位置变动
     */
    void onGroupInnerPagePostionChanged(int oldPosition, int newPosition);

    /**
     * 从别的表情组切过来的page位置变动
     */
    void onGroupPagePostionChangedTo(int position);

    /**
     * 表情组最大pager数变化
     */
    void onGroupMaxPageSizeChanged(int maxCount);

    void onDeleteImageClicked();

    void onExpressionClicked(TimEmojicon emojicon);

  }
}
