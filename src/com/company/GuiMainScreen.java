package com.company;

import com.sun.deploy.panel.ExceptionListDialog;
import javafx.scene.control.ComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by Recreational on 11/6/2017.
 */
public class GuiMainScreen implements ActionListener{
    //Variables
    private OpenWeatherMap weatherMap;
    private JFrame frame;
    private JPanel gridPanel;
    private GridBagConstraints gridConstraints = new GridBagConstraints();
    private ArrayList<String> favorites = new ArrayList<>();

    //Text area
    private JTextField locationTextField;
    private JComboBox locationDropDown;


    public GuiMainScreen(OpenWeatherMap weatherMap){
        String text = "";

        this.weatherMap = weatherMap;
        SetupFavorites();
        //Check if we have a default
        if(favorites.size() > 0) {
            try {
                Integer.parseInt(favorites.get(0));
                text = "zip=" + favorites.get(0);
            } catch (Exception e) {
                text = "q=" + favorites.get(0);
            }
            weatherMap.SetOpenWeatherMap(text);
        }
        SetupFrame();   //Setup the default for our JFrame

        this.Repaint();
    }

    private void ResetWeather(String request){
        JFrame lastFrame = frame;
        SetupFavorites();
        weatherMap.SetOpenWeatherMap(request);
        SetupFrame();
        Repaint();

        lastFrame.setVisible(false);
        lastFrame = null;
    }

    public void Repaint(){
        frame.repaint();
        frame.setVisible(true); //We need to be able to see our frame
        frame.pack();
    }

    private void SetupFrame(){
        frame = new JFrame();    //Setup or frame
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridBagLayout());

        frame.setTitle("Derek's Weather Application");  //Set the title of the frame/window
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   //Set our default close action

        gridPanel.setLayout(new GridBagLayout());
        frame.add(gridPanel);

        GridBagConstraints weatherConstraints = new GridBagConstraints();
        weatherConstraints.gridx = 0; weatherConstraints.gridy = 0;
        weatherConstraints.gridwidth = 100;
        gridPanel.add(WeatherLocation(), weatherConstraints);

        if(weatherMap.GetCurrentWeatherBlock() == null)
            return;

        GridBagConstraints leftConstraints = new GridBagConstraints();
        leftConstraints.gridx = 0; leftConstraints.gridy = 1;
        leftConstraints.ipady = 200; leftConstraints.ipadx = 200;
        leftConstraints.insets = new Insets(-500, 200, 0, 0);
        gridPanel.add(new JLabel("Current Weather: " + weatherMap.requestor), leftConstraints);
        leftConstraints.insets = new Insets(0, 0, 0, 0);
        gridPanel.add(weatherMap.GetCurrentWeatherBlock().GetGuiContent(), leftConstraints);



        GridBagConstraints rightConstraints = new GridBagConstraints();
        rightConstraints.gridx = 1; rightConstraints.gridy = 1;
        rightConstraints.ipady = 200; rightConstraints.ipadx = 200;
        rightConstraints.insets = new Insets(25, 25, 25, 25);
        rightConstraints.anchor = GridBagConstraints.EAST;
        rightConstraints.insets = new Insets(-500, 200, 0, 0);
        gridPanel.add(new JLabel("Hourly Forecast: " + weatherMap.requestor), rightConstraints);
        rightConstraints.insets = new Insets(25, 0, 0, 0);
        gridPanel.add(HourlyForecasts(), rightConstraints);

