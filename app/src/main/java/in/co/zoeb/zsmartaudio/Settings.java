package in.co.zoeb.zsmartaudio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Settings extends Activity {


    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button logout = (Button) findViewById(R.id.logout);
        Button Change = (Button) findViewById(R.id.change);
        final ToggleButton Autosync = (ToggleButton) findViewById(R.id.autosync);
        preferences = this.getSharedPreferences("in.co.zoeb.zsmartaudio", Context.MODE_PRIVATE);

        Spinner dropdown = (Spinner)findViewById(R.id.spinner1);
        String[] items = new String[]{"Auto Sync Minutes", "5", "15","30","60 (Recommended)","120","240","480","10 Seconds (Testing)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        dropdown.setSelection(preferences.getInt("in.co.zoeb.zsmartaudio.autoSyncTime",0));

        if (preferences.getBoolean("in.co.zoeb.zsmartaudio.autosync", false)) {
            Autosync.setChecked(true);

        }

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                preferences.edit().putInt("in.co.zoeb.zsmartaudio.autoSyncTime",position).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Change.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                http online = new http();
                online.set(Settings.this);
                EditText oldpassword = (EditText) findViewById(R.id.oldpasschange);
                EditText newpassword = (EditText) findViewById(R.id.newpasschange);
                EditText newpasswordagain = (EditText) findViewById(R.id.newpassagainchange);
                if (newpasswordagain.getText().toString().equals(newpassword.getText().toString()) && !newpassword.getText().toString().equals("") && !oldpassword.getText().toString().equals("")) {
                    int returncode = online.change(preferences.getString("in.co.zoeb.zsmartaudio.username", ""), oldpassword.getText().toString(), newpassword.getText().toString());
                    if (returncode == 0) {
                        Toast.makeText(Settings.this, "Please Connect To Internet", Toast.LENGTH_SHORT).show();
                    } else if (returncode == 3) {
                        Toast.makeText(Settings.this, "Password Change UnSuccessful", Toast.LENGTH_SHORT).show();
                    } else if (returncode == 1) {
                        Toast.makeText(Settings.this, "Password Change Successful", Toast.LENGTH_LONG).show();
                        preferences.edit().putString("in.co.zoeb.zsmartaudio.username", "").apply();
                        preferences.edit().putString("in.co.zoeb.zsmartaudio.password", "").apply();
                        Intent myIntent = new Intent(Settings.this, MainActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Settings.this.startActivity(myIntent);
                        finish();
                    }
                } else {
                    Toast.makeText(Settings.this, "Fields Empty Or Password Doesn't Match", Toast.LENGTH_SHORT).show();
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                http online = new http();
                online.set(Settings.this);
                int returncode = online.logout(preferences.getString("in.co.zoeb.zsmartaudio.username", ""), preferences.getString("in.co.zoeb.zsmartaudio.password", ""));
                if (returncode == 1) {
                    preferences.edit().putString("in.co.zoeb.zsmartaudio.username", "").apply();
                    preferences.edit().putString("in.co.zoeb.zsmartaudio.password", "").apply();
                    Intent myIntent = new Intent(Settings.this, MainActivity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Settings.this.startActivity(myIntent);
                    finish();
                } else {
                    Toast.makeText(Settings.this, "Logout Failed!!! Please Connect To Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button about = (Button) findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(Settings.this, "Z-SmartAudio Application\n is used for syncing and playing Music/Audio From Z-SmartAudio Web Server\n\nApplication And Web Server Developed By: Zoeb Jainuddin Chhatriwala \n[www.zoeb.co.in]", Toast.LENGTH_LONG).show();
            }
        });


        Autosync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(preferences.getInt("in.co.zoeb.zsmartaudio.autoSyncTime",0)!=0) {
                        preferences.edit().putBoolean("in.co.zoeb.zsmartaudio.autosync", true).apply();
                    }
                    else
                    {
                        Autosync.setChecked(false);
                        Toast.makeText(Settings.this, "Please Select Time!!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    preferences.edit().putBoolean("in.co.zoeb.zsmartaudio.autosync", false).apply();
                }
            }
        });


        final EditText AdvancesettingsOnOldPassword = (EditText) findViewById(R.id.oldpasschange);

        AdvancesettingsOnOldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(AdvancesettingsOnOldPassword.getText().toString().contains("#advancesettings"))
                {
                    Intent myIntent = new Intent(Settings.this, AdvanceSettings.class);
                    Settings.this.startActivity(myIntent);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        //if (id == R.id.action_settings) {
        //return true;
        //}
        return super.onOptionsItemSelected(item);
    }
}
