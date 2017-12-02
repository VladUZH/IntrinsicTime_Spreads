/**
 * Created by Vladimir Petrov on 05.09.2016.
 */
public class Runner {
    public double prevDC;
    public double extreme;
    public double deltaUp;
    public double deltaDown;
    public double osL;
    public int type;
    public boolean initalized;


    public Runner(double deltaUp, double deltaDown, int type){

        this.type = type; this.deltaUp = deltaUp; this.deltaDown = deltaDown; osL = 0; initalized = false;
    }


    public int run(Price aTick){

        if (!initalized){
            if (type == -1){
                extreme = aTick.getAsk();
                prevDC = aTick.getAsk();
            }
            else if (type == 1){
                extreme = aTick.getBid();
                prevDC = aTick.getBid();
            }
            initalized = true;
        }

        if (type == -1){
            if (aTick.getAsk() < extreme){
                extreme = aTick.getAsk();
                return 0;

            } else if ((aTick.getBid() - extreme) / extreme * 100.0 >= deltaUp){
                osL = (prevDC - extreme) / prevDC * 100.0;
                prevDC = aTick.getBid();
                extreme = aTick.getBid();
                type = 1;
                return 1;
            }

        }
        else if (type == 1){
            if (aTick.getBid() > extreme){
                extreme = aTick.getBid();
                return 0;

            } else if ((extreme - aTick.getAsk()) / extreme * 100.0 >= deltaDown){
                osL = (extreme - prevDC) / prevDC * 100.0;
                prevDC = aTick.getAsk();
                extreme = aTick.getAsk();
                type = -1;
                return -1;
            }
        }

        return 0;
    }



}
