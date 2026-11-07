package com.icsm.minicientificos_android.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.adapters.PildoraTallerHomeAdapter;
import com.icsm.minicientificos_android.controllers.HeroSliderManager;
import com.icsm.minicientificos_android.controllers.MiniPlanetaController;
import com.icsm.minicientificos_android.databinding.FragmentHomeBinding;
import com.icsm.minicientificos_android.models.PildoraTallerHome;
import com.icsm.minicientificos_android.models.PildorasTallerHomeCatalog;
import com.icsm.minicientificos_android.models.ServicioCientifico;
import com.icsm.minicientificos_android.models.ServiciosCatalogData;
import com.icsm.minicientificos_android.models.SliderItem;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HeroSliderManager heroSliderManager;
    private MiniPlanetaController miniPlanetaController;
    private HomeVideoScrollController videoScrollController;

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

        // BLOQUE 1: Hero Slider
        setupHeroSlider();

        // BLOQUES 2 Y 3: Servicios y 12 Píldoras
        setupServiciosYModal();
        setupPildoras12();

        // BLOQUE 4: Mini Planeta 3D (Existente de Integrante 2)
        setupMiniPlaneta();

        // BLOQUE 5: Video Scroll Nativo ExoPlayer y Tarjetas Flotantes
        setupVideoScroll();

        // BLOQUE 6: Pie de página corporativo
        setupFooter();
    }

    private void setupHeroSlider() {
        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(
                getString(R.string.hero_title_1),
                getString(R.string.hero_subtitle_1),
                getString(R.string.hero_badge_1),
                getString(R.string.hero_btn_1),
                R.drawable.ic_flask
        ));
        sliderItems.add(new SliderItem(
                getString(R.string.hero_title_2),
                getString(R.string.hero_subtitle_2),
                getString(R.string.hero_badge_2),
                getString(R.string.hero_btn_2),
                R.drawable.ic_flask
        ));
        sliderItems.add(new SliderItem(
                getString(R.string.hero_title_3),
                getString(R.string.hero_subtitle_3),
                getString(R.string.hero_badge_3),
                getString(R.string.hero_btn_3),
                R.drawable.ic_nav_shop
        ));

        heroSliderManager = new HeroSliderManager(binding.viewPagerHero, binding.tabLayoutDots);
        heroSliderManager.init(sliderItems, item -> {
            if (item.getTitle().contains("Talleres")) {
                abrirFragment(new WorkshopsFragment());
            } else if (item.getTitle().contains("Tienda") || item.getTitle().contains("Kits")) {
                abrirFragment(new ShopFragment());
            } else {
                abrirFragment(new ContactoFragment());
            }
        });
    }

    private void setupServiciosYModal() {
        if (binding.sectionServiciosPildoras == null) return;

        List<ServicioCientifico> servicios = ServiciosCatalogData.getServicios();

        binding.sectionServiciosPildoras.cardServicioTalleres.setOnClickListener(v -> {
            if (servicios.size() > 0) {
                ServicioHomeBottomSheet.newInstance(servicios.get(0)).show(getChildFragmentManager(), "ServicioTalleres");
            }
        });

        binding.sectionServiciosPildoras.cardServicioEventos.setOnClickListener(v -> {
            if (servicios.size() > 1) {
                ServicioHomeBottomSheet.newInstance(servicios.get(1)).show(getChildFragmentManager(), "ServicioEventos");
            }
        });

        binding.sectionServiciosPildoras.cardServicioFeria.setOnClickListener(v -> {
            if (servicios.size() > 2) {
                ServicioHomeBottomSheet.newInstance(servicios.get(2)).show(getChildFragmentManager(), "ServicioFeria");
            }
        });
    }

    private void setupPildoras12() {
        if (binding.sectionServiciosPildoras == null) return;

        List<PildoraTallerHome> pildoras = PildorasTallerHomeCatalog.getPildoras();
        PildoraTallerHomeAdapter adapter = new PildoraTallerHomeAdapter(pildoras, pildora -> {
            irATalleresYSeleccionar(pildora.getNombre());
        });

        binding.sectionServiciosPildoras.rvHomePildorasTalleres.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.sectionServiciosPildoras.rvHomePildorasTalleres.setAdapter(adapter);

        binding.sectionServiciosPildoras.btnTicketTalleres.setOnClickListener(v -> {
            abrirFragment(new WorkshopsFragment());
        });

        binding.sectionServiciosPildoras.btnHomeAsistenteTalleres.setOnClickListener(v -> {
            AsistenteTallerBottomSheet asistenteModal = new AsistenteTallerBottomSheet();
            asistenteModal.show(getChildFragmentManager(), "AsistenteTallerBottomSheet");
        });
    }

    private void irATalleresYSeleccionar(String nombreTaller) {
        WorkshopsFragment workshopsFragment = new WorkshopsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("selectedWorkshop", nombreTaller);
        workshopsFragment.setArguments(bundle);

        abrirFragment(workshopsFragment);

        if (getActivity() != null) {
            BottomNavigationView bnv = getActivity().findViewById(R.id.bottom_navigation);
            if (bnv != null) {
                bnv.setSelectedItemId(R.id.nav_workshops);
            }
        }
    }

    private void setupMiniPlaneta() {
        miniPlanetaController = new MiniPlanetaController(
                binding.sectionMiniPlaneta.flPlanetaContainer,
                binding.sectionMiniPlaneta.planet3dView,
                binding.sectionMiniPlaneta.tvPlanetaPin,
                binding.sectionMiniPlaneta.chipGroupPlaneta,
                getChildFragmentManager()
        );
        miniPlanetaController.setup();
    }

    private void setupVideoScroll() {
        if (binding.sectionVideoScroll == null) return;

        videoScrollController = new HomeVideoScrollController(
                requireContext(),
                binding.sectionVideoScroll.playerViewHomeVideo,
                binding.sectionVideoScroll.imgVideoFallback,
                binding.sectionVideoScroll.btnToggleVideoAudio,
                binding.sectionVideoScroll.cardScrollTitanTesla,
                binding.sectionVideoScroll.cardScrollFuturoManos
        );

        videoScrollController.initPlayer();
        videoScrollController.setupScrollAnimations(binding.nestedScrollViewHome);

        binding.sectionVideoScroll.btnScrollConocerPersonajes.setOnClickListener(v -> {
            abrirFragment(new PersonajesFragment());
        });

        binding.sectionVideoScroll.btnScrollExplorarTalleres.setOnClickListener(v -> {
            abrirFragment(new WorkshopsFragment());
        });
    }

    private void setupFooter() {
        CorporateFooterHelper.setupFooter(requireContext(), binding.sectionCorporateFooter);
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
        if (heroSliderManager != null) {
            heroSliderManager.startAutoPlay();
        }
        if (videoScrollController != null) {
            videoScrollController.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (heroSliderManager != null) {
            heroSliderManager.stopAutoPlay();
        }
        if (videoScrollController != null) {
            videoScrollController.onPause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (heroSliderManager != null) {
            heroSliderManager.stopAutoPlay();
        }
        if (videoScrollController != null) {
            videoScrollController.onDestroyView();
        }
        binding = null;
    }
}