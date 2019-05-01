// Arami Guerra de la Llera
// axg166831
// Walter Han
// wch170030
// NN assigment

import java.util.*;

import java.io.*;

public class Main{

    static double alpha;   // learning rate
    static int iter;   // max number of iterations
    static String trainningF;
    static String testF;
    static Node[] nn; // weights 
    static ArrayList<int[]> trainD; // all the data
    static String[] attri; // initial attribute name
    
    public static void main(String[] args) throws Exception {
        
        // get the information for the data
        trainningF = args[0];
        testF = args[1];
        alpha = Double.parseDouble(args[2]);
        iter = Integer.parseInt(args[3]);

        Scanner file = new Scanner(new File(trainningF));

        // get the attribute names
        attri = file.nextLine().split("[ /t]+");

        // init the weights
        nn = new Node[attri.length-1];

        trainD = new ArrayList<>();

        // set the weights
        for (int i = 0; i < attri.length-1; i++) {
            nn[i] = new Node(0, attri[i]);
        }

        // read in the training data
        while(file.hasNext())
        {
            int[] temp = new int[attri.length];
            for(int i =0; i < temp.length; i++)
            {
                temp[i] = file.nextInt();
            }

            trainD.add(temp);
        }

        // train with all iterations
        for (int k = 0; k < iter; k++) 
        {
              // get the y
            double _class = trainD.get(k%trainD.size())[attri.length-1];

            double wx = 0;

            // get dot product
            for(int j = 0; j < attri.length-1; j++)
            {
                wx += trainD.get(k%trainD.size())[j] * nn[j].weight; 
            }

            // get the error
            double error = _class - sigma(wx);

            for (int i = 0; i < nn.length; i++) 
            {  
                // update weights
              nn[i].weight = nn[i].weight + alpha*error*trainD.get(k%trainD.size())[i]*sigma(wx)*(1-sigma(wx));
            }    

            wx = 0;

            // get dot product of the updated weights
            for(int j = 0; j < attri.length-1; j++)
            {
                wx += trainD.get(k%trainD.size())[j] * nn[j].weight; 
            }
            
            // print the unpdated weights
           print(k+1, sigma(wx));
           
        }
        
        
        // print out the result of the training
        System.out.println();
       corr(trainningF, "training");
       // print out the result of the test
       System.out.println();
       corr(testF, "test");
        

    }

    // get the correctness of each file
    public static void corr(String file, String name) throws Exception
    {
        // read file
        Scanner lol = new Scanner(new File(file));
        // how many wrong and how many right
        int h =0;
        double right = 0;

        // skip the names
        lol.nextLine();
        // read until the end
        while(lol.hasNext())
        {
            // get the x's
            int[] temp = new int[attri.length];
            for(int i =0; i < temp.length; i++)
            {
                temp[i] = lol.nextInt();
            }

            double wx = 0;
            // get dot product
            for(int j = 0; j < attri.length-1; j++)
            {
                wx += temp[j] * nn[j].weight; 
            }

            // check if it outputs yes or no
            wx = sigma(wx) < .5 ? 0 : 1;

            // compare that to the input
            right += wx == temp[temp.length-1] ? 1:0;
            h++;
        }
        double percent = right/h;

        // print weight name and accuracy
        System.out.println("Accuracy on "+name+" set ("+h+" instances): "+String.format("%.2f", percent*100)+"%");

    }
    

    public static double sigma(double t)
    {
        return 1/ (1+Math.pow(Math.E, -1*t));
    }

    public static void print(int k, double out)
    {

        String output = "After iteration "+k+": ";

        for(int i =0; i < nn.length; i++)
        {
            output+="w("+nn[i].name+") = "+String.format("%.4f",nn[i].weight)+", ";
        }

        System.out.println(output +"output = " +String.format("%.4f",out));
    }
}


// node class 
// contains weight and name
class Node
{
    double weight;
    String name;
    
    public Node(double w, String n)
    {
        weight = w;
        name = n;
    }
}