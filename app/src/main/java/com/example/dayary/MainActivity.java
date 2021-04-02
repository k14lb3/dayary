package com.example.dayary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvPages;
    private ConstraintLayout clPages, clEmptyWatermark;
    private ImageView imgCreate, imgView, imgDark, imgSettings;
    private TextView txtDateToolbar, txtDate;
    private PageRecyclerViewAdapter adapter;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Set theme //
        setTheme(getAppTheme() ? R.style.Theme_Diary_Dark : R.style.Theme_Diary_Light);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        initViews();
        displayPages();
        setEvents();
        achievements();
    }

    private void displayPages() {

        // Update DatePicker dates based on displayed date //
        // Convert displayed String Date in TextView to Date to change the month to integer then change it back to String. //
        Date date;
        String currentlyPickedDate = "";

        try {
            date = new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).parse(txtDate.getText().toString());
            currentlyPickedDate = new SimpleDateFormat("MM yyyy", Locale.getDefault()).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Split words using space as a division, therefore, getting the Month and Year //
        String[] dateArray = currentlyPickedDate.split(" ");
        currentlyPickedDate = dateArray[0] + "-" + dateArray[1];

        DiaryDBHelper diaryDBHelper = new DiaryDBHelper(this);


        // Convert data from database to ArrayList //
        ArrayList<Page> pages = diaryDBHelper.getPages(currentlyPickedDate);
        Collections.sort(pages);

        adapter = new PageRecyclerViewAdapter(this);
        adapter.setPages(pages);
        rvPages.setAdapter(adapter);

        setLayout();

        // Shows the watermark image and hides the date if there is no created page or the diary is empty //
        if (diaryDBHelper.isEmpty()) {
            clEmptyWatermark.setVisibility(View.VISIBLE);
            txtDate.setVisibility(View.GONE);
            txtDateToolbar.setVisibility(View.GONE);
        }
    }

    private void setEvents() {
        imgCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgCreate.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.push));
                Intent intent = new Intent(MainActivity.this, PageCreateActivity.class);
                startActivity(intent);
            }
        });

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgView.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgView.setEnabled(true);
                    }
                }, 500);

                imgView.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in));
                rvPages.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in));

                if (getAppLayout()) {
                    txtDateToolbar.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out));
                    txtDate.setVisibility(View.VISIBLE);
                    txtDate.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            txtDateToolbar.clearAnimation();
                            txtDateToolbar.setVisibility(View.GONE);
                        }
                    }, 500);
                } else {
                    txtDateToolbar.setVisibility(View.VISIBLE);
                    txtDateToolbar.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in));
                    txtDate.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            txtDate.clearAnimation();
                            txtDate.setVisibility(View.GONE);
                        }
                    }, 500);
                }

                setAppLayout(!getAppLayout());
                displayPages();
            }
        });

        imgDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAppTheme(!getAppTheme());
            }
        });

        imgSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgSettings.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.push));
                BottomSheetDialog bottomSheetDialog;
                // Set dialog theme. //
                if (getAppTheme()) {
                    bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.Theme_Diary_Dark);
                } else {
                    bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.Theme_Diary_Light);
                }

                // Inflate view //
                bottomSheetDialog.setContentView(R.layout.window_settings);

                // Initialize view/s from inflated view //
                ImageView imgDown = bottomSheetDialog.findViewById(R.id.imgDown);
                TextView txtPassword = bottomSheetDialog.findViewById(R.id.txtPassword),
                        txtNotifications = bottomSheetDialog.findViewById(R.id.txtNotifications),
                        txtSetLoginAttemptLock = bottomSheetDialog.findViewById(R.id.txtSetLoginAttemptLock),
                        txtLockEnableStatus = bottomSheetDialog.findViewById(R.id.txtLockEnableStatus),
                        txtDeleteData = bottomSheetDialog.findViewById(R.id.txtDeleteData);
                SwitchMaterial switchNotifications = bottomSheetDialog.findViewById(R.id.switchNotfications);

                // Set contents of initialized view/s //
                imgDown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imgDown.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.push));
                        bottomSheetDialog.cancel();
                    }
                });

                // Set contents of initialized view/s //
                if (getLoginAttemptLock()) {
                    txtLockEnableStatus.setText("ON");
                } else {
                    txtLockEnableStatus.setText("OFF");
                }

                if (getNotification()) {
                    switchNotifications.setChecked(true);
                } else {
                    switchNotifications.setChecked(false);
                }

                // Set listeners of views //
                Runnable notificationListener = new Runnable() {
                    @Override
                    public void run() {
                        if (switchNotifications.isChecked()) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Set dialog theme. //
                                    if (getAppTheme()) {
                                        dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(MainActivity.this, R.style.Theme_Diary_Dark)));
                                    } else {
                                        dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(MainActivity.this, R.style.Theme_Diary_Light)));
                                    }

                                    // Set dialog view //
                                    View viewDialog = getLayoutInflater().inflate(R.layout.window_time_spinner, null);

                                    // Initialize view/s //
                                    TimePicker timePicker = viewDialog.findViewById(R.id.timePicker);
                                    TextView txtCancel = viewDialog.findViewById(R.id.txtCancel), txtSet = viewDialog.findViewById(R.id.txtSet);

                                    // Set contents of view/s //
                                    timePicker.setCurrentHour(19);
                                    timePicker.setCurrentMinute(30);

                                    // Set listeners of view/s //
                                    //Added delays that prevents instant close of alert dialogs that leads to showcase of animations. //
                                    txtCancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            txtCancel.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.push));
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    dialog.cancel();
                                                }
                                            }, 200);
                                        }
                                    });

                                    txtSet.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            txtSet.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.push));
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    dialog.dismiss();
                                                }
                                            }, 200);
                                            new Handler().postDelayed(new Runnable() {
                                                public void run() {
                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                                                    calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                                                    calendar.set(Calendar.SECOND, 0);
                                                    setNotification(true);
                                                    Intent intent = new Intent(MainActivity.this, DailyNotification.class);
                                                    intent.setAction("notification");
                                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
                                                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//                                                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                                                }
                                            }, 200);

                                        }
                                    });

                                    dialogBuilder.setView(viewDialog);
                                    dialog = dialogBuilder.create();

                                    // Set dialog listener/s //
                                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialogInterface) {
                                            switchNotifications.setChecked(false);
                                        }
                                    });

                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                    dialog.show();
                                }
                            }, 150);
                        }else{
                            Intent intent = new Intent(MainActivity.this, DailyNotification.class);
                            intent.setAction("notification");
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
                            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            alarmManager.cancel(pendingIntent);
                        }
                    }
                };

                txtNotifications.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        txtNotifications.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.push));
                        switchNotifications.setChecked(!switchNotifications.isChecked());
                        new Handler().post(notificationListener);
                    }
                });

                switchNotifications.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Handler().post(notificationListener);
                    }
                });

                txtPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        txtPassword.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.push));
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.putExtra("confirm_password", true);
                        intent.putExtra("login_change_password", true);
                        startActivity(intent);
                    }
                });

                txtSetLoginAttemptLock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        txtSetLoginAttemptLock.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.push));
                        // Set dialog theme depending on the chosen theme of the whole application //
                        if (getAppTheme()) {
                            dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(MainActivity.this, R.style.Theme_Diary_Dark)));
                        } else {
                            dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(MainActivity.this, R.style.Theme_Diary_Light)));
                        }

                        // Inflate view //
                        View viewDialog = getLayoutInflater().inflate(R.layout.window_set_login_limit_lock, null);

                        // Initialize view/s from inflated view //
                        ConstraintLayout clLockSettings = viewDialog.findViewById(R.id.clLockSettings);
                        TextView txtLockEnable = viewDialog.findViewById(R.id.txtLockEnable), txtLockDeleteData = viewDialog.findViewById(R.id.txtLockDeleteData);
                        EditText txtLoginAttemptCount = viewDialog.findViewById(R.id.txtLoginAttemptCount);
                        SwitchMaterial switchLockEnable = viewDialog.findViewById(R.id.switchLockEnable), switchDeleteAll = viewDialog.findViewById(R.id.switchDeleteAll);

                        // Set contents of initialized view/s //
                        if (getLoginAttemptLock()) {
                            switchLockEnable.setChecked(true);
                            String loginAttemptCount = Integer.toString(getLoginAttemptCount());
                            txtLoginAttemptCount.setText(loginAttemptCount);
                            switchDeleteAll.setChecked(getLoginAttemptLockDeleteAll());
                            clLockSettings.setVisibility(View.VISIBLE);
                        } else {
                            clLockSettings.setVisibility(View.GONE);
                            switchLockEnable.setChecked(false);
                        }

                        // Set listeners of views //
                        Runnable lockEnableListener = new Runnable() {
                            @Override
                            public void run() {
                                if (switchLockEnable.isChecked()) {
                                    txtLockEnableStatus.setText("ON");
                                    setLoginAttemptCount(3);
                                    String loginAttemptCount = Integer.toString(getLoginAttemptCount());
                                    txtLoginAttemptCount.setText(loginAttemptCount);
                                    setLoginAttemptLockDeleteAll(false);
                                    switchDeleteAll.setChecked(getLoginAttemptLockDeleteAll());
                                    clLockSettings.setVisibility(View.VISIBLE);
                                } else {
                                    clLockSettings.setVisibility(View.GONE);
                                    txtLockEnableStatus.setText("OFF");
                                }
                                setLoginAttemptLock(!getLoginAttemptLock());
                            }
                        };

                        txtLockEnable.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                txtLockEnable.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.push));
                                switchLockEnable.setChecked(!switchLockEnable.isChecked());
                                new Handler().post(lockEnableListener);
                            }
                        });

                        switchLockEnable.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new Handler().post(lockEnableListener);
                            }
                        });

                        txtLoginAttemptCount.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                if (txtLoginAttemptCount.getText().toString().isEmpty() || txtLoginAttemptCount.getText().toString().equals("0")) {
                                    setLoginAttemptCount(1);
                                } else {
                                    setLoginAttemptCount(Integer.parseInt(txtLoginAttemptCount.getText().toString()));
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                            }
                        });

                        Runnable deleteAllListener = new Runnable() {
                            @Override
                            public void run() {
                                if (switchDeleteAll.isChecked()) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Set dialog theme depending on the chosen theme of the whole application //
                                            if (getAppTheme()) {
                                                dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(MainActivity.this, R.style.Theme_Diary_Dark)));
                                            } else {
                                                dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(MainActivity.this, R.style.Theme_Diary_Light)));
                                            }

                                            // Inflate view //
                                            View viewDialog = getLayoutInflater().inflate(R.layout.window_dialog_yes_no, null);

                                            // Initialize view/s from inflated view //
                                            TextView txtText = viewDialog.findViewById(R.id.txtText), txtYes = viewDialog.findViewById(R.id.txtSet), txtNo = viewDialog.findViewById(R.id.txtCancel);

                                            // Set contents of initialized view/s //
                                            txtText.setText("All your data will be\ndeleted if the login attempt\nlock triggered, do you still\n want to continue?");

                                            // Set view of the dialog //
                                            dialogBuilder.setView(viewDialog);

                                            // Create the dialog //
                                            dialog = dialogBuilder.create();

                                            // Set listeners of views //
                                            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                @Override
                                                public void onCancel(DialogInterface dialogInterface) {
                                                    dialog.dismiss();
                                                    setLoginAttemptLockDeleteAll(false);
                                                    switchDeleteAll.setChecked(getLoginAttemptLockDeleteAll());
                                                }
                                            });

                                            txtYes.setOnClickListener(new View.OnClickListener() {
                                                public void onClick(View view) {
                                                    txtYes.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.push));
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            dialog.dismiss();
                                                            setLoginAttemptLockDeleteAll(true);
                                                            switchDeleteAll.setChecked(getLoginAttemptLockDeleteAll());
                                                        }
                                                    }, 200);
                                                }
                                            });

                                            txtNo.setOnClickListener(new View.OnClickListener() {
                                                public void onClick(View view) {
                                                    txtNo.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.push));
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            dialog.dismiss();
                                                            setLoginAttemptLockDeleteAll(false);
                                                            switchDeleteAll.setChecked(getLoginAttemptLockDeleteAll());
                                                        }
                                                    }, 150);
                                                }
                                            });

                                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                            dialog.show();
                                        }
                                    }, 200);
                                } else {
                                    setLoginAttemptLockDeleteAll(false);
                                }
                            }
                        };

                            txtLockDeleteData.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                txtLockDeleteData.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.push));
                                switchDeleteAll.setChecked(!switchDeleteAll.isChecked());
                                new Handler().post(deleteAllListener);
                            }
                        });

                        switchDeleteAll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new Handler().post(deleteAllListener);
                            }
                        });

                        // Set view of the dialog //
                        dialogBuilder.setView(viewDialog);

                        // Create the dialog //
                        dialog = dialogBuilder.create();

                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.show();
                    }
                });

                txtDeleteData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        txtDeleteData.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.push));
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.putExtra("confirm_password", true);
                        startActivity(intent);
                    }
                });
                bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                bottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                bottomSheetDialog.getWindow().setDimAmount((float) 0.5);
                bottomSheetDialog.setCanceledOnTouchOutside(true);
                bottomSheetDialog.setDismissWithAnimation(true);
                bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.anim.zoom_in;
                bottomSheetDialog.show();

            }
        });

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtDate.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.push));
                dateOnClickListener();
            }
        });

        txtDateToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtDateToolbar.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.push));
                dateOnClickListener();
            }
        });
    }

    private void createDialogAchievement(String text, int img, Runnable onCancelListener) {
        // Set dialog theme depending on the chosen theme of the whole application //
        if (getAppTheme()) {
            dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(MainActivity.this, R.style.Theme_Diary_Dark)));
        } else {
            dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(MainActivity.this, R.style.Theme_Diary_Light)));
        }

        // Inflate view //
        View viewDialog = getLayoutInflater().inflate(R.layout.window_dialog_achievement_unlocked, null);

        // Initialize view/s from inflated view //
        TextView txtAchievement = viewDialog.findViewById(R.id.txtAchievement);
        ImageView imgAchievement = viewDialog.findViewById(R.id.imgAchievement);

        // Set contents of initialized view/s //
        txtAchievement.setText(text);
        imgAchievement.setImageResource(img);

        // Set view of the dialog //
        dialogBuilder.setView(viewDialog);

        // Create the dialog //
        dialog = dialogBuilder.create();

        // Set Dialog listener/s //
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        new Handler().post(onCancelListener);
                    }
                }, 200);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private void setLayout() {
        ViewGroup.LayoutParams params = clPages.getLayoutParams();
        int height, viewType, icon;

        if (getAppLayout()) {
            // Vertical View
            icon = R.drawable.ic_view_vertical;
            height = ViewGroup.LayoutParams.MATCH_PARENT;
            rvPages.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
            viewType = 0;
        } else {
            // Horizontal View
            icon = R.drawable.ic_view_horizontal;
            height = ViewGroup.LayoutParams.WRAP_CONTENT;
            rvPages.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
            viewType = 1;
        }

        imgView.setImageResource(icon);
        params.height = height;
        clPages.setLayoutParams(params);
        adapter.toggleItemViewType(viewType);

    }

    private void dateOnClickListener() {
        // Set dialog theme. //
        if (getAppTheme()) {
            dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(MainActivity.this, R.style.Theme_Diary_Dark)));
        } else {
            dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(MainActivity.this, R.style.Theme_Diary_Light)));
        }

        // Set dialog view //
        View viewDialog = getLayoutInflater().inflate(R.layout.window_date_picker_spinner, null);

        // Initialize views //
        DatePicker datePicker = viewDialog.findViewById(R.id.timePicker);
        TextView txtCancel = viewDialog.findViewById(R.id.txtCancel), txtSet = viewDialog.findViewById(R.id.txtSet);

        // Remove day picker //
        datePicker.findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);

        // Update DatePicker dates based on displayed date //
        // Convert displayed String Date in TextView to Date to change the month to integer then change it back to String. //
        Date date;
        String strDate = "";

        try {
            date = new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).parse(txtDate.getText().toString());
            strDate = new SimpleDateFormat("MM yyyy", Locale.getDefault()).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] dateArray = strDate.split(" ");
        int year = Integer.parseInt(dateArray[1]);
        int month = Integer.parseInt(dateArray[0]);

        datePicker.updateDate(year, month - 1, 1);

        //Added delays that prevents instant close of alert dialogs that leads to showcase of animations. //

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtCancel.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.push));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 200);
            }
        });

        txtSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtSet.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.push));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 200);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String monthFull = new SimpleDateFormat("MMMM", Locale.getDefault()).format(new Date(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth()));
                        String dateString = monthFull + " " + datePicker.getYear();
                        if (txtDate.getVisibility() == View.VISIBLE) {
                            txtDate.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in));
                        } else {
                            txtDateToolbar.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in));
                        }
                        txtDateToolbar.setText(dateString);
                        txtDate.setText(dateString);
                        displayPages();
                    }
                }, 200);

            }
        });

        dialogBuilder.setView(viewDialog);
        dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    // Initialize views //
    private void initViews() {
        clEmptyWatermark = findViewById(R.id.clEmptyWatermark);
        clPages = findViewById(R.id.clPages);
        rvPages = findViewById(R.id.rvPages);
        txtDateToolbar = findViewById(R.id.txtDateToolbar);
        txtDate = findViewById(R.id.txtDate);
        if (getAppLayout()) {
            txtDateToolbar.setVisibility(View.VISIBLE);
            txtDate.setVisibility(View.GONE);
        } else {
            txtDateToolbar.setVisibility(View.GONE);
            txtDate.setVisibility(View.VISIBLE);
        }
        imgCreate = findViewById(R.id.imgAdd);
        imgView = findViewById(R.id.imgView);
        imgDark = findViewById(R.id.imgDark);
        imgSettings = findViewById(R.id.imgSettings);
        intent = getIntent();

        if(intent.getBooleanExtra("write", false)){
            Intent intent = new Intent(MainActivity.this, PageCreateActivity.class);
            startActivity(intent);
        }

        String date = "";
        if (intent.getStringExtra("currentlyPickedDate") == null) {
            date = new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(new Date());
        } else {
            try {
                Date dateDate = new SimpleDateFormat("MM yyyy", Locale.getDefault()).parse(intent.getStringExtra("currentlyPickedDate"));
                date = new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(dateDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        txtDate.setText(date);
        txtDateToolbar.setText(date);
    }

    // SharedPreferences

    // Achievement method/s //
    private void achievements() {
        // Ding Dong Brief Man, Pasensya Man, and Jovit Basketbolino
        if (getAchievementDingDongBriefMan() == 1 && getAchievementPasensyaMan() == 1 && getAchievementJovitBasketbolino() == 1) {
            Runnable onCancelListenerB = new Runnable() {
                @Override
                public void run() {
                    createDialogAchievement("You unlocked Jovit Basketbolino!", R.mipmap.img_jovitbasketbolino, null);
                }
            };
            Runnable onCancelListener = new Runnable() {
                @Override
                public void run() {
                    createDialogAchievement("You unlocked Pasensya Man!", R.mipmap.img_pasensyaman, onCancelListenerB);
                }
            };
            createDialogAchievement("You unlocked Ding Dong Brief Man!", R.mipmap.img_dingdongbriefman, onCancelListener);
            setAchievementDingDongBriefMan(2);
            setAchievementPasensyaMan(2);
            setAchievementJovitBasketbolino(2);
        } else // Ding Dong Brief Man and Pasensya Man
            if (getAchievementDingDongBriefMan() == 1 && getAchievementPasensyaMan() == 1) {
            Runnable onCancelListener = new Runnable() {
                @Override
                public void run() {
                    createDialogAchievement("You unlocked Pasensya Man!", R.mipmap.img_pasensyaman, null);
                }
            };
            createDialogAchievement("You unlocked Ding Dong Brief Man!", R.mipmap.img_dingdongbriefman, onCancelListener);
            setAchievementDingDongBriefMan(2);
            setAchievementPasensyaMan(2);
        } else // Ding Dong Brief Man and Jovit Basketbolino
            if (getAchievementDingDongBriefMan() == 1 && getAchievementJovitBasketbolino() == 1) {
            Runnable onCancelListener = new Runnable() {
                @Override
                public void run() {
                    createDialogAchievement("You unlocked Jovit Basketbolino!", R.mipmap.img_jovitbasketbolino, null);
                }
            };
            createDialogAchievement("You unlocked Ding Dong Brief Man!", R.mipmap.img_dingdongbriefman, onCancelListener);
            setAchievementDingDongBriefMan(2);
            setAchievementJovitBasketbolino(2);
        } else // Pasensya Man and Jovit Basketbolino
            if (getAchievementPasensyaMan() == 1 && getAchievementJovitBasketbolino() == 1) {
            Runnable onCancelListener = new Runnable() {
                @Override
                public void run() {
                    createDialogAchievement("You unlocked Jovit Basketbolino!", R.mipmap.img_jovitbasketbolino, null);
                }
            };
            createDialogAchievement("You unlocked Pasensya Man!", R.mipmap.img_pasensyaman, onCancelListener);
            setAchievementPasensyaMan(2);
            setAchievementJovitBasketbolino(2);
        } else if (getAchievementDingDongBriefMan() == 1) {
            createDialogAchievement("You unlocked Ding Dong Brief Man!", R.mipmap.img_dingdongbriefman, null);
            setAchievementDingDongBriefMan(2);
        } else if (getAchievementPasensyaMan() == 1) {
            createDialogAchievement("You unlocked Pasensya Man!", R.mipmap.img_pasensyaman, null);
            setAchievementPasensyaMan(2);
        } else if (getAchievementJovitBasketbolino() == 1) {
            createDialogAchievement("You unlocked Jovit Basketbolino!", R.mipmap.img_jovitbasketbolino, null);
            setAchievementJovitBasketbolino(2);
        }
    }

    private void setAchievementDingDongBriefMan(int count) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putInt("dingdongbriefman", count).apply();
    }

    private int getAchievementDingDongBriefMan(){
        return PreferenceManager.getDefaultSharedPreferences(this).getInt("dingdongbriefman", 0);
    }

    private void setAchievementPasensyaMan(int count) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putInt("pasensyaman", count).apply();
    }

    private int getAchievementPasensyaMan(){
        return PreferenceManager.getDefaultSharedPreferences(this).getInt("pasensyaman", 0);
    }

    private void setAchievementJovitBasketbolino(int count) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putInt("jovitbasketbolino", count).apply();
    }

    private int getAchievementJovitBasketbolino(){
        return PreferenceManager.getDefaultSharedPreferences(this).getInt("jovitbasketbolino", 0);
    }

    // Login attempt method/s //
    private void setLoginAttemptLock(boolean flag) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean("login_attempt_lock", flag).apply();
    }

    private boolean getLoginAttemptLock() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("login_attempt_lock", true);
    }

    // Login attempt count method/s //
    private int getLoginAttemptCount() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getInt("login_attempt_count", 3);
    }

    private void setLoginAttemptCount(int count) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putInt("login_attempt_count", count).apply();
    }

    // Login attempt lock delete all data method //
    private void setLoginAttemptLockDeleteAll(boolean flag) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean("login_attempt_lock_delete_all", flag).apply();
    }

    private boolean getLoginAttemptLockDeleteAll() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("login_attempt_lock_delete_all", false);
    }

    // Notification methods //
    private void setNotification(boolean flag) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean("notification", flag).apply();
    }

    private boolean getNotification() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notification", true);
    }

    // Layout method/s //
    private void setAppLayout(boolean flag) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean("vertical", flag).apply();
    }

    private boolean getAppLayout() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("vertical", false);
    }

    // Theme methods //
    private void setAppTheme(boolean flag) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean("dark_mode", flag).apply();
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private boolean getAppTheme() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_mode", false);
    }

}
