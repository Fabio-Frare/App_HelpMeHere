package br.com.udesc.helpmehere;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Atendimento {
    private String data;
    private String hora;
    private Double latitude;
    private Double longitude;
    private String cidade;
    private String estado;
    private String pais;
    private String foto;


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "Atendimento{" +
                "data='" + data + '\'' +
                ", hora='" + hora + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                ", pais='" + pais + '\'' +
                ", foto='" + foto + '\'' +
                '}';
    }
}
