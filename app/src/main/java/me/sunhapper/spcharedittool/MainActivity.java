package me.sunhapper.spcharedittool;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.sunhapper.spedittool.view.SpEditText;
import com.sunhapper.spedittool.view.SpEditText.KeyReactListener;
import com.sunhapper.spedittool.view.SpEditText.SpData;
import com.sunhapper.spedittool.span.GifSpanUtil;
import java.io.IOException;
import pl.droidsonroids.gif.GifDrawable;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";
  private SpEditText spEditText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    spEditText = findViewById(R.id.spEdt);
    spEditText.setReactKeys("@#%*");
    spEditText.setKeyReactListener(new KeyReactListener() {
      @Override
      public void onKeyReact(String key) {
        switch (key) {
          case "@":
            spEditText
                .insertSpecialStr(" @sunhapper ", true, 0, new ForegroundColorSpan(Color.RED));
            break;
          case "#":
            spEditText.insertSpecialStr(" #这是一个话题# ", true, 1, new ForegroundColorSpan(Color.RED));
            break;
          case "%":
            spEditText.insertSpecialStr(" 100% ", true, 2, new ForegroundColorSpan(Color.RED));
          case "*":
            spEditText.insertSpecialStr(" ******* ", true, 3, new ForegroundColorSpan(Color.RED));
            break;
        }


      }
    });
  }

  public void insertSp(View view) {
    spEditText.insertSpecialStr(" 这是插入的字符串 ", false, 4, new ForegroundColorSpan(Color.BLUE));
  }

  public void getData(View view) {
    SpData[] spDatas = spEditText.getSpDatas();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(spEditText.getText())
        .append("\n");
    for (SpData spData : spDatas) {
      stringBuilder.append(spData.toString())
          .append("\n");
    }
    Log.i(TAG, "getData: " + stringBuilder);
    Toast.makeText(this, stringBuilder, Toast.LENGTH_SHORT).show();
  }

  public void insertGif(View view) {
    try {
      GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.a);
      CharSequence charSequence = GifSpanUtil.getGifText("[a]", gifDrawable);
      GifSpanUtil.setText(spEditText, charSequence);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
