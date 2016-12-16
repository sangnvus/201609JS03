package com.favn.ambulance.commons;

import android.util.Log;
import android.widget.Toast;

import com.favn.ambulance.activities.WaitingActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Hung Gia on 12/13/2016.
 */

public class FirebaseHandle {
    private FirebaseDatabase database;
    private AmbulanceStatusReturnListener ambulanceStatusReturnListener;

    public FirebaseHandle(AmbulanceStatusReturnListener ambulanceStatusReturnListener) {
        this.ambulanceStatusReturnListener = ambulanceStatusReturnListener;
    }

    public void sendAmbulance(Ambulance ambulance) {
        // Init instant firebase database - KienMT
        database = FirebaseDatabase.getInstance();
        //Init firebase database reference
        DatabaseReference dbRef = database.getReference("ambulances");
        dbRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        DatabaseReference dbRef = database.getReference("ambulances/" + ambulance.getId());
//        dbRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                HashMap<String, String> data = dataSnapshot.get
//                Log.w("data_snapshot", dataSnapshot.toString());
//                Ambulance tmp = (Ambulance) dataSnapshot.getValue();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        updateAmbulance(Constants.AMBULANCE_STATUS_READY);
    }

    public String listenAmbulanceStatusChanged(final int ambulanceId) {
        // Init instant firebase database - KienMT
        database = FirebaseDatabase.getInstance();
        //Init firebase database reference
        //DatabaseReference dbRef = database.getReference("ambulances/" + ambulanceId);
        DatabaseReference dbRef = database.getReference("ambulances/" + ambulanceId);



        dbRef.addChildEventListener(new ChildEventListener() {
            String statusChanged;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                statusChanged = dataSnapshot.getValue().toString();
                ambulanceStatusReturnListener.getAmbulanceStatus(statusChanged);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return null;
    }



}
