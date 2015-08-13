package com.fifteentec.Fragment;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.fifteentec.FoundAdapter.EventAdapter;
import com.fifteentec.FoundAdapter.FavoriteAdapter;
import com.fifteentec.item.EventBrief;
import com.fifteentec.item.FavoriteBrief;
import com.fifteentec.yoko.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cj on 2015/8/7.
 */
public class FoundFavorite extends Fragment {
    private List<FavoriteBrief> eventList = new ArrayList<FavoriteBrief>();
    private ListView favorites;
    private FavoriteAdapter favoriteAdapter;
    private ImageView ivDeleteText;
    private EditText etSearch;
    private FragmentManager mFragmentManager;
    private FoundFavoriteItem favoriteItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_found_favorite, null);
        initListView(view);

        return view;
    }
    private void initSearchFrame(View parentView){
        ivDeleteText = (ImageView) parentView.findViewById(R.id.ivDeleteText2);
        etSearch = (EditText) parentView.findViewById(R.id.etSearch2);

        ivDeleteText.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                etSearch.setText("");
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    ivDeleteText.setVisibility(View.GONE);
                } else {
                    ivDeleteText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initListView(View parentView){
        createEventList(5, eventList);
        favorites = (ListView) parentView.findViewById(R.id.listView_favorite);
        favoriteAdapter = new FavoriteAdapter(getActivity().getLayoutInflater(),eventList);
        favorites.setAdapter(favoriteAdapter);

        favorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mFragmentManager = FoundFavorite.this.getParentFragment().getFragmentManager();
                FragmentTransaction mFmTrans= mFragmentManager.beginTransaction();

                if(favoriteItem==null){
                    favoriteItem = new FoundFavoriteItem();
                }
                if(mFragmentManager.findFragmentByTag("favoriteItem")!=null){
                    mFmTrans.remove(mFragmentManager.findFragmentByTag("favoriteItem"));
                }

                Bundle args = new Bundle();
                args.putString("name", eventList.get(position).getGroupName());
                args.putString("intro", eventList.get(position).getEventIntro());
                args.putString("uri", eventList.get(position).getLogoUri());
                args.putString("time", eventList.get(position).getTime());
                args.putString("location", eventList.get(position).getLocation());
                args.putString("tags", eventList.get(position).getTags());
                favoriteItem.setArguments(args);

                mFmTrans.add(R.id.id_content, favoriteItem, "eventItem");

                mFmTrans.addToBackStack("eventItem");
                mFmTrans.commit();
                mFmTrans.hide(FoundFavorite.this.getParentFragment());

            }
        });

    }

    private void createEventList(int eventNum,List<FavoriteBrief> list){
        list.clear();
        for(int i=0;i<eventNum;i++){
            FavoriteBrief newItem = new FavoriteBrief();
            newItem.setEventIntro("default");
            newItem.setGroupName("default");
            newItem.setTime("2020-8-12 08:00");
            newItem.setLocation("SJTU");
            list.add(newItem);
        }
    }
}
