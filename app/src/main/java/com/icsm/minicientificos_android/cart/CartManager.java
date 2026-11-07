package com.icsm.minicientificos_android.cart;

import com.icsm.minicientificos_android.models.Producto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartManager {

    private static CartManager instance;
    private final List<CartItem> items;
    private final List<OnCartChangeListener> listeners;

    private String codigoCuponActivo;
    private double porcentajeDescuento;
    private double montoDescuentoFijo;

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    private CartManager() {
        items = new ArrayList<>();
        listeners = new ArrayList<>();
        codigoCuponActivo = null;
        porcentajeDescuento = 0.0;
        montoDescuentoFijo = 0.0;
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addListener(OnCartChangeListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(OnCartChangeListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    private void notifyListeners() {
        for (OnCartChangeListener listener : listeners) {
            listener.onCartChanged();
        }
    }

    public void agregarProducto(Producto p) {
        for (CartItem item : items) {
            if (item.getProducto().getId().equals(p.getId())) {
                item.setCantidad(item.getCantidad() + 1);
                notifyListeners();
                return;
            }
        }
        items.add(new CartItem(p, 1));
        notifyListeners();
    }

    public void quitarProducto(String productoId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getProducto().getId().equals(productoId)) {
                items.remove(i);
                notifyListeners();
                break;
            }
        }
    }

    public void actualizarCantidad(String productoId, int nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            quitarProducto(productoId);
            return;
        }

        for (CartItem item : items) {
            if (item.getProducto().getId().equals(productoId)) {
                item.setCantidad(nuevaCantidad);
                notifyListeners();
                break;
            }
        }
    }

    public void vaciarCarrito() {
        items.clear();
        removerCupon();
    }

    public int obtenerConteoTotal() {
        int total = 0;
        for (CartItem item : items) {
            total += item.getCantidad();
        }
        return total;
    }

    public double obtenerMontoTotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public List<CartItem> obtenerListaItems() {
        return new ArrayList<>(items);
    }

    // ==========================================
    // MOTOR OFICIAL DE CUPONES (FASE 4)
    // ==========================================

    public boolean aplicarCupon(String codigo) {
        if (codigo == null) return false;
        String codigoLimpio = codigo.trim().toUpperCase(Locale.US);

        if (codigoLimpio.equals("MINI10")) {
            codigoCuponActivo = "MINI10";
            porcentajeDescuento = 0.10; // 10%
            montoDescuentoFijo = 0.0;
            notifyListeners();
            return true;
        } else if (codigoLimpio.equals("CIENCIA20")) {
            codigoCuponActivo = "CIENCIA20";
            porcentajeDescuento = 0.20; // 20%
            montoDescuentoFijo = 0.0;
            notifyListeners();
            return true;
        } else if (codigoLimpio.equals("EXPLORADOR15")) {
            codigoCuponActivo = "EXPLORADOR15";
            porcentajeDescuento = 0.15; // 15%
            montoDescuentoFijo = 0.0;
            notifyListeners();
            return true;
        }

        return false;
    }

    public void removerCupon() {
        codigoCuponActivo = null;
        porcentajeDescuento = 0.0;
        montoDescuentoFijo = 0.0;
        notifyListeners();
    }

    public String getCodigoCuponActivo() {
        return codigoCuponActivo;
    }

    public double obtenerDescuentoCalculado() {
        if (codigoCuponActivo == null) return 0.0;
        double subtotal = obtenerMontoTotal();
        double descuento = (subtotal * porcentajeDescuento) + montoDescuentoFijo;
        return Math.min(subtotal, descuento);
    }

    public double obtenerTotalConDescuento() {
        double subtotal = obtenerMontoTotal();
        double descuento = obtenerDescuentoCalculado();
        return Math.max(0.0, subtotal - descuento);
    }
}