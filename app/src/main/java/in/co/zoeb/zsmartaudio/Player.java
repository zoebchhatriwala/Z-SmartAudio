package in.co.zoeb.zsmartaudio;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.Random;


public class Player extends Activity {

    public static Activity activityfinish;
    private Button play;
    private Button pause;
    private TextView FileName, LoopStatus, shuffleStatus, EndTime, CurrentTime;
    private SeekBar PlayerSeekBar;
    private String FileNames[], username = "", password = "";
    private int CurrentPlay=0, loopint = 4, OldPlay = -1;
    private ListView FLV;
    private boolean shuffleBoolean = false;
    private MediaPlayer player;
    private http online;
    private Random random;
    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        //---- Filenames Capture
        Bundle FileNamesBundle = this.getIntent().getExtras();
        FileNames = FileNamesBundle.getStringArray("FileNames");
        //---- End
        Button seekback = (Button) findViewById(R.id.seekback);
        Button seekforward = (Button) findViewById(R.id.seekahead);
        play = (Button) findViewById(R.id.playbtn);
        pause = (Button) findViewById(R.id.pausebtn);
        Button filelist = (Button) findViewById(R.id.files);
        Button loop = (Button) findViewById(R.id.loopbtn);
        Button shuffle = (Button) findViewById(R.id.reshufflebtn);
        FileName = (TextView) findViewById(R.id.audioname);
        LoopStatus = (TextView) findViewById(R.id.loop);
        shuffleStatus = (TextView) findViewById(R.id.reshufflestatus);
        EndTime = (TextView) findViewById(R.id.end);
        CurrentTime = (TextView) findViewById(R.id.current);
        PlayerSeekBar = (SeekBar) findViewById(R.id.seekBar);
        FLV = (ListView) findViewById(R.id.filesListView);
        SharedPreferences preferences = this.getSharedPreferences("in.co.zoeb.zsmartaudio", Context.MODE_PRIVATE);
        username = preferences.getString("in.co.zoeb.zsmartaudio.username", "");
        password = preferences.getString("in.co.zoeb.zsmartaudio.password", "");
        online = new http();
        online.set(Player.this);
        random = new Random();
        PlayerSeekBar.setEnabled(false);
        FileName.setText(FileNames[0]);
        activityfinish = this;
        //---- Init Media Player
        player = new MediaPlayer();
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //---- End


        //---- MediaPlayer OnCompletion

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                if (loopint == 4) {
                    changeMusicNext(false);
                } else {
                    //noinspection ConstantConditions
                    if (loopint > 0) {
                        changeMusicNext(false);
                    } else {
                        player.reset();
                        LoopStatus.setText("0");
                        pause.setVisibility(View.INVISIBLE);
                        play.setVisibility(View.VISIBLE);

                    }
                    if (CurrentPlay == (FileNames.length - 1)) {
                        loopint--;
                    }
                }

            }
        });

        //---- End


        //---- SeekBar Update

        PlayerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    player.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //---- End

