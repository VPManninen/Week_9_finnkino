package com.example.week9finnkino;

import android.os.StrictMode;
import android.widget.ArrayAdapter;

import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static java.lang.System.exit;

public class MainClass {
    private static MainClass program =  new MainClass();
    private NodeList areaList;
    private ArrayList<Theatre> theatreList;
    private String theatreUrl = "https://www.finnkino.fi/xml/TheatreAreas/";

    // Creates the list of theatres in "MainClass"
    private MainClass() {
        theatreList = new ArrayList<Theatre>();
        areaList = readXML(theatreUrl, "TheatreArea");
        if (areaList != null) {
            for (int i = 1; i < areaList.getLength(); i++) {
                Node node = areaList.item(i);
                Element element = (Element) node;
                // creates the list of theatres
                theatreList.add(new Theatre(element.getElementsByTagName("ID").item(0).getTextContent(), element.getElementsByTagName("Name").item(0).getTextContent()));
            }
        } else { // should the website be unavailable : exits the app
            System.out.println("Null list. Exiting.");
            exit(0);
        }
    }
    // returns the instance of the class
    public static MainClass getInstance() {
        return program;
    }

    // function for reading XML from given string with a given tag
    // returns the NodeList or null depending on success
    public NodeList readXML(String url, String tag) {
        try {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document theatreDoc = dBuilder.parse(url);
            theatreDoc.getDocumentElement().normalize();
            NodeList areaList = theatreDoc.getDocumentElement().getElementsByTagName(tag);
            return areaList;
        } catch (IOException e) {
            System.out.println("IOEx");
        } catch (SAXException e) {
            System.out.println("SAEx");
        } catch (ParserConfigurationException e) {
            System.out.println("PCEx");
        } catch (NullPointerException e) {
            System.out.println("NPEx");
        } finally {
            System.out.println("Finished @readXML");
        }
        return null;
    }

    // function which returns the theatre names
    public ArrayList<String> getTheatreNames() {
        ArrayList<String> list = new ArrayList<String>();
        for (Theatre i : theatreList) {
            list.add(i.getName());
        }
        return list;
    }

    // Function which returns an String ArrayList containing all movies which fit the search criteria
    public ArrayList<String> getMoviesByCriteria(String theatreName, int day, int month, int year, int start, int  end) {
        String findById = null;
        for (Theatre i : theatreList) {
            if (i.getName().equals(theatreName)) {
                findById = i.getId();
            }
        }
        String movieUrl = String.format("https://www.finnkino.fi/xml/Schedule/?area=%s&dt=%s", findById, String.format("%d.%d.%d", day, month, year));
        System.out.println(movieUrl);
        ArrayList<String> movies = new ArrayList<String>();
        NodeList moviesList = readXML(movieUrl, "Show");
        for (int i = 0; i < moviesList.getLength(); i++) {
            Node node = moviesList.item(i);
            Element element = (Element) node;
            String movieName = element.getElementsByTagName("Title").item(0).getTextContent();
            String[] timeArray = element.getElementsByTagName("dttmShowStart").item(0).getTextContent().split("T")[1].split(":");
            if ((Integer.parseInt(timeArray[0]) >= start) && (Integer.parseInt(timeArray[0]) < end)) {
                movies.add(movieName + ":\n" + " Alkaa: " + timeArray[0] + ":" + timeArray[1]);
            }
        }
        if (movies.isEmpty()) {
            return null;
        }
        return movies;
    }

    // For creation of the date spinner
    public ArrayList<Integer> getDates() {
        int d = Integer.parseInt(new SimpleDateFormat("dd").format(Calendar.getInstance().getTime()));
        ArrayList<Integer> dates = new ArrayList<>();
        for (int i = d; i < 32; i++){
            dates.add(i);
        }
        for (int i = 1; i < d; i++){
            dates.add(i);
        }
        return dates;
    }

    // For creation of the month spinner
    public ArrayList<Integer> getMonths() {
        int d = Integer.parseInt(new SimpleDateFormat("MM").format(Calendar.getInstance().getTime()));
        ArrayList<Integer> months = new ArrayList<>();
        for (int i = d; i < 12; i++){
            months.add(i);
        }
        for (int i = 1; i < d; i++){
            months.add(i);
        }
        return months;
    }

    // For creation of the year spinner
    public ArrayList<Integer> getYears() {
        int d = Integer.parseInt(new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime()));
        ArrayList<Integer> years = new ArrayList<>();
        for (int i = d; i < d + 2; i++){
            years.add(i);
        }
        return years;
    }

    // For creation of the start time spinner
    public ArrayList<Integer> getTimes() {
        ArrayList<Integer> times = new ArrayList<>();
        for (int i = 0; i < 24; i++){
            times.add(i);
        }
        return times;
    }

    // For creation of the end time spinner
    public ArrayList<Integer> getTimesEnd(int c) {
        ArrayList<Integer> times = new ArrayList<>();
        for (int i = 24; i > c; i--){
            times.add(i);
        }
        return times;
    }
}

    // class for the theatre instances
class Theatre {
    String id;
    String name;
    public Theatre(String idIn, String nameIn) {
        id = idIn;
        name = nameIn;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }
}