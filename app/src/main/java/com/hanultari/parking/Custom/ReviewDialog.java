package com.hanultari.parking.Custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.hanultari.parking.AsyncTasks.InsertReview;
import com.hanultari.parking.DTO.ReviewDTO;
import com.hanultari.parking.R;

import java.util.ArrayList;

import static com.hanultari.parking.CommonMethod.loginDTO;

public class ReviewDialog extends Dialog {

  public Button positiveButton;
  public Button negativeButton;
  public EditText textContent;
  private ImageButton btnSet1;
  private ImageButton btnSet2;
  private ImageButton btnSet3;
  private ImageButton btnSet4;
  private ImageButton btnSet5;

  public int rating;
  public String content;

  private Context context;

  public ReviewDialog(@NonNull Context context) {
    super(context);
    this.context = context;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
    layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
    layoutParams.dimAmount = 0.8f;
    getWindow().setAttributes(layoutParams);

    setContentView(R.layout.dialog_review);

    positiveButton = findViewById(R.id.btnPostiveReview);
    negativeButton = findViewById(R.id.btnNegativeReview);
    textContent = findViewById(R.id.textContentReview);
    btnSet1 = findViewById(R.id.btnSet1Review);
    btnSet2 = findViewById(R.id.btnSet2Review);
    btnSet3 = findViewById(R.id.btnSet3Review);
    btnSet4 = findViewById(R.id.btnSet4Review);
    btnSet5 = findViewById(R.id.btnSet5Review);

    ArrayList<ImageButton> buttons = new ArrayList<>();
    buttons.add(btnSet1);
    buttons.add(btnSet2);
    buttons.add(btnSet3);
    buttons.add(btnSet4);
    buttons.add(btnSet5);

    content = textContent.getText().toString();

    for(int i = 0; i < buttons.size(); i++) {
      ImageButton button = buttons.get(i);
      int finalI = i;
      button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          rating = finalI + 1;
          Toast.makeText(context, String.valueOf(rating), Toast.LENGTH_SHORT).show();
          for(int j = 0; j <= finalI; j++) {
            ImageButton button1 = buttons.get(j);
            button1.setColorFilter(ContextCompat.getColor(context, R.color.yellow), PorterDuff.Mode.SRC_IN);
          }
          for (int j = buttons.size() - 1; j > finalI; j--) {
            ImageButton button1 = buttons.get(j);
            button1.setColorFilter(ContextCompat.getColor(context, R.color.light_gray), PorterDuff.Mode.SRC_IN);
          }
        }
      });
    }
  }



}
