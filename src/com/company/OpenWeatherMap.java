package com.company;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Recreational on 11/19/2017.
 */
public class OpenWeatherMap {
    //Variables
    public String requestor;
    private String currentJson;
    private String zipCode;
    private String city;

    //Weather blocks
    private WeatherBlock currentWeatherBlock;
    private ArrayList<WeatherBlock> forecastBlocks = new ArrayList<WeatherBlock>();
    private ArrayList<WeatherBlock> hourlyBlocks = new ArrayList<WeatherBlock>();

    //Getters and Setters
    public String GetCurrentJson(){
        return this.currentJson;
    }

    public void SetZip(String zip){
        this.zipCode = zip;
    }

    public void SetCity(String city){
        this.city = city;
    }

        //Weather map
    public WeatherBlock GetCurrentWeatherBlock(){
        return this.currentWeatherBlock;
    }

    public WeatherBlock GetForecastWeatherBlock(int index){
        return forecastBlocks.get(index);
    }

    public WeatherBlock GetHourlyWeatherBlock(int index){
        return hourlyBlocks.get(index);
    }

    //Constructors
    public OpenWeatherMap(){
        //Do nothing
    }

    //Funbctions and methods
    public void SetOpenWeatherMap(String requestFrom){
        currentWeatherBlock = new WeatherBlock();
        String currentJson = "";
        String forecastJson = "";

        try {
            //We want to get the json for the website api
            currentJson = SendRequestForJson("http://api.openweathermap.org/data/2.5/weather?" + requestFrom + "&APPID=5e6c77f1c3f8c18763bfc6154496a749");
            forecastJson = SendRequestForJson("http://api.openweathermap.org/data/2.5/forecast?" + requestFrom + "&APPID=5e6c77f1c3f8c18763bfc6154496a749");

            BufferedWriter weatherWriter = new BufferedWriter( new FileWriter("json.json"));
            weatherWriter.write(currentJson);
            weatherWriter.close();

            BufferedWriter writer = new BufferedWriter( new FileWriter("forecast.json"));
            writer.write(forecastJson);
            writer.close();

            this.requestor = requestFrom.split("=")[1];
        }catch(Exception e) {
            this.requestor = this.requestor + " (Cached)";

            try {
                // FileReader reads text files in the default encoding.
                FileReader fileReader = new FileReader("forecast.json");

                StringBuilder builder = new StringBuilder();
                String line;
                // Always wrap FileReader in BufferedReader.
                BufferedReader bufferedReader =
                        new BufferedReader(fileReader);

                while((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                }

                // Always close files.
                bufferedReader.close();
                fileReader.close();
                forecastJson = builder.toString();
            }
            catch(Exception ex) {
                System.out.println("Unable to open file'");
            }

            try {
                // FileReader reads text files in the default encoding.
                FileReader fileReader = new FileReader("json.json");

                StringBuilder builder = new StringBuilder();
                String line;
                // Always wrap FileReader in BufferedReader.
                BufferedReader bufferedReader =
                        new BufferedReader(fileReader);

                while((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                }

                // Always close files.
                bufferedReader.close();
                fileReader.close();
                currentJson = builder.toString();
            }
            catch(Exception ex) {
                System.out.println("Unable to open file'");
            }
        }

        JSONArray myArray = SeperateWeatherData(forecastJson);

        if(!myArray.isEmpty()) {
            //Go through and set 5 forecasts
            for (int day = 0; day < 5; day++) {
                WeatherBlock currentBlock = new WeatherBlock();
                SetWeatherData(currentBlock, myArray.get(day * 8).toString());
                forecastBlocks.add(currentBlock);
            }

            //Go through and set 8 hourlys
            for (int day = 0; day < 8; day++) {
                WeatherBlock currentBlock = new WeatherBlock();
                SetWeatherData(currentBlock, myArray.get(day).toString());
                hourlyBlocks.add(currentBlock);
            }
        }
        if(currentJson != ""){
            SetWeatherData(currentWeatherBlock, currentJson);
        }
    }

    //Functions methods
    private String SendRequestForJson(String request) throws Exception{
            URL url = new URL(request);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader inputReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();

            while ((inputLine = inputReader.readLine()) != null){
                content.append(inputLine);
            }

            inputReader.close();

            return content.toString();
    }

    //We need to set the data for the weather block
    private void SetWeatherData(WeatherBlock block, String Json){
//        System.out.println("Current Json" + Json);
        //We need to parse the JSON
        Reader reader;
        JSONObject object;
        JSONParser parser = new JSONParser();

        try {
            object = (JSONObject) parser.parse(Json);
            //Set all of the current weather data
            JSONObject main = (JSONObject)object.get("main");
            JSONObject wind = (JSONObject)object.get("wind");
            JSONObject system = (JSONObject)object.get("sys");

            block.SetTimeStamp(Long.parseLong(object.get("dt").toString()));
            block.SetTemperature(Double.parseDouble(main.get("temp").toString()));
            block.SetHumidity(Double.parseDouble(main.get("humidity").toString()));
            block.SetPressure(Double.parseDouble(main.get("pressure").toString()));
            block.SetWindSpeed(Double.parseDouble(wind.get("speed").toString()));

            if(system.get("sunrise") != null) {
                block.SetSunrise(Long.parseLong(system.get("sunrise").toString()));
                block.SetSunset(Long.parseLong(system.get("sunset").toString()));
            }
        }catch(Exception e){
            System.out.println("Object Errors suck: " + e.getMessage());
        }
    }

    private JSONArray SeperateWeatherData(String Json){

        Reader reader;
        JSONParser parser = new JSONParser();
        JSONArray listArray = new JSONArray();

        try {
            //Set all of the current weather data
            JSONObject list = (JSONObject)parser.parse(Json);
            if(list.get("list") != null) {
                listArray = (JSONArray) parser.parse(list.get("list").toString());
            }
        }catch(Exception e){
            System.out.println("Array Errors suck: " + e.getMessage());
        }

        return listArray;
    }
}
