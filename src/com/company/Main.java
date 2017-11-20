package com.company;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.Reader;

public class Main {

    public static void main(String[] args) {
        // write your code here
        OpenWeatherMap myWeatherMap = new OpenWeatherMap();
        new GuiMainScreen(myWeatherMap);
    }
}
