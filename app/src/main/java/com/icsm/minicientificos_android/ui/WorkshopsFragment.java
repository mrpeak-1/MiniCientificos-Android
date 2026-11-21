package com.icsm.minicientificos_android.ui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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

        setupBuscador();
        setupBotonVerTodos();
        setupBotonesCategoria();
        setupBotonesModalidad();
        setupHeroActions();
        setupCorporateFooter();

        actualizarUIStateFiltros();
        actualizarSubtituloDinamico();
        manejarDeepLinking();
    }

    private void setupBuscador() {
        binding.etBuscarTaller.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textoBusquedaActual = s.toString();
                filtrarLista();
                actualizarSubtituloDinamico();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.btnAccionBuscarTaller.setOnClickListener(v -> {
            textoBusquedaActual = binding.etBuscarTaller.getText() != null ? 
                    binding.etBuscarTaller.getText().toString().trim() : "";
            filtrarLista();
            actualizarSubtituloDinamico();
            binding.etBuscarTaller.clearFocus();
        });
    }

    private void setupBotonVerTodos() {
        binding.btnFiltroVerTodos.setOnClickListener(v -> {
            filtroCategoriaSeleccionada = "Todas";
            filtroModalidadSeleccionada = "Todas";
            textoBusquedaActual = "";
            binding.etBuscarTaller.setText("");
            
            actualizarUIStateFiltros();
            filtrarLista();
            actualizarSubtituloDinamico();
        });
    }

    private void setupBotonesCategoria() {
        configurarBotonCategoria(binding.btnCatCiencia, "Ciencia");
        configurarBotonCategoria(binding.btnCatNaturaleza, "Naturaleza");
        configurarBotonCategoria(binding.btnCatTecnologia, "Tecnología");
    }

    private void configurarBotonCategoria(View btn, String categoria) {
        btn.setOnClickListener(v -> {
            if (filtroCategoriaSeleccionada.equalsIgnoreCase(categoria)) {
                filtroCategoriaSeleccionada = "Todas"; // Toggle des-seleccionar
            } else {
                filtroCategoriaSeleccionada = categoria;
            }
            actualizarUIStateFiltros();
            filtrarLista();
            actualizarSubtituloDinamico();
        });
    }

    private void setupBotonesModalidad() {
        configurarBotonModalidad(binding.btnModPresencial, "Presencial");
        configurarBotonModalidad(binding.btnModVirtual, "Virtual");
    }

    private void configurarBotonModalidad(View btn, String modalidad) {
        btn.setOnClickListener(v -> {
            if (filtroModalidadSeleccionada.equalsIgnoreCase(modalidad)) {
                filtroModalidadSeleccionada = "Todas"; // Toggle des-seleccionar
            } else {
                filtroModalidadSeleccionada = modalidad;
            }
            actualizarUIStateFiltros();
            filtrarLista();
            actualizarSubtituloDinamico();
        });
    }

    private void setupHeroActions() {
        binding.btnHeroVerTalleres.setOnClickListener(v -> 
            binding.scrollWorkshops.smoothScrollTo(0, binding.layoutFiltrosTalleres.getTop())
        );

        binding.btnHeroPedirInfo.setOnClickListener(v -> WhatsAppHelper.abrirConsultaContacto(requireContext(), 
            "Padre/Apoderado", 
            "No especificado", 
            "No especificado", 
            "Información General de Talleres", 
            "¡Hola Mini Científicos! 🔬 Quisiera recibir información general sobre sus 12 talleres presenciales y virtuales para niños."));
    }

    private void setupCorporateFooter() {
        if (binding.layoutCorporateFooter != null) {
            CorporateFooterHelper.setupFooter(requireContext(), binding.layoutCorporateFooter);
        }
    }

    private void actualizarUIStateFiltros() {
        // Categorías
        boolean catCienciaActiva = filtroCategoriaSeleccionada.equalsIgnoreCase("Ciencia");
        boolean catNaturalezaActiva = filtroCategoriaSeleccionada.equalsIgnoreCase("Naturaleza");
        boolean catTecnologiaActiva = filtroCategoriaSeleccionada.equalsIgnoreCase("Tecnología");

        aplicarEstadoBoton(binding.btnCatCiencia, binding.tvLabelCatCiencia, "CIENCIA", catCienciaActiva);
        aplicarEstadoBoton(binding.btnCatNaturaleza, binding.tvLabelCatNaturaleza, "NATURALEZA", catNaturalezaActiva);
        aplicarEstadoBoton(binding.btnCatTecnologia, binding.tvLabelCatTecnologia, "TECNOLOGÍA", catTecnologiaActiva);

        // Modalidades
        boolean modPresencialActiva = filtroModalidadSeleccionada.equalsIgnoreCase("Presencial");
        boolean modVirtualActiva = filtroModalidadSeleccionada.equalsIgnoreCase("Virtual");

        aplicarEstadoBoton(binding.btnModPresencial, binding.tvLabelModPresencial, "PRESENCIAL", modPresencialActiva);
        aplicarEstadoBoton(binding.btnModVirtual, binding.tvLabelModVirtual, "VIRTUAL", modVirtualActiva);

        // Botón Ver Todos
        boolean nadaFiltrado = filtroCategoriaSeleccionada.equalsIgnoreCase("Todas") 
                && filtroModalidadSeleccionada.equalsIgnoreCase("Todas")
                && textoBusquedaActual.isEmpty();

        binding.btnFiltroVerTodos.setElevation(nadaFiltrado ? 6f : 2f);
        binding.tvTextoVerTodos.setText(nadaFiltrado ? "VER TODOS LOS TALLERES ✓" : "VER TODOS LOS TALLERES");
    }

    private void aplicarEstadoBoton(View contenedor, TextView textLabel, String baseText, boolean activo) {
        if (activo) {
            contenedor.animate().scaleX(1.04f).scaleY(1.04f).setDuration(150).start();
            contenedor.setElevation(8f);
            if (textLabel != null) {
                textLabel.setText(baseText + " ✓");
            }
        } else {
            contenedor.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start();
            contenedor.setElevation(2f);
            if (textLabel != null) {
                textLabel.setText(baseText + " →");
            }
        }
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
        textoBuilder.append(adapter != null ? adapter.getItemCount() : 12).append(" talleres");
        if (!cat.isEmpty() && !mod.isEmpty()) {
            textoBuilder.append(" de ").append(cat).append(" en modalidad ").append(mod);
        } else if (!cat.isEmpty()) {
            textoBuilder.append(" de ").append(cat);
        } else if (!mod.isEmpty()) {
            textoBuilder.append(" en modalidad ").append(mod);
        }
        
        binding.tvSubtituloFiltroDinamico.setText(textoBuilder.toString());
    }

    private void manejarDeepLinking() {
        if (getArguments() != null && getArguments().containsKey("taller_query")) {
            String query = getArguments().getString("taller_query", "").toLowerCase();
            if (!query.isEmpty()) {
                for (Taller t : listaTalleres) {
                    if (t.getId().equals(query) || t.getTituloCorto().toLowerCase().contains(query)) {
                        onVerDetalleClick(t);
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