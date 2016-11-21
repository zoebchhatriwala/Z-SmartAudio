package in.co.zoeb.zsmartaudio;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AdvanceSettings extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advancesettings);
        final EditText ServerAddress = (EditText) findViewById(R.id.serveraddr);
        Button Save = (Button) findViewById(R.id.SaveAS);

        final Spinner dropdown = (Spinner)findViewById(R.id.protocolselector);
        String[] items = new String[]{"Select Protocol", "Http", "Https"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        final SharedPreferences preferences = this.getSharedPreferences("in.co.zoeb.zsmartaudio", Context.MODE_PRIVATE);

        ServerAddress.setText(preferences.getString("in.co.zoeb.zsmartaudio.serveraddress", ""));

        dropdown.setSelection(preferences.getInt("in.co.zoeb.zsmartaudio.protocoselection",0));

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ServerAddress.getText().toString().replace(" ","").equals("")  && dropdown.getSelectedItemId() != 0)
                {
                    preferences.edit().putString("in.co.zoeb.zsmartaudio.serveraddress", ServerAddress.getText().toString()).apply();
                    preferences.edit().putInt("in.co.zoeb.zsmartaudio.protocoselection", dropdown.getSelectedItemPosition()).apply();
                    Toast.makeText(AdvanceSettings.this, "Information Saved", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(AdvanceSettings.this, "Please Fill Data!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
