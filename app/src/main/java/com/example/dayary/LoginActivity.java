package com.example.dayary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {

    private Intent intent;
    private ImageView imgBack;
    private TextView txtPass, txtLabel;
    private CardView cvZero, cvOne, cvTwo, cvThree, cvFour, cvFive, cvSix, cvSeven, cvEight, cvNine, cvDelete, cvLogin;
    private int loginAttempts = 0;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private String pass = "", passDisplay = "";
    private Vibrator vibrator;
    private int pass_number_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(getAppTheme() ? R.style.Theme_Diary_Dark : R.style.Theme_Diary_Light);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        initViews();
        setEvents();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void inputPass(String s) {
        if (getLoginEnabled()) {
            if (pass.length() < 9) {
                pass += s;
                if (getNew() || intent.getBooleanExtra("change_password", false)) {
                    passDisplay += s;
                } else {
                    passDisplay += "•";
                }
                txtPass.setText(passDisplay);
            }
        }
    }

    private void setEvents() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgBack.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.push));
                onBackPressed();
            }
        });

        cvZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvZero.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.push));
                inputPass("0");
            }
        });

        cvOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvOne.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.push));
                inputPass("1");
            }
        });

        cvTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvTwo.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.push));
                inputPass("2");
            }
        });

        cvThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvThree.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.push));
                inputPass("3");
            }
        });

        cvFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvFour.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.push));
                inputPass("4");
            }
        });

        cvFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvFive.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.push));
                inputPass("5");
            }
        });

        cvSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvSix.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.push));
                inputPass("6");
            }
        });

        cvSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvSeven.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.push));
                inputPass("7");
            }
        });

        cvEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvEight.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.push));
                inputPass("8");
            }
        });

        cvNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvNine.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.push));
                inputPass("9");
            }
        });

        cvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvDelete.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.push));
                if (getLoginEnabled()) {
                    if (pass.length() != 0) {
                        pass = pass.substring(0, pass.length() - 1);
                        passDisplay = passDisplay.substring(0, passDisplay.length() - 1);
                        txtPass.setText(passDisplay);
                    }
                }
            }
        });

        cvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvLogin.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.push));
                if (getNew() || intent.getBooleanExtra("change_password", false)) {
                    if (!(pass.length() < 4)) {
                        if (getPassword().equals("69") || !getPassword().equals(pass)) {
                            // Set button listeners //
                            Runnable yesListener = new Runnable() {
                                @Override
                                public void run() {
                                    setPassword(pass);
                                    setNew(false);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (!intent.getBooleanExtra("change_password", false)) {
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                onBackPressed();
                                            }
                                        }
                                    }, 500);
                                }
                            };

                            if (intent.getBooleanExtra("change_password", false)) {
                                createDialogYesNo("Are you sure you\nwant to set this as\nyour new password?", yesListener, null);

                            } else {
                                createDialogYesNo("Are you sure you\nwant to set this as\nyour password?", yesListener, null);
                            }

                        } else {
                            createDialog("You have the same\npassword already lol!");
                        }
                    } else {

                        if(pass.length() == 0){
                            createDialog("Password cannot be empty!");
                        }else{
                            createDialog("Password must be at least\n4 characters long!");
                        }

                    }
                } else {
                    if (getLoginEnabled()) {
                        if (pass.length() == 0) {
                            createDialog("Input a password!");
                        } else if (pass.equals(getPassword())) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (intent.getBooleanExtra("confirm_password", false)) {

                                        if (intent.getBooleanExtra("login_change_password", false)) {
                                            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                                            intent.putExtra("change_password", true);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Runnable yesListener = new Runnable() {
                                                @Override
                                                public void run() {
                                                    DiaryDBHelper diaryDBHelper = new DiaryDBHelper(LoginActivity.this);
                                                    diaryDBHelper.clearDiary();
                                                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
                                                    editor.clear().apply();
                                                    Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                    finishAffinity();
                                                }
                                            };

                                            Runnable noListener = new Runnable() {
                                                @Override
                                                public void run() {
                                                    onBackPressed();
                                                    finish();
                                                }
                                            };
                                            createDialogYesNo("This will delete all of\n your data, do you still\nwant to continue?", yesListener, noListener);
                                        }
                                    } else {
                                        Intent intentL = new Intent(LoginActivity.this, MainActivity.class);
                                        if(intent.getBooleanExtra("write", false)){
                                            intentL.putExtra("write", true);
                                        }
                                        startActivity(intentL);
                                        finish();
                                    }
                                }
                            }, 500);
                        } else {
                            vibrator.vibrate(250);
                            pass = "";
                            passDisplay = "";
                            txtPass.setText(passDisplay);

                            if (!(intent.getBooleanExtra("confirm_password", false))) {
                                loginAttempts++;
                                if (getLoginAttemptLock() && loginAttempts == getLoginAttemptCount()) {
                                    loginAttempts = 0;
                                    setLoginEnabled(false);
                                    createDialog("Max attempts reached!\nWait 60 seconds\nthen try again!");

                                    Intent intent = new Intent(LoginActivity.this, LoginAttemptLock.class);
                                    intent.setAction("lock");
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(LoginActivity.this, 1, intent, 0);
                                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                    alarmManager.setExact(AlarmManager.RTC, Calendar.getInstance().getTimeInMillis() + 60000, pendingIntent);
                                    if (getLoginAttemptLockDeleteAll()) {
                                        DiaryDBHelper diaryDBHelper = new DiaryDBHelper(LoginActivity.this);
                                        diaryDBHelper.clearDiary();
                                        setLoginAttemptLockDeleteAll(false);
                                    }
                                }

                            }
                        }
                    }
                }
            }
        });

    }

    private void createDialog(String text) {
        // Set dialog theme depending on the chosen theme of the whole application //
        if (getAppTheme()) {
            dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(LoginActivity.this, R.style.Theme_Diary_Dark)));
        } else {
            dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(LoginActivity.this, R.style.Theme_Diary_Light)));
        }

        // Inflate view //
        View viewDialog = getLayoutInflater().inflate(R.layout.window_dialog, null);

        // Initialize views from inflated view //
        TextView txtText = viewDialog.findViewById(R.id.txtText);

        // Set contents of initialized view/s //
        txtText.setText(text);

        // Set view of the dialog //
        dialogBuilder.setView(viewDialog);

        // Create the dialog //
        dialog = dialogBuilder.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private void createDialogYesNo(String text, Runnable yesListener, Runnable noListener) {
        // Set dialog theme depending on the chosen theme of the whole application //
        if (getAppTheme()) {
            dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(LoginActivity.this, R.style.Theme_Diary_Dark)));
        } else {
            dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(LoginActivity.this, R.style.Theme_Diary_Light)));
        }

        // Inflate view //
        View viewDialog = getLayoutInflater().inflate(R.layout.window_dialog_yes_no, null);

        // Initialize view/s from inflated view //
        TextView txtText = viewDialog.findViewById(R.id.txtText), txtYes = viewDialog.findViewById(R.id.txtSet), txtNo = viewDialog.findViewById(R.id.txtCancel);

        // Set contents of initialized view/s //
        txtText.setText(text);

        // Set view of the dialog //
        dialogBuilder.setView(viewDialog);

        // Set listeners of views //
        txtYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                txtYes.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.push));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        new Handler().post(yesListener);
                    }
                }, 200);
            }
        });

        txtNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                txtNo.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.push));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        new Handler().post(noListener);
                    }
                }, 200);
            }
        });

        // Create the dialog //
        dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    // Initialize views //
    private void initViews() {
        intent = getIntent();
        imgBack = findViewById(R.id.imgBack);
        txtLabel = findViewById(R.id.txtLabel);
        txtPass = findViewById(R.id.txtPass);
        pass_number_size = this.getResources().getDimensionPixelSize(R.dimen.pass_number_size);
        if (getNew() || intent.getBooleanExtra("change_password", false)) {
            txtLabel.setText("Set Password");
            txtPass.setTextSize(pass_number_size);
        }

        if (intent.getBooleanExtra("confirm_password", false) || intent.getBooleanExtra("change_password", false)) {
            imgBack.setVisibility(View.VISIBLE);
        }

        cvZero = findViewById(R.id.cvZero);
        cvOne = findViewById(R.id.cvOne);
        cvTwo = findViewById(R.id.cvTwo);
        cvThree = findViewById(R.id.cvThree);
        cvFour = findViewById(R.id.cvFour);
        cvFive = findViewById(R.id.cvFive);
        cvSix = findViewById(R.id.cvSix);
        cvSeven = findViewById(R.id.cvSeven);
        cvEight = findViewById(R.id.cvEight);
        cvNine = findViewById(R.id.cvNine);
        cvDelete = findViewById(R.id.cvDelete);
        cvLogin = findViewById(R.id.cvLogin);

        if (getAppTheme()) {
            cvZero.setCardElevation(0);
            cvOne.setCardElevation(0);
            cvTwo.setCardElevation(0);
            cvThree.setCardElevation(0);
            cvFour.setCardElevation(0);
            cvFive.setCardElevation(0);
            cvSix.setCardElevation(0);
            cvSeven.setCardElevation(0);
            cvEight.setCardElevation(0);
            cvNine.setCardElevation(0);
            cvDelete.setCardElevation(0);
            cvLogin.setCardElevation(0);
        }

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    //SharedPreferences

    // First time login method/s //
    private void setNew(Boolean flag) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean("new", flag).apply();
    }

    private boolean getNew() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("new", true);
    }

    // Password method/s //
    private void setPassword(String pass) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("pass", pass).apply();
    }

    private String getPassword() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString("pass", "69");
    }

    // Login method/s //
    private void setLoginEnabled(Boolean flag) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean("login_enabled", flag).apply();
    }

    private boolean getLoginEnabled() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("login_enabled", true);
    }

    // Login attempt lock method/s //
    private boolean getLoginAttemptLock() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("login_attempt_lock", true);
    }

    //Login attempt count method/s //
    private int getLoginAttemptCount() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getInt("login_attempt_count", 3);
    }

    // Login attempt lock delete all data method //
    private boolean getLoginAttemptLockDeleteAll() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("login_attempt_lock_delete_all", false);
    }

    private void setLoginAttemptLockDeleteAll(boolean flag) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean("login_attempt_lock_delete_all", flag).apply();
    }

    // Theme method/s //
    private boolean getAppTheme() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("dark_mode", false);
    }
}