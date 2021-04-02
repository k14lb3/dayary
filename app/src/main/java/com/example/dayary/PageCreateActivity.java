package com.example.dayary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class PageCreateActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private ImageView imgBack, imgCreate, imgThumbnail, imgCover,
            imgFavorite, imgHappy, imgSad, imgBad,
            imgPopUpFavorite, imgPopUpHappy, imgPopUpSad, imgPopUpBad,
            imgLandscapeThumbnail, imgPortraitThumbnail;
    private TextView txtDate;
    private EditText etTitle, etNote;
    private CardView cvNote, cvDefault;
    private ConstraintLayout clMoods;
    private final int SET_THUMBNAIL = 0, SET_COVER = 1;
    private int currentRequestCode;
    private Bitmap bmThumbnail, bmThumbnailTemp, bmCover;
    private int pageFavoriteVisibility = 8, pageHappyVisibility = 8, pageSadVisibility = 8, pageBadVisibility = 8;
    private int previousPageFavoriteVisibility, previousPageHappyVisibility, previousPageSadVisibility, previousPageBadVisibility;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Assign Theme: Dark Theme or Light Theme //
        setTheme(getAppTheme() ? R.style.Theme_Diary_Dark : R.style.Theme_Diary_Light);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_create);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        initViews();
        setEvents();

        moodsAnimations();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void setEvents() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgBack.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.push));
                onBackPressed();
            }
        });

        imgCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgCreate.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.push));
                DiaryDBHelper diaryDBHelper = new DiaryDBHelper(PageCreateActivity.this);

                if (etTitle.length() == 0 && etNote.length() == 0) {
                    createDialog("Title and Note\ncannot be empty!");
                } else if (etTitle.length() == 0) {
                    createDialog("Title cannot be empty!");
                } else if (etNote.length() == 0) {
                    createDialog("Note cannot be empty!");
                } else if (diaryDBHelper.checkDuplicate(txtDate.getText().toString().substring(6))) {
                    createDialog("There is already a\npage with that date!");
                } else {
                    String date = txtDate.getText().toString().substring(6);
                    Page page = new Page(date, etTitle.getText().toString(), etNote.getText().toString(), bmThumbnail, bmCover, pageFavoriteVisibility, pageHappyVisibility, pageSadVisibility, pageBadVisibility);
                    diaryDBHelper.addPage(page);
                    Intent intent = new Intent(PageCreateActivity.this, MainActivity.class);
                    intent.putExtra("currentlyPickedDate", date.substring(0, 2) + " " + date.substring(6));
                    startActivity(intent);
                    finishAffinity();
                }

            }
        });

        imgThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgThumbnail.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.push));

                // Set dialog theme. //
                if (getAppTheme()) {
                    dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(PageCreateActivity.this, R.style.Theme_Diary_Dark)));
                } else {
                    dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(PageCreateActivity.this, R.style.Theme_Diary_Light)));
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
                imgLandscapeThumbnail.setImageBitmap(bmThumbnail);
                imgPortraitThumbnail.setImageBitmap(bmThumbnail);
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
                        imgView.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.push));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (cvLandscape.getVisibility() == View.VISIBLE) {
                                    imgView.setImageResource(R.drawable.ic_view_vertical);
                                    cvLandscape.clearAnimation();
                                    cvLandscape.setVisibility(View.GONE);
                                    cvPortrait.setVisibility(View.VISIBLE);
                                    cvPortrait.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.fade_in));
                                } else {
                                    imgView.setImageResource(R.drawable.ic_view_horizontal);
                                    cvLandscape.setVisibility(View.VISIBLE);
                                    cvLandscape.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.fade_in));
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
                        imgLandscapeThumbnail.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.fade_in));
                        imgPortraitThumbnail.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.fade_in));
                        bmThumbnailTemp = BitmapFactory.decodeResource(getResources(), R.mipmap.img_def);
                        imgLandscapeThumbnail.setImageBitmap(bmThumbnailTemp);
                        imgPortraitThumbnail.setImageBitmap(bmThumbnailTemp);
                    }
                };

                imgPortraitDefault.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imgPortraitDefault.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.push));
                        cvPortraitDefault.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.push));
                        new Handler().post(defaultListener);
                    }
                });

                imgLandscapeDefault.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imgLandscapeDefault.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.push));
                        cvLandscapeDefault.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.push));
                        new Handler().post(defaultListener);
                    }
                });

                txtSet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        txtSet.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.push));
                        if (bmThumbnailTemp != null) {
                            bmThumbnail = bmThumbnailTemp;
                        } else {
                            bmThumbnail = BitmapFactory.decodeResource(getResources(), R.mipmap.img_def);
                        }
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
                        txtCancel.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.push));
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
                        imgLandscapeThumbnail.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.push));
                        new Handler().post(imgListener);
                    }
                });

                imgPortraitThumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imgPortraitThumbnail.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.push));
                        new Handler().post(imgListener);
                    }
                });

                dialogBuilder.setView(viewDialog);
                dialog = dialogBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();
            }
        });

        imgCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgCover.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.push));
                startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*"), SET_COVER);
            }
        });

        cvDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvDefault.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.push));
                imgCover.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.push));
                bmCover = BitmapFactory.decodeResource(getResources(), R.mipmap.img_def);
                imgCover.setImageBitmap(bmCover);
            }
        });

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set dialog theme. //
                if (getAppTheme()) {
                    dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(PageCreateActivity.this, R.style.Theme_Diary_Dark)));
                } else {
                    dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(PageCreateActivity.this, R.style.Theme_Diary_Light)));
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
                        txtCancel.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.push));
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
                        txtSet.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.push));
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
                    dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(PageCreateActivity.this, R.style.Theme_Diary_Dark)));
                } else {
                    dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(PageCreateActivity.this, R.style.Theme_Diary_Light)));
                }

                View chooseMoodView = getLayoutInflater().inflate(R.layout.window_mood_choose, null);

                // Initialize views from inflated layout's view //

                imgPopUpFavorite = chooseMoodView.findViewById(R.id.imgFavorite);
                imgPopUpHappy = chooseMoodView.findViewById(R.id.imgHappy);
                imgPopUpSad = chooseMoodView.findViewById(R.id.imgSad);
                imgPopUpBad = chooseMoodView.findViewById(R.id.imgBad);
                dialogBuilder.setView(chooseMoodView);
                dialog = dialogBuilder.create();

                // Set views onClickListeners //

                imgPopUpFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (pageFavoriteVisibility == 0) {
                            imgPopUpFavorite.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.mood_deselect));
                            pageFavoriteVisibility = 8;
                        } else {
                            imgPopUpFavorite.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.mood_select));
                            pageFavoriteVisibility = 0;
                        }
                    }
                });

                imgPopUpHappy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (pageHappyVisibility == 0) {
                            imgPopUpHappy.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.mood_deselect));
                            pageHappyVisibility = 8;
                        } else {
                            imgPopUpHappy.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.mood_select));
                            pageHappyVisibility = 0;
                        }
                    }
                });

                imgPopUpSad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (pageSadVisibility == 0) {
                            imgPopUpSad.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.mood_deselect));
                            pageSadVisibility = 8;
                        } else {
                            imgPopUpSad.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.mood_select));
                            pageSadVisibility = 0;
                        }

                    }
                });

                imgPopUpBad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (pageBadVisibility == 0) {
                            imgPopUpBad.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.mood_deselect));
                            pageBadVisibility = 8;
                        } else {
                            imgPopUpBad.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.mood_select));
                            pageBadVisibility = 0;
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
                            imgFavorite.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.mood_select));
                        } else {
                            imgFavorite.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.mood_deselect));
                        }

                        if (pageHappyVisibility == previousPageHappyVisibility) {
                        } else if (pageHappyVisibility == 0) {
                            imgHappy.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.mood_select));
                        } else {
                            imgHappy.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.mood_deselect));
                        }

                        if (pageSadVisibility == previousPageSadVisibility) {
                        } else if (pageSadVisibility == 0) {
                            imgSad.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.mood_select));
                        } else {
                            imgSad.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.mood_deselect));
                        }

                        if (pageBadVisibility == previousPageBadVisibility) {
                        } else if (pageBadVisibility == 0) {
                            imgBad.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.mood_select));
                        } else {
                            imgBad.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.mood_deselect));
                        }
                    }
                });

                dialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pageFavoriteVisibility == 8) {
                            imgPopUpFavorite.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.mood_deselect));
                        }
                        if (pageHappyVisibility == 8) {
                            imgPopUpHappy.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.mood_deselect));
                        }
                        if (pageSadVisibility == 8) {
                            imgPopUpSad.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.mood_deselect));
                        }
                        if (pageBadVisibility == 8) {
                            imgPopUpBad.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.mood_deselect));
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
                        bmThumbnailTemp = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUriResultCrop);
                        imgLandscapeThumbnail.setImageBitmap(bmThumbnailTemp);
                        imgPortraitThumbnail.setImageBitmap(bmThumbnailTemp);
                    } else if (currentRequestCode == SET_COVER) {
                        bmCover = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUriResultCrop);
                        imgCover.setImageBitmap(bmCover);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void imgCrop(@NonNull Uri uri, int requestCode) {
        UCrop uCrop;
        if (requestCode == SET_THUMBNAIL) {
            uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), "img.jpg")))
                    .withAspectRatio(6, 5);
        } else if (requestCode == SET_COVER) {
            uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), "img.jpg")));
        } else {
            uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), "img.jpg")));
        }
        uCrop.withOptions(getCropOptions());
        uCrop.start(PageCreateActivity.this);
    }

    private UCrop.Options getCropOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);
        return options;
    }

    private void moodsAnimations() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imgFavorite.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.mood_deselect));
                imgHappy.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.mood_deselect));
                imgSad.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.mood_deselect));
                imgBad.startAnimation(AnimationUtils.loadAnimation(PageCreateActivity.this, R.anim.mood_deselect));
            }
        }, 500);
    }


    private void createDialog(String message) {
        if (getAppTheme()) {
            dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(PageCreateActivity.this, R.style.Theme_Diary_Dark)));
        } else {
            dialogBuilder = new AlertDialog.Builder((new ContextThemeWrapper(PageCreateActivity.this, R.style.Theme_Diary_Light)));
        }
        View windowDialog = getLayoutInflater().inflate(R.layout.window_dialog, null);
        TextView txtMessage = windowDialog.findViewById(R.id.txtText);
        txtMessage.setText(message);
        dialogBuilder.setView(windowDialog);
        dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private void initViews() {
        imgBack = findViewById(R.id.imgBack);
        imgCreate = findViewById(R.id.imgCreate);
        imgThumbnail = findViewById(R.id.imgThumbnail);
        bmThumbnail = BitmapFactory.decodeResource(getResources(), R.mipmap.img_def);
        bmThumbnailTemp = bmThumbnail;
        imgCover = findViewById(R.id.imgCover);
        cvDefault = findViewById(R.id.cvDefault);
        bmCover = BitmapFactory.decodeResource(getResources(), R.mipmap.img_def);
        etTitle = findViewById(R.id.etTitle);
        txtDate = findViewById(R.id.txtDate);
        cvNote = findViewById(R.id.cvNote);
        etNote = findViewById(R.id.etNote);
        imgFavorite = findViewById(R.id.imgFavorite);
        imgHappy = findViewById(R.id.imgHappy);
        imgSad = findViewById(R.id.imgSad);
        imgBad = findViewById(R.id.imgBad);
        imgFavorite = findViewById(R.id.imgFavorite);
        clMoods = findViewById(R.id.clMoods);

        if (getAppTheme()) {
            cvNote.setCardElevation(0);
        }

        String date = new SimpleDateFormat("EEE / MM-dd-yyyy", Locale.getDefault()).format(new Date());
        txtDate.setText(date);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                etNote.requestFocus();
            }
        }, 1000);

    }

    // SharedPreferences //

    // Layout method/s //
    private boolean getAppLayout() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("vertical", false);
    }

    // Theme methods //
    private boolean getAppTheme() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_mode", false);
    }

}