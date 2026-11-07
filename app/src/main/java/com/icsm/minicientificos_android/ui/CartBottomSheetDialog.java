package com.icsm.minicientificos_android.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.icsm.minicientificos_android.adapters.CartAdapter;
import com.icsm.minicientificos_android.cart.CartItem;
import com.icsm.minicientificos_android.cart.CartManager;
import com.icsm.minicientificos_android.databinding.BottomSheetCartBinding;

import java.util.List;
import java.util.Locale;

public class CartBottomSheetDialog extends BottomSheetDialogFragment implements CartManager.OnCartChangeListener {

    private BottomSheetCartBinding binding;
    private CartManager cartManager;
    private BottomSheetBehavior<FrameLayout> behavior;

    public CartBottomSheetDialog() {
        // Constructor vacío requerido
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bsd = (BottomSheetDialog) dialogInterface;
            FrameLayout bottomSheet = bsd.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                behavior.setSkipCollapsed(true);
            }
        });
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomSheetCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cartManager = CartManager.getInstance();
        cartManager.addListener(this);

        setupRecyclerView();
        setupButtons();
        actualizarUI();
    }

    private void setupRecyclerView() {
        List<CartItem> listaItems = cartManager.obtenerListaItems();
        binding.rvCartItems.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvCartItems.setAdapter(new CartAdapter(listaItems));
    }

    private void setupButtons() {
        binding.btnCloseCart.setOnClickListener(v -> dismiss());
        binding.btnSeguirComprando.setOnClickListener(v -> dismiss());

        binding.btnVaciarCarrito.setOnClickListener(v -> {
            if (cartManager.obtenerConteoTotal() == 0) return;
            new AlertDialog.Builder(requireContext())
                    .setTitle("Vaciar carrito")
                    .setMessage("¿Estás seguro de que deseas eliminar todos los productos del carrito?")
                    .setPositiveButton("Sí, vaciar", (dialog, which) -> cartManager.vaciarCarrito())
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        binding.btnVerCarritoCompleto.setOnClickListener(v -> {
            if (behavior != null) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        binding.btnContinuarCompraPaso2.setOnClickListener(v -> abrirCheckoutPaso2());
    }

    private void actualizarUI() {
        if (binding == null) return;

        List<CartItem> listaActual = cartManager.obtenerListaItems();
        int totalItems = cartManager.obtenerConteoTotal();
        int totalProductosUnicos = listaActual.size();
        double totalMonto = cartManager.obtenerMontoTotal();

        binding.rvCartItems.setAdapter(new CartAdapter(listaActual));

        if (totalItems == 0) {
            binding.llCartEmptyState.setVisibility(View.VISIBLE);
            binding.rvCartItems.setVisibility(View.GONE);
            binding.layoutCartFooter.setVisibility(View.GONE);
            binding.tvCartHeaderItemsBadge.setText("0 ítems");
            binding.tvCartHeaderSubtitle.setText("0 unidades en tu carrito · MiniCientificos");
        } else {
            binding.llCartEmptyState.setVisibility(View.GONE);
            binding.rvCartItems.setVisibility(View.VISIBLE);
            binding.layoutCartFooter.setVisibility(View.VISIBLE);

            String badgeText = totalProductosUnicos + (totalProductosUnicos == 1 ? " ítem" : " ítems");
            binding.tvCartHeaderItemsBadge.setText(badgeText);

            String subtitleText = totalItems + " " + (totalItems == 1 ? "unidad" : "unidades") + " en tu carrito · MiniCientificos";
            binding.tvCartHeaderSubtitle.setText(subtitleText);

            String countResumenText = "📦 " + totalProductosUnicos + " " + (totalProductosUnicos == 1 ? "producto" : "productos");
            binding.tvResumenConteoProductos.setText(countResumenText);

            String totalStr = String.format(Locale.US, "S/ %.2f", totalMonto);
            binding.tvCartSubtotalGeneral.setText(totalStr);
        }
    }

    @Override
    public void onCartChanged() {
        actualizarUI();
    }

    private void abrirCheckoutPaso2() {
        if (cartManager.obtenerConteoTotal() == 0) return;

        dismiss();
        if (getParentFragmentManager() != null) {
            CheckoutBottomSheetDialog checkoutDialog = new CheckoutBottomSheetDialog();
            checkoutDialog.show(getParentFragmentManager(), "CheckoutBottomSheet");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cartManager.removeListener(this);
        binding = null;
    }
}