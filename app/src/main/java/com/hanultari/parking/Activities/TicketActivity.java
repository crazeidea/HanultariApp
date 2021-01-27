package com.hanultari.parking.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hanultari.parking.Fragment.NoticeListFragment;
import com.hanultari.parking.Fragment.TicketDetailFragment;
import com.hanultari.parking.Fragment.TicketListFragment;
import com.hanultari.parking.R;

import static com.hanultari.parking.CommonMethod.loginDTO;

public class TicketActivity extends AppCompatActivity {

  FragmentManager fm;
  FragmentTransaction transaction;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ticket);
    if(loginDTO != null) {
      fm = getSupportFragmentManager();
      transaction = fm.beginTransaction();
      transaction.add(R.id.ticketFragContainer, new TicketListFragment()).commit();
    } else {
      startActivity(new Intent(this, LoginActivity.class).putExtra("from", "ticket"));
      finish();
    }

    ImageButton btnBack = findViewById(R.id.btnBackTicket);
    btnBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

  @Override
  public void onBackPressed() {
    TicketDetailFragment detail = (TicketDetailFragment) getSupportFragmentManager().findFragmentByTag("Detail");
    if(detail != null && detail.isVisible()) {
      FragmentManager fm = getSupportFragmentManager();
      FragmentTransaction transaction = fm.beginTransaction();
      transaction.replace(R.id.ticketFragContainer, new TicketListFragment(), "List").commitAllowingStateLoss();
    } else {
      finish();
    }
  }
}
