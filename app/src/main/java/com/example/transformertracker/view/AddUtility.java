package com.example.transformertracker.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;


import com.example.transformertracker.R;
import com.example.transformertracker.model.Transformer;
import com.example.transformertracker.services.Services;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddUtility.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddUtility#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddUtility extends DialogFragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String TAG = "AddUtility";
    public static  final String ARG_PARAM1 = "transformer";
    Transformer mTransformer;

    private OnFragmentInteractionListener mListener;
    private Context getcontext;
    private EditText name, address, serial, meter, brand, year, status;
    private Spinner voltage, power, feeder, zone, cat;
    private String mVolt, mPower, mFeeder, mZone, mCat;
    private View mView;
    private ImageView imageView;
    private Uri mSelectedImage = null;

    Dialog mDialog;


    public AddUtility() {
        // Required empty public constructor
    }


    public static AddUtility newInstance(Transformer transformer) {
        AddUtility fragment = new AddUtility();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, transformer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTransformer = getArguments().getParcelable(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.add_utility_marker, container, false);
              return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(mView);

    }

    private void initialize(View view){
        Log.d(TAG, "initialize: initializing addUtility");
        name = view.findViewById(R.id.name);
        address = view.findViewById(R.id.address);
        serial = view.findViewById(R.id.serial_no);
        brand = view.findViewById(R.id.brand_name);
        meter = view.findViewById(R.id.meter_no);
        year = view.findViewById(R.id.year);
        voltage = view.findViewById(R.id.voltage);
        status = view.findViewById(R.id.status);
        power = view.findViewById(R.id.rating_sp);
        feeder  = view.findViewById(R.id.feeder);
        zone = view.findViewById(R.id.zone2);
        cat = view.findViewById(R.id.categoryS);
        imageView = view.findViewById(R.id.input_photo);
        FloatingActionButton submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(mView -> submit());
        Spinner[] spinners = {voltage, power, feeder, zone, cat};
        int[] arr = {R.array.ratings, R.array.voltage, R.array.agbarafeeder, R.array.agbaraZones, R.array.category};
        setUpSpinners(spinners, arr);
        voltage.setOnItemSelectedListener(this);
        power.setOnItemSelectedListener(this);
        feeder.setOnItemSelectedListener(this);
        zone.setOnItemSelectedListener(this);
        cat.setOnItemSelectedListener(this);
        imageView.setOnClickListener(mView -> {
            selectImage(getcontext);
        });

        mDialog = new Dialog(getActivity(), R.style.NewDialog);
        mDialog.addContentView(
                new ProgressBar(mView.getContext()),
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        );

        mDialog.setCancelable(true);

    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, (dialog, item) -> {

            if (options[item].equals("Take Photo")) {
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);

            } else if (options[item].equals("Choose from Gallery")) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);

            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            mSelectedImage = null;
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        mSelectedImage = data.getData();
//                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//                        imageView.setImageBitmap(bitmap);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        mSelectedImage =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                        if (mSelectedImage != null) {
//                            Cursor cursor = getcontext.getContentResolver().query(mSelectedImage,
//                                    filePathColumn, null, null, null);
//                            if (cursor != null) {
//                                cursor.moveToFirst();
//
//                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                                String picturePath = cursor.getString(columnIndex);
//                                imageView.setImageBitmap(null);
//                                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//                                cursor.close();
//                            }
//                        }

                    }
                    break;
            }
        }
    }

    private void submit() {
        mDialog.show();
        String mAssetName = name.getText().toString();
       String mAssetAddress = address.getText().toString();
       String mYrManufacture = year.getText().toString();
        String mMeterNo = meter.getText().toString();
        String mBrand = brand.getText().toString();
        String mSerialNo = serial.getText().toString();
        String mTimeStamp = getTime();
        String mStatus = status.getText().toString();

        if(validateForm(mAssetName, mAssetAddress, mBrand, mSerialNo, mYrManufacture, mZone, mFeeder, mVolt, mPower,mMeterNo, mStatus, mSelectedImage)){
//            Toast.makeText(getcontext,"Completed form", Toast.LENGTH_SHORT).show();
            Transformer transformer = new Transformer(mAssetName, mAssetAddress, mVolt, mFeeder, mZone, mPower, mBrand, mTransformer.latitude, mTransformer.longitude, mStatus, mSerialNo, mMeterNo, mYrManufacture, mTimeStamp, mCat,  "");
            LiveData<Boolean> res = Services.getInstance(getcontext).ret;
            res.observe(this, result->{
                if(result) {
                    Log.d(TAG, "submit: done");
                    dismiss();
                }else {
                    Log.d(TAG, "submit: error");
                    Toast.makeText(getcontext, "Please ensure Network Connection and try again!", Toast.LENGTH_SHORT).show();
                }
                mDialog.dismiss();
            });
            Services.getInstance(getcontext).submitDetails(transformer, mSelectedImage);



        }


    }
    private boolean validateForm(String name, String address, String brand, String serial, String year,
                                 String zone,String feeder,String voltage,
                                 String power, String meterNo, String status, Uri imagePath ) {
        if (TextUtils.isEmpty(name)) {
            this.name.setError(getString(R.string.required));
            return false;
        } else if (TextUtils.isEmpty(voltage)) {
            Toast.makeText(getcontext, "Choose type", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(address)) {
            this.address.setError(getString(R.string.required));
            return false;
        } else if (TextUtils.isEmpty(status)) {
            this.status.setError(getString(R.string.required));
            return false;
        } else if (TextUtils.isEmpty(brand)) {
            this.brand.setError(getString(R.string.required));
            return false;
        } else if (TextUtils.isEmpty(year)) {
            this.year.setError(getString(R.string.required));
            return false;
        } else if (TextUtils.isEmpty(zone)) {
            Toast.makeText(getcontext, "Choose zone", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(meterNo)) {
            meter.setError(getString(R.string.required));
            return false;
        }else if (TextUtils.isEmpty(serial)) {
            this.serial.setError(getString(R.string.required));
            return false;
        }else if (TextUtils.isEmpty(feeder)) {
            Toast.makeText(getcontext, "Choose feeder", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(power)) {
            Toast.makeText(getcontext, "Choose power rating", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(voltage)) {
            Toast.makeText(getcontext, "Choose voltage ratio", Toast.LENGTH_LONG).show();
            return false;
        }else if (mSelectedImage == null) {
            Toast.makeText(getcontext, "Include a picture", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (mCat == null) {
        Toast.makeText(getcontext, "Choose voltage ratio", Toast.LENGTH_LONG).show();
        return false;
    }
        else {
            this.name.setError(null);
            this.address.setError(null);
            this.serial.setError(null);
            this.meter.setError(null);
            this.status.setError(null);
            this.year.setError(null);
            this.brand.setError(null);
            return true;
        }
    }



    private void setUpSpinners(Spinner[] spinners, int[] arrayRes){
        for(int i = 0; i < spinners.length; i++){
            setupSpinner(getcontext, arrayRes[i], android.R.layout.simple_spinner_item, spinners[i] );
        }
    }

    public void setupSpinner(Context context, int textArrayResid, int textViewResid, Spinner spinner){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, textArrayResid, textViewResid);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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






    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        String time = sdf.format(new Date());
        return time;
    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
//            mListener.onFragmentInteraction(entry);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
            getcontext = context;

//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }





    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
//        void onFragmentInteraction(Entry entry);
    }
}
