/**
 * Vincent Zhang
 * March 26, 2019
 * Make a quiz. More detailed description will follow.
 * ICS3U7-02 Mr. Anthony
 */
// PLEASE MARK THIS ASSIGNMENT IN CONSOLE

import java.io.*;
import java.util.ArrayList;

public class QuizAssignment
{
	//////////////////////////////////////////////////////////////////////////////////////
	///////////////////// START OF PROGRAM / START OF GLOBAL DECLARATIONS ////////////////
	//////////////////////////////////////////////////////////////////////////////////////

	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static ArrayList<String> layout = new ArrayList<String>(); // static because referenced from static context
	final String RESET = "\u001B[0m"; // reset colour
	final int SCREEN_WIDTH = 80; // width between two double bar lines
	static int score = 0; // static because referenced from static context
	
	int getInput() // get no error user input
	{
		while(true)
			try
			{
				return Integer.parseInt(br.readLine());
			}
			catch(Exception e) // if not integer: InputMismatchException OR NumberFormatException
			{
				System.out.println("Enter a positive integer.");
			}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	///////////////////// END OF GLOBAL DECLARATIONS / START OF ACCESSORY METHODS ////////
	//////////////////////////////////////////////////////////////////////////////////////
	
	void printLayout() throws IOException, InterruptedException
	{
		new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor(); // clear console screen
		
		for(int i = 0; i < layout.size(); i++) // print items in arraylist
		{
			String s = layout.get(i);
			System.out.print("||"); // left double bar
			for(int j = 0; j < ((SCREEN_WIDTH - s.length())/2)+1; j++) // left spaces
				System.out.print(" ");
			System.out.print(i >= 5 && i <= 8 ? "\u001B[36m" + s.substring(0, s.length()-1) + RESET:s.substring(0, s.length()-1)); // arraylist content, print in CYAN if between and including lines 6 to 9
			for(int j = 0; j < SCREEN_WIDTH - s.length() - ((SCREEN_WIDTH - s.length())/2); j++) // right spaces
				System.out.print(" ");
			System.out.print("||\n"); // right double bar
			if(s.charAt(s.length()-1) == '-') // horizontal rule
			{
				System.out.print("||");
				for(int j = 0; j < SCREEN_WIDTH; j++)
					System.out.print("-");
				System.out.print("||\n");
			}
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	///////////////////// END ACCESSORY METHODS / START OF MAIN PAGE SETUP ///////////////
	//////////////////////////////////////////////////////////////////////////////////////

	void homePage() throws IOException, InterruptedException
	{
		printLayout(); // print arraylist
		
		System.out.print("Type \"Y\" to quit, any others if otherwise: ");
		if(br.readLine().toUpperCase().equals("Y"))
		{
			br.close(); // close buffered reader
			System.exit(0); // quit program --> return only quits the method, System.exit quits program
		}
		
		int questions;
		while(true) // find number of questions user wants to answer
		{
			System.out.print("Enter number of questions: ");
			try
			{
				questions = Integer.parseInt(br.readLine());
				if(questions > 0)
					break;
			}
			catch (Exception e)
			{
				System.out.println("Enter positive integer.");
			}
		}
		
		int difficulty;
		while(true) // find difficulty user wants to play at
		{
			System.out.println("Enter integer difficulty: easy(1), medium(2), hard(3)");
			try
			{
				difficulty = Integer.parseInt(br.readLine());
				if(difficulty >= 1 && difficulty <= 3)
					break;
			}
			catch (Exception e)
			{
				System.out.println("Enter 1, 2 or 3.");
			}
		}

		switch(difficulty) // call methods at different difficulty levels
		{
			case 1 : level1Math(questions); break;
			case 2 : level2Math(questions); break;
			case 3 : level3Math(questions); break;
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////
	///////////////////// END OF MAIN PAGE SETUP / START OF SUBPAGES ACCESSORY METHODS ///
	//////////////////////////////////////////////////////////////////////////////////////
	
	void returnHome() throws IOException, InterruptedException // return to homePage() after completing quiz
	{
		System.out.print("Enter anything to continue: ");
		String tempInput = br.readLine(); // wait for user to finish reading scores
		if(tempInput != null)
			homePage();
	}
	
	void outputResult(int level, int i, int [][] data) throws IOException, InterruptedException // print results / scores
	{
		layout.remove(layout.size()-1); // remove score in arraylist
		layout.add("Score: " + score + "-"); // add new score in arraylist
		printLayout(); // print arraylist
		System.out.format("%s%15s%24s%19s%19s%5s%n", "||", "Question #", "Question", "User Answer", "Correct Answer", "||");
		for(int j = 0; j <= i; j++) // print question number, question of a level, user answer of a level, correct answer of a level
			System.out.format("%s%15s%24s%28s%28s%5s%n", "||", (j+1), (level==1?"Sum of ":(level==2?"Sum from ":"Product from ")) + data[j][2] + (level==1?" and ":" to ") + data[j][3], (data[j][0] == data[j][1]) ? ("\u001B[32m" + data[j][0] + RESET):("\u001B[31m" + data[j][0] + RESET), ("\u001B[32m" + data[j][1] + RESET), "||");
	}
	
	int number(int level) // generate two numbers specific to a level, number1 > number2 and number1 != number 2
	{
		int number1 = (level==1)?((int)(Math.random()*100)):((level==2)?((int)(Math.random()*1000)):((int)(Math.random()*12)+1));
		int number2 = (level==1)?((int)(Math.random()*100)):((level==2)?((int)(Math.random()*1000)):((int)(Math.random()*12)+1));
		return number1==number2?number2*100000+number1+1:(number1>number2?number2*100000+number1:number1*100000+number2); // largest possible number is 999, combine number1 and number2 in one number
	}

	//////////////////////////////////////////////////////////////////////////////////////
	///////////////////// END OF SUBPAGES ACCESSORY METHODS / START OF SUBPAGES //////////
	//////////////////////////////////////////////////////////////////////////////////////

	void level1Math(int questions) throws IOException, InterruptedException // easy math, two number addition
	{
		int [][] easyQA = new int [questions][4];
		for(int i = 0; i < questions; i++)
		{
			int num = number(1);
			easyQA[i][2] = num/100000; // get number1
			easyQA[i][3] = num%100000; // get number2
			System.out.print("Question " + (i+1) + "\n" + easyQA[i][2] + " + " + easyQA[i][3] + " = ");
			easyQA[i][0] = getInput();
			easyQA[i][1] = easyQA[i][2] + easyQA[i][3];
			score = easyQA[i][0] == easyQA[i][1] ? score+1:score; // add score
			outputResult(1, i, easyQA); // output new result
		}
		returnHome(); // go home
	}
	void level2Math(int questions) throws IOException, InterruptedException // medium math, add consecutive numbesr                                                                                                                                        ////
	{
		int [][] mediumQA = new int  [questions][4];
		for(int i = 0; i < questions; i++)
		{
			int num = number(2);
			mediumQA[i][2] = num/100000; // get number1
			mediumQA[i][3] = num%100000; // get number2
			System.out.print("Question " + (i+1) + "\nEnter sum of all integers from " + mediumQA[i][2] + " to " + mediumQA[i][3] + " inclusive: ");
			mediumQA[i][0] = getInput();
			mediumQA[i][1] = (mediumQA[i][2] + mediumQA[i][3])*(mediumQA[i][3] - mediumQA[i][2] + 1)/2; // sum of consecutive numbers is (number1 + number2)*number of numbers/2
			score = mediumQA[i][0] == mediumQA[i][1] ? score+2:score; // add score
			outputResult(2, i, mediumQA); // output new result
		}
		returnHome(); // go home
	}	
	void level3Math(int questions) throws IOException, InterruptedException // product of numbers from 1 to 12
	{
		int [][] hardQA = new int [questions][4];
		for(int i = 0; i < questions; i++)
		{
			int num = number(3);
			hardQA[i][2] = num/100000; // get number1
			hardQA[i][3] = num%100000; // get number2
			System.out.print("Question " + (i+1) + "\nFind the product of all integers from " + hardQA[i][2] + " to " + hardQA[i][3] + " inclusive: ");
			hardQA[i][0] = getInput();
			hardQA[i][1] = 1;
			for(int j = hardQA[i][2]; j <= hardQA[i][3]; j++) // calculate product
				hardQA[i][1] *= j;
			score = hardQA[i][0] == hardQA[i][1] ? score+3:score; // add score
			outputResult(3, i, hardQA); // output new result
		}
		returnHome(); // go home
	}

	//////////////////////////////////////////////////////////////////////////////////////
	///////////////////// END OF SUBPAGES / START OF INITIALIZATION //////////////////////
	//////////////////////////////////////////////////////////////////////////////////////
	
	public static void main (String [] args) throws IOException, InterruptedException // initialize menu header
	{
		layout.add("  ____       _       _______             __ ");
		layout.add(" / __ \\__ __(_)__   /_  __(_)_ _  ___   / / ");
		layout.add("/ /_/ / // / /_ /    / / / /  ' \\/ -_) /_/  ");
		layout.add("\\___\\_\\_,_/_//__/   /_/ /_/_/_/_/\\__/ (_)  -");
		layout.add("Vincent Zhang | April 2, 2019 | ICS3U7-02-");
		layout.add("Quiz Time tests knowledge on mathematics. There are three difficulties: ");
		layout.add("Easy (1 point): sum of two integer numbers.    ");
		layout.add("Medium (2 points): sum of consecutive numbers. ");
		layout.add("Hard (3 points): product of consecutive.      -");
		layout.add("Score: " + score + "-");
		
		QuizAssignment obj = new QuizAssignment(); // create obj of class QuizAssignment
		obj.homePage(); // call method of object / instance of class QuizAssignment so references are made from non-static context
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	///////////////////// END OF INITIALIZATION / END OF PROGRAM /////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////
}