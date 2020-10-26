package com.example.transformertracker.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.transformertracker.R;
import com.example.transformertracker.model.Transformer;

import java.util.ArrayList;
import java.util.List;

public class MyClockAdapter extends RecyclerView.Adapter<MyClockAdapter.ClockViewHolder> {

    List<Transformer> mItems = new ArrayList<>();
//    private final Handler mHandler = new Handler();

    @NonNull
    @Override
    public ClockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClockViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClockViewHolder holder, int position) {
        Transformer model = mItems.get(position);
       holder.name.setText(model.name);
       holder.serial.setText(model.serialNo);
       holder.meter.setText(model.meterNo);
       holder.feeder.setText(model.feeder);
       holder.brand.setText(model.brand);

//        holder.itemView.setOnClickListener(view -> holder.gotoClockActivity(model));

    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setItems(List<Transformer> items){
        this.mItems.clear();
        this.mItems.addAll(items);
        notifyDataSetChanged();
    }


    class ClockViewHolder extends RecyclerView.ViewHolder {


        TextView name;
        TextView brand;
        TextView serial;
        TextView status;
        TextView feeder;
        TextView meter;

        public ClockViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.asset_name);
            meter = itemView.findViewById(R.id.meter);
            serial = itemView.findViewById(R.id.serial_no);
            brand = itemView.findViewById(R.id.brand_name);
            feeder = itemView.findViewById(R.id.feeder);

        }

//        public void gotoClockActivity(Transformer details){
//            Intent intent = new Intent(itemView.getContext(), DisplayActivity.class);
//            intent.putExtra(Constants.ACTIVITY_BUNDLE, details);
//            itemView.getContext().startActivity(intent);
//
//        }
    }


}
