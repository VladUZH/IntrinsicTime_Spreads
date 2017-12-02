import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by author.
 */
public class Tools {


    /**
     * This method should convert a string of information about price to the proper Price format. IMPORTANT: by default
     * the time of a price is supposed to be given in sec.
     * @param inputString is a string which describes a price. For example, "1.23,1.24,12213"
     * @param delimiter in the previous example the delimiter is ","
     * @param dateFormat is the date format if any. Otherwise, one should write ""
     * @param askIndex index of the ask price in the string format
     * @param bidIndex index of the bid price in the string format
     * @param timeIndex index of the time in the string format
     * @return an instance Price
     */
    public static Price priceLineToPrice(String inputString, String delimiter,  String dateFormat, int askIndex, int bidIndex, int timeIndex){
        String[] priceInfo = inputString.split(delimiter);
        float bid = (Float.parseFloat(priceInfo[bidIndex]));
        float ask = (Float.parseFloat(priceInfo[askIndex]));
        long time;
        if (dateFormat.equals("")){ // data in milliseconds
            time = Long.parseLong(priceInfo[timeIndex]) * 1000L;
        } else {
            time = stringToDate(priceInfo[timeIndex], dateFormat).getTime();
        }
        return new Price(bid, ask, time);
    }


    /**
     * The method converts a String into a Date instance.
     * @param inputStringDate is the date in the String format, for example "1992.12.01 13:23:54.012"
     * @param dateFormat is the date format, for example "yyyy.MM.dd HH:mm:ss.SSS"
     * @return converted to the Date format
     */
    public static Date stringToDate(String inputStringDate, String dateFormat){
        DateFormat formatDate = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        try {
            return formatDate.parse(inputStringDate);
        } catch (ParseException e){
            e.printStackTrace();
            return null;
        }
    }


    static double[] GenerateLogSpace(double min, double max, int nBins)
    {
        double[] logList = new double[nBins];
        double m = 1.0 / (nBins - 1);
        double quotient =  Math.pow(max / min, m);
        logList[0] = min;
        for (int i = 1; i < nBins; i++){
            logList[i] = logList[i - 1] * quotient;
        }
        return logList;
    }


    static double[] GenerateLinSpace(double min, double max, int nBins)
    {
        double[] linList = new double[nBins];
        double step = (max - min) / nBins;
        double nextPoint = min;
        for (int i = 0; i < nBins; i++){
            linList[i] = nextPoint;
            nextPoint += step;
        }
        return linList;
    }


    static void saveResultsToFile(String fileName, ArrayList<String> columnNames, ArrayList<double[]> columns, boolean Double){
        try {
            String dateString = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss").format(new Date());
            fileName = fileName + "_" + dateString + ".csv";
            PrintWriter writer = new PrintWriter("Results/" + fileName, "UTF-8");
            String colimnString = "";
            for (String columnName : columnNames){
                colimnString += columnName + ";";
            }
            writer.println(colimnString);

            int index = 0;
            while (index < columns.get(0).length){
                String string = "";
                for (double[] array : columns){
                    string += array[index] + ";";
                }
                writer.println(string);
                index += 1;
            }
            writer.close();
            System.out.println("The result is saved like " + fileName);

        } catch (IOException e){
            e.printStackTrace();
        }

    }


}
