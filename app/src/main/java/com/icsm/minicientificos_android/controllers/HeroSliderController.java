package com.icsm.minicientificos_android.controllers;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.icsm.minicientificos_android.adapters.SliderAdapter;
import com.icsm.minicientificos_android.models.SliderItem;

import java.util.List;

public class HeroSliderController {

    private static final long AUTO_SLIDE_DELAY = 4500; // 4.5s
    private final ViewPager2 viewPager;
    private final TabLayout tabLayout;
    private final Handler sliderHandler = new Handler(Looper.getMainLooper());
    private Runnable sliderRunnable;
    private boolean isUserDragging = false;

    public HeroSliderController(ViewPager2 viewPager, TabLayout tabLayout) {
        this.viewPager = viewPager;
        this.tabLayout = tabLayout;
    }

    public void setup(List<SliderItem> items, SliderAdapter.OnItemClickListener listener) {
        SliderAdapter adapter = new SliderAdapter(items, listener);
        viewPager.setAdapter(adapter);

        // PageTransformer con efecto Zoom-Out / Cross-Fade estilo Swiper.js
        viewPager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                float MIN_SCALE = 0.85f;
                float MIN_ALPHA = 0.5f;

                int pageWidth = page.getWidth();
                int pageHeight = page.getHeight();

                if (position < -1) { // [-Infinite,-1)
                    page.setAlpha(0f);
                } else if (position <= 1) { // [-1,1]
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
                } else { // (1,+Infinite]
                    page.setAlpha(0f);
                }
            }
        });

        // Conectar indicador de puntos
        if (tabLayout != null) {
            new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {}).attach();
        }

        // Listener de interacción táctil / arrastre
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    isUserDragging = true;
                    stopAutoSlide();
                } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    isUserDragging = false;
                    startAutoSlide();
                }
            }
        });

        // Configurar Runnable de Auto-Slide
        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isUserDragging && viewPager.getAdapter() != null) {
                    int itemCount = viewPager.getAdapter().getItemCount();
                    if (itemCount > 0) {
                        int nextItem = (viewPager.getCurrentItem() + 1) % itemCount;
                        viewPager.setCurrentItem(nextItem, true);
                    }
                }
                sliderHandler.postDelayed(this, AUTO_SLIDE_DELAY);
            }
        };

        startAutoSlide();
    }

    public void startAutoSlide() {
        stopAutoSlide();
        if (sliderRunnable != null) {
            sliderHandler.postDelayed(sliderRunnable, AUTO_SLIDE_DELAY);
        }
    }

    public void stopAutoSlide() {
        if (sliderRunnable != null) {
            sliderHandler.removeCallbacks(sliderRunnable);
        }
    }
}