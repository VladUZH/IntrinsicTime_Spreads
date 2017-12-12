import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Vladimir Petrov on 08.09.2016.
 */
public class OSscalingLaw {

    public double minDelta;
    public double maxDelta;
    public boolean logScale;
    public int nSteps;
    public double[] arrayOfdeltas;
    public Runner[] runners;
    public double[] sumArray;
    public long[] numEventsArray;

    OSscalingLaw(double minDelta, double maxDelta, int nSteps, boolean logScale){

        this.minDelta = minDelta;
        this.maxDelta = maxDelta;
        this.logScale = logScale;
        this.nSteps = nSteps;

        if (logScale){
            arrayOfdeltas = Tools.GenerateLogSpace(minDelta, maxDelta, nSteps);
        } else {
            arrayOfdeltas = Tools.GenerateLinSpace(minDelta, maxDelta, nSteps);
        }

        runners = new Runner[nSteps];

        for (int i = 0; i < nSteps; i++){
            runners[i] = new Runner(arrayOfdeltas[i], arrayOfdeltas[i], -1);
        }

        sumArray = new double[nSteps];
        numEventsArray = new long[nSteps];

        System.out.println("Average Overshoot Move is initialized");

    }


    public void run(Price aPrice){

        for (int i = 0; i < nSteps; i++){
            if (runners[i].run(aPrice) != 0){

//                System.out.println(aTick.bid + " " + aTick.ask + " " + runners[i].deltaUp + " " + runners[i].osL);

                sumArray[i] += runners[i].osL;
                numEventsArray[i] += 1;
            }
        }

    }


    public void finish(){
        for (int i = 0; i < nSteps; i++){
            sumArray[i] /= numEventsArray[i];
        }
    }


    public void saveToFile(String name){
        ArrayList<String> columnNames = new ArrayList<String>();
        columnNames.add("Delta");
        columnNames.add("Av_OS_move");
        columnNames.add("NumEvents");

        ArrayList<double[]> columnsData = new ArrayList<>();
        columnsData.add(arrayOfdeltas);
        columnsData.add(sumArray);
        columnsData.add(Arrays.stream(numEventsArray).asDoubleStream().toArray());

        Tools.saveResultsToFile("averageOvershoots_" + name, columnNames, columnsData, true);
    }



}
