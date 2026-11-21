package com.icsm.minicientificos_android.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.databinding.FragmentNosotrosBinding;

public class NosotrosFragment extends Fragment {

    private FragmentNosotrosBinding binding;
    private boolean metricasAnimadas = false;
    private ViewTreeObserver.OnScrollChangedListener scrollListener;

    public NosotrosFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNosotrosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cargarImagenesSeguras();
        setupListeners();
        setupMetricasScrollAnimation();
        setupFooter();
    }

    private void cargarImagenesSeguras() {
        if (getContext() == null || binding == null) return;
        Context context = requireContext();

        // Safe resolution rule 4
        int heroId = obtenerDrawableIdSeguro(context, "equipominicientificos", R.drawable.equipominicientificos);
        binding.imgNosotrosHero.setImageResource(heroId);

        int identidadId = obtenerDrawableIdSeguro(context, "nosotros_identidad", R.drawable.nosotros_identidad);
        binding.imgNosotrosIdentidad.setImageResource(identidadId);

        int AndyId = obtenerDrawableIdSeguro(context, "ing_andy_salazar", R.drawable.ing_andy_salazar);
        binding.imgFundadorAndy.setImageResource(AndyId);
    }

    private int obtenerDrawableIdSeguro(Context context, String nombreRes, int fallbackDrawable) {
        try {
            int id = context.getResources().getIdentifier(nombreRes, "drawable", context.getPackageName());
            if (id != 0) {
                return id;
            }
        } catch (Exception ignored) {
        }
        return fallbackDrawable;
    }

    private void setupListeners() {
        // NAVEGACIÓN A TALLERES
        binding.btnNosotrosVerTalleres.setOnClickListener(v -> abrirFragment(new WorkshopsFragment()));
        if (binding.btnNosotrosVerTalleresBottom != null) {
            binding.btnNosotrosVerTalleresBottom.setOnClickListener(v -> abrirFragment(new WorkshopsFragment()));
        }

        // SMOOTH SCROLL AL BLOQUE 2
        binding.btnNosotrosConoceMas.setOnClickListener(v -> {
            if (binding.vBloque2 != null) {
                binding.scrollNosotros.smoothScrollTo(0, binding.vBloque2.getTop());
            }
        });

        // NAVEGACIÓN A CONTACTO
        if (binding.btnNosotrosContactoBottom != null) {
            binding.btnNosotrosContactoBottom.setOnClickListener(v -> abrirFragment(new ContactoFragment()));
        }

        // GENERADOR DE CREDENCIAL Y DIPLOMA BOTTOM SHEET
        binding.btnAbrirGeneradorCredencial.setOnClickListener(v -> {
            GeneradorCredencialBottomSheet dialog = new GeneradorCredencialBottomSheet();
            dialog.show(getChildFragmentManager(), "GeneradorCredencialBottomSheet");
        });
    }

    private void setupMetricasScrollAnimation() {
        scrollListener = () -> {
            if (binding == null || metricasAnimadas) return;

            Rect scrollBounds = new Rect();
            binding.scrollNosotros.getHitRect(scrollBounds);

            if (binding.vBloqueMetricas.getLocalVisibleRect(scrollBounds)) {
                metricasAnimadas = true;
                animarMetricas();
            }
        };

        binding.scrollNosotros.getViewTreeObserver().addOnScrollChangedListener(scrollListener);
    }

    private void animarMetricas() {
        if (binding == null) return;

        // 1. Contador Niños (+500)
        ValueAnimator animNinos = ValueAnimator.ofInt(0, 500);
        animNinos.setDuration(1500);
        animNinos.setInterpolator(new DecelerateInterpolator());
        animNinos.addUpdateListener(anim -> {
            if (binding != null) {
                binding.tvCounterNinos.setText("+" + anim.getAnimatedValue());
            }
        });
        animNinos.start();

        // 2. Contador Talleres (12)
        ValueAnimator animTalleres = ValueAnimator.ofInt(0, 12);
        animTalleres.setDuration(1500);
        animTalleres.setInterpolator(new DecelerateInterpolator());
        animTalleres.addUpdateListener(anim -> {
            if (binding != null) {
                binding.tvCounterTalleres.setText(String.valueOf(anim.getAnimatedValue()));
            }
        });
        animTalleres.start();

        // 3. Contador Colegios (+30)
        ValueAnimator animColegios = ValueAnimator.ofInt(0, 30);
        animColegios.setDuration(1500);
        animColegios.setInterpolator(new DecelerateInterpolator());
        animColegios.addUpdateListener(anim -> {
            if (binding != null) {
                binding.tvCounterColegios.setText("+" + anim.getAnimatedValue());
            }
        });
        animColegios.start();

        // 4. Contador Diversión (100%)
        ValueAnimator animDiversion = ValueAnimator.ofInt(0, 100);
        animDiversion.setDuration(1500);
        animDiversion.setInterpolator(new DecelerateInterpolator());
        animDiversion.addUpdateListener(anim -> {
            if (binding != null) {
                binding.tvCounterDiversion.setText(anim.getAnimatedValue() + "%");
            }
        });
        animDiversion.start();
    }

    private void setupFooter() {
        if (getContext() != null && binding != null && binding.sectionCorporateFooterNosotros != null) {
            CorporateFooterHelper.setupFooter(requireContext(), binding.sectionCorporateFooterNosotros);
        }
    }

    private void abrirFragment(Fragment fragment) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onDestroyView() {
        if (binding != null && scrollListener != null) {
            binding.scrollNosotros.getViewTreeObserver().removeOnScrollChangedListener(scrollListener);
        }
        super.onDestroyView();
        binding = null;
    }
}