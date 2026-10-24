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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayoutMediator;
import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.adapters.ServiceAdapter;
import com.icsm.minicientificos_android.controllers.HeroSliderManager;
import com.icsm.minicientificos_android.controllers.MiniPlanetaController;
import com.icsm.minicientificos_android.databinding.FragmentHomeBinding;
import com.icsm.minicientificos_android.models.ServiceItem;
import com.icsm.minicientificos_android.models.SliderItem;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HeroSliderManager heroSliderManager;
    private MiniPlanetaController miniPlanetaController;

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

        setupHeroSlider();
        setupServicesList();
        setupCharactersSection();
        setupMiniPlaneta();
        setupCtaWhatsApp();
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

    private void setupServicesList() {
        List<ServiceItem> serviceItems = new ArrayList<>();
        serviceItems.add(new ServiceItem(
                getString(R.string.service_1_title),
                getString(R.string.service_1_desc),
                getString(R.string.service_1_badge),
                R.drawable.ic_flask
        ));
        serviceItems.add(new ServiceItem(
                getString(R.string.service_2_title),
                getString(R.string.service_2_desc),
                getString(R.string.service_2_badge),
                R.drawable.ic_nav_workshops
        ));
        serviceItems.add(new ServiceItem(
                getString(R.string.service_3_title),
                getString(R.string.service_3_desc),
                getString(R.string.service_3_badge),
                R.drawable.ic_nav_gallery
        ));
        serviceItems.add(new ServiceItem(
                getString(R.string.service_4_title),
                getString(R.string.service_4_desc),
                getString(R.string.service_4_badge),
                R.drawable.ic_nav_contact
        ));

        ServiceAdapter serviceAdapter = new ServiceAdapter(serviceItems, item -> {
            if (item.getTitle().contains("Talleres")) {
                abrirFragment(new WorkshopsFragment());
            } else if (item.getTitle().contains("Fiestas")) {
                abrirFragment(new GaleriaFragment());
            } else {
                abrirFragment(new NosotrosFragment());
            }
        });

        binding.rvServices.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvServices.setAdapter(serviceAdapter);
    }

    private void setupCharactersSection() {
        binding.cardTitan.setOnClickListener(v -> abrirFragment(new PersonajesFragment()));
        binding.cardTesla.setOnClickListener(v -> abrirFragment(new PersonajesFragment()));
    }

    private void setupMiniPlaneta() {
        miniPlanetaController = new MiniPlanetaController(
                binding.sectionMiniPlaneta.flPlanetaContainer,
                binding.sectionMiniPlaneta.ivPlaneta3d,
                binding.sectionMiniPlaneta.tvPlanetaPin,
                binding.sectionMiniPlaneta.chipGroupPlaneta,
                getChildFragmentManager()
        );
        miniPlanetaController.setup();
    }

    private void setupCtaWhatsApp() {
        binding.btnWhatsappCta.setOnClickListener(v -> {
            String url = "https://api.whatsapp.com/send?phone=51930754024&text=Hola%20Mini%20Cient%C3%ADficos%2C%20deseo%20m%C3%A1s%20informaci%C3%B3n.";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Abrir WhatsApp: " + url, Toast.LENGTH_SHORT).show();
            }
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
    }

    @Override
    public void onPause() {
        super.onPause();
        if (heroSliderManager != null) {
            heroSliderManager.stopAutoPlay();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (heroSliderManager != null) {
            heroSliderManager.stopAutoPlay();
        }
        binding = null;
    }
}