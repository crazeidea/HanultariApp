package com.hanultari.parking.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hanultari.parking.Adapter.TicketRecyclerViewAdapter;
import com.hanultari.parking.AsyncTasks.SelectTicket;
import com.hanultari.parking.DTO.TicketDTO;
import com.hanultari.parking.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TicketListFragment extends Fragment {

  ArrayList<TicketDTO> dtos = new ArrayList<>();

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_ticketlist, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    try {
      SelectTicket st = new SelectTicket();
      JSONArray array = st.execute().get();
      for(int i = 0; i < array.length(); i++ ) {
        JSONObject object = (JSONObject) array.get(i);
        TicketDTO dto = new TicketDTO();
        dto.setTitle(object.getString("title"));
        dto.setContent(object.getString("content"));
        dto.setWriter(object.getString("writer"));
        dto.setWritedate(new SimpleDateFormat().format(object.getLong("writedate")));
        dtos.add(dto);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      RecyclerView recyclerView = view.findViewById(R.id.ticketRecyclerView);
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
      TicketRecyclerViewAdapter adapter = new TicketRecyclerViewAdapter(getContext(), dtos);
      recyclerView.setAdapter(adapter);

      adapter.setOnItemClickListener(new TicketRecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
          TicketDTO dto = (TicketDTO) v.getTag();
          FragmentManager fm = getParentFragmentManager();
          for(Fragment fragment : fm.getFragments()) {
            fm.beginTransaction().remove(fragment).commit();
          }
          fm.beginTransaction().replace(R.id.ticketFragContainer, new TicketDetailFragment(dto), "Detail").commitAllowingStateLoss();
        }
      });
    }
  }
}
