package com.example.transformertracker.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.transformertracker.R;
import com.example.transformertracker.model.Transformer;
import com.example.transformertracker.model.Zones;
import com.example.transformertracker.recycler.MyClockAdapter;
import com.example.transformertracker.recycler.ZoneAdapter;
import com.example.transformertracker.services.Services;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment{

    private static final String TAG = "DetailsFragment";

    private final static int DATA_FETCHING_INTERVAL=5*1000; //5 seconds
    private int timer = 5;
    private RecyclerView recView;
    private MyClockAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mView;
    private long mLastFetchedDataTimeStamp;
    private LinearLayoutManager mLm;
    private GridLayoutManager mMZoneLayout;
    private boolean status = false;
    private ZoneAdapter mZoneAdapter;
    private FloatingActionButton mFab;

    public static DetailsFragment newInstance() { return new DetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        mView = view;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindViews();
    }

    private void bindViews() {
        // Toolbar toolbar = findViewById(R.id.toolbar);
        recView = mView.findViewById(R.id.recView);
        mSwipeRefreshLayout = mView.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (System.currentTimeMillis() - mLastFetchedDataTimeStamp < DATA_FETCHING_INTERVAL) {
                Log.d(TAG, "\tNot fetching from network because interval didn't reach");
                mSwipeRefreshLayout.setRefreshing(false);
                return;
            }
//            refresh();
            mSwipeRefreshLayout.setRefreshing(false);


        });
        mFab = mView.findViewById(R.id.fab);
        initialize();



        mFab.setOnClickListener(view ->
                {
//            recView.smoothScrollToPosition(0);
            initialize();


                }
        );
    }

    private void initialize(){
        mFab.setVisibility(View.GONE);
        mMZoneLayout = new GridLayoutManager(getActivity(), 3);
//        mAdapter = null;
        recView.setLayoutManager(mMZoneLayout);
        mZoneAdapter = new ZoneAdapter(getActivity());
        recView.setAdapter(mZoneAdapter);
        LiveData<List<Zones>> znList = Services.getInstance(getActivity()).getZoneDetails();
        int i=0;
        znList.observe(this, zns -> {
            Log.d(TAG, "initialize: "+ zns.size());
            mZoneAdapter.setItems(zns);
        });

        goToZone();


    }

    @Override
    public void onPause() {
        super.onPause();

    }


    @Override
    public void onResume() {
        super.onResume();
        bindViews();
    }

    private void refresh(Zones zone){
//        mZoneAdapter = null;
        mAdapter = new MyClockAdapter();
        mLm = new LinearLayoutManager(getActivity());
        mLm.setOrientation(LinearLayoutManager.VERTICAL);
        recView.setLayoutManager(mLm);
        recView.setAdapter(mAdapter);

        LiveData<List<Transformer>> txList = Services.getInstance(getActivity()).getAssetDetails(zone.name);
        status = true;
        mFab.setVisibility(View.VISIBLE);
        txList.observe(this, tx ->mAdapter.setItems(tx));
    }


    private void goToZone(){
        LiveData<Zones> mZoneLive = mZoneAdapter.getZone();
        mZoneLive.observe(this, this::refresh);
    }




}
