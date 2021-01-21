package com.hanultari.parking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.hanultari.parking.Activities.NoticeActivity;
import com.hanultari.parking.DTO.NoticeDTO;
import com.hanultari.parking.R;

import java.util.ArrayList;

public class NoticeRecyclerViewAdapter extends RecyclerView.Adapter<NoticeRecyclerViewAdapter.ViewHolder> {

  private final Context context;
  private final ArrayList<NoticeDTO> dtos;
  private OnItemClickListener listener = null;

  public NoticeRecyclerViewAdapter(Context context, ArrayList<NoticeDTO> dtos) {
    this.context = context;
    this.dtos = dtos;
  }

  public interface OnItemClickListener {
    void onItemCLick (View v, int position);
  }

  public void setOnItemClickListener(NoticeRecyclerViewAdapter.OnItemClickListener listener) {
    this.listener = listener;
  }



  public class ViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvtitle;
    private final TextView tvpreview;
    private final TextView tvdate;
    private final LinearLayout noticeItem;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      tvtitle = itemView.findViewById(R.id.noticeTitle);
      tvpreview = itemView.findViewById(R.id.noticePreview);
      tvdate = itemView.findViewById(R.id.noticeDate);
      noticeItem = itemView.findViewById(R.id.noticeItem);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          int pos = getAdapterPosition();
          if (pos != RecyclerView.NO_POSITION) {
            if (listener != null) {
              listener.onItemCLick(v, pos);
            }
          }
        }
      });
    }

  }

  @NonNull
  @Override
  public NoticeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_notice, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull NoticeRecyclerViewAdapter.ViewHolder holder, int position) {
    String title = dtos.get(position).getTitle();
    String preview = dtos.get(position).getTitle();
    String date = dtos.get(position).getWritedate();
    holder.tvtitle.setText(title);
    holder.tvpreview.setText(preview);
    holder.tvdate.setText(date);
    holder.itemView.setTag(dtos.get(position));
  }

  @Override
  public int getItemCount() {
    return dtos.size();
  }
}