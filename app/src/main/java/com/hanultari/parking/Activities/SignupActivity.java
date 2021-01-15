package com.hanultari.parking.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hanultari.parking.AsyncTasks.Signup;
import com.hanultari.parking.MainActivity;
import com.hanultari.parking.R;

import static com.hanultari.parking.CommonMethod.loginDTO;

public class SignupActivity extends AppCompatActivity {
  private static final String TAG = "SignupActivity";

  EditText inputEmail, inputPw, inputName, inputTel;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);

    Button btnSignup = findViewById(R.id.btnSignup);
    btnSignup.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        inputEmail = findViewById(R.id.inputEmail);
        inputPw = findViewById(R.id.inputPassword);
        inputName = findViewById(R.id.inputName);
        inputTel = findViewById(R.id.inputTel);
        String email = inputEmail.getText().toString();
        String pw = inputPw.getText().toString();
        String name = inputName.getText().toString();
        String tel = inputTel.getText().toString();
        try {
          Signup signup = new Signup(email, pw, name, tel);
          Boolean state = signup.execute().get();
          if(state) {
            Toast.makeText(SignupActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            loginDTO.setName(name);
            loginDTO.setPw(pw);
            loginDTO.setName(name);
            loginDTO.setTel(tel);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

          } else {

          }
        } catch (Exception e) {
          e.printStackTrace();
        }

      }
    });
  }
}
