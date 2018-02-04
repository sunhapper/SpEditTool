package me.sunhapper.spcharedittool.emoji;

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

public class EmojiconPagerView extends ViewPager {

  private Context context;
  private List<EmojiconGroupEntity> groupEntities;
  private PagerAdapter pagerAdapter;
  private int emojiconRows = 3;
  private int emojiconColumns = 7;
  private int firstGroupPageSize;
  private int maxPageCount;
  private int previousPagerPosition;
  private EaseEmojiconPagerViewListener pagerViewListener;
  private List<View> viewpages;

  public EmojiconPagerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
  }

  public EmojiconPagerView(Context context) {
    this(context, null);
  }


  public void init(List<EmojiconGroupEntity> emojiconGroupList, int emijiconColumns) {
    if (emojiconGroupList == null) {
      throw new RuntimeException("emojiconGroupList is null");
    }

    this.groupEntities = emojiconGroupList;
    this.emojiconColumns = emijiconColumns;

    viewpages = new ArrayList<>();
    for (int i = 0; i < groupEntities.size(); i++) {
      EmojiconGroupEntity group = groupEntities.get(i);
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
  public List<View> getGroupGridViews(EmojiconGroupEntity groupEntity) {
    List<PngFileEmoji> pngFileEmojiList = groupEntity.getPngFileEmojiList();
    int itemSize = emojiconColumns * emojiconRows - 1;
    int totalSize = pngFileEmojiList.size();
    int pageSize = totalSize % itemSize == 0 ? totalSize / itemSize : totalSize / itemSize + 1;
    List<View> views = new ArrayList<View>();
    for (int i = 0; i < pageSize; i++) {
      View view = View.inflate(context, R.layout.common_emoj_expression_gridview, null);
      GridView gv =  view.findViewById(R.id.gridview);
      gv.setNumColumns(emojiconColumns);
      List<PngFileEmoji> list = new ArrayList<PngFileEmoji>();
      if (i != pageSize - 1) {
        list.addAll(pngFileEmojiList.subList(i * itemSize, (i + 1) * itemSize));
      } else {
        list.addAll(pngFileEmojiList.subList(i * itemSize, totalSize));
      }
      PngFileEmoji deleteIcon = new PngFileEmoji();
      list.add(deleteIcon);
      final EmojiconGridAdapter gridAdapter = new EmojiconGridAdapter(context, 1, list);
      gv.setAdapter(gridAdapter);
      gv.setOnItemClickListener(new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          PngFileEmoji pngFileEmoji = gridAdapter.getItem(position);
          if (pagerViewListener != null) {
            if (pngFileEmoji.isDeleteIcon()) {
              pagerViewListener.onDeleteImageClicked();
            } else {
              pagerViewListener.onExpressionClicked(pngFileEmoji);
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
  public void addEmojiconGroup(EmojiconGroupEntity groupEntity, boolean notifyDataChange) {
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


  private int getPageSize(EmojiconGroupEntity groupEntity) {
    List<PngFileEmoji> pngFileEmojiList = groupEntity.getPngFileEmojiList();
    int itemSize = emojiconColumns * emojiconRows - 1;
    int totalSize = pngFileEmojiList.size();
    return totalSize % itemSize == 0 ? totalSize / itemSize : totalSize / itemSize + 1;
  }

  private class EmojiPagerChangeListener implements OnPageChangeListener {

    @Override
    public void onPageSelected(int position) {
      int endSize = 0;
      int groupPosition = 0;
      for (EmojiconGroupEntity groupEntity : groupEntities) {
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

    void onExpressionClicked(PngFileEmoji pngFileEmoji);

  }
}
