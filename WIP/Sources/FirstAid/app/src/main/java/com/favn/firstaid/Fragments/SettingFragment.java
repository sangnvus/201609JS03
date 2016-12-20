package com.favn.firstaid.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.favn.firstaid.R;
import com.favn.firstaid.utils.SettingPref;

import static android.R.attr.key;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {
    String phoneNumber;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new MyPreferenceFragment())
                .commit();

    }

    public static class MyPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        private String phoneNumber = null;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preference_screen);

            // set texts correctly
//            onSharedPreferenceChanged(null, "");
//
//            SwitchPreference sendInformation = (SwitchPreference) findPreference("switch_sending_information");
//            boolean checked = sendInformation.isEnabled();
//            if (checked) {
//
//            }

            phoneNumber = SettingPref.loadPhoneNumber(getActivity());
            if (phoneNumber != null) {
                Preference pref = findPreference("et_phone_number");
                pref.setSummary(phoneNumber);
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            // Set up a listener whenever a key changes
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            // Set up a listener whenever a key changes
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            Preference pref = findPreference(key);

            if (pref instanceof EditTextPreference) {
                EditTextPreference etPhoneNumber = (EditTextPreference) pref;
                phoneNumber = etPhoneNumber.getText();
                pref.setSummary(R.string.summary_switch_sending_phone_number);
                    pref.setSummary(phoneNumber);

            }


        }



    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
