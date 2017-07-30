package com.adliano.easypermissionsdemo;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/*
 *
 * Adriano Alves
 * July 302017
 * Sample code showing how to use the lib EasyPermissions
 *
 */

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Request Permissions
        // Make sure to add <uses-permission.../> on your Manifest.xml
        // for safety lets check (Optional)
        if(getApplicationRequestedPermissions().length > 0)
        ActivityCompat.requestPermissions(MainActivity.this, getApplicationRequestedPermissions(),1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms)
    {
        // Yeah ! do something....
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms)
    {
        // Oh NO! Notify user :-(
        Snackbar.make(getWindow().getDecorView(),"Permission(s) Denied",Snackbar.LENGTH_LONG).show();

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms))
        {
            // This will display a dialog directing them to enable the permission in app settings.
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // showing a Toast after user returned from app settings screen.
        if(requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE)
        {
            Toast.makeText(getApplicationContext(),"Granted",Toast.LENGTH_SHORT).show();
        }
    }

    // *********************** getApplicationRequestedPermissions ******************** //
    // Method to check if have any permission requested and return the array of all permission
    public String[] getApplicationRequestedPermissions()
    {
        // dynamic list to get any amount of permissions and return it as array
        List<String> permList = new ArrayList<>();

        try
        {
            // Throws PackageManager.NameNotFoundException
            PackageInfo info = getPackageManager().getPackageInfo(getApplicationContext()
                    .getPackageName(), PackageManager.GET_PERMISSIONS);
            if(info.requestedPermissions != null) permList = Arrays.asList(info.requestedPermissions);
        }
        catch(PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        return permList.toArray(new String[permList.size()]);
    }
}