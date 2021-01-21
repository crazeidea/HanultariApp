package com.hanultari.parking.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hanultari.parking.DTO.TicketDTO;
import com.hanultari.parking.R;

public class TicketDetailFragment extends Fragment {

  private final TicketDTO dto;

  public TicketDetailFragment(TicketDTO dto) {
    this.dto = dto;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_ticketdetail, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    TextView title = view.findViewById(R.id.ticketTitleDetail);
    TextView content = view.findViewById(R.id.ticketContentDetail);
    TextView writer = view.findViewById(R.id.ticketWriterDetail);
    TextView writedate = view.findViewById(R.id.ticketWritedateDetail);

    title.setText(dto.getTitle());
    content.setText(dto.getContent());
    writer.setText(dto.getWriter());
    writedate.setText(dto.getWritedate());

  }
}
