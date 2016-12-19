package com.favn.firstaid.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.favn.firstaid.R;
import com.favn.firstaid.activites.InstructionActivity;
import com.favn.firstaid.activites.MapsActivity;
import com.favn.firstaid.adapters.InjuryAdapter;
import com.favn.firstaid.database.DatabaseOpenHelper;
import com.favn.firstaid.services.location.LocationChangeListener;
import com.favn.firstaid.services.location.LocationFinder;
import com.favn.firstaid.services.location.LocationStatus;
import com.favn.firstaid.commons.CallerInfoSender;
import com.favn.firstaid.utils.Constants;
import com.favn.firstaid.commons.InformationSenderListener;
import com.favn.firstaid.utils.NetworkStatus;
import com.favn.firstaid.utils.SOSCalling;
import com.favn.firstaid.utils.SettingPref;
import com.favn.firstaid.utils.Sort;
import com.favn.firstaid.commons.Injury;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.DatabaseReference;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

import static com.favn.firstaid.utils.Constants.LISTVIEW_EMERGENCY;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmergencyFragment extends Fragment implements AdapterView.OnItemClickListener, LocationChangeListener, InformationSenderListener {
    private InjuryAdapter injuryAdapter;
    private DatabaseOpenHelper dbHelper;
    private ListView listView;
    private List<Injury> mInjuryList;

    // Sending information functionality
    private boolean isAllowedSendInformation;
    private IntentFilter intentFilter;
    private boolean isLocationEnable;
    private boolean isNetworkEnable;
    BroadcastReceiver connectivityBroadcastReceiver;
    private LocationFinder locationFinder;
    private boolean isCalled;
    //add this
    private Location mCurrentLocation;
    private boolean isSentUserInfo;

    DatabaseReference mDb;
    private LinearLayout llSendingStatus;
    private TextView tvSendingInformationStatus;

    private String phoneNumber;

    // Web service url, get caller info from app - KienMT : 11/27/2016
    private String urlAddress;

    MaterialSearchView searchView;

    public EmergencyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Assign url address value (web service url) - Kienmt : 11/27/2016
        urlAddress = "http://admin.rtsvietnam.com/caller";

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_emergency, container, false);

        listView = (ListView) rootView.findViewById(R.id.listView);
        dbHelper = DatabaseOpenHelper.getInstance(getActivity());
        dbHelper.createDatabase();
        dbHelper.openDatabase();
        mInjuryList = dbHelper.getListInjury();
        injuryAdapter = new InjuryAdapter(getActivity(), mInjuryList, LISTVIEW_EMERGENCY, false);
        listView.setAdapter(injuryAdapter);
        listView.setOnItemClickListener(this);
        setHasOptionsMenu(true);
        container.removeAllViews();

        Sort.sortByName(mInjuryList);

        // Sending information setup
        isLocationEnable = false;
        isNetworkEnable = false;

        // Get value isAllowSendInformation from SharePreference
        phoneNumber = SettingPref.loadPhoneNumber(getContext());
        isAllowedSendInformation = (phoneNumber != null) ? true : false;

        if (isAllowedSendInformation) {
            locationFinder = new LocationFinder(getContext(), EmergencyFragment.this);

            createBroadcast();
            intentFilter = new IntentFilter();
            intentFilter.addAction(Constants.INTENT_FILTER_PROVIDERS_CHANGED);
            intentFilter.addAction(Constants.INTENT_FILTER_CONNECTIVITY_CHANGE);
            isCalled = false;
            // add this
            isSentUserInfo = false;
        }

        searchView = (MaterialSearchView) getActivity().findViewById(R.id.search_view);
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {

            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                // Listview return default list
                injuryAdapter = new InjuryAdapter(getActivity(), mInjuryList, LISTVIEW_EMERGENCY, false);
                listView.setAdapter(injuryAdapter);
            }
        });


        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText != null && !newText.isEmpty()) {
                    List<Injury> searchedList = dbHelper.getSearchResultInjuryList(newText);
                    InjuryAdapter adapter = new InjuryAdapter(getActivity(), searchedList,
                            LISTVIEW_EMERGENCY, true);
                    listView.setAdapter(adapter);

                } else {
                    InjuryAdapter adapter = new InjuryAdapter(getActivity(), mInjuryList,
                            LISTVIEW_EMERGENCY, false);
                    listView.setAdapter(adapter);
                }
                return true;
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (connectivityBroadcastReceiver != null) {
            getContext().registerReceiver(connectivityBroadcastReceiver, intentFilter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (connectivityBroadcastReceiver != null) {
            getContext().unregisterReceiver(connectivityBroadcastReceiver);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Injury injury = (Injury) listView.getItemAtPosition(position);
        int injuryId = injury.getId();
        String injuryName = injury.getInjury_name();

        Intent intent = new Intent(getActivity(), InstructionActivity.class);
        intent.putExtra("id", injuryId);
        intent.putExtra("name", injuryName);
        intent.putExtra("typeOfAction", Constants.FROM_EMERGENCY);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        searchView.closeSearch();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_sos_calling) {
            isSentUserInfo = false;
            if (isAllowedSendInformation) {
                if (!isLocationEnable || !isNetworkEnable) {
                    createDialog();
                } else {
                    locationFinder.buildLocationFinder();
                    locationFinder.connectGoogleApiClient();
                    isCalled = true;
                    createSendingInformationUI(true);
                    updateSendingInformationUI();
                    SOSCalling.makeSOSCall(getContext());
                }
            } else {
                SOSCalling.makeSOSCall(getContext());
            }
        }
        if (id == R.id.action_direction) {
            startActivity(new Intent(getActivity(), MapsActivity.class));
        }
        if (id == R.id.action_search) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        searchView.closeSearch();
    }

    private void createBroadcast() {
        // Broadcast for Connectivity status
        connectivityBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Only work when click on off button
                if (intent.getAction().matches(Constants.INTENT_FILTER_PROVIDERS_CHANGED)) {
                    isLocationEnable = LocationStatus.checkLocationProvider(context);

                } else if (intent.getAction().matches(Constants.INTENT_FILTER_CONNECTIVITY_CHANGE)) {
                    isNetworkEnable = NetworkStatus.checkNetworkEnable(context);
                    // Check location enable in connectivity change
                    isLocationEnable = LocationStatus.checkLocationProvider(context);
                }
                if (isCalled && !isSentUserInfo) {
                    updateSendingInformationUI();
                }
            }
        };

        //add this
        if (isNetworkEnable && mCurrentLocation != null && !isSentUserInfo) {
            // Send caller info when having network and location
            sendCallerInfoToServer(mCurrentLocation);
        }
    }

    private void createDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Gửi vị trí")
                .setMessage("Chức năng gửi vị trí cần internet và gps")
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Start call 115
                        SOSCalling.makeSOSCall(getContext());
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        locationFinder.buildLocationFinder();
                        locationFinder.connectGoogleApiClient();
                        if (!NetworkStatus.checkNetworkEnable(getContext())) {
                            createNetworkSetting();
                        }
                        isCalled = true;
                        createSendingInformationUI(true);
                        updateSendingInformationUI();
                    }
                })
                .create()
                .show();
    }

    private void createNetworkSetting() {
        new AlertDialog.Builder(getContext())
                .setTitle("Kết nối Internet")
                .setMessage("Vào cài đặt Internet")
                .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivity(intent);
                    }
                })
                .create()
                .show();
    }

    private void createSendingInformationUI(boolean isShow) {
        llSendingStatus = (LinearLayout) getView().findViewById(R.id.include_sending_status);

        if (isShow) {
            llSendingStatus.setVisibility(View.VISIBLE);
            tvSendingInformationStatus = (TextView) getView().findViewById(R.id
                    .textview_sending_information_status);
        } else {
            llSendingStatus.setVisibility(View.GONE);
        }
    }

    private void updateSendingInformationUI() {
        if (isLocationEnable && isNetworkEnable) {
            tvSendingInformationStatus.setText(Constants.INFO_ENABLE_CONNECTIVITY);
            llSendingStatus.setBackgroundColor(getResources().getColor(R.color.colorSuccess));
        } else {
            tvSendingInformationStatus.setText(Constants.INFO_WARNING_CONNECTIVITY);
            llSendingStatus.setBackgroundColor(getResources().getColor(R.color.colorWarning));

        }
    }

    @Override
    public void createLocationSettingDialog(Status status) {
        try {
            status.startResolutionForResult(getActivity(), Constants.REQUEST_CHECK_SETTINGS);
        } catch (IntentSender.SendIntentException e) {
            //PendingIntent unable to execute request.
        }
    }

    @Override
    public void locationChangeSuccess(Location location) {

        //TODO send information to firebase - START comment by KienMT :
        // Init caller
//        Caller caller = new Caller();
//        caller.setPhone(phoneNo);
//        caller.setInjuryId(0);
//        caller.setLatitude(location.getLatitude());
//        caller.setLongitude(location.getLongitude());
//
//        mDb = FirebaseDatabase.getInstance().getReference();
//        mDb.child("callers").push().setValue(caller);

        // Set location to mCurrentLocation
        mCurrentLocation = location;
        if (isNetworkEnable && mCurrentLocation != null && !isSentUserInfo) {
            // Send caller info when having network and location
            sendCallerInfoToServer(mCurrentLocation);
        }
    }

    // Send caller infor to db server - KienMT : 11/27/2016
    private void sendCallerInfoToServer(Location location) {
        CallerInfoSender callerInforSender = new CallerInfoSender();

        // Assign values
//        ciSender.setContext(getActivity());
//        ciSender.setUrlAddress(urlAddress);
//        ciSender.setPhone(phoneNumber);
//        ciSender.setLatitude(location.getLatitude());
//        ciSender.setLongitude(location.getLongitude());
//        ciSender.setStatus("waiting");
//        ciSender.setInformationSenderListener(EmergencyFragment.this);
//        ciSender.execute();

        callerInforSender.setContext(getActivity());
        callerInforSender.setUrlAddress(urlAddress);
        callerInforSender.setPhone(phoneNumber);
        callerInforSender.setInjuryId(1);
        callerInforSender.setLatitude(location.getLatitude());
        callerInforSender.setLongitude(location.getLongitude());
        callerInforSender.setStatus("waiting");
        callerInforSender.setInformationSenderListener(EmergencyFragment.this);
        callerInforSender.execute();
    }

    @Override
    public void sendInformationListener(String sendingStatus) {
        switch (sendingStatus) {
            case Constants.INFO_SENDING_INFORMATION:
                tvSendingInformationStatus.setText(Constants.INFO_SENDING_INFORMATION);
                llSendingStatus.setBackgroundColor(getResources().getColor(R.color.colorProcessing));
                break;
            case Constants.INFO_SUCCESS_SENDING_INFORMATION:
                tvSendingInformationStatus.setText(Constants.INFO_SUCCESS_SENDING_INFORMATION);
                llSendingStatus.setBackgroundColor(getResources().getColor(R.color.colorSuccess));
                isSentUserInfo = true;
                break;
            case Constants.INFO_ERROR_SENDING_INFORMATION:
                tvSendingInformationStatus.setText(Constants.INFO_ERROR_SENDING_INFORMATION);
                llSendingStatus.setBackgroundColor(getResources().getColor(R.color.colorWarning));
                break;
        }
    }

}
