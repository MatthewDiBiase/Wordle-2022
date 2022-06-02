//Wordle Possible Guesses: https://github.com/tabatkins/wordle-list/blob/main/words
//Wordle Possible Words: https://gist.github.com/cfreshman/a7b776506c73284511034e63af1017ee
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
public class Wordle {

	private int numGuesses;
	private String word;
	private String guess;
	private StringBuilder usedLetters = new StringBuilder();
	private StringBuilder rightLetters = new StringBuilder();
	private StringBuilder spotLetters = new StringBuilder();

	/**
	 * Constructs wordle with a set word, for testing purposes.
	 * @param w
	 */
	public Wordle(String w) {

		numGuesses = 6;

		if (w.length() != 5) {

			throw new RuntimeException("Word length must be 5.");

		}
		else {

			word = w.toLowerCase();

		}

	}
	
	/**
	 * Constructs wordle with a random word.
	 */
	public Wordle() {

		numGuesses = 6;

		word = getRandomWord();

	}

	/**
	 * Sets up a canvas and plays the wordle game.
	 */
	public void play() {

		boolean solved = false;

		StdDraw.setCanvasSize(550, 600);
		StdDraw.setXscale(0, 550);
		StdDraw.setYscale(0, 600);
		StdDraw.enableDoubleBuffering();
		StdDraw.textLeft(295, 550, "Enter Guess:");
		StdDraw.show();

		double deltaX = 50;
		double startX = 50;
		double startY = 550;

		while (numGuesses > 0) {

			String g = getGuessCanvas();

			while (isWord(g) == false) { //makes sure guess is valid
				
				numGuesses++;
				StdDraw.text(420, 510, "Not a valid word.");
				StdDraw.show();
				StdDraw.pause(1000);
				StdDraw.setPenColor(Color.WHITE);
				StdDraw.filledRectangle(420, 510, 60, 10);
				StdDraw.setPenColor(Color.BLACK);

				int recX = 400;
				for (int j = 0; j < 5; j++) {

					StdDraw.setPenColor(Color.WHITE);
					StdDraw.filledRectangle(recX, 550, 8, 8);
					StdDraw.setPenColor(Color.BLACK);
					recX += 15;

				}

				StdDraw.show();

				g = getGuessCanvas();

			}

			String posGuess = getLetterPositions();
			int j = 0;

			for (int i = 0; i < posGuess.length(); i++) { //draws word

				double x = startX + j * deltaX;
				j++;
				double y = startY;

				String ch;

				if (posGuess.substring(i, i + 1).equals("[")) {

					StdDraw.setPenColor(Color.YELLOW);
					ch = posGuess.substring(i + 1, i + 2);
					i += 2;

				}
				else if (posGuess.substring(i, i + 1).equals("(")) {

					StdDraw.setPenColor(Color.GREEN);
					ch = posGuess.substring(i + 1, i + 2);
					i += 2;

				}
				else {

					StdDraw.setPenColor(Color.LIGHT_GRAY);
					ch = posGuess.substring(i, i + 1);

				}

				StdDraw.filledRectangle(x, y, 25, 25);
				StdDraw.setPenColor(Color.BLACK);
				StdDraw.rectangle(x, y, 25, 25);
				StdDraw.text(x, y, ch);

				StdDraw.show();
				StdDraw.pause(250);

			}
			
			double x1 = 50;
			double y1 = 225;
			String keys = "qwertyuiopasdfghjklzxcvbnm";

			for (int k = 0; k < 26; k++) { //draws keyboard

				String letter = Character.toString(keys.charAt(k));
				if (rightLetters.indexOf(letter) != -1) {

					StdDraw.setPenColor(Color.GREEN);

				}
				else if (spotLetters.indexOf(letter) != -1) {

					StdDraw.setPenColor(Color.YELLOW);

				}
				else if (usedLetters.indexOf(letter) != -1) {

					StdDraw.setPenColor(Color.GRAY);

				}
				else {

					StdDraw.setPenColor(Color.LIGHT_GRAY);

				}
				StdDraw.filledRectangle(x1, y1, 25, 25);
				StdDraw.setPenColor(Color.BLACK);
				StdDraw.rectangle(x1, y1, 25, 25);
				StdDraw.text(x1, y1, letter);
				x1 += 50;
				if (letter.equals("p")) {
					
					y1 -= 50;
					x1 = 75;

				}
				else if (letter.equals("l")) {
					
					y1 -= 50;
					x1 = 125;
					
				}

			}
			
			startY -= 50;
			StdDraw.show();
			StdDraw.pause(5);

			if (word.equals(guess)) {

				String output = "Congratulations! The word is " + "\"" + word + "\".";
				StdDraw.textLeft(25, 75, output);
				StdDraw.textLeft(25, 55, "You got it in " + (6 - numGuesses) + " guesses.");
				StdDraw.show();
				solved = true;
				break;

			}

		}

		if (solved == false) {

			String output = "Better luck next time. The word was " + "\"" + word + "\".";
			StdDraw.textLeft(25, 75, output);
			StdDraw.show();

		}

	}
	
