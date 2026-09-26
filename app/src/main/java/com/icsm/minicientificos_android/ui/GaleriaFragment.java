package com.icsm.minicientificos_android.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.adapters.GaleriaAdapter;
import com.icsm.minicientificos_android.databinding.FragmentGaleriaBinding;
import com.icsm.minicientificos_android.models.GaleriaMock;

import java.util.List;

public class GaleriaFragment extends Fragment {

    private FragmentGaleriaBinding binding;
    private GaleriaAdapter adapter;

    public GaleriaFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentGaleriaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        setupChipFilter();
    }

    private void setupRecyclerView() {
        List<GaleriaMock> galeriaItems = GaleriaMock.getGaleriaPrueba();

        adapter = new GaleriaAdapter(galeriaItems, this::mostrarDetalleFotoDialog);

        binding.rvGaleria.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.rvGaleria.setAdapter(adapter);
    }

    private void setupChipFilter() {
        binding.chipGroupGaleria.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;

            int checkedId = checkedIds.get(0);
            String categoria = "Todos";

            if (checkedId == R.id.chip_fotocatalisis) {
                categoria = "Fotocatálisis";
            } else if (checkedId == R.id.chip_minibiohuertos) {
                categoria = "Minibiohuertos";
            } else if (checkedId == R.id.chip_minicompostaje) {
                categoria = "Minicompostaje";
            } else if (checkedId == R.id.chip_voluntariado) {
                categoria = "Voluntariado";
            }

            if (adapter != null) {
                adapter.filtrarPorCategoria(categoria);
                if (adapter.getItemCount() == 0) {
                    binding.tvGaleriaVacio.setVisibility(View.VISIBLE);
                } else {
                    binding.tvGaleriaVacio.setVisibility(View.GONE);
                }
            }
        });
    }

    private void mostrarDetalleFotoDialog(GaleriaMock item) {
        View dialogView = getLayoutInflater().inflate(R.layout.item_galeria, null, false);
        ImageView iv = dialogView.findViewById(R.id.iv_galeria_imagen);
        TextView tvTitulo = dialogView.findViewById(R.id.tv_galeria_titulo);
        TextView tvBadge = dialogView.findViewById(R.id.tv_galeria_badge);
        TextView tvFecha = dialogView.findViewById(R.id.tv_galeria_fecha);

        if (iv != null) iv.setImageResource(item.getImagenResId());
        if (tvTitulo != null) tvTitulo.setText(item.getTitulo());
        if (tvBadge != null) tvBadge.setText(item.getCategoria());
        if (tvFecha != null) tvFecha.setText(item.getFecha());

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("📸 " + item.getTitulo())
                .setMessage(item.getDescripcion() + "\n\nCategoría: " + item.getCategoria() + "\nFecha: " + item.getFecha())
                .setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}