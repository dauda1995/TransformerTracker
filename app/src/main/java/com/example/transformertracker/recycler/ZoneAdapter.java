package com.example.transformertracker.recycler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transformertracker.R;
import com.example.transformertracker.model.Zones;

import java.util.ArrayList;
import java.util.List;

public class ZoneAdapter extends RecyclerView.Adapter<ZoneAdapter.ViewHolder> {

    private OnFragmentInteractionListener mListener;
    private ArrayList<Zones> mList = new ArrayList<>();
    private int pos;
    private static final String TAG = "ZoneAdapter";
    private Context getContext;
    MutableLiveData<Zones> zoneliveData = new MutableLiveData<>();

    public ZoneAdapter(Context context) {
        this.getContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder view = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.zone_xml, parent, false));
//        mListener = (OnFragmentInteractionListener) getContext;
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Zones model = mList.get(position);
        holder.setItems(model);
        Log.d(TAG, "onBindViewHolder: " + model.name + mList.size());
        holder.itemView.setOnClickListener(view ->{
            Log.d(TAG, "onBindViewHolder: " + model.name);
            this.pos = position;
            zoneliveData.setValue(model);



        });

    }



    public LiveData<Zones> getZone(){
        return zoneliveData;
    }

    public void setItems(List<Zones> items){
        this.mList.clear();
        notifyDataSetChanged();

        this.mList.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, number, recent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.categoryS);
            number = itemView.findViewById(R.id.amount);
            recent = itemView.findViewById(R.id.recent_up);
        }

        public void setItems(Zones zone){
            name.setText(zone.name);
            number.setText(zone.amount);
            recent.setText(zone.recentUpdate);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Zones entry);
    }
}