//---- SeekBar Update Handler


        //----

        //---- Files ListView
        ArrayAdapter FilesArrayAdapter = new ArrayAdapter<>(Player.this, android.R.layout.simple_list_item_1);
        //noinspection unchecked
        FilesArrayAdapter.addAll(FileNames);
        FLV.setAdapter(FilesArrayAdapter);
        //--- End

        //---- Shuffle

        shuffle.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                if (shuffleBoolean) {
                    shuffleBoolean = false;
                    shuffleStatus.setText("OFF");
                } else {
                    shuffleBoolean = true;
                    shuffleStatus.setText("ON");
                }

            }
        });

        //---- End

        //---- loop

        loop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loopint == 0) {
                    loopint = 1;
                    LoopStatus.setText("+1");
                } else if (loopint == 1) {
                    loopint = 2;
                    LoopStatus.setText("+2");
                } else if (loopint == 2) {
                    loopint = 3;
                    LoopStatus.setText("+3");
                } else if (loopint == 3) {
                    loopint = 4;
                    LoopStatus.setText("∞");
                } else if (loopint == 4) {
                    loopint = 0;
                    LoopStatus.setText("0");
                }
            }
        });
        //---- End

        //---- filelist Button
        filelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FLV.getVisibility() == View.VISIBLE) {
                    FLV.setVisibility(View.INVISIBLE);
                } else {
                    FLV.setVisibility(View.VISIBLE);
                }

            }
        });
        //---- End


        //---- Play & Pause

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause.setVisibility(View.VISIBLE);
                play.setVisibility(View.INVISIBLE);
                play();
            }
        });


        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause.setVisibility(View.INVISIBLE);
                play.setVisibility(View.VISIBLE);
                pause();
            }
        });
        //---- End


        //---- FLV OnClick

        FLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CurrentPlay = position;
                FileName.setText(FileNames[CurrentPlay]);
                player.reset();
                FLV.setVisibility(View.INVISIBLE);
                if (pause.getVisibility() == View.VISIBLE) {
                    play();
                }
            }
        });

        //--- End

        //---- Seek Forward

        seekforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMusicNext(true);
            }
        });

        //---- End


        //---- Seek Backward

        seekback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMusicPrev();
            }
        });

        //---- End

        //---- SeekBar

        Runnable SeekBarUpdater = new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                if (player != null) {

                    if (player.isPlaying()) {
                        int Time = player.getCurrentPosition();
                        PlayerSeekBar.setProgress(Time);
                        int MinuteTime = Time / (60 * 1000);
                        int SecondTime = (Time % (60 * 1000)) / 1000;
                        CurrentTime.setText(MinuteTime + ":" + SecondTime);
                    }
                }
                mHandler.postDelayed(this, 1000);
            }
        };

        SeekBarUpdater.run();

        //---- End


        // User Play Updater
        Runnable OnlineActivityUpdater = new Runnable() {

            @Override
            public void run() {

                if (player != null) {


                    if (player.isPlaying()) {

                        if (CurrentPlay != OldPlay) {

                            int resultcode = online.update(username, password, FileNames[CurrentPlay]);
                            if (resultcode != 1) {
                                Toast.makeText(Player.this, "Please Connect To Internet", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                OldPlay = CurrentPlay;
                            }

                        }

                    }
                }
                mHandler.postDelayed(this, 5000);
            }

        };

        OnlineActivityUpdater.run();
        //

        // Auto Sync

        if (preferences.getBoolean("in.co.zoeb.zsmartaudio.autosync", false)) {
            pause.setVisibility(View.VISIBLE);
            play.setVisibility(View.INVISIBLE);
            player.reset();
            CurrentPlay = 0;
            OldPlay = -1;
            play();
        }

        // End


    }

    @Override
    protected void onDestroy() {
        int resultcode = online.update(username, password, "Not Playing");
        if (resultcode != 1) {
            Toast.makeText(Player.this, "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
        OldPlay = -1;
        player.reset();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        if (FLV.getVisibility() == View.VISIBLE) {
            FLV.setVisibility(View.INVISIBLE);
        } else {
            player.stop();
            finish();
            super.onBackPressed();
        }

    }

    //---- Music Changer

    public void changeMusicNext(boolean id) {

        if (CurrentPlay < FileNames.length) {
            if (shuffleBoolean && id) {
                int Temprandom = CurrentPlay;
                while (CurrentPlay == Temprandom) {
                    Temprandom = random.nextInt((FileNames.length));
                }
                CurrentPlay = Temprandom;
                FileName.setText(FileNames[CurrentPlay]);
                player.reset();
                if (pause.getVisibility() == View.VISIBLE) {
                    play();
                }
            } else {
                if (CurrentPlay == (FileNames.length - 1)) {
                    CurrentPlay = 0;
                } else {
                    CurrentPlay++;
                }
                FileName.setText(FileNames[CurrentPlay]);
                player.reset();
                if (pause.getVisibility() == View.VISIBLE) {
                    play();
                }
            }
        }

    }

    public void changeMusicPrev() {
        if (shuffleBoolean) {
            int Temprandom = CurrentPlay;
            while (CurrentPlay == Temprandom) {
                Temprandom = random.nextInt((FileNames.length));
            }
            CurrentPlay = Temprandom;
            FileName.setText(FileNames[CurrentPlay]);
            player.reset();
            if (pause.getVisibility() == View.VISIBLE) {
                play();
            }
        } else {
            if (CurrentPlay == 0) {
                CurrentPlay = (FileNames.length - 1);
            } else {
                CurrentPlay--;
            }
            FileName.setText(FileNames[CurrentPlay]);
            player.reset();
            if (pause.getVisibility() == View.VISIBLE) {
                play();
            }
        }
    }
    //---- End


    @SuppressLint("SetTextI18n")
    private void play() {

        File Path = new File(Environment.getExternalStorageDirectory() + "/SmartAudio/" + FileNames[CurrentPlay]);
        if(Path.exists()) {
            try {
                player.setDataSource(Player.this, Uri.parse(Path.getPath()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                player.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
            int Time = player.getDuration();
            int MinuteTime = Time / (60 * 1000);
            int SecondTime = (Time % (60 * 1000)) / 1000;
            EndTime.setText(MinuteTime + ":" + SecondTime);
            PlayerSeekBar.setMax(Time);
            PlayerSeekBar.setEnabled(true);
            player.start();
        }
        else
        {

            if(FileNames.length > 1)
            {
                Toast.makeText(Player.this, FileNames[CurrentPlay] + " File Error!!! Please Go Back And ReSync", Toast.LENGTH_SHORT).show();
                changeMusicNext(false);
            }
            else
            {
                Toast.makeText(Player.this,  FileNames[CurrentPlay] + " File Error!!! Please ReSync", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void pause() {
        int resultcode = online.update(username, password, FileNames[CurrentPlay] + " [Paused]");
        if (resultcode != 1) {
            Toast.makeText(Player.this, "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
        OldPlay = -1;
        player.pause();
    }


}
