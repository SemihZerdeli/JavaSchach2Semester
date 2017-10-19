
// Das ist die Bewertungklasse. Sie wird lediglich zum bewerten einzelner Spielzüge anhand gewisser Kriterien genutzt.
public class Bewertung {

	// Folgend sind zweidimensionale Arrays für jeden Spielstein. Sie geben an welche Position wie gut oder schlecht ist und im Falle des Königs
	// wird der Zeitpunkt des Spieles mit einberechnet. Sie werden in der bewertePosition Methode ausgewertet.
	static int	pawnBoard[][]		= {{0, 0, 0, 0, 0, 0, 0, 0}, 
	          	             		   {50, 50, 50, 50, 50, 50, 50, 50}, 
	          	             		   {10, 10, 20, 30, 30, 20, 10, 10}, 
	          	             		   {5, 5, 10, 25, 25, 10, 5, 5}, 
	          	             		   {0, 0, 0, 20, 20, 0, 0, 0}, 
	          	             		   {5, -5, -10, 0, 0, -10, -5, 5}, 
	          	             		   {5, 10, 10, -20, -20, 10, 10, 5}, 
	          	             		   {0, 0, 0, 0, 0, 0, 0, 0}};

	static int	rookBoard[][]		= {{0, 0, 0, 0, 0, 0, 0, 0}, 
	          	             		   {5, 10, 10, 10, 10, 10, 10, 5}, 
	          	             		   {-5, 0, 0, 0, 0, 0, 0, -5}, 
	          	             		   {-5, 0, 0, 0, 0, 0, 0, -5},
	          	             		   {-5, 0, 0, 0, 0, 0, 0, -5}, 
	          	             		   {-5, 0, 0, 0, 0, 0, 0, -5}, 
	          	             		   {-5, 0, 0, 0, 0, 0, 0, -5}, 
	          	             		   {0, 0, 0, 5, 5, 0, 0, 0}};

	static int	knightBoard[][]		= {{-50, -40, -30, -30, -30, -30, -40, -50}, 
	          	               		   {-40, -20, 0, 0, 0, 0, -20, -40}, 
	          	               		   {-30, 0, 10, 15, 15, 10, 0, -30}, 
	          	               		   {-30, 5, 15, 20, 20, 15, 5, -30}, 
	          	               		   {-30, 0, 15, 20, 20, 15, 0, -30}, 
	          	               		   {-30, 5, 10, 15, 15, 10, 5, -30}, 
	          	               		   {-40, -20, 0, 5, 5, 0, -20, -40}, 
	          	               		   {-50, -40, -30, -30, -30, -30, -40, -50}};

	static int	bishopBoard[][]		= {{-20, -10, -10, -10, -10, -10, -10, -20}, 
	          	               		   {-10, 0, 0, 0, 0, 0, 0, -10}, 
	          	               		   {-10, 0, 5, 10, 10, 5, 0, -10}, 
	          	               		   {-10, 5, 5, 10, 10, 5, 5, -10}, 
	          	               		   {-10, 0, 10, 10, 10, 10, 0, -10}, 
	          	               		   {-10, 10, 10, 10, 10, 10, 10, -10},
	          	               		   {-10, 5, 0, 0, 0, 0, 5, -10}, 
	          	               		   {-20, -10, -10, -10, -10, -10, -10, -20}};

	static int	queenBoard[][]		= {{-20, -10, -10, -5, -5, -10, -10, -20}, 
	          	              		   {-10, 0, 0, 0, 0, 0, 0, -10}, 
	          	              		   {-10, 0, 5, 5, 5, 5, 0, -10}, 
	          	              		   {-5, 0, 5, 5, 5, 5, 0, -5}, 
	          	              		   {0, 0, 5, 5, 5, 5, 0, -5}, 
	          	              		   {-10, 5, 5, 5, 5, 5, 0, -10}, 
	          	              		   {-10, 0, 5, 0, 0, 0, 0, -10}, 
	          	              		   {-20, -10, -10, -5, -5, -10, -10, -20}};

	static int	kingMidBoard[][]	= {{-30, -40, -40, -50, -50, -40, -40, -30}, 
	          	                	   {-30, -40, -40, -50, -50, -40, -40, -30}, 
	          	                	   {-30, -40, -40, -50, -50, -40, -40, -30}, 
	          	                	   {-30, -40, -40, -50, -50, -40, -40, -30}, 
	          	                	   {-20, -30, -30, -40, -40, -30, -30, -20}, 
	          	                	   {-10, -20, -20, -20, -20, -20, -20, -10}, 
	          	                	   {20, 20, 0, 0, 0, 0, 20, 20},
	          	                	   {20, 30, 10, 0, 0, 10, 30, 20}};

	static int	kingEndBoard[][]	= {{-50, -40, -30, -20, -20, -30, -40, -50}, 
	          	                	   {-30, -20, -10, 0, 0, -10, -20, -30}, 
	          	                	   {-30, -10, 20, 30, 30, 20, -10, -30}, 
	          	                	   {-30, -10, 30, 40, 40, 30, -10, -30}, 
	          	                	   {-30, -10, 30, 40, 40, 30, -10, -30}, 
	          	                	   {-30, -10, 20, 30, 30, 20, -10, -30}, 
	          	                	   {-30, -30, 0, 0, 0, 0, -30, -30}, 
	          	                	   {-50, -30, -30, -30, -30, -30, -30, -50}};

