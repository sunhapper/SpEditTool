package me.sunhapper.spcharedittool.emoji;

import java.util.List;

/**
 * 一组表情所对应的实体类
 */
public class EmojiconGroupEntity {

  /**
   * 表情数据
   */
  private List<PngFileEmoji> pngFileEmojiList;
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

  public EmojiconGroupEntity(int icon, List<PngFileEmoji> pngFileEmojiList) {
    this.icon = icon;
    this.pngFileEmojiList = pngFileEmojiList;
  }



  public List<PngFileEmoji> getPngFileEmojiList() {
    return pngFileEmojiList;
  }

  public void setPngFileEmojiList(List<PngFileEmoji> pngFileEmojiList) {
    this.pngFileEmojiList = pngFileEmojiList;
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
