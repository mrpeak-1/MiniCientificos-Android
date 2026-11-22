package com.icsm.minicientificos_android.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.databinding.DialogStoryViewerBinding;
import com.icsm.minicientificos_android.models.GaleriaCatalogData;
import com.icsm.minicientificos_android.models.GaleriaFoto;
import com.icsm.minicientificos_android.utils.GameSoundAndVfxHelper;
import com.icsm.minicientificos_android.utils.WhatsAppHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Visor interactivo estilo Instagram Stories con barra segmentada animada
 * de 5 segundos por fotografía, avance/retroceso táctil y botón CTA directo.
 */
public class StoryViewerDialog extends DialogFragment {

    private static final String ARG_STORY = "arg_story_item";
    private static final long DURACION_STORY_MS = 5000L;

    private DialogStoryViewerBinding binding;
    private GaleriaCatalogData.StoryItem storyItem;
    private List<GaleriaFoto> fotosSubgrupo = new ArrayList<>();
    private final List<ProgressBar> progressBars = new ArrayList<>();

    private int currentIndex = 0;
    private ObjectAnimator progressAnimator;
    private boolean isAdvancingManually = false;

    public StoryViewerDialog() {
        // Constructor público vacío requerido
    }

    public static StoryViewerDialog newInstance(GaleriaCatalogData.StoryItem storyItem) {
        StoryViewerDialog dialog = new StoryViewerDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_STORY, storyItem);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        if (getArguments() != null) {
            Serializable ser = getArguments().getSerializable(ARG_STORY);
            if (ser instanceof GaleriaCatalogData.StoryItem) {
                storyItem = (GaleriaCatalogData.StoryItem) ser;
            }
        }
        if (storyItem == null) {
            storyItem = GaleriaCatalogData.obtenerStoriesCientificas().get(0);
        }

        fotosSubgrupo = GaleriaCatalogData.obtenerPorSubgrupo(storyItem.getSubgrupoObjetivo());
        if (fotosSubgrupo.isEmpty()) {
            fotosSubgrupo = GaleriaCatalogData.obtenerTodasLasFotos();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DialogStoryViewerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupHeaderMascota();
        crearBarrasDeProgreso();
        setupListeners();
        mostrarSlide(0);
    }

    private void setupHeaderMascota() {
        if (getContext() == null || binding == null) return;
        int avatarRes = GameSoundAndVfxHelper.obtenerDrawablePersonajeEstado(
                requireContext(),
                storyItem.getMascotaGuia(),
                "FELIZ"
        );
        binding.ivStoryMascotAvatar.setImageResource(avatarRes);
        binding.tvStorySubgrupoTitle.setText(storyItem.getEmoji() + " " + storyItem.getSubgrupoObjetivo() + " · Mini Científicos");
    }

    private void crearBarrasDeProgreso() {
        if (getContext() == null || binding == null) return;
        binding.llStoryProgressContainer.removeAllViews();
        progressBars.clear();

        int margenPx = (int) (3 * getResources().getDisplayMetrics().density);
        int alturaPx = (int) (4 * getResources().getDisplayMetrics().density);

        for (int i = 0; i < fotosSubgrupo.size(); i++) {
            ProgressBar pb = new ProgressBar(requireContext(), null, android.R.attr.progressBarStyleHorizontal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, alturaPx, 1f);
            params.setMargins(margenPx, 0, margenPx, 0);
            pb.setLayoutParams(params);
            pb.setMax(100);
            pb.setProgress(0);
            pb.setProgressDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.bg_story_progress_bar));

            binding.llStoryProgressContainer.addView(pb);
            progressBars.add(pb);
        }
    }

    private void setupListeners() {
        binding.btnCloseStory.setOnClickListener(v -> {
            GameSoundAndVfxHelper.getInstance().playClick();
            detenerAnimador();
            dismiss();
        });

        binding.viewStoryTapPrev.setOnClickListener(v -> {
            GameSoundAndVfxHelper.getInstance().playClick();
            if (currentIndex > 0) {
                mostrarSlide(currentIndex - 1);
            } else {
                mostrarSlide(0);
            }
        });

        binding.viewStoryTapNext.setOnClickListener(v -> {
            GameSoundAndVfxHelper.getInstance().playClick();
            if (currentIndex + 1 < fotosSubgrupo.size()) {
                mostrarSlide(currentIndex + 1);
            } else {
                detenerAnimador();
                dismiss();
            }
        });

        binding.btnStoryQuieroVivir.setOnClickListener(v -> {
            if (getContext() == null || fotosSubgrupo.isEmpty()) return;
            GameSoundAndVfxHelper.getInstance().playRespuestaCorrecta(requireContext());
            detenerAnimador();
            GaleriaFoto fotoActual = fotosSubgrupo.get(currentIndex);
            String mensaje = "¡Hola Mini Científicos! 🔬 Acabo de ver la Story Científica de *" +
                    storyItem.getSubgrupoObjetivo() + "* (\"" + fotoActual.getTitulo() +
                    "\") y ¡quiero vivir esta experiencia! ¿Cuándo es la próxima fecha disponible?";
            WhatsAppHelper.abrirSoporteDirecto(requireContext(), mensaje);
            dismiss();
        });
    }

    private void mostrarSlide(int index) {
        if (binding == null || getContext() == null || fotosSubgrupo.isEmpty()) return;
        if (index < 0 || index >= fotosSubgrupo.size()) {
            detenerAnimador();
            dismiss();
            return;
        }

        detenerAnimador();
        currentIndex = index;

        for (int i = 0; i < progressBars.size(); i++) {
            ProgressBar pb = progressBars.get(i);
            if (i < currentIndex) {
                pb.setProgress(100);
            } else {
                pb.setProgress(0);
            }
        }

        GaleriaFoto foto = fotosSubgrupo.get(currentIndex);
        binding.ivStoryFotoActual.setImageResource(foto.resolveDrawableResId(requireContext()));
        binding.tvStoryStepInfo.setText("Historia " + (currentIndex + 1) + " de " + fotosSubgrupo.size() + " · " + foto.getFechaLugar());
        binding.tvStoryFotoTitulo.setText(foto.getTitulo());
        binding.tvStoryFotoDesc.setText(foto.getDescripcionActividad());

        iniciarTemporizadorSlide(currentIndex);
    }

    private void iniciarTemporizadorSlide(int index) {
        if (index < 0 || index >= progressBars.size()) return;

        ProgressBar pbActual = progressBars.get(index);
        isAdvancingManually = false;

        progressAnimator = ObjectAnimator.ofInt(pbActual, "progress", 0, 100);
        progressAnimator.setDuration(DURACION_STORY_MS);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                isAdvancingManually = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isAdvancingManually || !isAdded() || binding == null) return;
                if (currentIndex + 1 < fotosSubgrupo.size()) {
                    mostrarSlide(currentIndex + 1);
                } else {
                    dismissAllowingStateLoss();
                }
            }
        });
        progressAnimator.start();
    }

    private void detenerAnimador() {
        if (progressAnimator != null) {
            isAdvancingManually = true;
            progressAnimator.removeAllListeners();
            progressAnimator.cancel();
            progressAnimator = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (progressAnimator != null && progressAnimator.isRunning()) {
            progressAnimator.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (progressAnimator != null && progressAnimator.isPaused()) {
            progressAnimator.resume();
        }
    }

    @Override
    public void onDestroyView() {
        detenerAnimador();
        super.onDestroyView();
        binding = null;
    }
}
