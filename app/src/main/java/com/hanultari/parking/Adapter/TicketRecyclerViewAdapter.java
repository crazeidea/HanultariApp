package com.hanultari.parking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanultari.parking.DTO.TicketDTO;
import com.hanultari.parking.R;

import java.util.ArrayList;

public class TicketRecyclerViewAdapter extends RecyclerView.Adapter<TicketRecyclerViewAdapter.ViewHolder> {

  private final Context context;
  private final ArrayList<TicketDTO> dtos;
  private final ArrayList<TicketDTO> answers;
  private OnItemClickListener listener = null;

  public TicketRecyclerViewAdapter(Context context, ArrayList<TicketDTO> dtos, ArrayList<TicketDTO> answers) {
    this.context = context;
    this.dtos = dtos;
    this.answers = answers;
  }

  public interface OnItemClickListener {
    void onItemClick(View v, int position);
  }

  public void setOnItemClickListener(TicketRecyclerViewAdapter.OnItemClickListener listener) {
    this.listener = listener;
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private final TextView ticketStatus;
    private final TextView ticketTitle;
    private final TextView ticketPreview;
    private final TextView ticketWritedate;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      ticketStatus = itemView.findViewById(R.id.ticketStatus);
      ticketTitle = itemView.findViewById(R.id.ticketTitle);
      ticketPreview = itemView.findViewById(R.id.ticketPreview);
      ticketWritedate = itemView.findViewById(R.id.ticketDate);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          int pos = getAdapterPosition();
          if (pos != RecyclerView.NO_POSITION) {
            if (listener != null) {
              listener.onItemClick(v, pos);
            }
          }
        }
      });
    }
  }


  @NonNull
  @Override
  public TicketRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_ticket, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull TicketRecyclerViewAdapter.ViewHolder holder, int position) {
    String title = dtos.get(position).getTitle();
    String preview = dtos.get(position).getContent();
    String writedate = dtos.get(position).getWritedate();
    int status = dtos.get(position).getStatus();
    if(status == 0) {
      holder.ticketStatus.setText("답변 대기");
      holder.ticketStatus.setBackgroundResource(R.drawable.label_yellow);
    } else if (status == 1) {
      holder.ticketStatus.setText("답변 완료");
      holder.ticketStatus.setBackgroundResource(R.drawable.label_green);
    }
    holder.ticketTitle.setText(title);
    holder.ticketPreview.setText(preview);
    holder.ticketWritedate.setText(writedate);
    holder.itemView.setTag(R.string.TICKET_TICKET, dtos.get(position));
    if(answers.size() > 0) holder.itemView.setTag(R.string.TICKET_ANSWER, answers.get(position) );
  }

  @Override
  public int getItemCount() {
    return dtos.size();
  }
}
