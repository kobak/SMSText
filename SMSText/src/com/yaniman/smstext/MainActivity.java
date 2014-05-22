package com.yaniman.smstext;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity
	extends Activity
	implements TextWatcher, View.OnClickListener
{
	Button clearButton;
	Button sendButton;
	TextView textLength;
	EditText editText;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sendButton = (Button) findViewById(R.id.sendButton);
		clearButton = (Button) findViewById(R.id.clearButton);
		textLength = (TextView) findViewById(R.id.textLength);
		editText = (EditText) findViewById(R.id.editText);

		clearButton.setOnClickListener(this);
		sendButton.setOnClickListener(this);
		editText.addTextChangedListener(this);
		textLength.setText("0");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.id.settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void afterTextChanged(Editable s)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String from = prefs.getString(SettingsActivity.KEY_FROM, SettingsActivity.DEF_FROM);
		String to = prefs.getString(SettingsActivity.KEY_TO, SettingsActivity.DEF_TO);

		int n = s.length();
		for(int i=0; i<n; i++)
		{
			int idx = from.indexOf(s.charAt(i));
			if(idx != -1) s.replace(i, i + 1, String.valueOf(to.charAt(idx)));
		}

		String str = s.toString();
		if(prefs.getBoolean(SettingsActivity.KEY_TRIM, SettingsActivity.DEF_TRIM)) str = str.trim();
		textLength.setText(String.valueOf(str.length()));
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
	}

	// buttons
	@Override
	public void onClick(View view)
	{
		switch(view.getId())
		{
		case R.id.clearButton:
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.remove_alert);
				builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						editText.setText("");
					}
				});
				builder.setNegativeButton(R.string.no, null);
				builder.create().show();
			}
			break;

		case R.id.sendButton:
			{
				String str = editText.getText().toString();
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
				if(prefs.getBoolean(SettingsActivity.KEY_TRIM, SettingsActivity.DEF_TRIM)) str = str.trim();

				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("smsto:"));
				intent.setType("vnd.android-dir/mms-sms");
				intent.putExtra("sms_body", str);
				startActivity(intent);
			}
			break;
		}
	}
}
