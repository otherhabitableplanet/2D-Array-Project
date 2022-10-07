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
    public static void putInCsv(String name, String[][] data){
        try {
            File myObj = new File(name);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
                FileWriter writer = new FileWriter(name);
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
            } else {
                System.out.println("File already exists.");
            }
          } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
          }

    }
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
                    score += 1;
                }
            }
            marksData[i - 1][4] = String.valueOf(score);
        }
        // printData(marksData);
        return marksData;
    }

    public static String[][] answers(Scanner reader, String answerDataFileName) {

        // String answerDataFileName = "answerData1.txt";
        int answerTotalLines = countLines(answerDataFileName);
        System.out.println(answerTotalLines);
        String[] lines = new String[answerTotalLines];
        readLines(answerDataFileName, lines);
        String[][] answerData = new String[(answerTotalLines + 1) / 3][];

        for (int i = 0; i < answerTotalLines; i += 3){
            String name = lines[i];
            String answer = lines[i + 1];
            answerData[i/3] = new String[2];
            answerData[i/3][0] = name;
            answerData[i/3][1] = answer;
        }

        printData(answerData);
        return answerData;
    }

    public static void readLines(String filename, String[] lines){
        BufferedReader br = null;
        int count = 0;
        System.out.println("Running readQuestions method:");
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
    public static String[][] students(Scanner reader) {
        String studentDataFileName = getStudentDataFileName(reader);
        int studentTotalLines = countLines(studentDataFileName);
        String[][] studentData = new String[studentTotalLines][ansLenCount(studentDataFileName)];
        readStudents(studentDataFileName, studentData);
        printData(studentData);
        return studentData;
    }
    public static String[][] questionsAndAnswers(Scanner reader) {
        String questionDataFileName = getQuestionDataFileName(reader);
        if (!isAnswerFile(questionDataFileName)){
            System.out.println("We made it somewhere");
            int questionTotalLines = countLines(questionDataFileName);
            String[] questionData = new String[questionTotalLines];
            readQuestions(questionDataFileName, questionData);
            print1DData(questionData);
            // return questionData;
            String answerDataFileName = getAnswerDataFileName(reader);
            return answers(reader, answerDataFileName);
        }
        return answers(reader, questionDataFileName);



    }
    public static String[][] responses(Scanner reader) {
        String responseDataFileName = getResponseDataFileName(reader);
        int responseTotalLines = countLines(responseDataFileName);
        String[][] responseData = new String[responseTotalLines][ansLenCount(responseDataFileName)];
        readResponses(responseDataFileName, responseData);
        printData(responseData);
        return responseData;
    }
    public static int ansLenCount(String filename){
        BufferedReader br = null;
        int answerLength = 0;
        System.out.println("Running ansLenCount method:");
        try {
            System.out.println("Hit the try:");
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
    public static void readResponses(String filename, String[][] responseData){
        BufferedReader br = null;
        int count = 0;
        System.out.println("Running readResponses method:");
        try {
            // Put our file reader into the file given to the method upon calling
            br = new BufferedReader(new FileReader(filename));
               	//One way of reading the file
			// System.out.println("Counting the number of lines...");
			String contentLine = br.readLine();
			while (contentLine != null) {


				System.out.println(contentLine + " " + count);
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
     *
     * @param filename
     * @param questionData
     */
    public static void readQuestions(String filename, String[] questionData){
        BufferedReader br = null;
        int count = 0;
        System.out.println("Running readQuestions method:");
        try {
            // Put our file reader into the file given to the method upon calling
            br = new BufferedReader(new FileReader(filename));
               	//One way of reading the file
			// System.out.println("Counting the number of lines...");
			String contentLine = br.readLine();
			while (contentLine != null) {

				System.out.println(contentLine + " " + count);
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
     * @param filename
     * @param studentData
     */
    public static void readStudents(String filename, String[][] studentData){
        BufferedReader br = null;
        int count = 0;
        System.out.println("Running readStudents method:");
        try {
            // Put our file reader into the file given to the method upon calling
            br = new BufferedReader(new FileReader(filename));
               	//One way of reading the file
			// System.out.println("Counting the number of lines...");
			String contentLine = br.readLine();
			while (contentLine != null) {

				System.out.println(contentLine + " " + count);
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
    public static void printData(String[][] data) {
        for (int i = 0; i < data.length; i++){
            System.out.println(String.join(" ", data[i]));
        }
    }
    public static void print1DData(String[] data) {
        for (int i = 0; i < data.length; i++){
            System.out.println(String.join(" ", data[i]));
        }
    }
    /**
     * Gets the name of the question data file as an input
     * Asks that they include the file extension
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
    public static boolean isAnswerFile(String filename){
        BufferedReader br = null;
        boolean isAnswerFile = false;
        System.out.println("Running readStudents method:");
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
     * Asks that they include the file extension
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

    public static String getAnswerDataFileName(Scanner reader){
        // Scanner reader = new Scanner(System.in);
        System.out.println("Input the name of the file containing the answer data (Make sure to include the file extension): ");
        // Reminder to make a checking loop to ensure that they enter a valid file
        String answerFile = reader.nextLine();
        // reader.close();
        return(answerFile);
    }
}
