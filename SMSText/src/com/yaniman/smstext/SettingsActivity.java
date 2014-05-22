package com.yaniman.smstext;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity
{
	public static final String KEY_TRIM = "pref_trim";
	public static final String KEY_FROM = "pref_from";
	public static final String KEY_TO = "pref_to";

	public static boolean DEF_TRIM = true;
	public static final String DEF_FROM = "áűőúóíÁŰŐÚÓÍ";
	public static final String DEF_TO   = "àüöùòìÅÜÖUOI";

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}
