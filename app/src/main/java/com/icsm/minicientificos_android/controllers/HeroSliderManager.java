package com.icsm.minicientificos_android.controllers;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.icsm.minicientificos_android.adapters.SliderAdapter;
import com.icsm.minicientificos_android.models.SliderItem;

import java.util.List;

public class HeroSliderManager {

    private static final long AUTO_PLAY_INTERVAL = 4500L; // 4.5s
    private final ViewPager2 viewPager;
    private final TabLayout tabLayout;
    private final Handler autoPlayHandler = new Handler(Looper.getMainLooper());
    private Runnable autoPlayRunnable;
    private boolean isUserDragging = false;

    public HeroSliderManager(@NonNull ViewPager2 viewPager, TabLayout tabLayout) {
        this.viewPager = viewPager;
        this.tabLayout = tabLayout;
    }

    public void init(List<SliderItem> items, SliderAdapter.OnItemClickListener clickListener) {
        SliderAdapter adapter = new SliderAdapter(items, clickListener);
        viewPager.setAdapter(adapter);

        // PageTransformer con Parallax + Zoom-Out + Cross-Fade (Swiper.js)
        viewPager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float MIN_SCALE = 0.85f;
                float MIN_ALPHA = 0.5f;

                int pageWidth = page.getWidth();
                int pageHeight = page.getHeight();

                if (position < -1) {
                    page.setAlpha(0f);
                } else if (position <= 1) {
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                    float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                    float horzMargin = pageWidth * (1 - scaleFactor) / 2;

                    if (position < 0) {
                        page.setTranslationX(horzMargin - vertMargin / 2);
                    } else {
                        page.setTranslationX(-horzMargin + vertMargin / 2);
                    }

                    page.setScaleX(scaleFactor);
                    page.setScaleY(scaleFactor);
                    page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
                } else {
                    page.setAlpha(0f);
                }
            }
        });

        if (tabLayout != null) {
            new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {}).attach();
        }

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    isUserDragging = true;
                    stopAutoPlay();
                } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    isUserDragging = false;
                    startAutoPlay();
                }
            }
        });

        autoPlayRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isUserDragging && viewPager.getAdapter() != null) {
                    int itemCount = viewPager.getAdapter().getItemCount();
                    if (itemCount > 0) {
                        int nextPos = (viewPager.getCurrentItem() + 1) % itemCount;
                        viewPager.setCurrentItem(nextPos, true);
                    }
                }
                autoPlayHandler.postDelayed(this, AUTO_PLAY_INTERVAL);
            }
        };

        startAutoPlay();
    }

    public void startAutoPlay() {
        stopAutoPlay();
        if (autoPlayRunnable != null) {
            autoPlayHandler.postDelayed(autoPlayRunnable, AUTO_PLAY_INTERVAL);
        }
    }

    public void stopAutoPlay() {
        if (autoPlayRunnable != null) {
            autoPlayHandler.removeCallbacks(autoPlayRunnable);
        }
    }
}