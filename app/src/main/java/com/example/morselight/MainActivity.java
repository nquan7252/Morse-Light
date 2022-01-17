package com.example.morselight;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.nio.channels.CancelledKeyException;
import java.security.Policy;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    ArrayList<Items> items;
    MediaPlayer mediaPlayer;
    Button button, changecolorbtn;
    TextView textbox, textView, light;
    String text, morse;
     boolean isSosRun=false;
    boolean isMuted=false;
    ImageView image,sosbtn,mutebtn,infobtn;
    boolean isImgclicked=false;
    CameraManager cameraManager;
    String cameraId=null;
    Camera cam;
    Camera.Parameters p;
    String sos="... --- ...*";
    int i=0;
    List<Camera.Size> previewSizes;
    StringBuilder submorse;
    Camera.Size previewSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textbox = findViewById(R.id.textbox);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            //ask for authorisation
        {
            new AlertDialog.Builder(this).setTitle("Permission Required").setMessage("Please turn on camera permission in order to fully ultilize the app").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        }
        else {
            cam = Camera.open();

            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.croppedmorsesound);

            p = cam.getParameters();
            // ss=p.getSupportedPreviewSizes();
            ///  s=ss.get(0);
            // for (int i = 0; i < previewSizes.size(); i++) {
            //      if (ss.get(i).width > s.width)
            //         s = ss.get(i);
            //   }
            //   p.setPictureSize(s.width,s.height);
            image = findViewById(R.id.image);
            items = new ArrayList<>();
            items.add(new Items('a', ".-"));
            items.add(new Items('b', "-..."));
            items.add(new Items('c', "-.-."));
            items.add(new Items('d', "-.."));
            items.add(new Items('e', "."));
            items.add(new Items('f', "..-."));
            items.add(new Items('g', "--."));
            items.add(new Items('h', "...."));
            items.add(new Items('i', ".."));
            items.add(new Items('j', ".---"));
            items.add(new Items('k', "-.-"));
            items.add(new Items('l', ".-.."));
            items.add(new Items('m', "--"));
            items.add(new Items('n', "-."));
            items.add(new Items('o', "---"));
            items.add(new Items('p', ".--."));
            items.add(new Items('q', "--.-"));
            items.add(new Items('r', ".-."));
            items.add(new Items('s', "..."));
            items.add(new Items('t', "-"));
            items.add(new Items('u', "..-"));
            items.add(new Items('v', "...-"));
            items.add(new Items('w', ".--"));
            items.add(new Items('x', "-..-"));
            items.add(new Items('y', "-.--"));
            items.add(new Items('z', "--.."));
            items.add(new Items('0', "-----"));
            items.add(new Items('1', ".----"));
            items.add(new Items('2', "..---"));
            items.add(new Items('3', "...--"));
            items.add(new Items('4', "....-"));
            items.add(new Items('5', "....."));
            items.add(new Items('6', "-...."));
            items.add(new Items('7', "--..."));
            items.add(new Items('8', "---.."));
            items.add(new Items('9', "----."));
            items.add(new Items(' ', "/"));
            textbox.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            textView = findViewById(R.id.textView);
            mutebtn = findViewById(R.id.mutebtn);
            mutebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isMuted = !isMuted;
                    if (isMuted) {
                        mutebtn.setImageResource(R.drawable.muteon);
                        mediaPlayer.setVolume(0, 0);
                    } else {
                        mutebtn.setImageResource(R.drawable.muteoff);
                        mediaPlayer.setVolume(1, 1);
                    }
                }
            });
            sosbtn = findViewById(R.id.sosbtn);
            sosbtn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    i = 0;
                    isSosRun = !isSosRun;
                    Log.d("Click", "clicked");
                    if (isSosRun) {
                        image.setClickable(false);
                        sosbtn.setImageResource(R.drawable.sosred);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                blink(sos);
                            }
                        }, 10);
                    } else {
                        sosbtn.setImageResource(R.drawable.sosblack);
                        image.setClickable(true);
                    }
                    //Handler handler = new Handler();
                    //  handler.removeCallbacksAndMessages(null);
                    //    handler.postDelayed(new Runnable() {
                    //       @Override
                    //     public void run() {
                    //            while (isSosRun) {
                    //   sosbtn.setImageResource(R.drawable.sosblack);
                }
                //  }
                //   }, 10);
                //  }

            });

            image.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    submorse = new StringBuilder();
                    sosbtn.setClickable(false);
                    isImgclicked = true;
                    image.setClickable(false);
                    i = 0;
                    text = (String) textbox.getText().toString().toLowerCase();
                    if (text.trim().length() > 0) {
                        Log.d("here", text + "h");
                    }

                    // Handler handler=new Handler();
                    // handler.postDelayed(new Runnable() {
                    //     @Override
                    //     public void run() {
                    morse = "";
                    textView.setText("");
                    for (int i = 0; i < text.length(); i++) {
                        for (int j = 0; j < items.size(); j++) {
                            if (text.charAt(i) == items.get(j).getName()) {
                                morse = morse + items.get(j).getCode() + " ";
                            }
                        }
                    }
                    morse = morse.trim();
                    if (morse == "") {
                        Toast.makeText(MainActivity.this, "There is nothing to translate", Toast.LENGTH_LONG).show();
                        image.setClickable(true);
                        sosbtn.setClickable(true);
                        isImgclicked = false;
                    } else {
                        blink(morse);
                    }
                    //   }
                    //  },10);


                    ;

                }

            });
            infobtn = findViewById(R.id.info);
            infobtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, Info.class));
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void blink(String newmorse){
             Handler handler=new Handler();
             if (i<newmorse.length()) {
                 if (newmorse.charAt(i) == '.') {
                     Log.d("test", "found .");
                     handler.postDelayed(new Runnable() {
                         @Override
                         public void run() {
                             if (isSosRun == false) {
                                 if (isImgclicked) {
                                     turnOnDot(newmorse);
                                     image.setImageResource(R.drawable.flashlighton);

                                     submorse.append(newmorse.charAt(i));
                                     textView.setText(submorse.toString());
                                 }
                             } else {
                                 turnOnDot(newmorse);
                             }
                         }
                     }, 250);

                 } else if (newmorse.charAt(i) == '-') {
                     handler.postDelayed(new Runnable() {
                         @Override
                         public void run() {
                             if (isSosRun == false) {
                                 if (isImgclicked) {
                                     turnOnDash(newmorse);
                                     image.setImageResource(R.drawable.flashlighton);

                                     submorse.append(newmorse.charAt(i));
                                     textView.setText(submorse.toString());
                                 }
                             } else {
                                 turnOnDash(newmorse);
                             }
                         }
                     }, 250);

                 } else if (newmorse.charAt(i) == ' ') {
                     handler.postDelayed(new Runnable() {
                         @Override
                         public void run() {
                             if (isSosRun == false) {
                                 if (isImgclicked) {
                                     if (i < newmorse.length() - 1) {

                                         submorse.append(newmorse.charAt(i));
                                         textView.setText(submorse.toString());
                                         i++;
                                         blink(newmorse);


                                     }
                                 }
                             } else {
                                 if (i < newmorse.length() - 1) {
                                     i++;
                                     blink(newmorse);
                                 }
                             }
                         }
                     }, 250);
                 } else if (newmorse.charAt(i) == '/') {
                     handler.postDelayed(new Runnable() {
                         @Override
                         public void run() {
                             if (isSosRun == false) {
                                 if (isImgclicked) {
                                     if (i < newmorse.length() - 1) {

                                         submorse.append(newmorse.charAt(i));
                                         textView.setText(submorse.toString());
                                         i++;
                                         blink(newmorse);


                                     }
                                 }
                             } else {
                                 if (i < newmorse.length() - 1) {
                                     i++;
                                     blink(newmorse);
                                 }
                             }
                         }
                     }, 1250);
                 } else {
                     handler.postDelayed(new Runnable() {
                         @Override
                         public void run() {
                             if (isSosRun) {
                                 i = 0;
                                 blink(newmorse);
                             }
                         }
                     }, 1250);
                 }
             }
            else if (i==newmorse.length()) {
                sosbtn.setClickable(true);
                image.setClickable(true);
                //blink(newmorse);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isImgclicked=false;
                    }
                },10);
            }
    }

@RequiresApi(api = Build.VERSION_CODES.M)
public void turnOnDot(String newmorse) {
        mediaPlayer.seekTo(1000);
            mediaPlayer.start();
            Handler handler1=new Handler();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            cam.setParameters(p);
            cam.startPreview();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.pause();
                    image.setImageResource(R.drawable.flashlightoff);
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    cam.setParameters(p);
                    cam.startPreview();
                    if (i<=newmorse.length()-1) {
                        if (isSosRun==false){
                            i++;
                            blink(newmorse);
                        }
                        else {
                            i++;
                            blink(newmorse);
                        }
                    }
                }
            },250);

        }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void turnOnDash(String newmorse) {
        mediaPlayer.seekTo(1000);
        mediaPlayer.start();
        Handler handler1=new Handler();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        cam.setParameters(p);
        cam.startPreview();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.pause();
                image.setImageResource(R.drawable.flashlightoff);

                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                cam.setParameters(p);
                cam.startPreview();
                if (i<=newmorse.length()-1) {
                    if (isSosRun==false){
                        i++;
                        blink(newmorse);
                    }
                    else {
                        i++;
                        blink(newmorse);
                    }
                }
            }
        },750);

    }

    }


