import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MyBiometricProject {

    static ArrayList<double[][]> userArrayList = new ArrayList<>();
    static int a, number;
    static double threshold;

    static ArrayList<double[][]> myDataArrayList = new ArrayList<>();
    static private double[][] userArray;
    private static int limit = 10;
    
    public static void main(String[] args) {
        initData();
        execute();
    }

    public static void execute() {

        Scanner input = new Scanner(System.in);
        int choice, userNumber;
        boolean condition = true;

        while (condition) {

        	System.out.println("Choose an option: ");
            System.out.println("1. Display Genuine Scores for the user.");
            System.out.println("2. Display mean Scores for the user.");
            System.out.println("3. Display Imposter Scores for the user.");
            System.out.println("4. Exit.");
            choice = input.nextInt();

            System.out.print("Enter a user number (1 - " + userArrayList.size() + ") ");
            int user = input.nextInt();
            userNumber = user - 1;
            System.out.print("Enter the template limit N: ");
            int n = input.nextInt();
            userArray= userArrayList.get(userNumber);
            myDataArrayList=userArrayList;
            limit=n;
            switch (choice) {
                case 1:

                    ArrayList<double[]> GenuineScoreUser = genuineScore();
                    System.out.println("Genuine Scores : " + user);
                    for (int i = 0; i < GenuineScoreUser.get(userNumber).length; i++) {
                        System.out.println(GenuineScoreUser.get(userNumber)[i] + " ");
                    }
                    
                case 2:
                    double[] testMean = crulateMeanTemplate();
                    System.out.println("Mean : "+ Arrays.toString(testMean));
                    input.nextLine();
                    break;
                    
                  
                case 3:
                    ArrayList<double[][]> ImposterScoreUser = imposterScore();
                    System.out.println("Imposter Scores " + user );
                    
                    double[][] temp= ImposterScoreUser.get(userNumber);
                    for (int i = 0; i < temp.length; i++) {
                        for (int c = 0; c < temp[i].length; c++) {
                            System.out.print(temp[i][c] + " ");
                        }
                       System.out.println();
                    }
                   
                    System.out.println("Press Enter to continue");
                    input.nextLine();                	
                	break;
                case 4:
                	 input.nextLine();
                    condition = false;
                    break;

                            }
        }
    }

    public static double[][] keyHoldCalc() {
        double[][] KhCalculator = userArray;
        double[][] keyHoldUser = new double[KhCalculator.length][11];
        int h = 0;

        for (int r = 0; r < KhCalculator.length; r++) {
            for (int c = 0; c < KhCalculator[r].length; c += 3) {

                keyHoldUser[r][h++] = KhCalculator[r][c];

            }
            h = 0;
        }
        return keyHoldUser;
    }

    public static double[][] KhCalculatorTemplate() {

        double[][] KhCalculator = userArray;
        double[][] userTempKH = new double[limit][11];
        int k = 0;

       
        for (int i = 0; i < limit; i++) {
            for (int c = 0; c < KhCalculator[i].length; c += 3) {

                userTempKH[i][k++] = KhCalculator[i][c];

            }
            k = 0;
        }
        return userTempKH;
    }

    public static double[][] KhCalculatorTesting() {
        double[][] testKeyHold = userArray;
        int testing = testKeyHold.length - limit;
        double[][] userKH = new double[testing][11];
        int k = 0;

        
        for (int r = limit; r < testKeyHold.length; r++) {
            for (int c = 0; c < testKeyHold[r].length; c += 3) {

                userKH[(r - limit)][k++] = testKeyHold[r][c];

            }
            k = 0;
        }
        return userKH;
    }

    public static double[] crulateMeanTemplate() {
        double[][] userKH = KhCalculatorTemplate();
        int k = userKH[0].length;

        double[] keyHoldMean = new double[k];

        for (int c = 0; c < k; c++) {
            for (int i = 0; i < userKH.length; i++) {

                keyHoldMean[c] += (userKH[i][c] / userKH.length);

            }
        }
        return keyHoldMean;
    }

   
    public static ArrayList<double[]> genuineScore() {

        int size = myDataArrayList.size();
        ArrayList<double[][]> userList = myDataArrayList;
        ArrayList<double[]> output = new ArrayList<>();
        double[][] test = KhCalculatorTesting();

        double genuine = 0;

        for (int x = 0; x < size; x++) {
            userArray = userList.get(x);
            double[] template = crulateMeanTemplate();
            double[] genuineScore = new double[test.length];

            for (int i = 0; i < test.length; i++) {
                for (int j = 0; j < template.length; j++) {
                    genuine += Math.abs(template[j] - test[i][j]);
                }
                genuine = (genuine / template.length);
                genuineScore[i] = genuine;
                genuine = 0;
            }
            output.add(genuineScore);

        }

        return output;

    }

    
    public static ArrayList<double[][]> imposterScore() {

        ArrayList<double[][]> userList = myDataArrayList;
        ArrayList<double[][]> imposterResult = new ArrayList<>();
        int size = myDataArrayList.size();
        int testsize = userArray.length - limit;
        double[] userMeanTempKH = new double[11];
        double temp = 0;
        double[][] imposterUser = new double[testsize][11];

        double[][] template = userArray;

        userMeanTempKH = crulateMeanTemplate();

        for (int j = 0; j < size; j++) {

            if (template != userList.get(j)) {
               
                userArray = userList.get(j);

                double[][] testVectorKH = new double[testsize][11];
                testVectorKH = KhCalculatorTesting();

                
                for (int x = 0; x < testsize; x++) {

                     
                    for (int y = 0; y < testVectorKH[y].length; y++) {

                        temp = +Math.abs(userMeanTempKH[y] - testVectorKH[x][y]);

                        imposterUser[x][y] = temp;
                        temp = 0;
                    }

                }

            }

            imposterResult.add(imposterUser);

        }

        return imposterResult;
    }
//data
    
       public static void initData() {

        userArrayList.add(userData("dataset/UserS002.txt"));
        userArrayList.add(userData("dataset/UserS003.txt"));
        userArrayList.add(userData("dataset/UserS004.txt"));
        userArrayList.add(userData("dataset/UserS005.txt"));
        userArrayList.add(userData("dataset/UserS006.txt"));

    }
   
    public static String inputData(String addDataset) {
        String line = null;
        String pointer = null;

        File file = new File(addDataset);

        StringBuilder stringbuilder = new StringBuilder();
        try (BufferedReader temp2 = new BufferedReader(new FileReader(file))) {

            while ((line = temp2.readLine()) != null) {
                stringbuilder.append(line);
                stringbuilder.append(" ");
            }

        } catch (FileNotFoundException ex) {
            System.out.println("Can't find file: " + file.toString());
        } catch (IOException e) {
            System.out.println("Cannot read the file " + file.toString());
        }
        pointer = stringbuilder.toString().replace("\t", " ");
        return pointer;
    }

   
    public static double[][] userData(String myData) {
        String userdata = inputData(myData);
        String[] saveResult = userdata.split(" ");

        double[] convert = new double[saveResult.length];

        for (int i = 0; i < saveResult.length; i++) {
            convert[i] = Double.parseDouble(saveResult[i]);
        }

        double[][] returnUser = new double[400][31];

        int p = 0;
        for (int i = 0; i < 400; i++) {

            for (int j = 0; j < 31; j++) {
                returnUser[i][j] = convert[p++];
            }

        }

        return returnUser;
    }

}