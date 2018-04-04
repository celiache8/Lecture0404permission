package com.example.celiachen.lecture0404_permission;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private Activity mActivity;

    private RelativeLayout mRootLayout;
    private Button mButton;

    private final static int REQUEST_CODE = 123; // any number that's greater or equal to 0
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the application context
        mContext = getApplicationContext();
        mActivity = MainActivity.this;

        // find your layout and your button
        mRootLayout = findViewById(R.id.rootLayout);
        mButton = findViewById(R.id.button);

        // override onclick so that when you click it, it checks permission
        mButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // if the version of the android is over API 23
                // I need to check permission at runtime
                // otherwise, I don't
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    // CHECK PERMISSION
                    checkPermission();
                }
            }
        });
    }

    protected void checkPermission() {

        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_CONTACTS)
                + ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {  // if the permission is not granted,

            // show an alert dialog / popup window with request explanations
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CAMERA)
                    || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_CONTACTS)
                    || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    ) {
                // build an alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setMessage("Camera, Read Contacts and Write external storage permissions are required to use this app");
                builder.setTitle("Please grant these permission.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(mActivity,
                                new String[]{
                                        Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_CODE);
                    }

                });


                builder.setNegativeButton("Cancel", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                // directly request for required permission of your app
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.READ_CONTACTS,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE);
            }

        } else {
            // if the permission have already been granted, then show a toast at the bottom
            // informing user
            Toast.makeText(mContext, "Permissions already granted", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if (requestCode == REQUEST_CODE){
            // when the request is cancelled, the result array is empty
            if ( grantResults.length > 0 &&
                    (grantResults[0] + grantResults[1] + grantResults[2] == PackageManager.PERMISSION_GRANTED)){
                // permissions are granted
                Toast.makeText(mContext, "Permission granted", Toast.LENGTH_SHORT).show();
            }
            else{
                // permission is not granted
                Toast.makeText(mContext, "Permissions denied", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
