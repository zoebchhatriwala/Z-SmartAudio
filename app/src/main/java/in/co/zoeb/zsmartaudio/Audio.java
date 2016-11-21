package in.co.zoeb.zsmartaudio;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;


public class Audio extends Activity {

    public  static ArrayList<Integer> arrayPosition;
    private static String FilesNamesSelected[];
    private http online;
    private String username = "";
    private String password = "";
    private ListView lv;
    private Boolean FlagForPlayer[], DownloadSuccessfulFlag, OnFlag;
    private ArrayList<String> arrayName, arrayFilelocation;
    private ProgressDialog progress;
    private int progressUpdateValue;
    private SharedPreferences preferences;
    private TextView noData;
    private Handler mHandler = new Handler();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FilesNamesSelected = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        OnFlag = false;
        finish();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        preferences = this.getSharedPreferences("in.co.zoeb.zsmartaudio", Context.MODE_PRIVATE);
        username = preferences.getString("in.co.zoeb.zsmartaudio.username", "");
        password = preferences.getString("in.co.zoeb.zsmartaudio.password", "");
        TextView text = (TextView) findViewById(R.id.audiotextview);
        text.setText("Hi, " + username.toUpperCase());
        arrayName = new ArrayList<>();
        arrayFilelocation = new ArrayList<>();
        arrayPosition = new ArrayList<>();
        online = new http();
        online.set(Audio.this);
        OnFlag = true;
        lv = (ListView) findViewById(R.id.listaudio);
        noData = (TextView) findViewById(R.id.nodata);
        Button play = (Button) findViewById(R.id.play);
        Button sync = (Button) findViewById(R.id.sync);
        play.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (arrayPosition.toArray(new Integer[arrayPosition.size()]).length > 0) {
                    if(preferences.getBoolean("in.co.zoeb.zsmartaudio.autosync",false))
                    {
                        Toast.makeText(Audio.this, "Please Disable AutoSync In Settings To Play Selected", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        downloadInit(false, true);
                    }
                } else {
                    downloadInit(true, true);
                }

            }
        });

        sync.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                    arrayName.clear();
                    arrayFilelocation.clear();
                    arrayPosition.clear();
                    sync();
            }
        });

        AutoSync.run();
    }

    Runnable AutoSync = new Runnable() {
        @SuppressWarnings("UnusedAssignment")
        @Override
        public void run() {


            //noinspection StringEquality
            if (preferences.getBoolean("in.co.zoeb.zsmartaudio.autosync", false) && OnFlag && preferences.getString("in.co.zoeb.zsmartaudio.username", "")!="")
            {
                arrayName.clear();
                arrayFilelocation.clear();
                arrayPosition.clear();
                sync();
                downloadInit(true, false);
                //3600000
                long TimeForAutoSync = 0;
                switch(preferences.getInt("in.co.zoeb.zsmartaudio.autoSyncTime",0))
                {
                    case 0:
                        break;
                    case 1:
                        TimeForAutoSync = 5*60*1000;
                        break;
                    case 2:
                        TimeForAutoSync = 15*60*1000;
                        break;
                    case 3:
                        TimeForAutoSync = 30*60*1000;
                        break;
                    case 4:
                        TimeForAutoSync = 60*60*1000;
                        break;
                    case 5:
                        TimeForAutoSync = 120*60*1000;
                        break;
                    case 6:
                        TimeForAutoSync = 240*60*1000;
                        break;
                    case 7:
                        TimeForAutoSync = 480*60*1000;
                        break;
                    case 8:
                        TimeForAutoSync = 10*1000;
                        break;

                }
                if(TimeForAutoSync > 0) {
                    mHandler.postDelayed(this, TimeForAutoSync);
                }
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(preferences.getBoolean("in.co.zoeb.zsmartaudio.autosync",false))
        {
            AutoSync.run();
        }
        arrayName.clear();
        arrayFilelocation.clear();
        arrayPosition.clear();
        sync();
    }



    public void downloadInit(boolean FilesNotSelected, boolean UsersClicked) {
if((!Arrays.equals(FilesNamesSelected,arrayFilelocation.toArray(new String[arrayFilelocation.size()])) || UsersClicked) && arrayFilelocation.size()>0) {
    if (!FilesNotSelected) {
        FilesNamesSelected = new String[arrayPosition.size()];
        for (int TempPosition = 0; TempPosition < arrayPosition.toArray(new Integer[arrayPosition.size()]).length; TempPosition++) {
            FilesNamesSelected[TempPosition] = arrayFilelocation.toArray(new String[arrayFilelocation.size()])[arrayPosition.toArray(new Integer[arrayPosition.size()])[TempPosition]];
        }
    } else {
        Toast.makeText(Audio.this, "All Audios Selected", Toast.LENGTH_SHORT).show();
        FilesNamesSelected = new String[arrayFilelocation.size()];
        FilesNamesSelected = arrayFilelocation.toArray(new String[arrayFilelocation.size()]);
    }

    FlagForPlayer = new Boolean[FilesNamesSelected.length];
    DownloadSuccessfulFlag = true;
    progressUpdateValue = 0;
    setProgressBar(FilesNamesSelected.length);
    for (int i = 0; i < FilesNamesSelected.length; i++) {

        FlagForPlayer[i] = false;
        Downloader download = new Downloader();
        download.set(FilesNamesSelected[i], i);
        download.execute(FilesNamesSelected[i]);
    }
    new Checker().execute();
}
    }

    public void setProgressBar(int max) {
        progress = new ProgressDialog(Audio.this);
        progress.setMax(max);
        progress.setMessage("Downloading Please Wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
            progress.show();
    }

    public void progressUpdate(int progressinfo) {

        progress.setProgress(progressinfo);
        if (progress.getProgress() == progress.getMax()) {
            progress.dismiss();
        }
    }


    private void sync() {

        String results = online.sync(username, password);
        if (!results.equals("")) {
            noData.setVisibility(View.INVISIBLE);
            try {

                JSONObject jsonRootObject = new JSONObject(results);
                //Get the instance of JSONArray that contains JSONObjects
                JSONArray jsonArray = jsonRootObject.optJSONArray("audio");
                //Iterate the jsonArray and print the info of JSONObjects
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String name = jsonObject.optString("name");
                    String filelocation = jsonObject.optString("filelocation");

                    arrayName.add(name);
                    arrayFilelocation.add(filelocation);

                }
                deleteUnused(arrayFilelocation.toArray(new String[arrayFilelocation.size()]));
                Toast.makeText(Audio.this, "Sync Successful", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                arrayName.clear();
                arrayFilelocation.clear();
                arrayPosition.clear();
                Toast.makeText(Audio.this, "Sync Error Please Try Again And Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
            CustomAdapter adapter = new CustomAdapter(this, arrayName.toArray(new String[arrayName.size()]), arrayFilelocation.toArray(new String[arrayFilelocation.size()]));
            lv.setAdapter(adapter);

        } else {
            Toast.makeText(Audio.this, "No Data Found Or Internet Not Connected", Toast.LENGTH_SHORT).show();
            noData.setVisibility(View.VISIBLE);
            try {
                Player.activityfinish.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
            arrayName.clear();
            arrayFilelocation.clear();
            arrayPosition.clear();
            lv.setAdapter(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.audio, menu);
        return true;
    }


    @SuppressWarnings({"ForLoopReplaceableByForEach", "ResultOfMethodCallIgnored"})
    private void deleteUnused(String[] str) {

        File Path = new File(Environment.getExternalStorageDirectory() + "/SmartAudio/");
        File[] comparestr = Path.listFiles();
        if(comparestr != null) {
            boolean exist = false;
            for (int i = 0; i < comparestr.length; i++) {

                for (int i2 = 0; i2 < str.length; i2++) {

                    if (comparestr[i].getName().equals(str[i2])) {
                        exist = true;
                    }
                }
                if (!exist) {
                    comparestr[i].delete();
                    exist = false;
                } else {
                    exist = false;
                }
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent myIntent = new Intent(Audio.this, Settings.class);
            Audio.this.startActivity(myIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // DownloadFile AsyncTask
    public class Downloader extends AsyncTask<String, Integer, Integer> {


        private String message, FileName;
        private int FlagForPlayerInt;

        @Override
        protected Integer doInBackground(String... Name) {


            try {

                String UrlString = preferences.getString("in.co.zoeb.zsmartaudio.serveraddress", "") + "/uploads/" + Name[0].replace(" ", "%20");
                URL url = new URL(UrlString);
                URLConnection connection =  url.openConnection();
                connection.setRequestProperty("Accept-Encoding", "identity");
                connection.connect();

                // Locate storage location
                String filepath = Environment.getExternalStorageDirectory().getPath();

                File folder = new File(Environment.getExternalStorageDirectory() + "/SmartAudio/");
                if (!folder.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    folder.mkdir();
                }

                File file = new File(Environment.getExternalStorageDirectory() + "/SmartAudio/" + Name[0]);
                if (!file.exists() || file.length() != connection.getContentLength()) {

                    // Download the file
                    InputStream input = new BufferedInputStream(url.openStream());


                    // Save the downloaded file
                    OutputStream output = new FileOutputStream(filepath + "/SmartAudio/" + Name[0]);


                    byte data[] = new byte[1024];
                    int count;
                    while ((count = input.read(data)) != -1) {
                        output.write(data, 0, count);
                    }

                    if(file.length() != connection.getContentLength() && connection.getContentLength() != -1)
                    {
                        doInBackground(Name[0]);
                    }

                    // Close connection
                    output.flush();
                    output.close();
                    input.close();
                    message = "File " + FileName + " Download Complete...";


                } else {

                        message = "File " + FileName + " Already Exist";
                }

            } catch (Exception e) {

                message = "Download Failed!!! Please try again";
                File ErrorPath = new File(Environment.getExternalStorageDirectory() + "/SmartAudio/" + FileName);
                if (ErrorPath.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    ErrorPath.delete();
                }
                DownloadSuccessfulFlag = false;
            }

            return null;
        }

        public void set(String FileName, int FlagForPlayerInt) {
            this.FileName = FileName;
            this.FlagForPlayerInt = FlagForPlayerInt;
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Toast.makeText(Audio.this, message, Toast.LENGTH_SHORT).show();
            progressUpdateValue++;
            progressUpdate(progressUpdateValue);
            FlagForPlayer[FlagForPlayerInt] = true;
        }


    }

    // Checker AsyncTask
    public class Checker extends AsyncTask<String, Integer, Integer> {

        int Count = 0;

        @SuppressWarnings({"PointlessBooleanExpression", "ForLoopReplaceableByForEach"})
        @Override
        protected Integer doInBackground(String... params) {
            int set = 0;
            while (set != 1) {
                for (int i = 0; i < FlagForPlayer.length; i++) {

                    if (FlagForPlayer[i] == true) {
                        Count++;
                    }
                    if (Count == FlagForPlayer.length) {

                        set = 1;
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (DownloadSuccessfulFlag) {

                try {
                    Player.activityfinish.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Bundle FileNamesBundle = new Bundle();
                FileNamesBundle.putStringArray("FileNames", FilesNamesSelected);
                Intent myIntent = new Intent(Audio.this, Player.class);
                myIntent.putExtras(FileNamesBundle);
                Audio.this.startActivity(myIntent);
            } else {
                Toast.makeText(Audio.this, "Your Download Was Not Please Successful Trying Again...", Toast.LENGTH_SHORT).show();
            }
        }
    }




}
