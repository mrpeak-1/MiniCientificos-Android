package com.icsm.minicientificos_android.ui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.chip.Chip;
import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.adapters.TallerAdapter;
import com.icsm.minicientificos_android.databinding.FragmentWorkshopsBinding;
import com.icsm.minicientificos_android.models.Taller;
import com.icsm.minicientificos_android.models.TalleresCatalogData;
import com.icsm.minicientificos_android.utils.WhatsAppHelper;

import java.util.List;
import java.util.Objects;

public class WorkshopsFragment extends Fragment implements TallerAdapter.OnTallerClickListener {

    private FragmentWorkshopsBinding binding;
    private TallerAdapter adapter;
    private String filtroCategoriaSeleccionada = "Todas";
    private String filtroModalidadSeleccionada = "Todas";
    private String textoBusquedaActual = "";
    private List<Taller> listaTalleres;

    public WorkshopsFragment() {}

    public static WorkshopsFragment newInstanceWithTaller(String query) {
        WorkshopsFragment fragment = new WorkshopsFragment();
        Bundle args = new Bundle();
        args.putString("taller_query", query);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkshopsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        animarOrb(binding.orbTelescopio, 0);
        animarOrb(binding.orbMatraz, 600);
        animarOrb(binding.orbPlanta, 1200);

        listaTalleres = TalleresCatalogData.getTalleres();

        adapter = new TallerAdapter(listaTalleres, this);
        binding.rvTalleres.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvTalleres.setAdapter(adapter);

        binding.etBuscarTaller.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textoBusquedaActual = s.toString();
                filtrarLista();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        configurarBotonCategoria(binding.btnCatTodo, "Todas");
        configurarBotonCategoria(binding.btnCatCiencia, "Ciencia");
        configurarBotonCategoria(binding.btnCatNaturaleza, "Naturaleza");

        configurarBotonModalidad(binding.btnModPresencial, "Presencial");
        configurarBotonModalidad(binding.btnModVirtual, "Virtual");
        configurarBotonModalidad(binding.btnModHibrido, "Híbrido");

        binding.btnHeroVerTalleres.setOnClickListener(v -> 
            binding.scrollWorkshops.smoothScrollTo(0, binding.layoutFiltrosTalleres.getTop())
        );

        binding.btnHeroPedirInfo.setOnClickListener(v -> WhatsAppHelper.abrirConsultaContacto(requireContext(), 
            "Padre/Apoderado", 
            "No especificado", 
            "No especificado", 
            "Información General de Talleres", 
            "¡Hola Mini Científicos! 🔬 Quisiera recibir información general sobre sus 12 talleres presenciales y virtuales para niños."));

        actualizarSubtituloDinamico();
        
        manejarDeepLinking();
    }
    
    private void manejarDeepLinking() {
        if (getArguments() != null && getArguments().containsKey("taller_query")) {
            String query = getArguments().getString("taller_query", "").toLowerCase();
            if (!query.isEmpty()) {
                for (Taller t : listaTalleres) {
                    if (t.getId().equals(query) || t.getTituloCorto().toLowerCase().contains(query)) {
                        onVerDetalleClick(t);
                        // Limpiar argumentos para que no reabra al rotar
                        getArguments().remove("taller_query");
                        break;
                    }
                }
            }
        }
    }

