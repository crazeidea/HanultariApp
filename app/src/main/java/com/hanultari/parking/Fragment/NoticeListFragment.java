package com.hanultari.parking.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hanultari.parking.Activities.NoticeActivity;
import com.hanultari.parking.Adapter.NoticeRecyclerViewAdapter;
import com.hanultari.parking.AsyncTasks.SelectNotice;
import com.hanultari.parking.DTO.NoticeDTO;
import com.hanultari.parking.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NoticeListFragment extends Fragment {
  private static final String TAG = "NoticeListFragment";


  public NoticeListFragment(){};
  ArrayList<NoticeDTO> list = new ArrayList<>();

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_noticelist, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    try {
      SelectNotice sn = new SelectNotice();
      JSONArray array = sn.execute().get();
      for (int i = 0; i < array.length(); i++) {
        JSONObject object = (JSONObject) array.get(i);
        NoticeDTO dto = new NoticeDTO();
        view.setTag(i);
        dto.setId(object.getInt("id"));
        dto.setTitle(object.getString("title"));
        dto.setContent(object.getString("content"));
        dto.setWriter(object.getString("writer"));
        dto.setWritedate(new SimpleDateFormat().format(object.getLong("writedate")));
        list.add(dto);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      RecyclerView recyclerView = view.findViewById(R.id.noticeRecyclerView);
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
      NoticeRecyclerViewAdapter adapter = new NoticeRecyclerViewAdapter(getContext(), list);
      recyclerView.setAdapter(adapter);

      adapter.setOnItemClickListener(new NoticeRecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemCLick(View v, int position) {
          NoticeDTO dto = (NoticeDTO) v.getTag();
          FragmentManager fm = getParentFragmentManager();
          for (Fragment fragment : fm.getFragments() ){
            fm.beginTransaction().remove(fragment).commit();
          }
          fm.beginTransaction().replace(R.id.noticeFragContainer, new NoticeDetailFragment(dto), "Detail").commitAllowingStateLoss();
        }
      });
    }
  }
}
