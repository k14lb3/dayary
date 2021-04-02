package com.example.dayary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

public class PageActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView txtDate, txtEditLabel;
    private EditText etTitle, etNote;
    private ImageView imgMenu, imgCover,
            imgFavorite, imgHappy, imgSad, imgBad, imgBack,
            imgEdit, imgCancel,
            imgPopUpFavorite, imgPopUpHappy, imgPopUpSad, imgPopUpBad,
            imgLandscapeThumbnail, imgPortraitThumbnail,
            imgDefault;
    private CardView cvDefault, cvNote;
    private Intent intent;
    private String pageDate, pageDateDayOfWeek, pageTitle, pageNote;
    private Bitmap pageThumbnail, pageThumbnailTemp, pageCover;
    private ConstraintLayout clMoods;
    int pageFavoriteVisibility, pageHappyVisibility, pageSadVisibility, pageBadVisibility;
    private int previousPageFavoriteVisibility, previousPageHappyVisibility, previousPageSadVisibility, previousPageBadVisibility;
    private final int SET_THUMBNAIL = 0, SET_COVER = 1;
    private int currentRequestCode;
    private boolean edit, edited;
    private int order = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(getAppTheme() ? R.style.Theme_Diary_Dark : R.style.Theme_Diary_Light);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        initViews();
        getIntentValues();
        setViewsContent();
        setEvents();
    }

    private void setEvents() {

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgMenu.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.push));
                BottomSheetDialog bottomSheetDialog;
                // Set dialog theme. //
                if (getAppTheme()) {
                    bottomSheetDialog = new BottomSheetDialog(PageActivity.this, R.style.Theme_Diary_Dark);
                } else {
                    bottomSheetDialog = new BottomSheetDialog(PageActivity.this, R.style.Theme_Diary_Light);
                }

                // Inflate view //
                bottomSheetDialog.setContentView(R.layout.window_page_settings);

                // Initialize view/s from inflated view //
                ImageView imgDown = bottomSheetDialog.findViewById(R.id.imgDown);
                TextView txtThumbnail = bottomSheetDialog.findViewById(R.id.txtCancel),
                        txtEdit = bottomSheetDialog.findViewById(R.id.txtEdit),
                        txtDelete = bottomSheetDialog.findViewById(R.id.txtDelete);

                // Set contents of initialized view/s //
                imgDown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imgDown.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.push));
                        bottomSheetDialog.cancel();
                    }
                });

                txtThumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        txtThumbnail.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.push));
                        // Set dialog theme. //
                        if (getAppTheme()) {
                            dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(PageActivity.this, R.style.Theme_Diary_Dark)));
                        } else {
                            dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(PageActivity.this, R.style.Theme_Diary_Light)));
                        }

                        // Set dialog view. //
                        View viewDialog = getLayoutInflater().inflate(R.layout.window_set_thumbnail, null);

                        // Initialize views //
                        CardView cvPortrait = viewDialog.findViewById(R.id.cvPortrait),
                                cvPortraitDefault = viewDialog.findViewById(R.id.cvPortraitDefault),
                                cvLandscape = viewDialog.findViewById(R.id.cvLandscape),
                                cvLandscapeDefault = viewDialog.findViewById(R.id.cvLandscapeDefault);
                        ImageView imgView = viewDialog.findViewById(R.id.imgView),
                                imgPortraitDefault = viewDialog.findViewById(R.id.imgPortraitDefault),
                                imgLandscapeDefault = viewDialog.findViewById(R.id.imgLandscapeDefault);
                        imgLandscapeThumbnail = viewDialog.findViewById(R.id.imgLandscapeThumbnail);
                        imgPortraitThumbnail = viewDialog.findViewById(R.id.imgPortraitThumbnail);
                        TextView txtSet = viewDialog.findViewById(R.id.txtSet),
                                txtCancel = viewDialog.findViewById(R.id.txtCancel),
                                txtDayNum = viewDialog.findViewById(R.id.txtDayNum),
                                txtDay = viewDialog.findViewById(R.id.txtDay),
                                txtDayNumLandscape = viewDialog.findViewById(R.id.txtDayNumLandscape),
                                txtDayNumPortrait = viewDialog.findViewById(R.id.txtDayNumPortrait);

                        // Set contents of initialized view/s //
                        imgLandscapeThumbnail.setImageBitmap(pageThumbnail);
                        imgPortraitThumbnail.setImageBitmap(pageThumbnail);
                        txtDay.setText(txtDate.getText().toString().substring(0, 3));
                        txtDayNum.setText(txtDate.getText().toString().substring(6, 8));
                        txtDayNumPortrait.setText(txtDate.getText().toString().substring(0, 3));
                        txtDayNumLandscape.setText(txtDate.getText().toString().substring(6, 8));

                        if (getAppLayout()) {
                            imgView.setImageResource(R.drawable.ic_view_horizontal);
                            cvLandscape.setVisibility(View.VISIBLE);
                            cvPortrait.setVisibility(View.GONE);
                        } else {
                            imgView.setImageResource(R.drawable.ic_view_vertical);
                            cvLandscape.setVisibility(View.GONE);
                            cvPortrait.setVisibility(View.VISIBLE);
                        }

                        // Set listeners of views //
                        imgView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                imgView.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.push));

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (cvLandscape.getVisibility() == View.VISIBLE) {
                                            imgView.setImageResource(R.drawable.ic_view_vertical);
                                            cvLandscape.clearAnimation();
                                            cvLandscape.setVisibility(View.GONE);
                                            cvPortrait.setVisibility(View.VISIBLE);
                                            cvPortrait.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.fade_in));
                                        } else {
                                            imgView.setImageResource(R.drawable.ic_view_horizontal);
                                            cvLandscape.setVisibility(View.VISIBLE);
                                            cvLandscape.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.fade_in));
                                            cvPortrait.clearAnimation();
                                            cvPortrait.setVisibility(View.GONE);
                                        }
                                    }
                                }, 200);
                            }
                        });

                        Runnable defaultListener = new Runnable() {
                            @Override
                            public void run() {
                                imgLandscapeThumbnail.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.fade_in));
                                imgPortraitThumbnail.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.fade_in));
                                pageThumbnailTemp = BitmapFactory.decodeResource(getResources(), R.mipmap.img_def);
                                imgLandscapeThumbnail.setImageBitmap(pageThumbnailTemp);
                                imgPortraitThumbnail.setImageBitmap(pageThumbnailTemp);
                            }
                        };

                        imgPortraitDefault.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                imgPortraitDefault.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.push));
                                cvPortraitDefault.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.push));
                                new Handler().post(defaultListener);
                            }
                        });

                        imgLandscapeDefault.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                imgLandscapeDefault.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.push));
                                cvLandscapeDefault.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.push));
                                new Handler().post(defaultListener);
                            }
                        });

                        txtSet.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                txtSet.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.push));
                                DiaryDBHelper diaryDBHelper = new DiaryDBHelper(PageActivity.this);
                                pageThumbnail = pageThumbnailTemp;
                                Page page = new Page(txtDate.getText().toString().substring(6), etTitle.getText().toString(), etNote.getText().toString(), pageThumbnail, pageCover, pageFavoriteVisibility, pageHappyVisibility, pageSadVisibility, pageBadVisibility);
                                diaryDBHelper.updatePage(txtDate.getText().toString().substring(6), page);
                                edited = true;
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                    }
                                }, 200);
                            }
                        });

                        txtCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                txtCancel.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.push));
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                    }
                                }, 200);
                            }
                        });

                        Runnable imgListener = new Runnable() {
                            @Override
                            public void run() {
                                startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*"), SET_THUMBNAIL);
                            }
                        };

                        imgLandscapeThumbnail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                imgLandscapeThumbnail.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.push));
                                new Handler().post(imgListener);
                            }
                        });

                        imgPortraitThumbnail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                imgPortraitThumbnail.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.push));
                                new Handler().post(imgListener);
                            }
                        });

                        dialogBuilder.setView(viewDialog);
                        dialog = dialogBuilder.create();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.show();
                    }
                });

                txtEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        txtEdit.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.push));
                        edit = true;
                        imgMenu.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.fade_out));
                        imgEdit.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.fade_in));
                        imgCancel.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.fade_in));
                        cvDefault.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.fade_in));

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                imgMenu.clearAnimation();
                                imgEdit.clearAnimation();
                                cvDefault.clearAnimation();
                                imgDefault.clearAnimation();
                                imgMenu.setVisibility(View.GONE);
                                imgEdit.setVisibility(View.VISIBLE);
                                imgCancel.setVisibility(View.VISIBLE);
                                cvDefault.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        txtEditLabel.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.fade_in));
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                txtEditLabel.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.fade_out));
                                            }
                                        }, 1000);
                                    }
                                }, 500);
                            }
                        }, 500);

                        editSetEvents();
                        bottomSheetDialog.cancel();
                    }
                });

                txtDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        txtDelete.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.push));
                        Runnable yesListener = new Runnable() {
                            @Override
                            public void run() {
                                DiaryDBHelper diaryDBHelper = new DiaryDBHelper(PageActivity.this);
                                diaryDBHelper.deletePage(pageDate);
                                Intent intent = new Intent(PageActivity.this, MainActivity.class);
                                intent.putExtra("currentlyPickedDate", pageDate.substring(0, 2) + " " + pageDate.substring(6));
                                startActivity(intent);
                                finishAffinity();
                            }
                        };

                        createDialogYesNo("Are you sure you want\nto delete this page?", yesListener, null);

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

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgBack.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.push));
                onBackPressed();
            }
        });

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgEdit.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.push));

                Runnable noListener = new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                };

                Runnable yesListener = new Runnable() {
                    @Override
                    public void run() {
                        DiaryDBHelper diaryDBHelper = new DiaryDBHelper(PageActivity.this);

                        if (etTitle.length() == 0 && etNote.length() == 0) {
                            createDialog("Title and Note\ncannot be empty!");
                        } else if (etTitle.length() == 0) {
                            createDialog("Title cannot be empty!");
                        } else if (etNote.length() == 0) {
                            createDialog("Note cannot be empty!");
                        } else if (diaryDBHelper.checkDuplicate(txtDate.getText().toString().substring(6)) && !txtDate.getText().toString().substring(6).equals(pageDate)) {
                            createDialog("There is already a\npage with that date!");
                        } else {

                            imgMenu.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.fade_in));
                            imgEdit.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.fade_out));
                            imgCancel.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.fade_out));
                            cvDefault.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.fade_out));

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    imgMenu.clearAnimation();
                                    imgEdit.clearAnimation();
                                    imgCancel.clearAnimation();
                                    cvDefault.clearAnimation();
                                    imgMenu.setVisibility(View.VISIBLE);
                                    imgEdit.setVisibility(View.GONE);
                                    imgCancel.setVisibility(View.GONE);
                                    cvDefault.setVisibility(View.GONE);
                                }
                            }, 500);

                            etTitle.setEnabled(false);
                            etNote.setEnabled(false);
                            txtDate.setOnClickListener(null);
                            clMoods.setOnClickListener(null);
                            imgCover.setOnClickListener(null);
                            edit = false;
                            edited = true;

                            if (order == 8 && (etTitle.getText().toString().equals("DINGDONGBRIEFMAN") || etTitle.getText().toString().equals("PASENSYAMAN") || etTitle.getText().toString().equals("JOVITBASKETBOLINO"))) {
                                Bitmap img = null;
                                if (etTitle.getText().toString().equals("DINGDONGBRIEFMAN")) {
                                    img = BitmapFactory.decodeResource(getResources(), R.mipmap.img_dingdongbriefman);
                                    if (getAchievementDingDongBriefMan() == 0) {
                                        setAchievementDingDongBriefMan(1);
                                    }
                                } else if (etTitle.getText().toString().equals("PASENSYAMAN")) {
                                    img = BitmapFactory.decodeResource(getResources(), R.mipmap.img_pasensyaman);
                                    if (getAchievementPasensyaMan() == 0) {
                                        setAchievementPasensyaMan(1);
                                    }
                                } else if (etTitle.getText().toString().equals("JOVITBASKETBOLINO")) {
                                    img = BitmapFactory.decodeResource(getResources(), R.mipmap.img_jovitbasketbolino);
                                    if (getAchievementJovitBasketbolino() == 0) {
                                        setAchievementJovitBasketbolino(1);
                                    }
                                }
                                pageThumbnail = img;
                                pageCover = img;
                                imgCover.setImageBitmap(img);
                                etTitle.setText(pageTitle);
                                order = 0;
                            }

                            Page page = new Page(txtDate.getText().toString().substring(6), etTitle.getText().toString(), etNote.getText().toString(), pageThumbnail, pageCover, pageFavoriteVisibility, pageHappyVisibility, pageSadVisibility, pageBadVisibility);
                            diaryDBHelper.updatePage(pageDate, page);
                        }

                    }
                };

                createDialogYesNo("Do you want to\nsave the changes?", yesListener, noListener);
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgCancel.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.push));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgMenu.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.fade_in));
                        imgEdit.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.fade_out));
                        imgCancel.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.fade_out));
                        cvDefault.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.fade_out));

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                imgMenu.clearAnimation();
                                imgEdit.clearAnimation();
                                imgCancel.clearAnimation();
                                imgMenu.setVisibility(View.VISIBLE);
                                imgEdit.setVisibility(View.GONE);
                                imgCancel.setVisibility(View.GONE);
                                cvDefault.setVisibility(View.GONE);
                            }
                        }, 500);

                        imgMenu.setVisibility(View.VISIBLE);
                        imgEdit.setVisibility(View.GONE);
                        imgCancel.setVisibility(View.GONE);
                        etTitle.setEnabled(false);
                        etNote.setEnabled(false);
                        txtDate.setOnClickListener(null);
                        clMoods.setOnClickListener(null);
                        imgCover.setOnClickListener(null);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                edit = false;
                            }
                        }, 600);
                        getIntentValues();
                        setViewsContent();

                    }
                }, 200);
            }
        });

    }

    private void editSetEvents() {
        etTitle.setEnabled(true);
        etNote.setEnabled(true);
        imgCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgCover.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.push));
                startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*"), SET_COVER);
            }
        });

        imgDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgDefault.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.push));
                cvDefault.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.push));
                pageCover = BitmapFactory.decodeResource(getResources(), R.mipmap.img_def);
                imgCover.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.fade_in));
                imgCover.setImageBitmap(pageCover);
            }
        });

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set dialog theme. //
                if (getAppTheme()) {
                    dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(PageActivity.this, R.style.Theme_Diary_Dark)));
                } else {
                    dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(PageActivity.this, R.style.Theme_Diary_Light)));
                }

                // Set dialog view. //
                View viewDialog = getLayoutInflater().inflate(R.layout.window_date_picker_spinner, null);

                // Initialize views //
                DatePicker datePicker = viewDialog.findViewById(R.id.timePicker);
                TextView txtCancel = viewDialog.findViewById(R.id.txtCancel), txtSet = viewDialog.findViewById(R.id.txtSet);

                // Update DatePicker dates based on displayed date //
                String currentlyPickedDate = txtDate.getText().toString();
                datePicker.updateDate(Integer.parseInt(currentlyPickedDate.substring(12)), Integer.parseInt(currentlyPickedDate.substring(6, 8)) - 1, Integer.parseInt(currentlyPickedDate.substring(9, 11)));

                // Add delays that prevents instant close of alert dialogs that  to show animations. //
                txtCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        txtCancel.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.push));
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
                        txtSet.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.push));
                        pageDate = txtDate.getText().toString().substring(6);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 200);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                String dateString = new SimpleDateFormat("EEE / MM-dd-yyyy", Locale.getDefault()).format(new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth()).getTime());
                                txtDate.setText(dateString);
                            }
                        }, 200);
                    }
                });

                dialogBuilder.setView(viewDialog);
                dialog = dialogBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();

            }
        });

        clMoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getAppTheme()) {
                    dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(PageActivity.this, R.style.Theme_Diary_Dark)));
                } else {
                    dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(PageActivity.this, R.style.Theme_Diary_Light)));
                }

                View windowMoodChoose = getLayoutInflater().inflate(R.layout.window_mood_choose, null);

                // Initialize views from inflated layout's view //
                imgPopUpFavorite = windowMoodChoose.findViewById(R.id.imgFavorite);
                imgPopUpHappy = windowMoodChoose.findViewById(R.id.imgHappy);
                imgPopUpSad = windowMoodChoose.findViewById(R.id.imgSad);
                imgPopUpBad = windowMoodChoose.findViewById(R.id.imgBad);
                dialogBuilder.setView(windowMoodChoose);
                dialog = dialogBuilder.create();

                // Set views onClickListeners //

                imgPopUpFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (pageFavoriteVisibility == 0) {
                            imgPopUpFavorite.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.mood_deselect));
                            pageFavoriteVisibility = 8;
                        } else {
                            imgPopUpFavorite.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.mood_select));
                            pageFavoriteVisibility = 0;
                        }

                        if (order == 0) {
                            order = 1;
                        } else if (order == 4) {
                            order = 5;
                        } else {
                            order = 0;
                        }

                    }
                });

                imgPopUpHappy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (pageHappyVisibility == 0) {
                            imgPopUpHappy.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.mood_deselect));
                            pageHappyVisibility = 8;
                        } else {
                            imgPopUpHappy.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.mood_select));
                            pageHappyVisibility = 0;
                        }

                        if (order == 3) {
                            order = 4;
                        } else if (order == 5) {
                            order = 6;
                        } else {
                            order = 0;
                        }

                    }
                });

                imgPopUpSad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (pageSadVisibility == 0) {
                            imgPopUpSad.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.mood_deselect));
                            pageSadVisibility = 8;
                        } else {
                            imgPopUpSad.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.mood_select));
                            pageSadVisibility = 0;
                        }

                        if (order == 1) {
                            order = 2;
                        } else if (order == 6) {
                            order = 7;
                        } else {
                            order = 0;
                        }

                    }
                });

                imgPopUpBad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (pageBadVisibility == 0) {
                            imgPopUpBad.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.mood_deselect));
                            pageBadVisibility = 8;
                        } else {
                            imgPopUpBad.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.mood_select));
                            pageBadVisibility = 0;
                        }

                        if (order == 2) {
                            order = 3;
                        } else if (order == 7) {
                            order = 8;
                        } else {
                            order = 0;
                        }

                    }
                });


                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                previousPageFavoriteVisibility = pageFavoriteVisibility;
                previousPageHappyVisibility = pageHappyVisibility;
                previousPageSadVisibility = pageSadVisibility;
                previousPageBadVisibility = pageBadVisibility;

                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        if (pageFavoriteVisibility == previousPageFavoriteVisibility) {
                        } else if (pageFavoriteVisibility == 0) {
                            imgFavorite.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.mood_select));
                        } else {
                            imgFavorite.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.mood_deselect));
                        }

                        if (pageHappyVisibility == previousPageHappyVisibility) {
                        } else if (pageHappyVisibility == 0) {
                            imgHappy.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.mood_select));
                        } else {
                            imgHappy.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.mood_deselect));
                        }

                        if (pageSadVisibility == previousPageSadVisibility) {
                        } else if (pageSadVisibility == 0) {
                            imgSad.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.mood_select));
                        } else {
                            imgSad.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.mood_deselect));
                        }

                        if (pageBadVisibility == previousPageBadVisibility) {
                        } else if (pageBadVisibility == 0) {
                            imgBad.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.mood_select));
                        } else {
                            imgBad.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.mood_deselect));
                        }
                    }
                });

                dialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pageFavoriteVisibility == 8) {
                            imgPopUpFavorite.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.mood_deselect));
                        }
                        if (pageHappyVisibility == 8) {
                            imgPopUpHappy.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.mood_deselect));
                        }
                        if (pageSadVisibility == 8) {
                            imgPopUpSad.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.mood_deselect));
                        }
                        if (pageBadVisibility == 8) {
                            imgPopUpBad.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.mood_deselect));
                        }
                    }
                }, 250);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == SET_THUMBNAIL || requestCode == SET_COVER) && resultCode == RESULT_OK) {
            Uri imgUri = data.getData();
            if (imgUri != null) {
                currentRequestCode = requestCode;
                imgCrop(imgUri, requestCode);
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri imageUriResultCrop = UCrop.getOutput(data);
            if (imageUriResultCrop != null) {
                try {
                    if (currentRequestCode == SET_THUMBNAIL) {
                        pageThumbnailTemp = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUriResultCrop);
                        imgLandscapeThumbnail.setImageBitmap(pageThumbnailTemp);
                        imgPortraitThumbnail.setImageBitmap(pageThumbnailTemp);
                    } else if (currentRequestCode == SET_COVER) {
                        pageCover = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUriResultCrop);
                        imgCover.setImageBitmap(pageCover);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void imgCrop(@NonNull Uri uri, int requestCode) {
        UCrop uCrop = null;
        UCrop.Options options = new UCrop.Options();
        options.setHideBottomControls(false);

        if (requestCode == SET_THUMBNAIL) {
            uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), "img.jpg")))
                    .withAspectRatio(6, 5);
        } else if (requestCode == SET_COVER) {
            uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), "img.jpg")));
            options.setFreeStyleCropEnabled(true);
        }
        uCrop.withOptions(options);
        uCrop.start(PageActivity.this);
    }


    private void createDialogYesNo(String text, Runnable yesListener, Runnable noListener) {
        // Set dialog theme depending on the chosen theme of the whole application //
        if (getAppTheme()) {
            dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(PageActivity.this, R.style.Theme_Diary_Dark)));
        } else {
            dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(PageActivity.this, R.style.Theme_Diary_Light)));
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
                txtYes.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.push));
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
                txtNo.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.push));
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

    private void createDialog(String message) {
        if (getAppTheme()) {
            dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(PageActivity.this, R.style.Theme_Diary_Dark)));
        } else {
            dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(PageActivity.this, R.style.Theme_Diary_Light)));
        }
        View windowDialog = getLayoutInflater().inflate(R.layout.window_dialog, null);
        TextView txtMessage = windowDialog.findViewById(R.id.txtText);
        txtMessage.setText(message);
        dialogBuilder.setView(windowDialog);
        dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (edit) {
            Runnable noListener = new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            };

            Runnable yesListener = new Runnable() {
                @Override
                public void run() {
                    edit = false;
                    onBackPressed();
                }
            };
            createDialogYesNo("Are you sure you want\nto close the page?", yesListener, noListener);
        } else if (edited) {
            Intent intent = new Intent(PageActivity.this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    private void setViewsContent() {
        imgCover.setImageBitmap(pageCover);
        etTitle.setText(pageTitle);
        String date = pageDateDayOfWeek + " / " + pageDate;
        txtDate.setText(date);
        etNote.setText(pageNote);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!edit) {
                    if (pageFavoriteVisibility == 8) {
                        imgFavorite.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.mood_deselect));
                    }
                    if (pageHappyVisibility == 8) {
                        imgHappy.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.mood_deselect));
                    }
                    if (pageSadVisibility == 8) {
                        imgSad.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.mood_deselect));
                    }
                    if (pageBadVisibility == 8) {
                        imgBad.startAnimation(AnimationUtils.loadAnimation(PageActivity.this, R.anim.mood_deselect));
                    }
                }
            }
        }, 500);
    }

    // Get intent values //
    private void getIntentValues() {
        intent = getIntent();
        byte[] bytes;
        bytes = intent.getByteArrayExtra("thumbnail");
        pageThumbnail = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        pageThumbnailTemp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        bytes = intent.getByteArrayExtra("cover");
        pageCover = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        pageDate = intent.getStringExtra("date");
        pageDateDayOfWeek = intent.getStringExtra("dateDayOfTheWeek");
        pageTitle = intent.getStringExtra("title");
        pageNote = intent.getStringExtra("note");
        pageFavoriteVisibility = intent.getIntExtra("favorite", 0);
        pageHappyVisibility = intent.getIntExtra("happy", 0);
        pageSadVisibility = intent.getIntExtra("sad", 0);
        pageBadVisibility = intent.getIntExtra("bad", 0);
    }

    // Initialize views //
    private void initViews() {
        imgMenu = findViewById(R.id.imgMenu);
        imgCover = findViewById(R.id.imgCover);
        cvDefault = findViewById(R.id.cvDefault);
        imgDefault = findViewById(R.id.imgDefault);
        txtDate = findViewById(R.id.txtDate);
        txtEditLabel = findViewById(R.id.txtEditLabel);
        etTitle = findViewById(R.id.etTitle);
        cvNote = findViewById(R.id.cvNote);
        etNote = findViewById(R.id.etNote);
        clMoods = findViewById(R.id.clMoods);
        imgFavorite = findViewById(R.id.imgFavorite);
        imgHappy = findViewById(R.id.imgHappy);
        imgSad = findViewById(R.id.imgSad);
        imgBad = findViewById(R.id.imgBad);
        imgBack = findViewById(R.id.imgBack);
        imgEdit = findViewById(R.id.imgEdit);
        imgCancel = findViewById(R.id.imgCancel);
        edit = false;

        if (getAppTheme()) {
            cvNote.setCardElevation(0);
        }

    }

    // SharedPreferences //

    // Achievement method/s //
    private void setAchievementDingDongBriefMan(int count) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putInt("dingdongbriefman", count).apply();
    }

    private int getAchievementDingDongBriefMan() {
        return PreferenceManager.getDefaultSharedPreferences(this).getInt("dingdongbriefman", 0);
    }

    private void setAchievementPasensyaMan(int count) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putInt("pasensyaman", count).apply();
    }

    private int getAchievementPasensyaMan() {
        return PreferenceManager.getDefaultSharedPreferences(this).getInt("pasensyaman", 0);
    }

    private void setAchievementJovitBasketbolino(int count) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putInt("jovitbasketbolino", count).apply();
    }

    private int getAchievementJovitBasketbolino() {
        return PreferenceManager.getDefaultSharedPreferences(this).getInt("jovitbasketbolino", 0);
    }


    // Layout method/s //
    private boolean getAppLayout() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("vertical", false);
    }

    // Theme methods //
    private boolean getAppTheme() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("dark_mode", false);
    }

}