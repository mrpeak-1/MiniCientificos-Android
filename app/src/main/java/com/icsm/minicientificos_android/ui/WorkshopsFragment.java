package com.icsm.minicientificos_android.ui;

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
import com.icsm.minicientificos_android.models.TallerMock;

import java.util.List;

public class WorkshopsFragment extends Fragment implements TallerAdapter.OnTallerClickListener {

    private FragmentWorkshopsBinding binding;
    private TallerAdapter adapter;
    private String filtroCategoriaSeleccionada = "Todas";
    private String textoBusquedaActual = "";

    public WorkshopsFragment() {
        // Required empty public constructor
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

        // Cargar talleres de prueba
        List<TallerMock> listaTalleres = TallerMock.getTalleresPrueba();

        // Configurar Adapter y RecyclerView
        adapter = new TallerAdapter(listaTalleres, this);
        binding.rvTalleres.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvTalleres.setAdapter(adapter);

        // Configurar Búsqueda
        binding.etSearchTalleres.addTextChangedListener(new TextWatcher() {
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

        // Configurar Chips de Filtro
        binding.chipGroupTalleres.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                filtroCategoriaSeleccionada = "Todas";
            } else {
                int selectedId = checkedIds.get(0);
                Chip chip = group.findViewById(selectedId);
                if (chip != null) {
                    filtroCategoriaSeleccionada = chip.getText().toString();
                } else {
                    filtroCategoriaSeleccionada = "Todas";
                }
            }
            filtrarLista();
        });
    }

    private void filtrarLista() {
        if (adapter != null) {
            adapter.aplicarFiltros(textoBusquedaActual, filtroCategoriaSeleccionada);
            actualizarEstadoVacio();
        }
    }

    private void actualizarEstadoVacio() {
        if (adapter.getItemCount() == 0) {
            binding.tvEmptyTalleres.setVisibility(View.VISIBLE);
            binding.rvTalleres.setVisibility(View.GONE);
        } else {
            binding.tvEmptyTalleres.setVisibility(View.GONE);
            binding.rvTalleres.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onVerDetalleClick(TallerMock taller) {
        TallerDetalleBottomSheet bottomSheet = TallerDetalleBottomSheet.newInstance(taller);
        bottomSheet.show(getChildFragmentManager(), "TallerDetalleBottomSheet");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}