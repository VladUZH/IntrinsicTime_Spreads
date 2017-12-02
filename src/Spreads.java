import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 * In Nutshell: for all available ticks in two time series compute spread taking into account synchronisation of their
// * timestamps and save results into a CSV file. (((Later, the spreads will be converted to a base currency in the following way: if
// * one is computing the spread between ccy1/ccy2 and ccy3/ccy4 and wants to covert it into ccy5 than ccy1/ccy2 has to be
// * multiplied by ccy2/ccy5 and ccy3/ccy4 by ccy4/ccy5. For example:
// * BTC/CHF * CHF/USD -
// * EUR/RUB * RUB/USD =>
// *        USD  )))
 */
public class Spreads {

    public static void main(String[] args){

        String ccy1ccy2 = "EURUSD";
        String ccy3ccy4 = "CHFJPY";
//        String ccy2ccy5 = "CHFUSD";
//        String ccy4ccy5 = "JPYUSD";

        String commonNamePart = "_UTC_Ticks_Bid_2012.07.20_2017.07.21.csv"; // common part from all names (optional)

        String priceSource = "E:/Data/Forex/2012_2017/"; // path to the first file

        String outputPath = "E:/Data/Forex/2012_2017/Spreads/"; // path to the output


        PricesToSpread pricesToSpread = new PricesToSpread();

        try {
            PrintWriter writer = new PrintWriter(outputPath + ccy1ccy2 + "_" + ccy3ccy4 + ".csv");

            BufferedReader brCcy1ccy2 = new BufferedReader(new FileReader(priceSource + ccy1ccy2 + commonNamePart));
            BufferedReader brCcy3ccy4 = new BufferedReader(new FileReader(priceSource + ccy3ccy4 + commonNamePart));
//            BufferedReader brCcy2ccy5 = new BufferedReader(new FileReader(priceSource + ccy2ccy5 + commonNamePart));
//            BufferedReader brCcy4ccy5 = new BufferedReader(new FileReader(priceSource + ccy4ccy5 + commonNamePart));

            // Just to avoid header:
            String lineCcy1ccy2 = brCcy1ccy2.readLine();
            String lineCcy3ccy4 = brCcy3ccy4.readLine();
//            String lineCcy2ccy5 = brCcy2ccy5.readLine();
//            String lineCcy4ccy5 = brCcy4ccy5.readLine();

            lineCcy1ccy2 = brCcy1ccy2.readLine(); // the first reference point (old)
            Price oldPriceCcy1ccy2 = Tools.priceLineToPrice(lineCcy1ccy2, ",", "yyyy.MM.dd HH:mm:ss.SSS", 1, 2, 0);

            lineCcy1ccy2 = brCcy1ccy2.readLine(); // the second reference point (new)
            Price newPriceCcy1ccy2 = Tools.priceLineToPrice(lineCcy1ccy2, ",", "yyyy.MM.dd HH:mm:ss.SSS", 1, 2, 0);

            lineCcy3ccy4 = brCcy3ccy4.readLine(); // price of the second exchange rate
            Price priceCcy3ccy4 = Tools.priceLineToPrice(lineCcy3ccy4, ",", "yyyy.MM.dd HH:mm:ss.SSS", 1, 2, 0);
            while (priceCcy3ccy4.getTime() <= oldPriceCcy1ccy2.getTime()){ // juts to be sure that this price is indeed
                // inside or after the range old-new
                lineCcy3ccy4 = brCcy3ccy4.readLine();
                priceCcy3ccy4 = Tools.priceLineToPrice(lineCcy3ccy4, ",", "yyyy.MM.dd HH:mm:ss.SSS", 1, 2, 0);
            }

            OSscalingLaw oSscalingLaw = new OSscalingLaw(0.05, 4, 100, true);


            // From here we synchronise the process:
            long i = 0;
            outerloop:
            while (true){
                while(priceCcy3ccy4.getTime() > oldPriceCcy1ccy2.getTime() && priceCcy3ccy4.getTime() <= newPriceCcy1ccy2.getTime()){


                    float spread = -pricesToSpread.midSpread(oldPriceCcy1ccy2, priceCcy3ccy4);



                    Price spreadPrice = new Price(spread, spread, priceCcy3ccy4.getTime());
                    oSscalingLaw.run(spreadPrice);









                    if (i % 100000 == 0){
                        System.out.println("Time: " + priceCcy3ccy4.getTime() + ", spread: " + spread);
                    }
//                    writer.println(priceCcy3ccy4.getTime() + "," + spread);
                    lineCcy3ccy4 = brCcy3ccy4.readLine();
                    if (checkNull(lineCcy3ccy4)){
                        break outerloop;
                    }
                    priceCcy3ccy4 = Tools.priceLineToPrice(lineCcy3ccy4, ",", "yyyy.MM.dd HH:mm:ss.SSS", 1, 2, 0);
                    i++;
                }
                oldPriceCcy1ccy2 = newPriceCcy1ccy2.clone();
                lineCcy1ccy2 = brCcy1ccy2.readLine();
                if (checkNull(lineCcy1ccy2)){
                    break;
                }
                newPriceCcy1ccy2 = Tools.priceLineToPrice(lineCcy1ccy2, ",", "yyyy.MM.dd HH:mm:ss.SSS", 1, 2, 0);
            }
            // until here.

            oSscalingLaw.finish();
            oSscalingLaw.saveToFile();

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
