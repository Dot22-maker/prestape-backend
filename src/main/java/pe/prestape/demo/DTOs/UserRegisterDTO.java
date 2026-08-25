package pe.prestape.demo.DTOs;

import java.time.LocalDate;

public class UserRegisterDTO {
    private String username;
    private String password;
    private String nombreCompleto;
    private String email;
    private String provincia;
    private String distrito;
    private LocalDate fechaNacimiento;
    private String dni;
    private String telefono;

    public UserRegisterDTO() {
    }

    public UserRegisterDTO(String username, String password, String nombreCompleto, String email, String provincia, String distrito, LocalDate fechaNacimiento, String dni, String telefono) {
        this.username = username;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.provincia = provincia;
        this.distrito = distrito;
        this.fechaNacimiento = fechaNacimiento;
        this.dni = dni;
        this.telefono = telefono;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}