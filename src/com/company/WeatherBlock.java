package com.company;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Recreational on 11/19/2017.
 */
public class WeatherBlock {
    //Variables
    private long timeStamp; //Unix timestamp
    private double temperature; //In fahrenheit
    private double windSpeed;
    private double pressure;
    private double humidity;
    private long sunrise;   //Unix timestamp
    private long sunset;    //Unix timestamp

    //Setters and Getters
        //Timestamp
    public void SetTimeStamp(long timeStamp){
        this.timeStamp = timeStamp * 1000;
    }
    public long GetTimeStamp(){
        return this.timeStamp;
    }
        //Temperature
    public void SetTemperature(Double temperature){
        //Convert from kelvin to fahrenheit
        this.temperature = ((9.0/5.0) * (temperature - 273) + 32);
    }
    public double GetTemperature(){
        return this.temperature;
    }
        //Wind speed
    public double GetWindSpeed(){
        return this.windSpeed;
    }
    public void SetWindSpeed(double windSpeed){
        this.windSpeed = windSpeed * 2.2369;
    }

        //Pressure
    public double GetPressure(){
        return this.pressure;
    }
    public void SetPressure(double pressure){
        this.pressure = pressure;
    }

        //Humidity
    public double GetHumidity(){
        return this.humidity;
    }
    public void SetHumidity(double humidity){
        this.humidity = humidity;
    }

        //Sunrise
    public long GetSunrise(){
        return this.sunrise;
    }
    public void SetSunrise(long sunrise){
        this.sunrise = sunrise * 1000;
    }

        //Sunset
    public long GetSunset(){
        return this.sunset;
    }
    public void SetSunset(long sunset){
        this.sunset = sunset * 1000;
    }

    //Constructors
    public WeatherBlock(){
        //Do nothing
    }

    public JPanel GetGuiContent(){
        JPanel newPanel = new JPanel();
//        newPanel.setPreferredSize(new Dimension(200, 100));
        newPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        GridBagConstraints constraints = new GridBagConstraints();
        GridBagLayout layout = new GridBagLayout();
        newPanel.setLayout(layout);

        Calendar calendar = Calendar.getInstance();
        Date myDate = new Date(this.timeStamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat( "dd-MMM-yyyy HH:mm:ss");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        JLabel currentTimeLabel = new JLabel(dateFormat.format(timeStamp));
        JLabel currentWeatherLabel = new JLabel("Weather for: " + dateFormat.format(timeStamp));
        JLabel temperatureLabel = new JLabel("Temperature " + String.format("%.2f", this.temperature) + "F");
        JLabel pressureLabel = new JLabel("Pressure " + String.format("%.0f", this.pressure) + "hpa");
        JLabel windLabel = new JLabel("Wind Speed " + String.format("%.1f", this.windSpeed) + "mph");
        JLabel humidityLabel = new JLabel("Humidity " + this.humidity + "%");
        JLabel sunriseLabel = new JLabel("Sunrise " + hourFormat.format(this.sunrise));
        JLabel sunsetLabel = new JLabel("Sunset " + hourFormat.format(this.sunset));


        constraints.gridx = 0; constraints.gridy = 0; constraints.ipady = 10;
        newPanel.add(currentWeatherLabel, constraints);
        constraints.gridx = 0; constraints.gridy = 1;
        newPanel.add(temperatureLabel, constraints);
        constraints.gridx = 0; constraints.gridy = 2;
        newPanel.add(humidityLabel, constraints);
        constraints.gridx = 0; constraints.gridy = 3;
        newPanel.add(pressureLabel, constraints);
        constraints.gridx = 0; constraints.gridy = 4;
        newPanel.add(windLabel, constraints);
//        constraints.gridx = 0; constraints.gridy = 7;
//        newPanel.add(currentTimeLabel, constraints);

        if(sunrise != 0 || sunset != 0) {
            constraints.gridx = 0;
            constraints.gridy = 5;
            newPanel.add(sunriseLabel, constraints);
            constraints.gridx = 0;
            constraints.gridy = 6;
            newPanel.add(sunsetLabel, constraints);
        }

        return newPanel;
    }
}
