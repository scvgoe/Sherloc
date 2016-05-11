package com.example.daesungkim.sherloc;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by daeseongkim on 2015. 11. 29..
 */
public class ObjectListFragment extends Fragment {
    ListView _listView ;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.object_list_fragment, container, false);
        _listView = (ListView)rootView.findViewById(R.id.object_listView);
        swipeRefreshLayout= (SwipeRefreshLayout) rootView.findViewById(R.id.refreshView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                get_list();
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                get_list();
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        return rootView;
    }

    public void get_list () {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("object");
        query.whereEqualTo("owner", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objList, ParseException e) {
                if (e == null) {
                    ArrayList<String> list = new ArrayList<String>();
                    list.clear();
                    for (int i = 0; i < objList.size(); i++) {
                        list.add(objList.get(i).getString("objectName"));
                    }

                    CustomAdapter adapter = new CustomAdapter(getActivity(), list, getResources());
                    _listView.setAdapter(adapter);
                    swipeRefreshLayout.setRefreshing(false);


                } else {

                    Toast.makeText(getContext(), "No object to show", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

