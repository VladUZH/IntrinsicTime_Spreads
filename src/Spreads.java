import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 * In Nutshell: for all available ticks in two time series compute spread taking into account synchronisation of their
 * timestamps and save results into a CSV file. Worth looking into the synchronisation solution.
 */
public class Spreads {

    public static void main(String[] args){

        String pairNameA = "EURCHF"; // name of the first pair
        String pairNameB = "USDJPY"; // name of the second pair
        String commonNamePart = "_UTC_Ticks_Bid_2012.07.20_2017.07.21.csv"; // common part from both names (optional)

        String priceSourceA = "E:/Data/Forex/2012_2017/"; // path to the first file
        String priceSourceB = "E:/Data/Forex/2012_2017/"; // path to the second file

        String outputPath = "E:/Data/Forex/2012_2017/Spreads/"; // path to the output


        PricesToSpread pricesToSpread = new PricesToSpread();

        try {
            PrintWriter writer = new PrintWriter(outputPath + pairNameA + "_" + pairNameB + ".csv");

            BufferedReader bufferedReaderA = new BufferedReader(new FileReader(priceSourceA + pairNameA + commonNamePart));
            BufferedReader bufferedReaderB = new BufferedReader(new FileReader(priceSourceB + pairNameB + commonNamePart));

            String lineA = bufferedReaderA.readLine(); // just to avoid header
            String lineB = bufferedReaderB.readLine(); // just to avoid header

            lineA = bufferedReaderA.readLine();
            Price oldPriceA = Tools.priceLineToPrice(lineA, ",", "yyyy.MM.dd HH:mm:ss.SSS", 1, 2, 0);

            lineA = bufferedReaderA.readLine();
            Price newPriceA = Tools.priceLineToPrice(lineA, ",", "yyyy.MM.dd HH:mm:ss.SSS", 1, 2, 0);

            lineB = bufferedReaderB.readLine();
            Price priceB = Tools.priceLineToPrice(lineB, ",", "yyyy.MM.dd HH:mm:ss.SSS", 1, 2, 0);


            // From here we synchronise the process:
            long i = 0;
            outerloop:
            while (true){
                while(priceB.getTime() > oldPriceA.getTime() && priceB.getTime() <= newPriceA.getTime()){
                    float spread = pricesToSpread.midSpread(oldPriceA, priceB);
                    if (i % 100000 == 0){
                        System.out.println("Time: " + priceB.getTime() + ", spread: " + spread);
                    }
                    writer.println(priceB.getTime() + "," + spread);
                    lineB = bufferedReaderB.readLine();
                    if (checkNull(lineB)){
                        break outerloop;
                    }
                    priceB = Tools.priceLineToPrice(lineB, ",", "yyyy.MM.dd HH:mm:ss.SSS", 1, 2, 0);
                    i++;
                }
                oldPriceA = newPriceA.clone();
                lineA = bufferedReaderA.readLine();
                if (checkNull(lineA)){
                    break;
                }
                newPriceA = Tools.priceLineToPrice(lineA, ",", "yyyy.MM.dd HH:mm:ss.SSS", 1, 2, 0);
            }
            // until here.

            System.out.println("End of a file.");

            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static boolean checkNull(String string){
        return string == null;
    }

}
