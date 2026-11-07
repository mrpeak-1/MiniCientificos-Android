package com.icsm.minicientificos_android.ui;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.button.MaterialButton;
import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.adapters.ProductoAdapter;
import com.icsm.minicientificos_android.cart.CartManager;
import com.icsm.minicientificos_android.databinding.FragmentShopBinding;
import com.icsm.minicientificos_android.models.Producto;
import com.icsm.minicientificos_android.models.ProductoCatalogData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ShopFragment extends Fragment implements ProductoAdapter.OnProductClickListener, CartManager.OnCartChangeListener {

    private FragmentShopBinding binding;
    private ProductoAdapter adapter;
    private List<Producto> masterList;
    private CartManager cartManager;

    private String currentQuery = "";
    private String currentCategory = "Todos";
    private int currentSortMode = 0;

    public ShopFragment() {
        // Constructor vacío requerido
    }

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

        masterList = ProductoCatalogData.getCatalog();
        cartManager = CartManager.getInstance();
        cartManager.addListener(this);

        setupRecyclerView();
        setupFilters();
        setupSearch();
        setupSorting();
        setupCartButton();

        updateCartBadge(false);
        applyFilters();
    }

    private void setupRecyclerView() {
        adapter = new ProductoAdapter(this);
        binding.rvProductos.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.rvProductos.setAdapter(adapter);
    }

    private void setupSearch() {
        binding.etSearchShop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentQuery = s.toString().trim().toLowerCase();
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.btnShopBuscarAccion.setOnClickListener(v -> applyFilters());
    }

    private void setupFilters() {
        binding.chipTodos.setChecked(true);

        binding.cgShopFilters.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                currentCategory = "Todos";
                return;
            }
            int id = checkedIds.get(0);
            if (id == R.id.chip_todos) currentCategory = "Todos";
            else if (id == R.id.chip_peluches) currentCategory = "Peluches";
            else if (id == R.id.chip_accesorios) currentCategory = "Accesorios";
            else if (id == R.id.chip_ropa) currentCategory = "Ropa";
            else if (id == R.id.chip_papeleria) currentCategory = "Papeleria";

            applyFilters();
        });
    }

    private void setupSorting() {
        binding.tvOrdenar.setOnClickListener(v -> {
            currentSortMode = (currentSortMode + 1) % 4;
            String text = "Ordenar por: Más relevantes ▾";
            if (currentSortMode == 0) text = "Ordenar por: Más relevantes ▾";
            else if (currentSortMode == 1) text = "Nombre (A-Z) ▾";
            else if (currentSortMode == 2) text = "Menor precio ▾";
            else if (currentSortMode == 3) text = "Mayor precio ▾";

            binding.tvOrdenar.setText(text);
            applyFilters();
        });
    }

    private void setupCartButton() {
        binding.layoutMascotCartButton.setOnClickListener(v -> {
            CartBottomSheetDialog dialog = new CartBottomSheetDialog();
            dialog.show(getChildFragmentManager(), "CartBottomSheetDialog");
        });
    }

    private void updateCartBadge(boolean animate) {
        if (binding == null) return;
        int count = cartManager.obtenerConteoTotal();
        binding.tvShopCartBadgeCount.setText(String.valueOf(count));

        if (animate && count > 0) {
            ScaleAnimation anim = new ScaleAnimation(
                    1.0f, 1.4f, 1.0f, 1.4f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f
            );
            anim.setDuration(250);
            anim.setRepeatCount(1);
            anim.setRepeatMode(ScaleAnimation.REVERSE);
            binding.tvShopCartBadgeCount.startAnimation(anim);
        }
    }

    private void applyFilters() {
        List<Producto> filteredList = new ArrayList<>();

        for (Producto p : masterList) {
            boolean matchesCategory = currentCategory.equals("Todos") || p.getCategoria().equalsIgnoreCase(currentCategory);
            boolean matchesQuery = p.getNombre().toLowerCase().contains(currentQuery) ||
                                   p.getDescripcion().toLowerCase().contains(currentQuery);

            if (matchesCategory && matchesQuery) {
                filteredList.add(p);
            }
        }

        if (currentSortMode == 1) {
            Collections.sort(filteredList, (p1, p2) -> p1.getNombre().compareToIgnoreCase(p2.getNombre()));
        } else if (currentSortMode == 2) {
            Collections.sort(filteredList, Comparator.comparingDouble(Producto::getPrecio));
        } else if (currentSortMode == 3) {
            Collections.sort(filteredList, (p1, p2) -> Double.compare(p2.getPrecio(), p1.getPrecio()));
        }

        adapter.updateList(filteredList);

        binding.tvResultadosCount.setText(filteredList.size() + " productos encontrados");

        if (filteredList.isEmpty()) {
            binding.rvProductos.setVisibility(View.GONE);
            binding.layoutEmptyStateContainer.setVisibility(View.VISIBLE);
        } else {
            binding.rvProductos.setVisibility(View.VISIBLE);
            binding.layoutEmptyStateContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCartChanged() {
        updateCartBadge(true);
    }

    @Override
    public void onProductClick(Producto producto) {
        ProductoDetalleBottomSheet bottomSheet = new ProductoDetalleBottomSheet(producto);
        bottomSheet.show(getChildFragmentManager(), bottomSheet.getTag());
    }

    @Override
    public void onAddToCart(Producto producto, MaterialButton btn) {
        String originalText = btn.getText().toString();
        ColorStateList originalColor = btn.getBackgroundTintList();

        btn.setText("¡Listo! ✓");
        btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#25D366")));

        cartManager.agregarProducto(producto);
        CustomTopSnackbar.show(requireActivity(), producto.getNombre());

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isAdded() && btn != null) {
                btn.setText(originalText);
                btn.setBackgroundTintList(originalColor);
            }
        }, 1200);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cartManager.removeListener(this);
        binding = null;
    }
}