	// Deklaration der bewerte Methode. Die bewerte Methode wird genutzt um jeden Spielzug anhand von Kriterien wie Position, Menge der restlichen 
	// Spielsteine, den möglichen Attacken oder den möglichen Bewegungen auszuwerten und eine Bewertung in Form einer Zahl zu geben.
	// Sie wird in der AlphaBetaSuche genutzt.
	public static int bewerte(int list, int depth) {

		int counter = 0;
		int material = bewerteSteine();

		counter += bewerteAttacke();
		counter += material;
		counter += bewerteBewegungen(list, depth, material);
		counter += bewertePositionen(material);

		Schach.dreheSpielfeld();
		material = bewerteSteine();

		counter -= bewerteAttacke();
		counter -= material;
		counter -= bewerteBewegungen(list, depth, material);
		counter -= bewertePositionen(material);

		Schach.dreheSpielfeld();

		return -(counter + depth * 50);
	}

	// Deklaration der bewerteAttacke Methode. Bewertet einen Spielzug anhand der gewähleisteten Sicherheit die ein Spielstein hat.
	public static int bewerteAttacke() {
		int counter = 0;
		int temp = Schach.positionKoenigGroß;
		for (int i = 0; i < 64; i++) {
			switch (Schach.schachBrett[i / 8][i % 8]) { // [row][column]
				case "P" : {
					Schach.positionKoenigGroß = i;
					if (!Schach.koenigSicher()) {
						counter -= 64;
					}
				}
					break;
				case "R" : {
					Schach.positionKoenigGroß = i;
					if (!Schach.koenigSicher()) {
						counter -= 500;
					}
				}
					break;
				case "K" : {
					Schach.positionKoenigGroß = i;
					if (!Schach.koenigSicher()) {
						counter -= 300;
					}
				}
					break;
				case "B" : {
					Schach.positionKoenigGroß = i;
					if (!Schach.koenigSicher()) {
						counter -= 300;
					}
				}
					break;
				case "Q" : {
					Schach.positionKoenigGroß = i;
					if (!Schach.koenigSicher()) {
						counter -= 900;
					}
				}
					break;
			}
		}
		Schach.positionKoenigGroß = temp;
		if (!Schach.koenigSicher()) {
			counter -= 200;
		}
		return counter / 2;

	}

	// Deklaration der bewerteSteine methode. Bewertet anhand der Quantität des Spielfelds.
	public static int bewerteSteine() {
		int counter = 0;
		int bishopCounter = 0;
		for (int i = 0; i < 64; i++) {
			switch (Schach.schachBrett[i / 8][i % 8]) { // [row][column]
				case "P" :
					counter += 100;
					break;
				case "R" :
					counter += 500;
					break;
				case "K" :
					counter += 300;
					break;
				case "B" :
					bishopCounter++;
					break;
				case "Q" :
					counter += 900;
					break;
			}
		}
		if (bishopCounter >= 2) {
			counter += 300 * bishopCounter;
		} else if (bishopCounter == 1) {
			counter += 250;
		}
		return counter;
	}

	// Deklaration der bewerteBewegungen methode. Bewertet anhand der möglichen Bewegunsfreiheit die der König besitzt.
	public static int bewerteBewegungen(int listLength, int depth, int material) {
		int counter = 0;
		counter += listLength; // 5 Punkte für jeden Move
		if (listLength == 0) {
			if (!Schach.koenigSicher()) { // Schachmatt
				counter -= 200000 * depth;

			} else {
				counter -= 150000 * depth;
			}
		}

		return counter;
	}

	// Deklaration der bewertePosition methode.
	// Bewertet anhand einzelner Positionen die je nach Spielstein unterschiedliche Werte zurückliefert.
	public static int bewertePositionen(int material) {
		int counter = 0;

		for (int i = 0; i < 64; i++) {

			switch (Schach.schachBrett[i / 8][i % 8]) { // [row][column]
				case "P" :
					counter += pawnBoard[i / 8][i % 8];
					break;
				case "R" :
					counter += rookBoard[i / 8][i % 8];
					break;
				case "K" :
					counter += knightBoard[i / 8][i % 8];
					break;
				case "B" :
					counter += bishopBoard[i / 8][i % 8];
					break;
				case "Q" :
					counter += queenBoard[i / 8][i % 8];
					break;
				case "A" :
					if (material >= 1750) {
						counter += kingMidBoard[i / 8][i % 8];
						counter += Schach.moeglichA(Schach.positionKoenigGroß).length() * 10;
					} else {
						counter += kingEndBoard[i / 8][i % 8];
						counter += Schach.moeglichA(Schach.positionKoenigGroß).length() * 30;
					}
					break;
			}
		}

		return counter;
	}
}
