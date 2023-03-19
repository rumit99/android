package com.trendingnews.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.trendingnews.R;
import com.trendingnews.adapter.NewsCatAdapter;
import com.trendingnews.model.UploadDataModel;

import java.util.ArrayList;
import java.util.List;


public class FragmentHome extends Fragment {

    View rootView;

    ProgressBar progressBar;

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    private ArrayList<UploadDataModel> imagesList;
    private NewsCatAdapter imageAdapter = null;
    private Query needsQuery;

    TextView tv_no_data_found;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_home, container, false);


        tv_no_data_found = rootView.findViewById(R.id.tv_no_data_found);
        progressBar = rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);


        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(false);


        getData();

        return rootView;

    }

    List<String> colors;
    int colorsint;

    public void getData() {
        colorsint = 0;
        colors = new ArrayList<String>();

        colors.add("#5E97F6");
        colors.add("#91c951");
        colors.add("#FF8A65");
        colors.add("#9E9E9E");
        colors.add("#7981ad");
        colors.add("#4f849e");
        colors.add("#90b368");
        colors.add("#F6BF26");
        colors.add("#fa9d14");
        colors.add("#43c1d1");
        colors.add("#BA68C8");
        colors.add("#f5794c");

        imagesList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("newsCat");

        needsQuery = databaseReference.orderByChild("index");
        needsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
                imagesList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UploadDataModel imagemodel = dataSnapshot.getValue(UploadDataModel.class);
                    imagemodel.setColorId(Color.parseColor(colors.get(colorsint)));
                    imagesList.add(imagemodel);
                    colorsint++;
                    if (colors.size() == colorsint)
                        colorsint = 0;
                }

                imageAdapter = new NewsCatAdapter(getContext(), getActivity(), imagesList);
                recyclerView.setAdapter(imageAdapter);
                imageAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                if (imagesList.size() == 0)
                    tv_no_data_found.setVisibility(View.VISIBLE);
                else
                    tv_no_data_found.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
