package com.hanultari.parking.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hanultari.parking.AsyncTasks.Login;
import com.hanultari.parking.DTO.MemberDTO;
import com.hanultari.parking.MainActivity;
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
          startActivity(new Intent(LoginActivity.this, MainActivity.class));
        } else {
          Toast.makeText(LoginActivity.this, "올바르지 않은 계정입니다.", Toast.LENGTH_SHORT).show();
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
      if(object != null) {
        loginDTO = new MemberDTO();
        loginDTO.setId(object.getInt("id"));
        loginDTO.setEmail(object.getString("email"));
        loginDTO.setPw(object.getString("pw"));
        loginDTO.setName(object.getString("name"));
        loginDTO.setNickname(object.getString("nickname"));
        loginDTO.setTel(object.getString("tel"));
        loginDTO.setAdmin(object.getString("admin").equals("y"));
        loginSuccess = true;
      }
    } catch (Exception e) {
      loginSuccess = false;
      e.printStackTrace();
    }
    return loginSuccess;
  }
}
