package in.co.zoeb.zsmartaudio;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;


public class http {

    private String server = "";
    private  boolean IsHTTPS = false;

    public void set(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("in.co.zoeb.zsmartaudio", Context.MODE_PRIVATE);

        server = preferences.getString("in.co.zoeb.zsmartaudio.serveraddress", "") + "/api/";

        IsHTTPS = preferences.getInt("in.co.zoeb.zsmartaudio.protocoselection", 0) == 2;
    }


    public int register(String userid, String password, String pin) {
        String result = "";
        try {
            userid = userid.replace(" ", "");
            password = password.replace(" ", "");
            pin = pin.replace(" ", "");
            String urlStr = server + "register.php?userid=" + userid + "&password=" + password + "&pin=" + pin;
            URL url = new URL(urlStr);
            HttpURLConnection conn;
            if(!IsHTTPS)
            {
                conn = (HttpURLConnection) url.openConnection();
            } else
            {
                conn = (HttpsURLConnection) url.openConnection();
            }
            InputStream stream;
            if (conn.getResponseCode() == 200) //success
            {
                stream = conn.getInputStream();

            } else {

                return 0;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            return 0;
        }
        if (result.contains("#PASS")) {
            return 1;
        } else {
            return 3;
        }
        //code 0 Internet Error
        //code 1 Success
        //code 3 Validation Error
    }

    public int login(String username, String password) {
        String result = "";
        try {
            username = username.replace(" ", "");
            password = password.replace(" ", "");
            String urlStr = server + "login.php?username=" + username + "&password=" + password;
            URL url = new URL(urlStr);
            HttpURLConnection conn;
            if(!IsHTTPS)
            {
                conn = (HttpURLConnection) url.openConnection();
            } else
            {
                conn = (HttpsURLConnection) url.openConnection();
            }
            InputStream stream;
            if (conn.getResponseCode() == 200) //success
            {
                stream = conn.getInputStream();

            } else {

                return 0;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            return 0;
        }
        if (result.contains("#PASS")) {
            return 1;
        } else {
            return 3;
        }
        //code 0 Internet Error
        //code 1 Success
        //code 3 Validation Error
    }

    public int logout(String username, String password) {
        String result = "";
        try {
            username = username.replace(" ", "");
            password = password.replace(" ", "");
            String urlStr = server + "logout.php?username=" + username + "&password=" + password;
            URL url = new URL(urlStr);
            HttpURLConnection conn;
            if(!IsHTTPS)
            {
                conn = (HttpURLConnection) url.openConnection();
            } else
            {
                conn = (HttpsURLConnection) url.openConnection();
            }
            InputStream stream;
            if (conn.getResponseCode() == 200) //success
            {
                stream = conn.getInputStream();

            } else {

                return 0;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            return 0;
        }
        if (result.contains("#PASS")) {
            return 1;
        } else {
            return 3;
        }
        //code 0 Internet Error
        //code 1 Success
        //code 3 Validation Error
    }

    public int change(String username, String password, String passwordnew) {
        String result = "";
        try {
            username = username.replace(" ", "");
            password = password.replace(" ", "");
            passwordnew = passwordnew.replace(" ", "");
            String urlStr = server + "change_password.php?username=" + username + "&oldpass=" + password + "&newpass=" + passwordnew;
            URL url = new URL(urlStr);
            HttpURLConnection conn;
            if(!IsHTTPS)
            {
                conn = (HttpURLConnection) url.openConnection();
            } else
            {
                conn = (HttpsURLConnection) url.openConnection();
            }
            InputStream stream;
            if (conn.getResponseCode() == 200) //success
            {
                stream = conn.getInputStream();

            } else {

                return 0;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            return 0;
        }
        if (result.contains("#PASS")) {
            return 1;
        } else {
            return 3;
        }
        //code 0 Internet Error
        //code 1 Success
        //code 3 Validation Error
    }

    public String sync(String username, String password) {
        String result = "";
        try {
            username = username.replace(" ", "");
            password = password.replace(" ", "");
            String urlStr = server + "sync.php?username=" + username + "&password=" + password;
            URL url = new URL(urlStr);
            HttpURLConnection conn;
            if(!IsHTTPS)
            {
                conn = (HttpURLConnection) url.openConnection();
            } else
            {
                conn = (HttpsURLConnection) url.openConnection();
            }
            InputStream stream;
            if (conn.getResponseCode() == 200) //success
            {
                stream = conn.getInputStream();

            } else {

                return "";
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            return "";
        }
        if (result.contains("filelocation")) {
            return result;
        } else {
            return "";
        }
        //code 0 Internet Error
        //code 1 Success
        //code 3 Validation Error
    }

    public int update(String userid, String password, String playing) {
        String result = "";
        try {
            userid = userid.replace(" ", "");
            password = password.replace(" ", "");
            playing = playing.replace(" ", "%20");
            String urlStr = server + "update.php?userid=" + userid + "&password=" + password + "&playing=" + playing;
            URL url = new URL(urlStr);
            HttpURLConnection conn;
            if(!IsHTTPS)
            {
                conn = (HttpURLConnection) url.openConnection();
            } else
            {
                conn = (HttpsURLConnection) url.openConnection();
            }
            InputStream stream;
            if (conn.getResponseCode() == 200) //success
            {
                stream = conn.getInputStream();

            } else {

                return 0;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            return 0;
        }
        if (result.contains("#PASS")) {
            return 1;
        } else {
            return 3;
        }
        //code 0 Internet Error
        //code 1 Success
        //code 3 Validation Error
    }

}
