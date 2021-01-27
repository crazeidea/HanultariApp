package com.hanultari.parking.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hanultari.parking.Adapter.TicketRecyclerViewAdapter;
import com.hanultari.parking.AsyncTasks.SelectTicket;
import com.hanultari.parking.AsyncTasks.SelectTicketAnswer;
import com.hanultari.parking.DTO.TicketDTO;
import com.hanultari.parking.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TicketListFragment extends Fragment {

  ArrayList<TicketDTO> dtos = new ArrayList<>();
  ArrayList<TicketDTO> answers = new ArrayList<>();

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_ticketlist, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    if (dtos != null) {
      try {
        SelectTicket st = new SelectTicket();
        SelectTicketAnswer sta = new SelectTicketAnswer();
        JSONArray array = st.execute().get();
        for (int i = 0; i < array.length(); i++) {
          JSONObject object = (JSONObject) array.get(i);
          TicketDTO dto = new TicketDTO();
          dto.setTitle(object.getString("title"));
          dto.setContent(object.getString("content"));
          dto.setWriter(object.getString("writer"));
          dto.setWritedate(new SimpleDateFormat().format(object.getLong("writedate")));
          dto.setStatus(object.getInt("status"));
          dtos.add(dto);
          JSONObject answerObject = sta.execute(object.getInt("id")).get();
          TicketDTO answer = new TicketDTO();
          answer.setTitle(answerObject.getString("title"));
          answer.setContent(answerObject.getString("content"));
          answer.setWriter(answerObject.getString("writer"));
          answer.setWritedate(new SimpleDateFormat().format(object.getLong("writedate")));
          answers.add(answer);
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        RecyclerView recyclerView = view.findViewById(R.id.ticketRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        TicketRecyclerViewAdapter adapter = new TicketRecyclerViewAdapter(getContext(), dtos, answers);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new TicketRecyclerViewAdapter.OnItemClickListener() {
          @Override
          public void onItemClick(View v, int position) {
            TicketDTO dto = (TicketDTO) v.getTag(R.string.TICKET_TICKET);
            TicketDTO answer = (TicketDTO) v.getTag(R.string.TICKET_ANSWER);
            FragmentManager fm = getParentFragmentManager();
            for (Fragment fragment : fm.getFragments()) {
              fm.beginTransaction().remove(fragment).commit();
            }
            fm.beginTransaction().replace(R.id.ticketFragContainer, new TicketDetailFragment(dto, answer), "Detail").commitAllowingStateLoss();
          }
        });
      }
    } else {
      TextView textTicket = getActivity().findViewById(R.id.textTicket);
      textTicket.setVisibility(View.VISIBLE);
    }
  }
}
