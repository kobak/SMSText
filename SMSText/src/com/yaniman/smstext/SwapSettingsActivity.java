package com.yaniman.smstext;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class SwapSettingsActivity
	extends Activity
	implements View.OnClickListener
{
	Button resetButton;
	Button addButton;
	Button saveButton;
	LinearLayout list;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swap_settings);

		resetButton = (Button) findViewById(R.id.resetButton);
		addButton = (Button) findViewById(R.id.addButton);
		saveButton = (Button) findViewById(R.id.saveButton);
		list = (LinearLayout) findViewById(R.id.list);

		resetButton.setOnClickListener(this);
		addButton.setOnClickListener(this);
		saveButton.setOnClickListener(this);

		fillList();
	}

	// removeButton
	@Override
	public void onClick(View view)
	{
		switch(view.getId())
		{
		case R.id.addButton:
			{
				LayoutInflater inflater = getLayoutInflater();
				View v = inflater.inflate(R.layout.swap_row, null);
				EditText v_from = (EditText) v.findViewById(R.id.from);
				//EditText v_to = (EditText) v.findViewById(R.id.to);
				Button v_removeButton = (Button) v.findViewById(R.id.removeButton);
				v_removeButton.setOnClickListener(this);
				list.addView(v);
				v_from.requestFocus();
			}
			break;

		case R.id.removeButton:
			{
				final View remove = (View) view.getParent();

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.remove_alert);
				builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						list.removeView(remove);
					}
				});
				builder.setNegativeButton(R.string.no, null);
				builder.create().show();
			}
			break;

		case R.id.saveButton:
			{
				String from = "", to = "";
				int n = list.getChildCount();
				for(int i=0; i<n; i++)
				{
					View v = list.getChildAt(i);
					EditText v_from = (EditText) v.findViewById(R.id.from);
					EditText v_to = (EditText) v.findViewById(R.id.to);
					String sf = v_from.getText().toString();
					String tf = v_to.getText().toString();
					if(sf.length() != 1 || tf.length() != 1)
					{
						if(sf.length() != 1) v_from.requestFocus();
						else v_to.requestFocus();
						AlertDialog.Builder builder = new AlertDialog.Builder(this);
						builder.setMessage(R.string.pref_err_length);
						builder.setNegativeButton(R.string.ok, null);
						builder.create().show();
						return;
					}
					if(from.indexOf(sf.charAt(0)) != -1)
					{
						v_from.requestFocus();
						AlertDialog.Builder builder = new AlertDialog.Builder(this);
						builder.setMessage(R.string.pref_err_duplicate);
						builder.setNegativeButton(R.string.ok, null);
						builder.create().show();
						return;
					}
					from += sf;
					to += tf;
				}
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
				Editor editor = prefs.edit();
				editor.putString(SettingsActivity.KEY_FROM, from);
				editor.putString(SettingsActivity.KEY_TO, to);
				editor.commit();
				finish();
			}
			break;

		case R.id.resetButton:
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.reset_msg);
				builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SwapSettingsActivity.this);
						Editor editor = prefs.edit();
						editor.putString(SettingsActivity.KEY_FROM, SettingsActivity.DEF_FROM);
						editor.putString(SettingsActivity.KEY_TO, SettingsActivity.DEF_TO);
						editor.commit();
						fillList();
					}
				});
				builder.setNegativeButton(R.string.no, null);
				builder.create().show();
			}
			break;
		}
	}

	void fillList()
	{
		list.removeAllViews();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String from = prefs.getString(SettingsActivity.KEY_FROM, SettingsActivity.DEF_FROM);
		String to = prefs.getString(SettingsActivity.KEY_TO, SettingsActivity.DEF_TO);
		LayoutInflater inflater = getLayoutInflater();
		for(int i=0; i<from.length(); i++)
		{
			View view = inflater.inflate(R.layout.swap_row, null);
			EditText v_from = (EditText) view.findViewById(R.id.from);
			EditText v_to = (EditText) view.findViewById(R.id.to);
			Button v_removeButton = (Button) view.findViewById(R.id.removeButton);
			v_from.setText(String.valueOf(from.charAt(i)));
			v_to.setText(String.valueOf(to.charAt(i)));
			v_removeButton.setOnClickListener(this);
			list.addView(view);
		}
	}
}
