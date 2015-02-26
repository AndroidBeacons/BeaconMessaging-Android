package com.yahoo.beaconmessaging.fragment;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.yahoo.beaconmessaging.activity.HomeActivity;
import com.yahoo.beaconmessaging.api.ExhibitClient;
import com.yahoo.beaconmessaging.model.Exhibit;

import java.util.Collections;
import java.util.List;

public class NearbyExhibitListFragment extends ExhibitListFragment {
    private BeaconManager beaconManager;

    private static final int REQUEST_ENABLE_BT = 1234;
    private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);
    private static final String TAG = NearbyExhibitListFragment.class.getSimpleName();
    private boolean isVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //configure beacon manager
        configureBeaconManager();
    }

    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if device supports Bluetooth Low Energy.
        if (!beaconManager.hasBluetooth()) {
            Toast.makeText(this.getActivity(), "Device does not have Bluetooth Low Energy", Toast.LENGTH_LONG).show();
            return;
        }

        // If Bluetooth is not enabled, let user enable it.
        if (!beaconManager.isBluetoothEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            //connectToService();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        beaconManager.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconManager.disconnect();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible=true;
            //Toast.makeText(this.getActivity(), "HUrray im visible",Toast.LENGTH_SHORT).show();
            connectToService();
        }
        else { 
            //gone
            isVisible=false;
            if(beaconManager!=null){
                beaconManager.disconnect(); // stop ranging
                ((HomeActivity) getActivity()).getSupportActionBar().setSubtitle("");
            }

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                //connectToService();
                Toast.makeText(this.getActivity(), "Hurray Bluetooth Enabled :)", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this.getActivity(), "Bluetooth not enabled", Toast.LENGTH_LONG).show();
                ((HomeActivity)getActivity()).getSupportActionBar().setSubtitle("Bluetooth not enabled");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    /**
     * Private Methods 
     */
    private void configureBeaconManager() {
        // Configure BeaconManager.
        beaconManager = new BeaconManager(this.getActivity());
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
                // Note that results are not delivered on UI thread.
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Note that beacons reported here are already sorted by estimated
                        // distance between device and beacon.
                        ((HomeActivity) getActivity()).getSupportActionBar().setSubtitle("Found Exhibits: " + beacons.size());
                        //Update the list of beacons in range
                        replaceBeaconsWith(beacons);
                    }
                });
            }
        });
    }

    private void replaceBeaconsWith(List<Beacon> beacons) {
        //Toast.makeText(this.getActivity(), "found "+beacons.size() +" beacons",Toast.LENGTH_SHORT).show();
        //1. make the network request after processing the beacons
        //2. send the exhibit ids to the adapter to populate the recycler view
        if(beacons.size()>0){
            ExhibitClient.getNearByExhibits(beacons, new FindCallback<Exhibit>() {
                @Override
                public void done(List<Exhibit> exhibits, ParseException e) {
                    //add to the adapter
                    addExhibits(exhibits);
                }
            });
        }

    }

    private void connectToService() {
        ((HomeActivity)getActivity()).getSupportActionBar().setSubtitle("Scanning...");
        replaceBeaconsWith(Collections.<Beacon>emptyList());
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);
                } catch (RemoteException e) {
                    Toast.makeText(getActivity(), "Cannot start ranging, something terrible happened",
                            Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Cannot start ranging", e);
                }
            }
        });
    }
    
    
    
    /**
     * Protected Methods 
     */


    @Override
    protected void populateStream() {
        ExhibitClient.resetCachedNearByExhibits();
        ((HomeActivity) getActivity()).getSupportActionBar().setSubtitle("Scanning...");
        connectToService();
    }

    @Override
    protected void loadMore() {

    }
    
}
