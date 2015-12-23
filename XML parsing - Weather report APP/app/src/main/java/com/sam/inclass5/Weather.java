package com.sam.inclass5;

/**
 * InClass5
 * Sam Painter and Praveen Suenani
 * Weather.java
 */
public class Weather {

    private String temperature, humidity, pressure, windSpeed, windDirection, clouds, precipitation, symbol;

    public Weather(String temperature, String humidity, String pressure, String windSpeed, String windDirection, String clouds, String precipitation, String symbol) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.clouds = clouds;
        this.precipitation = precipitation;
        this.symbol = symbol;
    }

    public Weather() {
    }

    public String getTemperature() {

        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }


    public String getPressure() {
        return pressure;

    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getClouds() {
        return clouds;
    }

    public void setClouds(String clouds) {
        this.clouds = clouds;
    }

    public String getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(String precipitation) {
        if (precipitation == null) {
            this.precipitation = "No";
            return;
        }
        this.precipitation = precipitation;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
