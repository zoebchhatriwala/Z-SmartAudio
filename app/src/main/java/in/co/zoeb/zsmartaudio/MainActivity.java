package in.co.zoeb.zsmartaudio;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            if (android.os.Build.VERSION.SDK_INT >= 23) {
                //noinspection StatementWithEmptyBody
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                  //All OKAY
                } else {

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                }
            }
        }



        preferences = this.getSharedPreferences("in.co.zoeb.zsmartaudio", Context.MODE_PRIVATE);
        //noinspection StringEquality
        if (preferences.getString("in.co.zoeb.zsmartaudio.username", "") != "" && preferences.getString("in.co.zoeb.zsmartaudio.password", "") != "") {
            Intent myIntent = new Intent(MainActivity.this, Audio.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            MainActivity.this.startActivity(myIntent);
        }
        setContentView(R.layout.activity_main);
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                http online = new http();
                online.set(MainActivity.this);
                EditText username = (EditText) findViewById(R.id.username);
                EditText password = (EditText) findViewById(R.id.password);
                if (!username.getText().toString().equals("") && !password.getText().toString().equals("")) {
                    int returncode = online.login(username.getText().toString(), password.getText().toString());
                    if (returncode == 0) {
                        Toast.makeText(MainActivity.this, "Please Connect To Internet", Toast.LENGTH_SHORT).show();
                    } else if (returncode == 3) {
                        Toast.makeText(MainActivity.this, "Wrong Credentials Or User Already Logged In", Toast.LENGTH_SHORT).show();
                    } else if (returncode == 1) {
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        preferences.edit().putString("in.co.zoeb.zsmartaudio.username", username.getText().toString().replace(" ", "")).apply();
                        preferences.edit().putString("in.co.zoeb.zsmartaudio.password", password.getText().toString().replace(" ", "")).apply();
                        Intent myIntent = new Intent(MainActivity.this, Audio.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        MainActivity.this.startActivity(myIntent);
                        finish();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Fields Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Register.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        final EditText AdvancesettingsOnOldPassword = (EditText) findViewById(R.id.password);

        AdvancesettingsOnOldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(AdvancesettingsOnOldPassword.getText().toString().contains("#advancesettings"))
                {
                    Intent myIntent = new Intent(MainActivity.this, AdvanceSettings.class);
                    MainActivity.this.startActivity(myIntent);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        //if (id == R.id.action_settings) {
        //	return true;
        //}
        return super.onOptionsItemSelected(item);
    }
}
