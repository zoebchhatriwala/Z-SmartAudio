package in.co.zoeb.zsmartaudio;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button register = (Button) findViewById(R.id.register2);
        register.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                http online = new http();
                online.set(Register.this);
                EditText userid = (EditText) findViewById(R.id.userid);
                EditText password = (EditText) findViewById(R.id.pass);
                EditText passwordagain = (EditText) findViewById(R.id.passagain);
                EditText pin = (EditText) findViewById(R.id.pin);
                if (passwordagain.getText().toString().equals(password.getText().toString()) && !userid.getText().toString().equals("") && !password.getText().toString().equals("") && !pin.getText().toString().equals("")) {
                    int returncode = online.register(userid.getText().toString(), password.getText().toString(), pin.getText().toString());
                    if (returncode == 0) {
                        Toast.makeText(Register.this, "Please Connect To Internet", Toast.LENGTH_SHORT).show();
                    } else if (returncode == 3) {
                        Toast.makeText(Register.this, "UserID Already Exist Or Wrong Pin Entered", Toast.LENGTH_LONG).show();
                    } else if (returncode == 1) {
                        Toast.makeText(Register.this, "Registration Success", Toast.LENGTH_LONG).show();
                        finish();
                    }
                } else {
                    Toast.makeText(Register.this, "Fields Empty Or Passwords Doesn't Match", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
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
