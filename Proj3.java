import java.util.Scanner; 
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * Purpose of program:  Determines if a new movie review is. 
 * positive or negative based on previously analyzed
 * movie reviews in a dataset from Rotten Tomatoes
 * Author Name: Meghana Karthic 
 * JHED ID: 785802
 * Date: October 14, 2021
 */
 
public class Proj3 {

   /**
    * Generates a menu-driven interface that allows the user to 
    * repeatedly select a task, until they decide to quit. 
    *
    * @param file the movie review file that the user selects
    * @param scnr to read the user input
    */
   public static void generateMenu(String file, 
                                   Scanner scnr) throws IOException {
   
      final String GETFILE = "enter input filename: ";
      final String GETWORD = "enter word to score --> ";
      final String GETREVIEW = "enter review line --> ";
      final String GETDELTA = "enter new delta [0,1] --> ";
      final String NOCOMPUTE = "review score can't be computed";
      final String DOWN = "thumbs down";
      final String UP = "thumbs up";
      final String BYE = "goodbye";
      final String INVALID = "invalid option, try again";
      final String GETCHOICE = "enter choice by number --> ";
      final String OPTION1 = "1. quit program";
      final String OPTION2 = "2. word scores";
      final String OPTION3 = "3. full review";
      final String OPTION4 = "4. cutoff delta";
   
      boolean inputDone = false;
      String word = "";
      String reviewLine;
      double delta = 0.0;
      String commentary = "";
      int reviewCount;
      
      while (!inputDone) {
       
         System.out.println(OPTION1);
         System.out.println(OPTION2);
         System.out.println(OPTION3);
         System.out.println(OPTION4);
         System.out.print(GETCHOICE);
         
         String input = scnr.nextLine();
         
         if ("1".equals(input)) {
            System.out.println(BYE);
            inputDone = true;
         }
         
         else if ("2".equals(input)) {
            System.out.print(GETWORD);
            word = scnr.nextLine().toLowerCase();
            reviewCount = countReviews(word, file);
            double averageScore = calculateScore(word, file) 
               / (double) reviewCount;
            createFile(word, file);
            
            if (reviewCount == 0) {
               System.out.println(word + " appears in 0 reviews" + "\n");
            }
            
            else {
               System.out.println(word + " appears in "
                   + (int) reviewCount + " reviews");
               System.out.printf("average score for those reviews is %.5f"
                  + "\n", averageScore);
               System.out.println("");
            }
         }
         
         else if ("3".equals(input)) {
            System.out.print(GETREVIEW);
            reviewLine = scnr.nextLine().toLowerCase();
            double reviewScore = calculateSentimentScore(reviewLine, file); 
            
            if (reviewScore == -1) {
               System.out.println(NOCOMPUTE + "\n");
            }
            
            else if (reviewScore > 2 + delta) {
               commentary = UP;
               System.out.printf("full review score is %.5f" 
                  + "\n", reviewScore);
               System.out.println(commentary + "\n"); 
            }
               
            else if (reviewScore < 2 - delta) {
               commentary = DOWN;
               System.out.printf("full review score is %.5f" 
                  + "\n", reviewScore);
               System.out.println(commentary + "\n"); 
            } 
            
            else {
               System.out.printf("full review score is %.5f" 
                  + "\n" + "\n", reviewScore);
            }      
         
         }
         
         else if ("4".equals(input)) {
            System.out.print(GETDELTA); 
            delta = scnr.nextDouble();
            scnr.nextLine(); // captures new line character
            System.out.println("");
         }
         
         else {
            System.out.println(INVALID + "\n");
         }  
      }
   }
   
   /**
   * Counts the number of movie reviews that the user's word appears in.
   *
   * @param userWord the word entered by the user in menu option 2
   * @param file the movie review file that the user selects
   * @return the number of the reviews that the word appears in  
   */
   public static int countReviews(String userWord, 
                                  String file) throws IOException {
     
      FileInputStream fileByteStream = new FileInputStream(file);
      Scanner inFS = new Scanner(fileByteStream);
   
      int count = 0; 
      
      while (inFS.hasNextLine()) {
         String currLine = inFS.nextLine();
         currLine = currLine.toLowerCase();
        
         if (currLine.contains(userWord)) {
            count++;
         } 
      }
      
      fileByteStream.close();
      
      return count;
   }
   