        GridBagConstraints fiveDayForecast = new GridBagConstraints();
        fiveDayForecast.gridx = 0; fiveDayForecast.gridy = 2;
        fiveDayForecast.gridwidth = 200;
        fiveDayForecast.ipady = 50; fiveDayForecast.ipadx = 200;
        fiveDayForecast.insets = new Insets(-175, 200, 0, 0);
        gridPanel.add(new JLabel("Weekly Forecast: "  + weatherMap.requestor), fiveDayForecast);
        fiveDayForecast.insets = new Insets(25, 0, 0, 0);
        gridPanel.add(FutureForcasts(), fiveDayForecast);
    }

    private JPanel FutureForcasts(){
        JPanel newPanel = new JPanel();
        GridLayout myGrid = new GridLayout(1, 5);
        myGrid.setVgap(-500);

        newPanel.setLayout(myGrid);

        for(int i = 0;i < 5;i++){
            newPanel.add(weatherMap.GetForecastWeatherBlock(i).GetGuiContent());
        }

        return newPanel;
    }

    private JPanel HourlyForecasts(){
        JPanel newPanel = new JPanel();
        FlowLayout layout = new FlowLayout();
        newPanel.setPreferredSize(new Dimension(450, 220));
        newPanel.setLayout(layout);

        for(int i = 0;i < 8;i++){
            newPanel.add(weatherMap.GetHourlyWeatherBlock(i).GetGuiContent());
        }

        return newPanel;
    }

    private void SetupFavorites(){
        favorites = new ArrayList<String>();
        File file = new File("favorites.txt");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String currentLine = "";

            while((currentLine = reader.readLine()) != null && !currentLine.equals("")){
                favorites.add(currentLine);
            }

            reader.close();
        }catch(Exception e){
            try {
                FileWriter writer = new FileWriter(file);
                writer.write("");
                writer.close();
            }catch(Exception ex){
                System.out.println("We really screwed up: " + ex.getMessage());
            }
        }
    }

    private JPanel WeatherLocation() {
        JPanel newPanel = new JPanel();
        newPanel.setLayout(new FlowLayout());


        newPanel.setBackground(Color.lightGray);

        //We need to create a drop down for favorites
        locationDropDown = new JComboBox(favorites.toArray());
        locationDropDown.addActionListener(this);

        //Add the buttons to the doodad
        JLabel currentLocation = new JLabel("Location");
        this.locationTextField = new JTextField("", 24);
        System.out.println(locationTextField.getText());


        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        JButton addToFavorites = new JButton("Add");
        addToFavorites.addActionListener(this);
        JButton removeFromFavorites = new JButton("Remove");
        removeFromFavorites.addActionListener(this);
//        JButton setAsDefault = new JButton("Set as Default");
//        setAsDefault.addActionListener(this);

        newPanel.add(searchButton);
        newPanel.add(currentLocation);
        newPanel.add(this.locationTextField);
        newPanel.add(locationDropDown);
        newPanel.add(addToFavorites);
        newPanel.add(removeFromFavorites);
//        newPanel.add(setAsDefault);


        return newPanel;
    }

    public void actionPerformed(ActionEvent event){
        //Check if this is blank
//        System.out.println(event.getActionCommand());
        if(event.getActionCommand() == "comboBoxChanged"){
            String text = this.locationDropDown.getSelectedItem().toString();
            System.out.println(text);

            this.locationTextField.setText(text);
            RemoveFromFavorites(text);
            AddFavorite(text);

            System.out.println("Searching...." + this.locationTextField.getText());

            //Check if we are a zipcode or city
            try{
                Integer.parseInt(text);
                ResetWeather("zip=" + text);
            }catch(Exception e){
                ResetWeather("q=" + text);
            }
        }


        if(event.getActionCommand() == "Search"){
            System.out.println("Searching....");

            String text = this.locationTextField.getText();
            //Check if we are a zipcode or city
            try{
                Integer.parseInt(text);
                ResetWeather("zip=" + text);
            }catch(Exception e){
                ResetWeather("q=" + text);
            }
        }
        if(event.getActionCommand() == "Add"){
            System.out.println("Adding to favorites" + this.locationTextField.getText());
            AddFavorite(this.locationTextField.getText());

            //Check if we are a zipcode or city
            try{
                Integer.parseInt(this.locationTextField.getText());
                ResetWeather("zip=" + this.locationTextField.getText());
            }catch(Exception e){
                ResetWeather("q=" + this.locationTextField.getText());
            }
        }
        if(event.getActionCommand() == "Remove"){
            System.out.println("Removing from favorites");
            RemoveFromFavorites(this.locationDropDown.getSelectedItem().toString());

            //Check if we are a zipcode or city
            try{
                Integer.parseInt(this.locationDropDown.getSelectedItem().toString());
                ResetWeather("zip=" + this.locationDropDown.getSelectedItem().toString());
            }catch(Exception e){
                ResetWeather("q=" + this.locationDropDown.getSelectedItem().toString());
            }
        }
        if(event.getActionCommand() == "Set as Default") {
            System.out.println("Setting as Default");
            RemoveFromFavorites(locationTextField.getText());
            AddFavorite(this.locationTextField.getText());
        }
    }

    private void AddFavorite(String favorite){
        try{
            File file = new File("favorites.txt");
            File fileRename = new File("temp.txt");
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            FileOutputStream fileOutputStream = new FileOutputStream(fileRename);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            PrintWriter writer = new PrintWriter(outputStreamWriter);

            writer.println(favorite);
            for (String line;(line = reader.readLine()) != null;) {
                if(!line.contains(favorite)) {
                    writer.println(line);
                }
            }
            reader.close();
            inputStreamReader.close();
            fileInputStream.close();
            writer.close();
            outputStreamWriter.close();
            fileOutputStream.close();
            file.delete();
            fileRename.renameTo(file);
        }catch(Exception e){
            //Do nothing,
            System.out.println("Something fed up" + e.getMessage());
        }

        SetupFavorites();
    }

    private void RemoveFromFavorites(String delete){
        try{
            File file = new File("favorites.txt");
            File fileRename = new File("temp.txt");
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            FileOutputStream fileOutputStream = new FileOutputStream(fileRename);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            PrintWriter writer = new PrintWriter(outputStreamWriter);
            for (String line;(line = reader.readLine()) != null;) {
                if(!line.contains(delete)) {
                    writer.println(line);
                }
            }
            reader.close();
            inputStreamReader.close();
            fileInputStream.close();
            writer.close();
            outputStreamWriter.close();
            fileOutputStream.close();
            file.delete();
            fileRename.renameTo(file);
        }catch(Exception e){
            //Do nothing,
            System.out.println("Something fed up" + e.getMessage());
        }

        SetupFavorites();
    }
}