    private void animarOrb(View orb, long startDelay) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(orb, "translationY", -10f, 10f);
        animator.setDuration(1500);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setStartDelay(startDelay);
        animator.start();
    }

    private void configurarBotonCategoria(View btn, String categoria) {
        btn.setOnClickListener(v -> {
            filtroCategoriaSeleccionada = categoria;
            actualizarUIBotonesCategoria();
            filtrarLista();
            actualizarSubtituloDinamico();
        });
    }

    private void configurarBotonModalidad(View btn, String modalidad) {
        btn.setOnClickListener(v -> {
            if (filtroModalidadSeleccionada.equals(modalidad)) {
                filtroModalidadSeleccionada = "Todas"; // Toggle off
            } else {
                filtroModalidadSeleccionada = modalidad;
            }
            actualizarUIBotonesModalidad();
            filtrarLista();
            actualizarSubtituloDinamico();
        });
    }

    private void actualizarUIBotonesCategoria() {
        binding.btnCatTodo.setBackgroundResource(Objects.equals(filtroCategoriaSeleccionada, "Todas") ? R.drawable.bg_filter_pill_green_active : R.drawable.bg_filter_pill_white_inactive);
        binding.btnCatTodo.setTextColor(getResources().getColor(Objects.equals(filtroCategoriaSeleccionada, "Todas") ? R.color.white : R.color.text_light, null));

        binding.btnCatCiencia.setBackgroundResource(Objects.equals(filtroCategoriaSeleccionada, "Ciencia") ? R.drawable.bg_filter_pill_green_active : R.drawable.bg_filter_pill_white_inactive);
        binding.btnCatCiencia.setTextColor(getResources().getColor(Objects.equals(filtroCategoriaSeleccionada, "Ciencia") ? R.color.white : R.color.text_light, null));

        binding.btnCatNaturaleza.setBackgroundResource(Objects.equals(filtroCategoriaSeleccionada, "Naturaleza") ? R.drawable.bg_filter_pill_green_active : R.drawable.bg_filter_pill_white_inactive);
        binding.btnCatNaturaleza.setTextColor(getResources().getColor(Objects.equals(filtroCategoriaSeleccionada, "Naturaleza") ? R.color.white : R.color.text_light, null));
    }

    private void actualizarUIBotonesModalidad() {
        binding.btnModPresencial.setBackgroundResource(Objects.equals(filtroModalidadSeleccionada, "Presencial") ? R.drawable.bg_filter_pill_green_active : R.drawable.bg_filter_pill_white_inactive);
        binding.btnModPresencial.setTextColor(getResources().getColor(Objects.equals(filtroModalidadSeleccionada, "Presencial") ? R.color.white : R.color.text_light, null));

        binding.btnModVirtual.setBackgroundResource(Objects.equals(filtroModalidadSeleccionada, "Virtual") ? R.drawable.bg_filter_pill_green_active : R.drawable.bg_filter_pill_white_inactive);
        binding.btnModVirtual.setTextColor(getResources().getColor(Objects.equals(filtroModalidadSeleccionada, "Virtual") ? R.color.white : R.color.text_light, null));

        binding.btnModHibrido.setBackgroundResource(Objects.equals(filtroModalidadSeleccionada, "Híbrido") ? R.drawable.bg_filter_pill_green_active : R.drawable.bg_filter_pill_white_inactive);
        binding.btnModHibrido.setTextColor(getResources().getColor(Objects.equals(filtroModalidadSeleccionada, "Híbrido") ? R.color.white : R.color.text_light, null));
    }

    private void filtrarLista() {
        if (adapter != null) {
            adapter.aplicarFiltros(textoBusquedaActual, filtroCategoriaSeleccionada, filtroModalidadSeleccionada);
            actualizarEstadoVacio();
        }
    }

    private void actualizarEstadoVacio() {
        if (adapter.getItemCount() == 0) {
            binding.layoutEmptyTalleres.setVisibility(View.VISIBLE);
            binding.rvTalleres.setVisibility(View.GONE);
        } else {
            binding.layoutEmptyTalleres.setVisibility(View.GONE);
            binding.rvTalleres.setVisibility(View.VISIBLE);
        }
    }

    private void actualizarSubtituloDinamico() {
        String cat = Objects.equals(filtroCategoriaSeleccionada, "Todas") ? "" : filtroCategoriaSeleccionada;
        String mod = Objects.equals(filtroModalidadSeleccionada, "Todas") ? "" : filtroModalidadSeleccionada;
        
        StringBuilder textoBuilder = new StringBuilder("Mostrando ");
        textoBuilder.append(adapter.getItemCount()).append(" talleres");
        if (!cat.isEmpty() && !mod.isEmpty()) {
            textoBuilder.append(" de ").append(cat).append(" en modalidad ").append(mod);
        } else if (!cat.isEmpty()) {
            textoBuilder.append(" de ").append(cat);
        } else if (!mod.isEmpty()) {
            textoBuilder.append(" en modalidad ").append(mod);
        }
        
        binding.tvSubtituloFiltroDinamico.setText(textoBuilder.toString());
    }

    @Override
    public void onVerDetalleClick(Taller taller) {
        TallerDetalleBottomSheet bottomSheet = TallerDetalleBottomSheet.newInstance(taller);
        bottomSheet.show(getChildFragmentManager(), "TallerDetalleBottomSheet");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}