package com.icsm.minicientificos_android.ui;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.icsm.minicientificos_android.adapters.CheckoutResumenAdapter;
import com.icsm.minicientificos_android.cart.CartItem;
import com.icsm.minicientificos_android.cart.CartManager;
import com.icsm.minicientificos_android.databinding.BottomSheetCheckoutEnvioBinding;
import com.icsm.minicientificos_android.models.DatosEnvioCliente;
import com.icsm.minicientificos_android.utils.CheckoutStorageHelper;
import com.icsm.minicientificos_android.utils.WhatsAppHelper;

import java.util.List;
import java.util.Locale;

public class CheckoutBottomSheetDialog extends BottomSheetDialogFragment implements CartManager.OnCartChangeListener {

    private BottomSheetCheckoutEnvioBinding binding;
    private CartManager cartManager;

    private static final String[] DEPARTAMENTOS_PERU = new String[]{
            "Amazonas", "Áncash", "Apurímac", "Arequipa", "Ayacucho",
            "Cajamarca", "Callao", "Cusco", "Huancavelica", "Huánuco",
            "Ica", "Junín", "La Libertad", "Lambayeque", "Lima",
            "Loreto", "Madre de Dios", "Moquegua", "Pasco", "Piura",
            "Puno", "San Martín", "Tacna", "Tumbes", "Ucayali"
    };

    public CheckoutBottomSheetDialog() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomSheetCheckoutEnvioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cartManager = CartManager.getInstance();
        cartManager.addListener(this);

        setupDropdownDepartamento();
        cargarDatosGuardados();
        setupRecyclerViewResumen();
        actualizarMontosYCuponUI();

        binding.btnCerrarCheckout.setOnClickListener(v -> dismiss());

        binding.btnAplicarCupon.setOnClickListener(v -> aplicarCuponIngresado());
        binding.btnQuitarCupon.setOnClickListener(v -> quitarCuponActivo());

        binding.btnConfirmarPedidoFinal.setOnClickListener(v -> procesarEnvioYFinalizar());

