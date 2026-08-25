package pe.prestape.demo.DTOs;

public class PrestamosPorClienteDTO {

    private String nombreCliente;
    private Long cantidadPrestamos;

    public PrestamosPorClienteDTO() {
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public Long getCantidadPrestamos() {
        return cantidadPrestamos;
    }

    public void setCantidadPrestamos(Long cantidadPrestamos) {
        this.cantidadPrestamos = cantidadPrestamos;
    }
}
