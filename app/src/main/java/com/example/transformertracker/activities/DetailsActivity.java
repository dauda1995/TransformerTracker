package com.example.transformertracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.transformertracker.R;
import com.example.transformertracker.model.Transformer;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.transformertracker.recycler.MyClockAdapter.ARG_BUNDLE;
import static com.example.transformertracker.view.AddUtility.ARG_PARAM1;

public class DetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Transformer mTransformer;
    private static final String TAG = "DetailsActivity";

    private Context getcontext;
    private EditText name, address, serial, meter, brand, year, status;
    private Spinner voltage, power, feeder, zone, cat;
    private String mVolt, mPower, mFeeder, mZone, mCat;
    private TextView location;
    private Chip meterR, loadR, faultR;
    private View mView;
    private ImageView imageView;
    private Uri mSelectedImage = null;
    private String[] mSelectedArgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mTransformer = getIntent().getParcelableExtra(ARG_BUNDLE);
        mSelectedArgs = new String[]{mTransformer.rating, mTransformer.voltageRatio,
                mTransformer.feeder, mTransformer.zone, mTransformer.category};
        initialize();
        setValues(mTransformer);
    }

    private void initialize(){
        name = findViewById(R.id.name);
        address =findViewById(R.id.address);
        serial =findViewById(R.id.serial_no);
        brand = findViewById(R.id.brand_name);
        meter = findViewById(R.id.meter_no);
        year = findViewById(R.id.year);
        voltage = findViewById(R.id.voltage);
        status = findViewById(R.id.status);
        power = findViewById(R.id.rating_sp);
        feeder  = findViewById(R.id.feeder);
        zone = findViewById(R.id.zone2);
        cat = findViewById(R.id.categoryS);
        imageView = findViewById(R.id.input_photo);
        location = findViewById(R.id.location);
        meterR = findViewById(R.id.meter_reading);
        loadR = findViewById(R.id.load_reading);
        faultR = findViewById(R.id.fault_rec);
        meterR.setOnClickListener(view-> meterReading());
        loadR.setOnClickListener(v -> loadReading());
        faultR.setOnClickListener(v -> faultRecord());
        FloatingActionButton submit = findViewById(R.id.submit);
//        submit.setOnClickListener(mView -> );
        Spinner[] spinners = {voltage, power, feeder, zone, cat};
        int[] arr = {R.array.ratings, R.array.voltage, R.array.agbarafeeder, R.array.agbaraZones, R.array.category};
        setUpSpinners(spinners, arr);
        voltage.setOnItemSelectedListener(this);
        power.setOnItemSelectedListener(this);
        feeder.setOnItemSelectedListener(this);
        zone.setOnItemSelectedListener(this);
        cat.setOnItemSelectedListener(this);
        imageView.setOnClickListener(mView -> {
        });
    }

    private void faultRecord() {
    }

    private void loadReading() {
    }

    private void meterReading() {
    }

    private void setValues(Transformer transformer){
        name.setText(transformer.name);
        address.setText(transformer.location);
        serial.setText(transformer.serialNo);
        brand.setText(transformer.brand);
        meter.setText(transformer.meterNo);
        year.setText(transformer.year);
        status.setText(transformer.status);
        location.setText(transformer.latitude + ", " + transformer.longitude);
        
    }

    private void setUpSpinners(Spinner[] spinners, int[] arrayRes){
        for(int i = 0; i < spinners.length; i++){

            setupSpinner(getApplicationContext(), arrayRes[i], android.R.layout.simple_list_item_1, spinners[i], mSelectedArgs);
        }
    }

    public void setupSpinner(Context context, int textArrayResid, int textViewResid, Spinner spinner, String[] selected){

        String[] arr = getResources().getStringArray(textArrayResid);
        for(int i=0; i< arr.length; i++ ){
            for(int j = 0; j<selected.length; j++){
                Log.d(TAG, "setupSpinner: " + selected[j]);
                if(arr[i].equals(selected[j])){
                    Log.d(TAG, "setupSpinner: inside if");
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, textArrayResid, textViewResid);
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setSelection(i);
                    return;
                }
            }

        }
//

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.voltage:
                mVolt = ((String) voltage.getSelectedItem());
                Log.d(TAG, "onItemSelected: " + mVolt);
                break;
            case R.id.rating_sp:
                mPower = ((String) power.getSelectedItem());
                Log.d(TAG, "onItemSelected: " + mPower);
                break;
            case R.id.feeder:
                mFeeder = ((String) feeder.getSelectedItem());
                Log.d(TAG, "onItemSelected: " + mFeeder);
                break;
            case R.id.zone2:
                mZone = ((String) zone.getSelectedItem());
                Log.d(TAG, "onItemSelected: " + mZone);
                break;
            case R.id.categoryS:
                mCat = ((String) cat.getSelectedItem());
                Log.d(TAG, "onItemSelected: " + mCat);
                break;

            default:
                break;


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}

