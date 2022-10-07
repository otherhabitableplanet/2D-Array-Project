import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;

class math_checker {
    public static void main(String[] args){

        Scanner reader = new Scanner(System.in);

        String[][] studentData = students(reader);

        String[][] answerData = questionsAndAnswers(reader);


        String[][] responseData = responses(reader);



        System.out.println("Enter the name of your new file (include file type): ");

        String newFileName = reader.nextLine();

        putInCsv(newFileName, checkAnswers(responseData, answerData));

        reader.close();
    }
    /**
     * Writes a two dimensional array into a csv file
     * @param name name of the file to be created
     * @param data the 2D array that is being written into the file
     */
    public static void putInCsv(String name, String[][] data){
        try {
            // Initialize new file
            File myObj = new File(name);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
                FileWriter writer = new FileWriter(name);
                // Loop through both axis of the array and write each section into the file
                // Separate j values with commas and i values with new lines
                for (int i = 0; i < data.length; i++){
                    for (int j = 0; j < data[i].length; j++){
                        writer.write(data[i][j]);
                        if (j < data[i].length - 1){
                            writer.write(",");
                        }
                    }
                    writer.write("\n");

                }
                writer.close();
            // If file already exists, give an appropriate warning
            } else {
                System.out.println("File already exists.");
            }
          } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
          }

    }
    /**
     * Checks the answer file data against the response file data
     * @param responseData the response file data in the form of a 2D array
     * @param answerData the answer file data in the form of a 2D array
     * @return the marks data including the information of the students along with their total score
     */
    public static String[][] checkAnswers(String[][] responseData, String[][] answerData){
        String[][] marksData = new String[responseData.length - 1][5];
        for (int i = 1; i < responseData.length; i++){
            String[] student = responseData[i];
            marksData[i - 1][0] = student[0];
            marksData[i - 1][1] = student[1];
            marksData[i - 1][2] = student[2];
            marksData[i - 1][3] = student[3];
            int score = 0;
            for (int j = 4; j < student.length; j++){
                String studentAnswer = student[j];
                String correctAnswer = answerData[j-4][1];
                if (studentAnswer.equals(correctAnswer)){
                    // Adds one to the score of the given student for each correct answer
                    score += 1;
                }
            }
            marksData[i - 1][4] = String.valueOf(score);
        }
        // printData(marksData);
        return marksData;
    }
    /**
     * Counts the lines of the answer file, reads each line, then arranges a 2D array by answer number
     * [A1, 4, 6]
     * [A2, 3, 5]
     * @param reader the Scanner object that is used throughout the program to read user inputs
     * @param answerDataFileName the name of the answer data file
     * @return a 2D array of the answer data for each question
     */
    public static String[][] answers(Scanner reader, String answerDataFileName) {

        // String answerDataFileName = "answerData1.txt";
        int answerTotalLines = countLines(answerDataFileName);
        // System.out.println(answerTotalLines);
        String[] lines = new String[answerTotalLines];
        readLines(answerDataFileName, lines);
        String[][] answerData = new String[(answerTotalLines + 1) / 3][];

        // Loops through the lines in the answer file
        // Bundles the data into groups of 2, answer number and answer values
        for (int i = 0; i < answerTotalLines; i += 3){
            String name = lines[i];
            String answer = lines[i + 1];
            answerData[i/3] = new String[2];
            answerData[i/3][0] = name;
            answerData[i/3][1] = answer;
        }

        // printData(answerData);
        return answerData;
    }
    /**
     * Reads through the lines of a file and assign each line to a string array called lines
     * @param filename the name of the file to read through
     * @param lines the string array to read the data into
     */
    public static void readLines(String filename, String[] lines){
        BufferedReader br = null;
        int count = 0;
        // System.out.println("Running readQuestions method:");
        try {
            // Put our file reader into the file given to the method upon calling
            br = new BufferedReader(new FileReader(filename));
               	//One way of reading the file
			// System.out.println("Counting the number of lines...");
			String contentLine = br.readLine();
			while (contentLine != null) {

				// System.out.println(contentLine + " " + count);
                lines[count] = contentLine;
                contentLine = br.readLine();

                count++;
			}
       	}
		catch (IOException e){
	   		e.printStackTrace();
       	}
       	finally{
	   		try {
	      		if (br != null){
					br.close();
				}
	   		}
	   		catch (IOException e) {
				System.out.println("Error in closing the BufferedReader");

	   		}
		}
    }
    /**
     * Takes input for the name of the student data file, counts the lines in the file, creates a 2D array, then reads the data into that array
     * @param reader the Scanner object used to take user input
     * @return a 2D array of the student data separated by spaces
     */
    public static String[][] students(Scanner reader) {
        String studentDataFileName = getStudentDataFileName(reader);
        int studentTotalLines = countLines(studentDataFileName);
        String[][] studentData = new String[studentTotalLines][ansLenCount(studentDataFileName)];
        readStudents(studentDataFileName, studentData);
        // printData(studentData);
        return studentData;
    }
    /**
     * Gets the name of a file
     * If that file has question data, count lines, read the data into an array
     * If it has answer data, skip question data step and count answer lines, read that data into an array
     * @param reader the Scanner object used to take user input
     * @return a 2D array of the answer data either given instead of question data or after it
     */
    public static String[][] questionsAndAnswers(Scanner reader) {
        String questionDataFileName = getQuestionDataFileName(reader);
        if (!isAnswerFile(questionDataFileName)){
            // System.out.println("We made it somewhere");
            int questionTotalLines = countLines(questionDataFileName);
            String[] questionData = new String[questionTotalLines];
            readQuestions(questionDataFileName, questionData);
            // print1DData(questionData);
            // return questionData;
            String answerDataFileName = getAnswerDataFileName(reader);
            return answers(reader, answerDataFileName);
        }
        return answers(reader, questionDataFileName);


    }
    /**
     * Takes input for the name of the response data file, counts lines, reads data into a 2D array
     * @param reader the Scanner object used to take user input
     * @return a 2D array of the response data
     */
    public static String[][] responses(Scanner reader) {
        String responseDataFileName = getResponseDataFileName(reader);
        int responseTotalLines = countLines(responseDataFileName);
        String[][] responseData = new String[responseTotalLines][ansLenCount(responseDataFileName)];
        readResponses(responseDataFileName, responseData);
        // printData(responseData);
        return responseData;
    }
    /**
     * Reads through a file of response data and counts the number of commas to determine the length needed to read it into an array
     * @param filename the name of the file to count answers in
     * @return the number of commas in the csv file
     */
    public static int ansLenCount(String filename){
        BufferedReader br = null;
        int answerLength = 0;
        System.out.println("Running ansLenCount method:");
        try {
            // System.out.println("Hit the try:");
            // Put our file reader into the file given to the method upon calling
            br = new BufferedReader(new FileReader(filename));
               	//One way of reading the file
			// System.out.println("Counting the number of lines...");
			String contentLine = br.readLine();
			for (int i = 0; i < contentLine.length(); i++){
                if (contentLine.charAt(i) == ','){
                    answerLength++;
                }
                else {
                    continue;
                }
            }
       	}
		catch (IOException e){
	   		e.printStackTrace();
       	}
       	finally{
	   		try {
	      		if (br != null){
					br.close();
				}
	   		}
	   		catch (IOException e) {
				System.out.println("Error in closing the BufferedReader");

	   		}
		}
            return answerLength;
    }
    /**
     * Reads data from a response data file into a 2D array
     * @param filename name of file to read responses from
     * @param responseData 2D array to read responses into
     */
    public static void readResponses(String filename, String[][] responseData){
        BufferedReader br = null;
        int count = 0;
        // System.out.println("Running readResponses method:");
        try {
            // Put our file reader into the file given to the method upon calling
            br = new BufferedReader(new FileReader(filename));
               	//One way of reading the file
			// System.out.println("Counting the number of lines...");
			String contentLine = br.readLine();
			while (contentLine != null) {


				// System.out.println(contentLine + " " + count);
                responseData[count] = contentLine.split(",");
                contentLine = br.readLine();
                count++;
			}
       	}
		catch (IOException e){
	   		e.printStackTrace();
       	}
       	finally{
	   		try {
	      		if (br != null){
					br.close();
				}
	   		}
	   		catch (IOException e) {
				System.out.println("Error in closing the BufferedReader");

	   		}
		}
    }
    /**
     * Reads questions data from a given file into a string array
     * @param filename the name of the file to read from
     * @param questionData the array to read data into
     */
    public static void readQuestions(String filename, String[] questionData){
        BufferedReader br = null;
        int count = 0;
        // System.out.println("Running readQuestions method:");
        try {
            // Put our file reader into the file given to the method upon calling
            br = new BufferedReader(new FileReader(filename));
               	//One way of reading the file
			// System.out.println("Counting the number of lines...");
			String contentLine = br.readLine();
			while (contentLine != null) {

				// System.out.println(contentLine + " " + count);
                questionData[count] = contentLine;
                contentLine = br.readLine();

                count++;
			}
       	}
		catch (IOException e){
	   		e.printStackTrace();
       	}
       	finally{
	   		try {
	      		if (br != null){
					br.close();
				}
	   		}
	   		catch (IOException e) {
				System.out.println("Error in closing the BufferedReader");

	   		}
		}
    }
    /**
     * Gets the name of the student data file as an input
     * Asks that they include the file extension
     * @param reader the Scanner object used to take user input
     * @return the file name
     */
    public static String getStudentDataFileName(Scanner reader){
        // Scanner reader = new Scanner(System.in);
        System.out.println("Input the name of the file containing the student data (Make sure to include the file extension): ");
        // Reminder to make a checking loop to ensure that they enter a valid file
        String file = reader.nextLine();
        // reader.close();
        return(file);
    }
    /**
     * Counts the number of lines in a file and prints the file
     * @param file the file that is being counted
     * @return the number of lines in the file
     */
    public static int countLines(String file) {
        // Declare a file reader called br
        BufferedReader br = null;
        // Declare a counter
        int count = 0;
        try {
            // Put our file reader into the file given to the method upon calling
            br = new BufferedReader(new FileReader(file));
               	//One way of reading the file
			System.out.println("Running countLines method:");
			String contentLine = br.readLine();
			while (contentLine != null) {
                count++;
				// System.out.println(contentLine);
				contentLine = br.readLine();
			}
            return count;
       	}
		catch (IOException e){
	   		e.printStackTrace();
            return -1;
       	}
       	finally{
	   		try {
	      		if (br != null){
					br.close();
				}
	   		}
	   		catch (IOException e) {
				System.out.println("Error in closing the BufferedReader");
                return -1;
	   		}
		}
    }
    /**
     * Reads the file and inputs the data into the list "studentdata"
     * @param filename the name of the file that is being read
     * @param studentData the 2D array that we are reading data into
     */
    public static void readStudents(String filename, String[][] studentData){
        BufferedReader br = null;
        int count = 0;
        // System.out.println("Running readStudents method:");
        try {
            // Put our file reader into the file given to the method upon calling
            br = new BufferedReader(new FileReader(filename));
               	//One way of reading the file
			// System.out.println("Counting the number of lines...");
			String contentLine = br.readLine();
			while (contentLine != null) {

				// System.out.println(contentLine + " " + count);
                studentData[count] = contentLine.split(",");
                contentLine = br.readLine();
                count++;
			}
       	}
		catch (IOException e){
	   		e.printStackTrace();
       	}
       	finally{
	   		try {
	      		if (br != null){
					br.close();
				}
	   		}
	   		catch (IOException e) {
				System.out.println("Error in closing the BufferedReader");

	   		}
		}
    }
    /**
     * Prints a 2D array line by line
     * @param data the 2D array that we want to print
     */
    public static void printData(String[][] data) {
        for (int i = 0; i < data.length; i++){
            System.out.println(String.join(" ", data[i]));
        }
    }
    /**
     * Prints a string array line by line
     * @param data the array that we want to print
     */
    public static void print1DData(String[] data) {
        for (int i = 0; i < data.length; i++){
            System.out.println(String.join(" ", data[i]));
        }
    }
    /**
     * Gets the name of the question data file as an input
     * Asks that they include the file extension
     * @param reader the Scanner object used to take user input
     * @return the file name
     */
    public static String getQuestionDataFileName(Scanner reader){
        // Scanner reader = new Scanner(System.in);
        System.out.println("Input the name of the file containing the question data or containing the answer data (Second option will skip the rest of this step. Make sure to include the file extension): ");
        // Reminder to make a checking loop to ensure that they enter a valid file
        String questionFile = reader.nextLine();
        // reader.close();
        return(questionFile);
    }
    /**
     * Checks to see if a file name that has been given as an input is a file of answer data
     * @param filename the name of the file that we want to check
     * @return if it is or is not an answer file
     */
    public static boolean isAnswerFile(String filename){
        BufferedReader br = null;
        boolean isAnswerFile = false;
        // System.out.println("Running readStudents method:");
        try {
            // Put our file reader into the file given to the method upon calling
            br = new BufferedReader(new FileReader(filename));
               	//One way of reading the file
			// System.out.println("Counting the number of lines...");
			String contentLine = br.readLine();
			if (contentLine.toLowerCase().startsWith("a")){
                isAnswerFile = true;
            }
            else {
                isAnswerFile = false;
            }
			}

		catch (IOException e){
	   		e.printStackTrace();
       	}
       	finally{
	   		try {
	      		if (br != null){
					br.close();
				}
	   		}
	   		catch (IOException e) {
				System.out.println("Error in closing the BufferedReader");

	   		}
		}
        return isAnswerFile;
    }
    /**
     * Gets the name of the question data file as an input
     * @param reader the Scanner object used to take user input
     * @return the file name
     */
    public static String getResponseDataFileName(Scanner reader){
        // Scanner reader = new Scanner(System.in);
        System.out.println("Input the name of the file containing the response data (Make sure to include the file extension): ");
        // Reminder to make a checking loop to ensure that they enter a valid file
        String responseFile = reader.nextLine();
        // reader.close();
        return(responseFile);
    }
    /**
     * Gets the name of the answer data file as an input
     * @param reader the Scanner object used to take user input
     * @return the file name
     */
    public static String getAnswerDataFileName(Scanner reader){
        // Scanner reader = new Scanner(System.in);
        System.out.println("Input the name of the file containing the answer data (Make sure to include the file extension): ");
        // Reminder to make a checking loop to ensure that they enter a valid file
        String answerFile = reader.nextLine();
        // reader.close();
        return(answerFile);
    }
}
