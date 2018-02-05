package me.sunhapper.spcharedittool.emoji;

import java.util.List;

/**
 * 一组表情所对应的实体类
 */
public class EmojiconGroupEntity {

  /**
   * 表情数据
   */
  private List<DefaultGifEmoji> defaultGifEmojiList;
  /**
   * 图片
   */
  private int icon;
  /**
   * 组名
   */
  private String name;


  public EmojiconGroupEntity() {
  }

  public EmojiconGroupEntity(int icon, List<DefaultGifEmoji> defaultGifEmojiList) {
    this.icon = icon;
    this.defaultGifEmojiList = defaultGifEmojiList;
  }



  public List<DefaultGifEmoji> getDefaultGifEmojiList() {
    return defaultGifEmojiList;
  }

  public void setDefaultGifEmojiList(List<DefaultGifEmoji> defaultGifEmojiList) {
    this.defaultGifEmojiList = defaultGifEmojiList;
  }

  public int getIcon() {
    return icon;
  }

  public void setIcon(int icon) {
    this.icon = icon;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


}
