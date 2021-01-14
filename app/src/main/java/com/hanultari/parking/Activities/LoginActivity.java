package com.hanultari.parking.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hanultari.parking.AsyncTasks.Login;
import com.hanultari.parking.Custom.AES256Chiper;
import com.hanultari.parking.R;

import org.json.JSONObject;

import java.util.HashMap;

import static com.hanultari.parking.CommonMethod.loginDTO;

public class LoginActivity extends AppCompatActivity {
  private static final String TAG = "LoginActivity";

  EditText email, pw;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    Button btnLogin = findViewById(R.id.buttonLogin);
    btnLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        email = findViewById(R.id.inputEmail);
        String emailinfo = email.getText().toString();
        pw = findViewById(R.id.inputPassword);
        String pwinfo = pw.getText().toString();
        boolean isLogin = executeLogin(emailinfo, pwinfo);
        if(isLogin) {

        }
      }
    });
  }

  private boolean executeLogin(String email, String pw) {
    HashMap<String, String> info = new HashMap<>();
    info.put("email", email);
    info.put("pw", pw);
    boolean loginSuccess = false;
    try {
      Login login = new Login();
      JSONObject object = login.execute(info).get();
      if(object.length() > 0) {
        loginDTO.setEmail(object.getString("email"));
        loginDTO.setPw(AES256Chiper.AES_Encode(object.getString("pw")));
        loginDTO.setName(object.getString("name"));
        loginDTO.setNickname(object.getString("nickname"));
        loginDTO.setTel(object.getString("tel"));
        loginDTO.setAdmin(object.getBoolean("admin"));
        loginSuccess = true;
      }
    } catch (Exception e) {
      loginSuccess = false;
      e.printStackTrace();
    }
    return loginSuccess;
  }
}
