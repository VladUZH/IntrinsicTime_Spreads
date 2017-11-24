import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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


}
