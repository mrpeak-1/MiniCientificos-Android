package com.icsm.minicientificos_android.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.adapters.QuickWorkshopPillAdapter;
import com.icsm.minicientificos_android.adapters.WelcomeSlideAdapter;
import com.icsm.minicientificos_android.databinding.FragmentHomeBinding;
import com.icsm.minicientificos_android.models.QuickWorkshopPillCatalog;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeVideoScrollManager videoScrollManager;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupWelcomeCarousel();
        setupVideoAndParallax();
        setupServiceCards();
        setupQuickWorkshopPills();
        startTicketPulseAnimation();
    }

    private void setupWelcomeCarousel() {
        View welcomeSection = binding.getRoot().findViewById(R.id.welcomeSection);
        ViewPager2 vpWelcome = welcomeSection.findViewById(R.id.vpWelcome);
        
        List<WelcomeSlideAdapter.SlideItem> slides = new ArrayList<>();
        slides.add(new WelcomeSlideAdapter.SlideItem("¡Diviértete!", "¡Bienvenido a Mini Científicos!", "Aprender ciencia nunca fue tan divertido. Experimentos interactivos para niños inquisitivos.", R.drawable.ic_flask));
        slides.add(new WelcomeSlideAdapter.SlideItem("¡Aprende!", "Cursos Increíbles", "Despierta la curiosidad con nuestros talleres de robótica y química.", R.drawable.ic_nav_workshops));
        slides.add(new WelcomeSlideAdapter.SlideItem("¡Descubre!", "Ferias Escolares", "Llevamos la ciencia a tu colegio para hacer la feria más divertida.", R.drawable.ic_nav_gallery));

        vpWelcome.setAdapter(new WelcomeSlideAdapter(slides));

        LinearLayout dotsIndicator = welcomeSection.findViewById(R.id.dotsIndicator);
        setupDots(vpWelcome, dotsIndicator, slides.size());
    }

    private void setupDots(ViewPager2 viewPager, LinearLayout dotsLayout, int size) {
        ImageView[] dots = new ImageView[size];
        for (int i = 0; i < size; i++) {
            dots[i] = new ImageView(requireContext());
            dots[i].setImageResource(R.drawable.bg_indicator_arch);
            
            // Tint dynamically
            dots[i].setColorFilter(i == 0 ? Color.parseColor("#FFD700") : Color.parseColor("#FFFFFF"));
            
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, 70);
            params.setMargins(6, 0, 6, 0);
            dots[i].setLayoutParams(params);
            dots[i].setAlpha(i == 0 ? 1.0f : 0.5f);
            dotsLayout.addView(dots[i]);
        }

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < size; i++) {
                    dots[i].setAlpha(i == position ? 1.0f : 0.5f);
                    dots[i].setColorFilter(i == position ? Color.parseColor("#FFD700") : Color.parseColor("#FFFFFF"));
                }
            }
        });
    }

    private void setupVideoAndParallax() {
        View videoSection = binding.getRoot().findViewById(R.id.videoSection);
        PlayerView playerView = videoSection.findViewById(R.id.playerView);
        View cardTitanTesla = videoSection.findViewById(R.id.cardTitanTesla);
        View cardFuturoManos = videoSection.findViewById(R.id.cardFuturoManos);
        FloatingActionButton btnMuteToggle = videoSection.findViewById(R.id.btnMuteToggle);

        videoScrollManager = new HomeVideoScrollManager(requireContext(), playerView, cardTitanTesla, cardFuturoManos);
        videoScrollManager.initPlayer();
        videoScrollManager.setupMuteToggle(btnMuteToggle);
        videoScrollManager.attachScrollPhysics(binding.nestedScrollView);
    }

    private void setupServiceCards() {
        View servicesSection = binding.getRoot().findViewById(R.id.servicesPillsSection);
        
        View cardTalleres = servicesSection.findViewById(R.id.cardTalleres);
        View cardEventos = servicesSection.findViewById(R.id.cardEventos);
        View cardFerias = servicesSection.findViewById(R.id.cardFerias);

        View.OnClickListener cardListener = v -> {
            String title = "";
            String desc = "";
            if (v.getId() == R.id.cardTalleres) {
                title = "Talleres Científicos";
                desc = "Aprende haciendo con experimentos prácticos. Incluye materiales y diploma.";
            } else if (v.getId() == R.id.cardEventos) {
                title = "Eventos Científicos";
                desc = "Show de ciencia para cumpleaños y más. Tus invitados serán los protagonistas.";
            } else if (v.getId() == R.id.cardFerias) {
                title = "Feria Científica";
                desc = "Para colegios e instituciones educativas. Fomentamos la cultura STEM.";
            }
            ServicioDetalleBottomSheet bottomSheet = ServicioDetalleBottomSheet.newInstance(title, desc);
            bottomSheet.show(getChildFragmentManager(), "ServicioDetalle");
        };

        cardTalleres.setOnClickListener(cardListener);
        cardEventos.setOnClickListener(cardListener);
        cardFerias.setOnClickListener(cardListener);
    }

    private void setupQuickWorkshopPills() {
        View servicesSection = binding.getRoot().findViewById(R.id.servicesPillsSection);
        RecyclerView rvQuickPills = servicesSection.findViewById(R.id.rvQuickPills);

        QuickWorkshopPillAdapter adapter = new QuickWorkshopPillAdapter(QuickWorkshopPillCatalog.getTalleres(), pill -> {
            Bundle args = new Bundle();
            args.putString("selectedWorkshop", pill.getName());
            WorkshopsFragment fragment = new WorkshopsFragment();
            fragment.setArguments(args);
            abrirFragment(fragment);
        });

        rvQuickPills.setAdapter(adapter);
    }

    private void startTicketPulseAnimation() {
        // La animación del imgTicketTalleres fue removida porque el ícono fue eliminado para limpiar el diseño.
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
    public void onResume() {
        super.onResume();
        if (videoScrollManager != null) {
            videoScrollManager.resumePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (videoScrollManager != null) {
            videoScrollManager.pausePlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (videoScrollManager != null) {
            videoScrollManager.releasePlayer();
        }
        binding = null;
    }
}