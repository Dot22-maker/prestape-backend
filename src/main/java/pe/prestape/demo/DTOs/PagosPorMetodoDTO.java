package pe.prestape.demo.DTOs;

public class PagosPorMetodoDTO {

    private String metodoPago;
    private Long cantidadPagos;
    private Double montoTotal; // <-- Nuevo campo

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Long getCantidadPagos() {
        return cantidadPagos;
    }

    public void setCantidadPagos(Long cantidadPagos) {
        this.cantidadPagos = cantidadPagos;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }
}