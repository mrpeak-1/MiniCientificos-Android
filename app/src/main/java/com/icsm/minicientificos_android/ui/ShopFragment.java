package com.icsm.minicientificos_android.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.chip.Chip;
import com.icsm.minicientificos_android.adapters.ProductoAdapter;
import com.icsm.minicientificos_android.databinding.FragmentShopBinding;
import com.icsm.minicientificos_android.models.ProductoMock;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ShopFragment extends Fragment implements ProductoAdapter.OnProductoClickListener {

    private FragmentShopBinding binding;
    private ProductoAdapter adapter;
    private String categoriaSeleccionada = "Todas";
    private String textoBusqueda = "";

    public ShopFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentShopBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<ProductoMock> productos = ProductoMock.getProductosPrueba();

        adapter = new ProductoAdapter(productos, this);
        binding.rvProductos.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.rvProductos.setAdapter(adapter);

        binding.etSearchTienda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textoBusqueda = s.toString();
                filtrar();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.chipGroupTienda.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                categoriaSeleccionada = "Todas";
            } else {
                int selectedId = checkedIds.get(0);
                Chip chip = group.findViewById(selectedId);
                categoriaSeleccionada = chip != null ? chip.getText().toString() : "Todas";
            }
            filtrar();
        });
    }

    private void filtrar() {
        if (adapter != null) {
            adapter.filtrar(textoBusqueda, categoriaSeleccionada);
            if (adapter.getItemCount() == 0) {
                binding.tvEmptyProductos.setVisibility(View.VISIBLE);
                binding.rvProductos.setVisibility(View.GONE);
            } else {
                binding.tvEmptyProductos.setVisibility(View.GONE);
                binding.rvProductos.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onComprarClick(ProductoMock producto) {
        try {
            String mensaje = "Hola Mini Científicos, deseo consultar/comprar el producto: *"
                    + producto.getNombre() + "* (" + producto.getPrecioFormateado() + ").";
            String url = "https://api.whatsapp.com/send?phone=51930754024&text="
                    + URLEncoder.encode(mensaje, StandardCharsets.UTF_8.name());
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}