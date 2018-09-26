import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Bayesian {

	static Attri[] attri;
	static double class1;
	static double class0;
	
	public static void main(String[] args) throws FileNotFoundException {
		
		train("train2.dat");
		printTrainingData();
		test("train2.dat", "training");
		test("test2.dat", "test");

	}
	
	public static void test(String f, String setName) throws FileNotFoundException
	{
		Scanner file = new Scanner(new File(f));
		int cnt =0;
		double right = 0.0;
		
		
		
		file.nextLine();
		
		while(file.hasNext()) 
		{
			String[] temp = file.nextLine().split(" ");
			
			double resP = 0.0;
			double resN = 0.0;
			
			
			for(int i =0; i < temp.length-1; i++)
			{
				if(Integer.parseInt(temp[i])==0)
				{
					resP += Math.log((attri[i]._0to1/class1));
					resN += Math.log((attri[i]._0to0/class0));
					
				}
				else
				{
					resP += Math.log((attri[i]._1to1/class1));
					resN += Math.log((attri[i]._1to0/class0));
				}
			}
			
			resP += Math.log((class1/(class1+class0)));
			resN += Math.log((class0/(class1+class0)));
			
			int des = resP > resN ? 1:0;
			
		
			
			right += Integer.parseInt(temp[temp.length-1]) == des ? 1:0;
			
			cnt++;
			
		}
		
		System.out.println("Accurary on "+setName+" set ("+cnt+" instances): "+String.format("%.2f", right/cnt*100)+"%");
		
	}
	
	public static void printTrainingData()
	{
		String out1 ="P(class=0)="+String.format("%.2f", (class0/(class1+class0)));
		String out2 ="P(class=1)="+String.format("%.2f",(class1/(class1+class0)));
		for(int i =0; i< attri.length; i++)
		{
			out1+= "P("+attri[i].name+")=0|0)="+String.format("%.2f",(attri[i]._0to0/class0))+
					"P("+attri[i].name+")=1|0)="+String.format("%.2f",(attri[i]._1to0/class0));
					
		}
		for(int i =0; i< attri.length; i++)
		{
			out2+= "P("+attri[i].name+")=1|1)="+String.format("%.2f",(attri[i]._1to1/class1))+
					"P("+attri[i].name+")=0|1)="+String.format("%.2f",(attri[i]._0to1/class1));
		}
		
		System.out.println(out1);
		System.out.println(out2);
		
	}
	
	public static void train(String f) throws FileNotFoundException
	{
		Scanner file = new Scanner(new File(f));

		String[] head = file.nextLine().split(" ");

		attri = new Attri[head.length - 1];

		for (int i = 0; i < attri.length; i++) {
			attri[i] = new Attri(head[i]);
		}
		

		class1 = 0;
		class0 = 0;

		while (file.hasNext()) {
			head = file.nextLine().split(" ");

			for (int i = 0; i < head.length - 1; i++) {
				if (Integer.parseInt(head[i]) == 1) {
					if (Integer.parseInt(head[head.length - 1]) == 1) {
						attri[i]._1to1++;
					} else {
						attri[i]._1to0++;
					}
				} else {
					if (Integer.parseInt(head[head.length - 1]) == 1) {
						attri[i]._0to1++;
					} else {
						attri[i]._0to0++;
					}
				}
			}
			

			if (Integer.parseInt(head[head.length - 1]) == 0) {
				class0++;
			} else {
				class1++;
			}
		}
		
	}

}

class Attri {
	String name;

	int _0to0;
	int _0to1;
	int _1to0;
	int _1to1;

	public Attri(String n) {
		name = n;
	}

}
