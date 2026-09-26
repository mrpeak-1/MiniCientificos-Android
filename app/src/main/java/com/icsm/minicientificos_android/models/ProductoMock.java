package com.icsm.minicientificos_android.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.icsm.minicientificos_android.R;

public class ProductoMock implements Serializable {
    private int id;
    private String nombre;
    private String categoria; // "Kits", "Accesorios", "Ropa", "Ecológico"
    private double precio;
    private String disponibilidad; // "En Stock", "Pocas Unidades", "Agotado"
    private int imagenResId;
    private String descripcion;

    public ProductoMock(int id, String nombre, String categoria, double precio,
                        String disponibilidad, int imagenResId, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.disponibilidad = disponibilidad;
        this.imagenResId = imagenResId;
        this.descripcion = descripcion;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public double getPrecio() { return precio; }
    public String getPrecioFormateado() { return String.format(java.util.Locale.US, "S/ %.2f", precio); }
    public String getDisponibilidad() { return disponibilidad; }
    public int getImagenResId() { return imagenResId; }
    public String getDescripcion() { return descripcion; }

    public static List<ProductoMock> getProductosPrueba() {
        List<ProductoMock> lista = new ArrayList<>();

        lista.add(new ProductoMock(
                101,
                "Kit Bobina Tesla",
                "Kits",
                65.00,
                "En Stock",
                R.drawable.ic_tesla_avatar,
                "Generador de micro arcos eléctricos seguro para experimentos escolares."
        ));

        lista.add(new ProductoMock(
                102,
                "Minibiohuerto Kit",
                "Ecológico",
                45.00,
                "En Stock",
                R.drawable.ic_flask,
                "Macetas biodegradables, semillas de huerta orgánica y tierra preparada."
        ));

        lista.add(new ProductoMock(
                103,
                "Camiseta Mini Científicos",
                "Ropa",
                35.00,
                "En Stock",
                R.drawable.ic_titan_avatar,
                "Polera 100% algodón suave con diseño exclusivo de Titan y Tesla."
        ));

        lista.add(new ProductoMock(
                104,
                "Lupa de Explorador Científico",
                "Accesorios",
                25.00,
                "Pocas Unidades",
                R.drawable.ic_flask,
                "Lupa ergonómica de 5x con luz LED integrada para exploración de campo."
        ));

        return lista;
    }
}