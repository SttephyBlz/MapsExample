package com.sttephyblz.mapexample.events;

/**
 * Created by Sttephy on 22/02/2017.
 */

public class FusedLocationEvent {
    private String message ="";
    private Double lat = -0.0;
    private Double lng = -0.0;

    public FusedLocationEvent(String message, Double lat, Double lng) {
        this.message = message;
        this.lat = lat;
        this.lng = lng;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }
}