        binding.btnVolverAlCarrito.setOnClickListener(v -> volverAlCarrito());
    }

    private void setupDropdownDepartamento() {
        if (getContext() == null) return;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                DEPARTAMENTOS_PERU
        );
        binding.actvCheckoutDepartamento.setAdapter(adapter);
    }

    private void cargarDatosGuardados() {
        if (getContext() == null) return;
        DatosEnvioCliente guardados = CheckoutStorageHelper.obtenerDatosEnvioGuardados(requireContext());
        if (guardados != null && guardados.isRecordarDatos()) {
            binding.etCheckoutNombre.setText(guardados.getNombreCompleto());
            binding.etCheckoutCelular.setText(guardados.getCelular());
            binding.etCheckoutCorreo.setText(guardados.getCorreo());
            binding.etCheckoutDireccion.setText(guardados.getDireccion());
            binding.etCheckoutDistrito.setText(guardados.getDistrito());
            binding.etCheckoutProvincia.setText(guardados.getProvincia());
            binding.actvCheckoutDepartamento.setText(guardados.getDepartamento(), false);
            binding.cbRecordarDatos.setChecked(true);
        }
    }

    private void setupRecyclerViewResumen() {
        List<CartItem> lista = cartManager.obtenerListaItems();
        CheckoutResumenAdapter adapter = new CheckoutResumenAdapter(lista);
        binding.rvCheckoutResumenItems.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvCheckoutResumenItems.setAdapter(adapter);
    }

    private void actualizarMontosYCuponUI() {
        double subtotal = cartManager.obtenerMontoTotal();
        double descuento = cartManager.obtenerDescuentoCalculado();
        double totalFinal = cartManager.obtenerTotalConDescuento();
        String cuponActivo = cartManager.getCodigoCuponActivo();

        int totalProductos = cartManager.obtenerConteoTotal();
        binding.tvCheckoutSubtotalLabel.setText(String.format(Locale.US, "Subtotal (%d productos)", totalProductos));
        binding.tvCheckoutSubtotalValor.setText(String.format(Locale.US, "S/ %.2f", subtotal));

        if (cuponActivo != null && descuento > 0) {
            binding.llCheckoutFilaDescuento.setVisibility(View.VISIBLE);
            binding.tvCheckoutDescuentoValor.setText(String.format(Locale.US, "- S/ %.2f", descuento));
            binding.tvFeedbackCupon.setText(String.format(Locale.US, "✓ ¡Cupón %s aplicado con éxito! (-S/ %.2f)", cuponActivo, descuento));
            binding.tvFeedbackCupon.setTextColor(Color.parseColor("#15803D"));
            binding.btnQuitarCupon.setVisibility(View.VISIBLE);
        } else {
            binding.llCheckoutFilaDescuento.setVisibility(View.GONE);
            binding.btnQuitarCupon.setVisibility(View.GONE);
            if (binding.etCodigoCupon.getText() == null || binding.etCodigoCupon.getText().toString().trim().isEmpty()) {
                binding.tvFeedbackCupon.setText("Prueba: MINI10, CIENCIA20 o EXPLORADOR15");
                binding.tvFeedbackCupon.setTextColor(Color.parseColor("#64748B"));
            }
        }

        binding.tvCheckoutTotalEstimado.setText(String.format(Locale.US, "S/ %.2f", totalFinal));
    }

    private void aplicarCuponIngresado() {
        String codigo = binding.etCodigoCupon.getText() != null ? binding.etCodigoCupon.getText().toString().trim() : "";
        if (codigo.isEmpty()) {
            sacudirVista(binding.etCodigoCupon);
            Toast.makeText(requireContext(), "Ingresa un código de cupón", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean exito = cartManager.aplicarCupon(codigo);
        if (exito) {
            Toast.makeText(requireContext(), "¡Cupón aplicado!", Toast.LENGTH_SHORT).show();
        } else {
            binding.tvFeedbackCupon.setText("❌ Cupón inválido. Prueba MINI10, CIENCIA20 o EXPLORADOR15");
            binding.tvFeedbackCupon.setTextColor(Color.parseColor("#EF4444"));
            sacudirVista(binding.etCodigoCupon);
        }
    }

    private void quitarCuponActivo() {
        cartManager.removerCupon();
        binding.etCodigoCupon.setText("");
        Toast.makeText(requireContext(), "Cupón removido", Toast.LENGTH_SHORT).show();
    }

    private void procesarEnvioYFinalizar() {
        limpiarErrores();

        String nombre = getTexto(binding.etCheckoutNombre);
        String celular = getTexto(binding.etCheckoutCelular);
        String correo = getTexto(binding.etCheckoutCorreo);
        String direccion = getTexto(binding.etCheckoutDireccion);
        String distrito = getTexto(binding.etCheckoutDistrito);
        String provincia = getTexto(binding.etCheckoutProvincia);
        String departamento = binding.actvCheckoutDepartamento.getText() != null ? binding.actvCheckoutDepartamento.getText().toString().trim() : "";

        if (nombre.isEmpty()) {
            binding.tilCheckoutNombre.setError("Ingresa tu nombre completo");
            sacudirVista(binding.tilCheckoutNombre);
            return;
        }

        if (celular.isEmpty()) {
            binding.tilCheckoutCelular.setError("Ingresa tu número de celular");
            sacudirVista(binding.tilCheckoutCelular);
            return;
        }

        if (!celular.matches("^9\\d{8}$")) {
            binding.tilCheckoutCelular.setError("Debe ser un número válido de 9 dígitos (ej. 987654321)");
            sacudirVista(binding.tilCheckoutCelular);
            return;
        }

        if (direccion.isEmpty()) {
            binding.tilCheckoutDireccion.setError("Ingresa la dirección de envío");
            sacudirVista(binding.tilCheckoutDireccion);
            return;
        }

        if (distrito.isEmpty()) {
            binding.tilCheckoutDistrito.setError("Ingresa tu distrito");
            sacudirVista(binding.tilCheckoutDistrito);
            return;
        }

        if (provincia.isEmpty()) {
            binding.tilCheckoutProvincia.setError("Ingresa tu provincia");
            sacudirVista(binding.tilCheckoutProvincia);
            return;
        }

        if (departamento.isEmpty()) {
            binding.tilCheckoutDepartamento.setError("Selecciona tu departamento");
            sacudirVista(binding.tilCheckoutDepartamento);
            return;
        }

        boolean recordar = binding.cbRecordarDatos.isChecked();
        DatosEnvioCliente envio = new DatosEnvioCliente(
                nombre, celular, correo, direccion, distrito, provincia, departamento, recordar
        );

        CheckoutStorageHelper.guardarDatosEnvio(requireContext(), envio);

        List<CartItem> items = cartManager.obtenerListaItems();
        double subtotal = cartManager.obtenerMontoTotal();
        String cupon = cartManager.getCodigoCuponActivo();
        double descuento = cartManager.obtenerDescuentoCalculado();
        double totalFinal = cartManager.obtenerTotalConDescuento();

        WhatsAppHelper.abrirPedidoCheckoutCompleto(
                requireContext(), items, subtotal, cupon, descuento, totalFinal, envio
        );

        dismiss();
    }

    private void volverAlCarrito() {
        dismiss();
        if (getParentFragmentManager() != null) {
            CartBottomSheetDialog cartDialog = new CartBottomSheetDialog();
            cartDialog.show(getParentFragmentManager(), "CartBottomSheet");
        }
    }

    private String getTexto(EditText editText) {
        return editText != null && editText.getText() != null ? editText.getText().toString().trim() : "";
    }

    private void limpiarErrores() {
        binding.tilCheckoutNombre.setError(null);
        binding.tilCheckoutCelular.setError(null);
        binding.tilCheckoutCorreo.setError(null);
        binding.tilCheckoutDireccion.setError(null);
        binding.tilCheckoutDistrito.setError(null);
        binding.tilCheckoutProvincia.setError(null);
        binding.tilCheckoutDepartamento.setError(null);
    }

    private void sacudirVista(View view) {
        TranslateAnimation shake = new TranslateAnimation(0, 14, 0, 0);
        shake.setDuration(350);
        shake.setInterpolator(new CycleInterpolator(3));
        view.startAnimation(shake);
        view.requestFocus();
    }

    @Override
    public void onCartChanged() {
        actualizarMontosYCuponUI();
        if (binding != null && binding.rvCheckoutResumenItems.getAdapter() != null) {
            binding.rvCheckoutResumenItems.setAdapter(new CheckoutResumenAdapter(cartManager.obtenerListaItems()));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cartManager.removeListener(this);
        binding = null;
    }
}