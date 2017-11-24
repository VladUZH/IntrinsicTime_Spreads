/**
 * Created by author.
 */
public class PricesToSpread {

    PricesToSpread(){}

    public float midSpread(Price priceA, Price priceB){
        return priceA.getMid() - priceB.getMid();
    }


}
