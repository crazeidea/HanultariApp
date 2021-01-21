package com.hanultari.parking.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hanultari.parking.DTO.NoticeDTO;
import com.hanultari.parking.R;

public class NoticeDetailFragment extends Fragment {

  private final NoticeDTO dto;

  public NoticeDetailFragment(NoticeDTO dto){
    this.dto = dto;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_noticedetail, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    TextView title = view.findViewById(R.id.noticeTitleDetail);
    TextView content = view.findViewById(R.id.noticeContentDetail);
    TextView writer = view.findViewById(R.id.noticeWriterDetail);
    TextView writedate = view.findViewById(R.id.noticeWritedateDetail);

    title.setText(dto.getTitle());
    content.setText(dto.getContent());
    writer.setText(dto.getWriter());
    writedate.setText(dto.getWritedate());
  }
}
