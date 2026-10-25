package com.icsm.minicientificos_android.ui;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.AspectRatioFrameLayout;
import androidx.media3.ui.PlayerView;

import com.google.android.material.button.MaterialButton;

public class HomeVideoScrollController {

    private final Context context;
    private final PlayerView playerView;
    private final ImageView imgFallback;
    private final MaterialButton btnToggleAudio;
    private final View cardTitanTesla;
    private final View cardFuturoManos;

    private ExoPlayer exoPlayer;
    private boolean isMuted = true;

    public HomeVideoScrollController(@NonNull Context context,
                                      @NonNull PlayerView playerView,
                                      @NonNull ImageView imgFallback,
                                      @NonNull MaterialButton btnToggleAudio,
                                      @NonNull View cardTitanTesla,
                                      @NonNull View cardFuturoManos) {
        this.context = context;
        this.playerView = playerView;
        this.imgFallback = imgFallback;
        this.btnToggleAudio = btnToggleAudio;
        this.cardTitanTesla = cardTitanTesla;
        this.cardFuturoManos = cardFuturoManos;
    }

    public void initPlayer() {
        try {
            exoPlayer = new ExoPlayer.Builder(context).build();
            playerView.setPlayer(exoPlayer);

            int rawResId = context.getResources().getIdentifier("video_talleres", "raw", context.getPackageName());
            if (rawResId != 0) {
                Uri videoUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + rawResId);
                MediaItem mediaItem = MediaItem.fromUri(videoUri);
                exoPlayer.setMediaItem(mediaItem);
            }

            exoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
            exoPlayer.setVolume(0f);

            exoPlayer.addListener(new Player.Listener() {
                @Override
                public void onRenderedFirstFrame() {
                    if (imgFallback != null) {
                        imgFallback.setVisibility(View.GONE);
                    }
                }
            });

            exoPlayer.prepare();
            exoPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
            if (imgFallback != null) {
                imgFallback.setVisibility(View.VISIBLE);
            }
        }

        if (btnToggleAudio != null) {
            btnToggleAudio.setOnClickListener(v -> toggleAudio());
        }

        // Estado Inicial de Tarjetas
        cardTitanTesla.setAlpha(0f);
        cardTitanTesla.setTranslationY(130f);
        cardTitanTesla.setScaleX(0.90f);
        cardTitanTesla.setScaleY(0.90f);

        cardFuturoManos.setAlpha(0f);
        cardFuturoManos.setTranslationY(130f);
        cardFuturoManos.setScaleX(0.90f);
        cardFuturoManos.setScaleY(0.90f);
    }

    private void toggleAudio() {
        if (exoPlayer == null) return;
        if (isMuted) {
            exoPlayer.setVolume(1f);
            if (btnToggleAudio != null) btnToggleAudio.setText("🔊 Sonido activado");
            isMuted = false;
        } else {
            exoPlayer.setVolume(0f);
            if (btnToggleAudio != null) btnToggleAudio.setText("🔇 Sonido desactivado");
            isMuted = true;
        }
    }

    public void setupScrollAnimations(@NonNull NestedScrollView scrollView) {
        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            int screenHeight = context.getResources().getDisplayMetrics().heightPixels;

            // Animar Card Titan & Tesla
            int[] loc1 = new int[2];
            cardTitanTesla.getLocationOnScreen(loc1);
            if (loc1[1] < screenHeight * 0.82f && cardTitanTesla.getAlpha() < 0.1f) {
                cardTitanTesla.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(800)
                        .setInterpolator(new OvershootInterpolator(1.25f))
                        .start();
            }

            // Animar Card Futuro en sus Manos
            int[] loc2 = new int[2];
            cardFuturoManos.getLocationOnScreen(loc2);
            if (loc2[1] < screenHeight * 0.82f && cardFuturoManos.getAlpha() < 0.1f) {
                cardFuturoManos.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(800)
                        .setInterpolator(new OvershootInterpolator(1.25f))
                        .start();
            }

            // Parallax suave y seguro relativo al viewport
            if (playerView != null && playerView.getHeight() > 0) {
                int[] videoLoc = new int[2];
                playerView.getLocationOnScreen(videoLoc);
                int screenCenter = screenHeight / 2;
                int videoCenter = videoLoc[1] + (playerView.getHeight() / 2);
                float relativeOffset = (screenCenter - videoCenter) * 0.10f;
                playerView.setTranslationY(relativeOffset);
            }
        });
    }

    public void onResume() {
        if (exoPlayer != null) {
            exoPlayer.play();
        }
    }

    public void onPause() {
        if (exoPlayer != null) {
            exoPlayer.pause();
        }
    }

    public void onDestroyView() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }
}