   /** 
   * Calculates the sum of the scores of the movie reviews that. 
   * contain the user's word
   * 
   * @param userWord the word entered by the user in menu option 2
   * @param file the movie review file that the user selects
   * @return the sum of the review scores
   */
   public static int calculateScore(String userWord, 
                                    String file) throws IOException {
   
      FileInputStream fileByteStream = new FileInputStream(file);
      Scanner inFS = new Scanner(fileByteStream);
      
      int scoreSum = 0;
      
      while (inFS.hasNextLine()) {
         int score = inFS.nextInt();
         String currLine = inFS.nextLine();
         currLine = currLine.toLowerCase();
         
         if (currLine.contains(userWord)) {
            scoreSum = scoreSum + score;
         }
      }
      
      fileByteStream.close();
      
      return scoreSum;
   }
   
   /**
   * Creates a file with the movie reviews that the user's word.
   * appears in
   *
   * @param userWord the word entered by the user in menu option 2
   * @param file the movie review file that the user selects
   */
   public static void createFile(String userWord, 
                                  String file) throws IOException {
     
      FileInputStream fileByteStream = new FileInputStream(file);
      Scanner inFS = new Scanner(fileByteStream);
      
      FileOutputStream fileStream = new FileOutputStream(userWord + ".txt");
      PrintWriter outFS = new PrintWriter(fileStream);
   
      int count = 0; 
      
      while (inFS.hasNextLine()) {
         String currLine = inFS.nextLine();
         currLine = currLine.toLowerCase();
         
         if (currLine.contains(userWord)) {
            outFS.println(currLine);
         } 
      }
      
      outFS.flush();
      fileByteStream.close();
      fileStream.close();
   }
   
   /** 
   * Calculates the sentiment score of the user's review.
   * 
   * @param review the review entered by the user in menu option 3
   * @param file the movie review file that the user selects
   * @return the sentiment score of the user's movie review
   */
   public static double calculateSentimentScore(String review, 
                                               String file) throws IOException {
      Scanner linescan = new Scanner(review);
      
      // average score of the word
      double wordScore = 0;
      
      // sum of the average scores of each word in the line
      double totalScore = 0;
      
      /* the number of words in the review line that 
      don't appear in any movie reviews */
      double removeWords = 0;
      
      // the total number of words in the review line
      int totalWords = 0;
      
      /* total number of movie reviews that each word in 
      the line appears in */
      int totalCount = 0; 
      
      while (linescan.hasNext()) {
         
         String word = linescan.next();
         
         // finds the number of movie reviews that the word appears in
         int count = countReviews(word, file);
         totalCount = totalCount + count;
         
         /* calculates average score of word, 
         if the word appears in any movie reviews */
         if (count != 0) {
            int score = calculateScore(word, file); 
            wordScore = score / (double) count;
         }
         
         /* keeps track of the number of words that don't appear in 
         any movie reviews */
         else {
            removeWords++;
         }
         
         // calculates the total score of the user review
         totalScore = totalScore + wordScore;
         
         // counts the total number of words in the review
         totalWords++;
         
      }
      
      // calculates the average score of the user review
      if (totalCount > 0) {
         double sentimentScore = totalScore 
            / (totalWords - removeWords);
         return sentimentScore;
      }
      
      /* -1 is returned if none of the words appear in
      any movie reviews or else the method will produce NaN error */
      else {
         return -1;
      }
      
   } 
            
   public static void main(String[] args) throws IOException {
   
      Scanner scnr = new Scanner(System.in);
      System.out.print("enter input filename: ");
      String fileName = scnr.nextLine();
      System.out.println(""); 
      generateMenu(fileName, scnr);
      
   }
}