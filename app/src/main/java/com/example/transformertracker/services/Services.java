package com.example.transformertracker.services;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ajts.androidmads.library.SQLiteToExcel;
import com.example.transformertracker.db.DBHelper;
import com.example.transformertracker.db.DBQueries;
import com.example.transformertracker.model.FirebaseQueryLiveData;
import com.example.transformertracker.model.Transformer;
import com.example.transformertracker.model.Zones;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Services extends ViewModel {
    private static Services ourInstance = null;
    private static final DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
    private static final String assetlist = "assets";
    public MutableLiveData<Boolean> ret = new MutableLiveData<>();
    private List<Transformer> txList = new ArrayList<Transformer>();
    private List<Transformer> assList = new ArrayList<Transformer>();
    private List<Zones> znList = new ArrayList<>();
    FirebaseQueryLiveData mLiveData;
    private static final String TAG = "Services";

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    static DBHelper dbHelper;
    static DBQueries dbQueries;
    String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Backup/";
    SQLiteToExcel sqliteToExcel;
    static Context mContext;
    public MutableLiveData<String> mExportResult;

    public static Services getInstance(Context context) {
        if(ourInstance == null){
        ourInstance = new Services();
            mContext = context;
            dbHelper = new DBHelper(mContext);
            dbQueries = new DBQueries(mContext);

        }
        return ourInstance;
    }

    private Services() {
        File file = new File(directory_path);
        if (!file.exists()) {
            Log.v("File Created", String.valueOf(file.mkdirs()));
        }


    }

    public void submitDetails(Transformer transformer, Uri filePath){
        dbQueries.open();
        dbQueries.insertUser(transformer);
        String uuid = UUID.randomUUID().toString();
        transformer.imageUri = uuid;

        DatabaseReference mdatabase = mref.child(assetlist).child(transformer.zone).child(transformer.name);
        mdatabase.setValue(transformer).addOnCompleteListener(view ->{
          if(view.isSuccessful()){
              uploadImage(filePath, transformer.zone, uuid);


          }else {
              ret.setValue(false);
          }
        });
    }

    public LiveData<List<Transformer>> getAssetDetails(String zone){
        DatabaseReference mdatabase = mref.child(assetlist).child(zone);
        mLiveData = new FirebaseQueryLiveData(mdatabase);
        LiveData<List<Transformer>> mAssetLiveData =
                Transformations.map(mLiveData, new DeserializerAssetList());
        return mAssetLiveData;
    }

    public LiveData<List<Transformer>> getMapDetails(){
        DatabaseReference mdatabase = mref.child(assetlist);
        mLiveData = new FirebaseQueryLiveData(mdatabase);
        LiveData<List<Transformer>> mAssetLiveData =
                Transformations.map(mLiveData, new DeserializerMapList());
        return mAssetLiveData;
    }

    public LiveData<List<Zones>> getZoneDetails(){
        mLiveData= null;


        DatabaseReference mdatabase = mref.child(assetlist);
        mLiveData = new FirebaseQueryLiveData(mdatabase);
        LiveData<List<Zones>> mAssetLiveData =
                Transformations.map(mLiveData, new DeserializerZoneList());
        return mAssetLiveData;

//        LiveData<List<Zones>> mAss = generate();
//        return mAss;
    }

    public MutableLiveData<List<Zones>> generate(){
        MutableLiveData<List<Zones>> arrs = new MutableLiveData<>();
        List<Zones> arr = new ArrayList<>();
        arr.add(new Zones("dada", "sds", "ddsda"));
        arr.add(new Zones("dadad", "sds", "ddsda"));
        arr.add(new Zones("dadada", "sds", "ddsda"));
        arrs.postValue(arr);
        return arrs;
    }

    public void uploadImage(Uri filePath, String zone, String uuid){

        StorageReference ref
                = storageReference.child("images/" + zone + "/" + uuid);
        ref.putFile(filePath)
                .addOnSuccessListener(taskSnapshot -> ret.setValue(true)).addOnFailureListener(e -> ret.setValue(false));
    }

    public void exportAsExcel(){
        mExportResult = new MutableLiveData<>();
        sqliteToExcel = new SQLiteToExcel(mContext, DBHelper.DB_NAME, directory_path);
        String fileName = "transformerlist.xls";
        sqliteToExcel.exportAllTables(fileName, new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted(String s)
            {
                Log.d(TAG, "onCompleted: "+ s);
                mExportResult.postValue(directory_path + fileName);
            }

            @Override
            public void onError(Exception e)
            {
                Log.d(TAG, "onError: "+ e.getMessage());
                mExportResult.setValue("false");
            }
        });
    }

    private class DeserializerAssetList implements Function<DataSnapshot, List<Transformer>> {

        @Override
        public List<Transformer> apply(DataSnapshot dataSnapshot) {
            txList.clear();
            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                //Log.d("TAG","Peeru Value"+snap.getValue().toString());
//                for(DataSnapshot snap2 : snap.getChildren()){
//                    Log.d(TAG, "apply: " +snap2.getKey());
                    Transformer tx = snap.getValue(Transformer.class);
                    Log.d(TAG, "apply: " + tx.name);
                    txList.add(tx);
//                }

            }
            return txList;
        }
    }

    private class DeserializerMapList implements Function<DataSnapshot, List<Transformer>> {

        @Override
        public List<Transformer> apply(DataSnapshot dataSnapshot) {
            assList.clear();
            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                Log.d("TAG","Peeru Value"+snap.getValue().toString());
                for(DataSnapshot snap2 : snap.getChildren()){
                    Log.d(TAG, "apply: " +snap2.getKey());
                Transformer tx = snap2.getValue(Transformer.class);
                Log.d(TAG, "apply: map " + tx.name);
                assList.add(tx);
                }

            }
            return assList;
        }
    }



    private class DeserializerZoneList implements Function<DataSnapshot, List<Zones>> {

        @Override
        public List<Zones> apply(DataSnapshot dataSnapshot) {
            znList.clear();
            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                //Log.d("TAG","Peeru Value"+snap.getValue().toString());
                String name = snap.getKey();
                Zones zn = new Zones();
                zn.name = name;
                Log.d(TAG, "apply: " + zn.name);
                znList.add(zn);
            }
            return znList;
        }
    }


}