	/**
	 * Gets the guess from the canvas and displays it.
	 * @return
	 */
	public String getGuessCanvas() {

		//StdDraw.textLeft(295, 550, "Enter Guess:");

		ArrayList<Character> showGuess = new ArrayList<Character>();

		int x = 400;
		int y = 550;
		String result = "";
		char ch;
		boolean enterPressed = false;
		boolean bsPressed = false;

		while (enterPressed == false) {

			if (StdDraw.hasNextKeyTyped()) {

				ch = StdDraw.nextKeyTyped();
				if (ch == '\n') {

					enterPressed = true;

				}
				if (ch == '\b' && showGuess.size() > 0) {

					bsPressed = true;
					showGuess.remove(showGuess.size() - 1);

				}
				else if (ch == '\b') {

					bsPressed = true;
					x += 15;

				}

				if ((int)(ch) >= 97 && (int)(ch) <= 123) {

					if (enterPressed == false && bsPressed == false) {

						showGuess.add(ch);

					}

				}
				
				if ((int)(ch) < 97 || (int)(ch) > 123) {
					
					//do nothing
					
				}
				else if (bsPressed == false) {

					StdDraw.text(x, y, Character.toString(ch));
					x += 15;

				}
				if (bsPressed == true) {

					x -= 15;
					StdDraw.setPenColor(Color.WHITE);
					StdDraw.filledRectangle(x, y, 8, 8);
					StdDraw.setPenColor(Color.BLACK);

				}

				bsPressed = false;

				StdDraw.show();

			}	

		}

		for (int i = 0; i < showGuess.size(); i++) {

			result += showGuess.get(i);

		}

		x = 400;
		for (int j = 0; j < 15; j++) {

			StdDraw.setPenColor(Color.WHITE);
			StdDraw.filledRectangle(x, y, 8, 8);
			StdDraw.setPenColor(Color.BLACK);
			x += 15;

		}
		
		numGuesses--;
		guess = result;
		return result;

	}

	/**
	 * Makes sure that word is a valid word from the attached files.
	 * @param word
	 * @return
	 */
	public boolean isWord(String word) {

		BufferedReader input;
		try {
			input = new BufferedReader(new FileReader("allwordle.txt"));
			String line;

			while ((line = input.readLine()) != null) {

				String[] words = line.split("\n");
				if (words[0].equals(word)) {

					input.close();
					return true;

				}

			}
			
		} catch (IOException e) {

			e.printStackTrace();

		}

		return false;

	}

	/**
	 * Checks if the letters are in the right positions.
	 * If a letter is in the right position, it is wrapped in ().
	 * If a letter is in the word but the wrong position, it is wrapped in [].
	 * Otherwise, the letter is wrapped in nothing. Returns this new string.
	 * @return
	 */
	public String getLetterPositions() {

		ArrayList<Character> rightChars = new ArrayList<Character>();
		for (int i = 0; i < word.length(); i++) {

			rightChars.add(word.charAt(i));

		}

		ArrayList<Character> guessChars = new ArrayList<Character>();
		for (int j = 0; j < guess.length(); j++) {

			guessChars.add(guess.charAt(j));

		}

		ArrayList<String> newChars = new ArrayList<String>(5);
		//copies rightChars into newChars
		for (int c = 0; c < 5; c++) {

			newChars.add(rightChars.get(c).toString());

		}

		ArrayList<String> resultChars = new ArrayList<String>(5);
		//copies rightChars into resultChars
		for (int c = 0; c < 5; c++) {

			resultChars.add(null);

		}

		for (int k = 0; k < guess.length(); k++) {

			if (guessChars.get(k) == rightChars.get(k)) { //if right spot

				resultChars.set(k, "(" + guessChars.get(k) + ")");
				rightLetters.append(guessChars.get(k));
				newChars.set(k, "!"); //placeholder

			}

		}

		for (int d = 0; d < 5; d++) {

			int temp = newChars.indexOf(guessChars.get(d).toString());

			if (temp != -1 && (!newChars.get(d).equals("!"))) {

				resultChars.set(d, "[" + guessChars.get(d).toString() + "]");
				spotLetters.append(guessChars.get(d));
				newChars.set(temp, "@");

			}

		}

		for (int a = 0; a < 5; a++) {

			if (resultChars.get(a) == null) {

				resultChars.set(a, guessChars.get(a).toString());

				if (usedLetters.indexOf(guessChars.get(a).toString()) == -1) {

					usedLetters.append(guessChars.get(a).toString());

				}

			}

		}

		StringBuilder s = new StringBuilder();

		for (int b = 0; b < newChars.size(); b++) {

			s.append(resultChars.get(b));

		}

		return s.toString();

	} 

	/**
	 * Gets a random word from the attached file of words.
	 * @return
	 */
	public String getRandomWord() {

		BufferedReader input;
		try {
			input = new BufferedReader(new FileReader("wordlesolutions.txt"));
			String line;
			int randomLine = (int)(Math.random() * 2309);
			int numTimes = 0;

			while ((line = input.readLine()) != null) {

				String[] words = line.split("\n");
				if (numTimes == randomLine) {

					input.close();
					return words[0].toLowerCase();

				}

				numTimes++;

			}

		} catch (IOException e) {

			e.printStackTrace();

		}

		return "Error Occured";

	}

}


