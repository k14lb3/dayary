package com.example.dayary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView imgBg, imgLogo;
    TextView txtName;
    private int red, orange, yellow, green, blue, indigo, violet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(SplashScreenActivity.this).edit();
        editor.clear().apply();

        setTheme(getAppTheme() ? R.style.Theme_Diary_Dark : R.style.Theme_Diary_Light);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initViews();

        if (getPassword().equals("161803399") || getPassword().equals("314159265")) {
            txtName.setText("Diarrhea");
            imgBg.setVisibility(View.VISIBLE);
            imgLogo.startAnimation(AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.rotate_fast));
            txtName.startAnimation(AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.left_to_right));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    imgBg.setBackgroundColor(red);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imgBg.setBackgroundColor(orange);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    imgBg.setBackgroundColor(yellow);

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            imgBg.setBackgroundColor(green);

                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    imgBg.setBackgroundColor(blue);

                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            imgBg.setBackgroundColor(indigo);

                                                            new Handler().postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    imgBg.setBackgroundColor(violet);

                                                                    new Handler().postDelayed(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            imgBg.setBackgroundColor(red);

                                                                            new Handler().postDelayed(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    imgBg.setBackgroundColor(orange);

                                                                                    new Handler().postDelayed(new Runnable() {
                                                                                        @Override
                                                                                        public void run() {
                                                                                            imgBg.setBackgroundColor(yellow);

                                                                                            new Handler().postDelayed(new Runnable() {
                                                                                                @Override
                                                                                                public void run() {
                                                                                                    imgBg.setBackgroundColor(green);

                                                                                                    new Handler().postDelayed(new Runnable() {
                                                                                                        @Override
                                                                                                        public void run() {
                                                                                                            imgBg.setBackgroundColor(blue);
                                                                                                            new Handler().postDelayed(new Runnable() {
                                                                                                                @Override
                                                                                                                public void run() {
                                                                                                                    imgBg.setBackgroundColor(indigo);
                                                                                                                    new Handler().postDelayed(new Runnable() {
                                                                                                                        @Override
                                                                                                                        public void run() {
                                                                                                                            imgBg.setBackgroundColor(violet);
                                                                                                                            new Handler().postDelayed(new Runnable() {
                                                                                                                                @Override
                                                                                                                                public void run() {
                                                                                                                                    imgBg.setBackgroundColor(red);

                                                                                                                                    new Handler().postDelayed(new Runnable() {
                                                                                                                                        @Override
                                                                                                                                        public void run() {
                                                                                                                                            imgBg.setBackgroundColor(orange);

                                                                                                                                            new Handler().postDelayed(new Runnable() {
                                                                                                                                                @Override
                                                                                                                                                public void run() {
                                                                                                                                                    imgBg.setBackgroundColor(yellow);

                                                                                                                                                    new Handler().postDelayed(new Runnable() {
                                                                                                                                                        @Override
                                                                                                                                                        public void run() {
                                                                                                                                                            imgBg.setBackgroundColor(green);

                                                                                                                                                            new Handler().postDelayed(new Runnable() {
                                                                                                                                                                @Override
                                                                                                                                                                public void run() {
                                                                                                                                                                    imgBg.setBackgroundColor(blue);
                                                                                                                                                                    new Handler().postDelayed(new Runnable() {
                                                                                                                                                                        @Override
                                                                                                                                                                        public void run() {
                                                                                                                                                                            imgBg.setBackgroundColor(indigo);
                                                                                                                                                                            new Handler().postDelayed(new Runnable() {
                                                                                                                                                                                @Override
                                                                                                                                                                                public void run() {
                                                                                                                                                                                    imgBg.setBackgroundColor(violet);
                                                                                                                                                                                    new Handler().postDelayed(new Runnable() {
                                                                                                                                                                                        @Override
                                                                                                                                                                                        public void run() {
                                                                                                                                                                                            imgBg.setBackgroundColor(red);

                                                                                                                                                                                            new Handler().postDelayed(new Runnable() {
                                                                                                                                                                                                @Override
                                                                                                                                                                                                public void run() {
                                                                                                                                                                                                    imgBg.setBackgroundColor(orange);

                                                                                                                                                                                                    new Handler().postDelayed(new Runnable() {
                                                                                                                                                                                                        @Override
                                                                                                                                                                                                        public void run() {
                                                                                                                                                                                                            imgBg.setBackgroundColor(yellow);

                                                                                                                                                                                                            new Handler().postDelayed(new Runnable() {
                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                public void run() {
                                                                                                                                                                                                                    imgBg.setBackgroundColor(green);

                                                                                                                                                                                                                    new Handler().postDelayed(new Runnable() {
                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                        public void run() {
                                                                                                                                                                                                                            imgBg.setBackgroundColor(blue);
                                                                                                                                                                                                                            new Handler().postDelayed(new Runnable() {
                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                public void run() {
                                                                                                                                                                                                                                    imgBg.setBackgroundColor(indigo);
                                                                                                                                                                                                                                    new Handler().postDelayed(new Runnable() {
                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                        public void run() {
                                                                                                                                                                                                                                            imgBg.setBackgroundColor(violet);
                                                                                                                                                                                                                                            new Handler().postDelayed(new Runnable() {
                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                public void run() {
                                                                                                                                                                                                                                                    imgBg.setBackgroundColor(red);

                                                                                                                                                                                                                                                    new Handler().postDelayed(new Runnable() {
                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                        public void run() {
                                                                                                                                                                                                                                                            imgBg.setBackgroundColor(orange);

                                                                                                                                                                                                                                                            new Handler().postDelayed(new Runnable() {
                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                public void run() {
                                                                                                                                                                                                                                                                    imgBg.setBackgroundColor(yellow);

                                                                                                                                                                                                                                                                    new Handler().postDelayed(new Runnable() {
                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                        public void run() {
                                                                                                                                                                                                                                                                            imgBg.setBackgroundColor(green);

                                                                                                                                                                                                                                                                            new Handler().postDelayed(new Runnable() {
                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                public void run() {
                                                                                                                                                                                                                                                                                    imgBg.setBackgroundColor(blue);
                                                                                                                                                                                                                                                                                    new Handler().postDelayed(new Runnable() {
                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                        public void run() {
                                                                                                                                                                                                                                                                                            imgBg.setBackgroundColor(indigo);
                                                                                                                                                                                                                                                                                            new Handler().postDelayed(new Runnable() {
                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                public void run() {
                                                                                                                                                                                                                                                                                                    imgBg.setBackgroundColor(violet);
                                                                                                                                                                                                                                                                                                    new Handler().postDelayed(new Runnable() {
                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                        public void run() {
                                                                                                                                                                                                                                                                                                            imgBg.setBackgroundColor(red);

                                                                                                                                                                                                                                                                                                            new Handler().postDelayed(new Runnable() {
                                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                                public void run() {
                                                                                                                                                                                                                                                                                                                    imgBg.setBackgroundColor(orange);

                                                                                                                                                                                                                                                                                                                    new Handler().postDelayed(new Runnable() {
                                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                                        public void run() {
                                                                                                                                                                                                                                                                                                                            imgBg.setBackgroundColor(yellow);

                                                                                                                                                                                                                                                                                                                            new Handler().postDelayed(new Runnable() {
                                                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                                                public void run() {
                                                                                                                                                                                                                                                                                                                                    imgBg.setBackgroundColor(green);

                                                                                                                                                                                                                                                                                                                                    new Handler().postDelayed(new Runnable() {
                                                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                                                        public void run() {
                                                                                                                                                                                                                                                                                                                                            imgBg.setBackgroundColor(blue);
                                                                                                                                                                                                                                                                                                                                            new Handler().postDelayed(new Runnable() {
                                                                                                                                                                                                                                                                                                                                                @Override
                                                                                                                                                                                                                                                                                                                                                public void run() {
                                                                                                                                                                                                                                                                                                                                                    imgBg.setBackgroundColor(indigo);
                                                                                                                                                                                                                                                                                                                                                    new Handler().postDelayed(new Runnable() {
                                                                                                                                                                                                                                                                                                                                                        @Override
                                                                                                                                                                                                                                                                                                                                                        public void run() {
                                                                                                                                                                                                                                                                                                                                                            imgBg.setBackgroundColor(violet);

                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                    }, 100);

                                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                                            }, 100);

                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                    }, 100);

                                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                                            }, 100);

                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                    }, 100);

                                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                                            }, 100);

                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                    }, 100);
                                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                                            }, 100);

                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                    }, 100);

                                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                                            }, 100);

                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                    }, 100);

                                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                            }, 100);

                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                    }, 100);

                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                            }, 100);
                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                    }, 100);

                                                                                                                                                                                                                                }
                                                                                                                                                                                                                            }, 100);

                                                                                                                                                                                                                        }
                                                                                                                                                                                                                    }, 100);

                                                                                                                                                                                                                }
                                                                                                                                                                                                            }, 100);

                                                                                                                                                                                                        }
                                                                                                                                                                                                    }, 100);

                                                                                                                                                                                                }
                                                                                                                                                                                            }, 100);

                                                                                                                                                                                        }
                                                                                                                                                                                    }, 100);
                                                                                                                                                                                }
                                                                                                                                                                            }, 100);

                                                                                                                                                                        }
                                                                                                                                                                    }, 100);

                                                                                                                                                                }
                                                                                                                                                            }, 100);

                                                                                                                                                        }
                                                                                                                                                    }, 100);

                                                                                                                                                }
                                                                                                                                            }, 100);

                                                                                                                                        }
                                                                                                                                    }, 100);

                                                                                                                                }
                                                                                                                            }, 100);

                                                                                                                        }
                                                                                                                    }, 100);

                                                                                                                }
                                                                                                            }, 100);

                                                                                                        }
                                                                                                    }, 100);

                                                                                                }
                                                                                            }, 100);

                                                                                        }
                                                                                    }, 100);

                                                                                }
                                                                            }, 100);

                                                                        }
                                                                    }, 100);
                                                                }
                                                            }, 100);

                                                        }
                                                    }, 100);

                                                }
                                            }, 100);

                                        }
                                    }, 100);

                                }
                            }, 100);

                        }
                    }, 100);

                }
            }, 100);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3200);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    txtName.startAnimation(AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.fade_in_slow));
                    imgLogo.startAnimation(AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.rotate));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 3200);
                }
            }, 220);
        }

    }

    private void initViews() {
        imgBg = findViewById(R.id.imgBg);
        imgLogo = findViewById(R.id.imgLogo);
        txtName = findViewById(R.id.txtName);
        red = getResources().getColor(R.color.red);
        orange = getResources().getColor(R.color.orange);
        yellow = getResources().getColor(R.color.yellow);
        green = getResources().getColor(R.color.green);
        blue = getResources().getColor(R.color.blue);
        indigo = getResources().getColor(R.color.indigo);
        violet = getResources().getColor(R.color.violet);
    }

    // SharedPreferences //

    // Theme methods //
    private boolean getAppTheme() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("dark_mode", false);
    }

    // Password method/s //
    private String getPassword() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString("pass", "69");
    }

}