package LibraryInfo;

import Persons.BorrowDetails;
import Persons.Subscriber;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Constants {
    private static final String path = "src/data/";
    private static final String pathImage = "src/libraryImage/";
    public static final String subscribersFile = path + "subscriber.csv";
    public static final String actorsFile = path + "actor.csv";
    public static final String authorsFile = path + "author.csv";
    public static final String singersFile = path + "singer.csv";
    public static final String directorsFile = path + "director.csv";
    public static final String borrowFile = path + "borrow.csv";
    public static final String mediasFile = path + "media.csv";
    public static final String qtyFile = path + "qty.csv";
    public static final String newAvailableFile = path + "newAvailable.csv";
    public static LocalDate loadDate = java.time.LocalDate.now();
    public  static String userIcon = pathImage + "user icon.png";
    public static String addWishIcon = pathImage + "wishes.png";
    public static String removeWishIcon = pathImage + "removeWishList.png";

    public static Subscriber correctPassword(String email, String password, ArrayList<Subscriber> subscribers){
        for (Subscriber s : subscribers) {
            if(email.equals(s.getEmail()) && password.equals(s.getPassword()))
                return s;
        }
        return null;
    }

    public static List<String> getCSV(String fileName){
        Path pathToFile = Paths.get(fileName);
        List<String> csvBody = new ArrayList<>();
        // create an instance of BufferedReader
        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.US_ASCII)) {

            // read the first line from the text file
            String line = br.readLine();
            // loop until all lines are read
            while (line != null) {
                csvBody.add(line);
                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return csvBody;
    }

    public static void writeCSV(String fileName, List<String> csvBody){
        File fold = new File(fileName);
        fold.delete();
        try (FileWriter f = new FileWriter(fileName, true);
             BufferedWriter b = new BufferedWriter(f);
             PrintWriter p = new PrintWriter(b);) {
            for (String s: csvBody) {
                p.println(s);
            }

        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static void returnOverdue(ArrayList<BorrowDetails> borrowDetails){
        for(BorrowDetails bd : borrowDetails){
            if(Constants.loadDate.isAfter(bd.getEndDate()) && bd.getStatus() == 1)
                bd.returnMedia();
        }
    }

    public static void forwardWeeks(int numWeeks){
        Constants.loadDate = Constants.loadDate.plusWeeks(numWeeks);
    }
}
