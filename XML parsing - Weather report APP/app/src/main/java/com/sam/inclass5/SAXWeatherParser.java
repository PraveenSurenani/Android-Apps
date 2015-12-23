package com.sam.inclass5;

import android.util.Log;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * InClass5
 * Sam Painter and Praveen Suenani
 * SAXWeatherParser.java
 */
public class SAXWeatherParser extends DefaultHandler {
    ArrayList<Weather> weatherList;
    StringBuilder it;
    static String location;
    static double minTemp;
    static double maxTemp;
    static Boolean firstPass = true;

    public static double getMinTemp() {
        return minTemp;
    }

    public static void setMinTemp(int minTemp) {
        SAXWeatherParser.minTemp = minTemp;
    }

    public static double getMaxTemp() {
        return maxTemp;
    }

    public static void setMaxTemp(int maxTemp) {
        SAXWeatherParser.maxTemp = maxTemp;
    }

    Weather forecast;

    Boolean name = false;
    Boolean country = false;

    static public ArrayList<Weather> parseWeather(InputStream in) throws IOException, SAXException {
        SAXWeatherParser parser =new SAXWeatherParser();
        Xml.parse(in, Xml.Encoding.UTF_8, parser);
        return parser.getWeatherList();
    }

    public ArrayList<Weather> getWeatherList() {
        return weatherList;
    }

    public static String getLocation(){
        return location;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);

        if(name) {
            location = "Location: " + new String(ch,start,length);
            name = false;
        }

        if(country) {
            location = location + "," + new String(ch,start,length);
            country = false;
            Log.d("demo", location);
        }

        it = new StringBuilder();
    }

    public static void setLocation(String location) {
        SAXWeatherParser.location = location;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equals("name")) {
            name = true;
        }

        if (qName.equals("country")) {
            country = true;
        }

        if (localName.equals("time")) {
            forecast = new Weather();
        } else if (localName.equals("symbol")) {
            forecast.setSymbol(attributes.getValue("var"));
        } else if (localName.equals("precipitation")) {
            forecast.setPrecipitation(attributes.getValue("value"));
        } else if (localName.equals("windDirection")) {
            forecast.setWindDirection(attributes.getValue("name"));
        } else if (localName.equals("windSpeed")) {
            forecast.setWindSpeed(attributes.getValue("name"));
        } else if (localName.equals("temperature")) {
            forecast.setTemperature(attributes.getValue("value"));
            double minTempThisBlock = Double.parseDouble(attributes.getValue("min"));
            double maxTempThisBlock = Double.parseDouble(attributes.getValue("max"));
            if (minTempThisBlock < minTemp || firstPass)

                minTemp = minTempThisBlock;
            if (maxTempThisBlock > maxTemp || firstPass)
                maxTemp = maxTempThisBlock;
            firstPass = false;
        } else if (localName.equals("pressure")) {
            forecast.setPressure(attributes.getValue("value"));
        } else if (localName.equals("humidity")) {
            forecast.setHumidity(attributes.getValue("value"));
        } else if (localName.equals("clouds")) {
            forecast.setClouds(attributes.getValue("value"));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        if(localName.equals("time")) {
            weatherList.add(forecast);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        weatherList = new ArrayList<Weather>();
        it = new StringBuilder();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }
}