/*
 * Copyright 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.apps.muzei.settings;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.google.android.apps.muzei.NewWallpaperNotificationReceiver;
import com.google.android.apps.muzei.render.MuzeiBlurRenderer;

import net.nurik.roman.muzei.R;

/**
 * Fragment for allowing the user to configure advanced settings.
 */
public class SettingsAdvancedFragment extends Fragment
        implements SettingsActivity.SettingsActivityMenuListener {

    private Handler mHandler = new Handler();
    private SeekBar mBlurSeekBar;
    private SeekBar mDimSeekBar;
    private SeekBar mGreySeekBar;

    public SettingsAdvancedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_advanced_fragment, container, false);

        mBlurSeekBar = (SeekBar) rootView.findViewById(R.id.blur_amount);
        mBlurSeekBar.setProgress(Prefs.getSharedPreferences(getContext())
                .getInt(Prefs.PREF_BLUR_AMOUNT, MuzeiBlurRenderer.DEFAULT_BLUR));
        mBlurSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
                if (fromUser) {
                    mHandler.removeCallbacks(mUpdateBlurRunnable);
                    mHandler.postDelayed(mUpdateBlurRunnable, 750);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mDimSeekBar = (SeekBar) rootView.findViewById(R.id.dim_amount);
        mDimSeekBar.setProgress(Prefs.getSharedPreferences(getContext())
                .getInt(Prefs.PREF_DIM_AMOUNT, MuzeiBlurRenderer.DEFAULT_MAX_DIM));
        mDimSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
                if (fromUser) {
                    mHandler.removeCallbacks(mUpdateDimRunnable);
                    mHandler.postDelayed(mUpdateDimRunnable, 750);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mGreySeekBar = (SeekBar) rootView.findViewById(R.id.grey_amount);
        mGreySeekBar.setProgress(Prefs.getSharedPreferences(getContext())
                .getInt(Prefs.PREF_GREY_AMOUNT, MuzeiBlurRenderer.DEFAULT_GREY));
        mGreySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
                if (fromUser) {
                    mHandler.removeCallbacks(mUpdateGreyRunnable);
                    mHandler.postDelayed(mUpdateGreyRunnable, 750);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        CheckBox mNotifyNewWallpaperCheckBox = (CheckBox) rootView.findViewById(
                R.id.notify_new_wallpaper_checkbox);
        mNotifyNewWallpaperCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton button, boolean checked) {
                        PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                                .putBoolean(NewWallpaperNotificationReceiver.PREF_ENABLED, checked)
                                .apply();
                    }
                });
        mNotifyNewWallpaperCheckBox.setChecked(PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean(NewWallpaperNotificationReceiver.PREF_ENABLED, true));
        CheckBox mBlurOnLockScreenCheckBox = (CheckBox) rootView.findViewById(
                R.id.blur_on_lockscreen_checkbox);
        mBlurOnLockScreenCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton button, boolean checked) {
                        Prefs.getSharedPreferences(getContext()).edit()
                                .putBoolean(Prefs.PREF_DISABLE_BLUR_WHEN_LOCKED, !checked)
                                .apply();
                    }
                }
        );
        mBlurOnLockScreenCheckBox.setChecked(!Prefs.getSharedPreferences(getContext())
                .getBoolean(Prefs.PREF_DISABLE_BLUR_WHEN_LOCKED, false));
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        ((SettingsActivity) activity).inflateMenuFromFragment(R.menu.settings_advanced);
        super.onAttach(activity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    private Runnable mUpdateBlurRunnable = new Runnable() {
        @Override
        public void run() {
            Prefs.getSharedPreferences(getContext()).edit()
                    .putInt(Prefs.PREF_BLUR_AMOUNT, mBlurSeekBar.getProgress())
                    .apply();
        }
    };

    private Runnable mUpdateDimRunnable = new Runnable() {
        @Override
        public void run() {
            Prefs.getSharedPreferences(getContext()).edit()
                    .putInt(Prefs.PREF_DIM_AMOUNT, mDimSeekBar.getProgress())
                    .apply();
        }
    };

    private Runnable mUpdateGreyRunnable = new Runnable() {
        @Override
        public void run() {
            Prefs.getSharedPreferences(getContext()).edit()
                    .putInt(Prefs.PREF_GREY_AMOUNT, mGreySeekBar.getProgress())
                    .apply();
        }
    };

    @Override
    public void onSettingsActivityMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_reset_defaults) {
            Prefs.getSharedPreferences(getContext()).edit()
                    .putInt(Prefs.PREF_BLUR_AMOUNT, MuzeiBlurRenderer.DEFAULT_BLUR)
                    .putInt(Prefs.PREF_DIM_AMOUNT, MuzeiBlurRenderer.DEFAULT_MAX_DIM)
                    .putInt(Prefs.PREF_GREY_AMOUNT, MuzeiBlurRenderer.DEFAULT_GREY)
                    .apply();
            mBlurSeekBar.setProgress(MuzeiBlurRenderer.DEFAULT_BLUR);
            mDimSeekBar.setProgress(MuzeiBlurRenderer.DEFAULT_MAX_DIM);
            mGreySeekBar.setProgress(MuzeiBlurRenderer.DEFAULT_GREY);
        }
    }
}
