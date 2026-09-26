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
import com.icsm.minicientificos_android.adapters.SliderAdapter;
import com.icsm.minicientificos_android.databinding.FragmentHomeBinding;
import com.icsm.minicientificos_android.models.ServiceItem;
import com.icsm.minicientificos_android.models.SliderItem;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

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
        setupCtaWhatsApp();
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

        SliderAdapter adapter = new SliderAdapter(sliderItems, item -> {
            if (item.getTitle().contains("Talleres")) {
                abrirFragment(new WorkshopsFragment());
            } else if (item.getTitle().contains("Tienda") || item.getTitle().contains("Kits")) {
                abrirFragment(new ShopFragment());
            } else {
                abrirFragment(new ContactoFragment());
            }
        });

        binding.viewPagerHero.setAdapter(adapter);

        // Vincula los puntos indicadores (TabLayout) con el ViewPager2 (Emulando Swiper.js)
        new TabLayoutMediator(binding.tabLayoutDots, binding.viewPagerHero, (tab, position) -> {
            // Los indicadores son formas personalizadas vía selector XML
        }).attach();
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

    private void setupCtaWhatsApp() {
        binding.btnWhatsappCta.setOnClickListener(v -> {
            String url = "https://minicientificos-proces.vercel.app/";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Visita nuestra web oficial: " + url, Toast.LENGTH_SHORT).show();
            }
        });
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
        super.onDestroyView();
        binding = null;
    }
}