package com.example.transformertracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.transformertracker.services.Services;
import com.example.transformertracker.view.SectionsPagerAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MapsActivity extends AppCompatActivity {

    private GoogleMap mMap;
    private LiveData<String> res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Tx Tracker");



        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.savexlc ){
            Services.getInstance(getApplicationContext()).exportAsExcel();
            res= Services.getInstance(getApplicationContext()).mExportResult;
            res.observe(this, results ->{
                if(results != "false"){
                    new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Export Successful").show();
                    String subject = getString(R.string.email_sub);
                    sendEmailWithAttachment(this, subject, results);
                }else {
                    new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Export Unsuccessful")

                            .show();
                }
            });

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        return true;
    }


    public static  void sendEmailWithAttachment(Context ctx, String subject, String fileAndLocation)
    {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("application/excel");
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,  subject);


        File file = new File(fileAndLocation);
        //  File file = getFileStreamPath();
        if (file.exists())
        {
            Log.v("Farmgraze", "Email file_exists!" );
        }
        else
        {
            Log.v("Farmgraze", "Email file does not exist!" );
        }


        Log.v("FarmGraze", "SEND EMAIL FileUri=" + Uri.parse("file:///"+ fileAndLocation));
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///"+  fileAndLocation));

        ctx.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }//end method
}
