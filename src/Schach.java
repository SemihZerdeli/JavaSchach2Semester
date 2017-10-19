import javax.swing.JFrame;
import javax.swing.JOptionPane;


// Das ist die Schachklasse (Die Hauptklasse). In ihr enthalten ist die grundlegende Logik. Sowie einige Methoden zur Initialisierung von
// Grafik-Elementen.
public class Schach {

	// Darstellung des Schachbrettes in Form eines zweidimensionalen Arrays
	static String			schachBrett[][]	= {										  // r
											{"r", "k", "b", "q", "a", "b", "k", "r"}, // 0
											{"p", "p", "p", "p", "p", "p", "p", "p"}, // 1
											{" ", " ", " ", " ", " ", " ", " ", " "}, // 2
											{" ", " ", " ", " ", " ", " ", " ", " "}, // 3
											{" ", " ", " ", " ", " ", " ", " ", " "}, // 4
											{" ", " ", " ", " ", " ", " ", " ", " "}, // 5
											{"P", "P", "P", "P", "P", "P", "P", "P"}, // 6
											{"R", "K", "B", "Q", "A", "B", "K", "R"},}; // 7
										// c  0    1    2    3    4    5    6    7

	// Deklaration und/oder Initialisierung der Member-Variablen 
	public static JFrame	f;
	static int				positionKoenigGroﬂ, positionKoenigKlein;
	static int				globaleTiefe;	
	static int				schwierigkeit	= 0;

	// Deklaration des Konstruktors der Schach-Klasse
	public Schach() {
		// Methodenaufruf mit Argument¸bergabe
		erstelleFenster("Schach v1.0");
		System.out.println(moeglicheZuege());
		// initialisierung der variablen positionKoenigGroﬂ und positionKoenigKlein
		while (!"A".equals(schachBrett[positionKoenigGroﬂ / 8][positionKoenigGroﬂ % 8])) {
			positionKoenigGroﬂ++;
		}
		while (!"a".equals(schachBrett[positionKoenigKlein / 8][positionKoenigKlein % 8])) {
			positionKoenigKlein++;
		}

		// Auswahl des Schwierigkeitsgrades. Mit zunehmender Schwierigkeit nimmt die variable globaleTiefe zu.
		Object[] optionen = {"Einfach", "Mittel", "Schwer"};
		schwierigkeit = JOptionPane.showOptionDialog
				(null, "W‰hle eine Schwierigkeit", "Modus", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionen, optionen[0]);

		
		switch (schwierigkeit) {
			case 0 :
				globaleTiefe = 3;
				break;
			case 1 :
				globaleTiefe = 5;
				break;
			case 2 :
				globaleTiefe = 7;
				break;
			default :
				globaleTiefe = 3;
				break;
		}
		
		
		
	}

	// Main-Methode. Erstellt lediglich eine neue Instanz der Klasse Schach
	public static void main(String[] args) {
		new Schach();
		
	}
	
	// Deklaration der Methode erstelleFenster. Beinhaltet einen String-Parameter um den Titel des JFrames zu setzen
	public static void erstelleFenster(String title) {
		f = new JFrame(title);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Grafik ui = new Grafik();
		f.add(ui);
		f.setSize(512, 534);
		f.setLocationRelativeTo(null);
		f.setResizable(false);
		f.setVisible(true);
	}

	// Deklaration der Methode alphaBetaSuche. Beinhaltet 5 Parameter. Die AlphaBetaSuche baut auf dem MiniMax-Prinzip auf und beschleunigt
	// so den Algorithmus, indem er ganze Teile des Baumes wegschneidet. Mehr dazu in der Dokumentation.
	public static String alphaBetaSuche(int tiefe, int beta, int alpha, String move, int player) {
		// returned ein String in der Form von x1,y1,x2,y2,score
		String liste = moeglicheZuege();
		if (tiefe == 0 || liste.length() == 0) {
			return move + (Bewertung.bewerte(liste.length(), tiefe) * (player * 2 - 1));
		}
		liste = sortiereBewegungen(liste);
		player = 1 - player; // either 1 or 0
		for (int i = 0; i < liste.length(); i += 5) {
			spieleZug(liste.substring(i, i + 5));
			dreheSpielfeld();
			String returnString = alphaBetaSuche(tiefe - 1, beta, alpha, liste.substring(i, i + 5), player);
			int value = Integer.valueOf(returnString.substring(5));
			dreheSpielfeld();
			spieleZugRueckgaengig(liste.substring(i, i + 5));
			if (player == 0) {
				if (value <= beta) {
					beta = value;
					if (tiefe == globaleTiefe) {
						move = returnString.substring(0, 5);
					}
				}
			} else {
				if (value > alpha) {
					alpha = value;
					if (tiefe == globaleTiefe) {
						move = returnString.substring(0, 5);
					}
				}

			}
			if (alpha >= beta) {
				if (player == 0) {
					return move + beta;
				} else {
					return move + alpha;
				}
			}
		}
		if (player == 0) {
			return move + beta;
		} else {
			return move + alpha;
		}
	}

	// Deklaration der Methode dreheSpielfeld. Sie wird benˆtigt, da das Programm aus der Sicht von nur einem Spieler geschrieben wurde. 
	// Durch diese Methode wird bewirkt das alle Spielsteine des Schachbrettes die Seite wechseln ,sodass Berechnungen und Spielz¸ge 
	// richtig brerechnet werden.
	public static void dreheSpielfeld() {
		String temp = " ";
		for (int i = 0; i < 32; i++) {
			int r = i / 8, c = i % 8;
			if (Character.isUpperCase(schachBrett[r][c].charAt(0))) {
				temp = schachBrett[r][c].toLowerCase();
			} else {
				temp = schachBrett[r][c].toUpperCase();
			}
			if (Character.isUpperCase(schachBrett[7 - r][7 - c].charAt(0))) {
				schachBrett[r][c] = schachBrett[7 - r][7 - c].toLowerCase();
			} else {
				schachBrett[r][c] = schachBrett[7 - r][7 - c].toUpperCase();
			}
			schachBrett[7 - r][7 - c] = temp;
		}
		// Auswechseln der KingPosition zum weiteren Tracken
		int kingTemp = positionKoenigGroﬂ;
		positionKoenigGroﬂ = 63 - positionKoenigKlein;
		positionKoenigKlein = 63 - kingTemp;
	}

	// Deklaration der Methode spieleZug. Beinhaltet einen String-Parameter. Dieser wird benˆtigt um den Zug zu t‰tigen.
	public static void spieleZug(String move) {
		if (move.charAt(4) != 'P') {
			// Normaler Spielzug in Form von: x1,y1,x2,y2,eroberter Spielstein
			schachBrett[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] = 
					schachBrett[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))];
			schachBrett[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))] = " ";
			// Kˆnig bewegt sich
			if ("A".equals(schachBrett[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))])) {

				positionKoenigGroﬂ = 8 * Character.getNumericValue(move.charAt(2)) + Character.getNumericValue(move.charAt(3));
			}
		}
		// wenn Bauer befˆrdert wird
		// Bauern befˆrderung: alte reihe, neue reihe, eroberter Spielstein, neuer Spielstein, P
		else {
			schachBrett[1][Character.getNumericValue(move.charAt(0))] = " ";
			schachBrett[0][Character.getNumericValue(move.charAt(1))] = String.valueOf(move.charAt(3));
		}
	}

	// Deklaration der Methode spieleZugRueckgaengig. Beinhaltet einen String-Parameter. Dieser wird benˆtigt einen Zug rueckgaengig zu machen.
	public static void spieleZugRueckgaengig(String move) {
		if (move.charAt(4) != 'P') {
			// Normaler Spielzug in Form von: x1,y1,x2,y2,eroberter Spielstein
			schachBrett[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))] = 
					schachBrett[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))];
			
			schachBrett[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] = String.valueOf(move.charAt(4));
			if ("A".equals(schachBrett[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))])) {

				positionKoenigGroﬂ = 8 * Character.getNumericValue(move.charAt(0)) + Character.getNumericValue(move.charAt(1));
			}
		}
		// wenn Bauer befˆrdert wird
		// Bauern befˆrderung: alte reihe, neue reihe, eroberter Spielstein, neuer Spielstein, P
		else {
			schachBrett[1][Character.getNumericValue(move.charAt(0))] = "P";
			schachBrett[0][Character.getNumericValue(move.charAt(1))] = String.valueOf(move.charAt(2));
		}
	}

	// Deklaration der Methode moeglicheZuege. Geht einmal das ganze Schachbrett aus sicht von Weiﬂ durch und ermittelt alle mˆglichen
	// Z¸ge anhand von Methoden, welche f¸r jeden einzelnen Spielstein angefertig wurde und einen String Wert returned. Diese
	// String-Werte werden in der String variable liste abgespeichert.
	public static String moeglicheZuege() {
		String list = "";
		for (int i = 0; i < 64; i++) {
			switch (schachBrett[i / 8][i % 8]) { // [row][column]
				case "P" :
					list += moeglichP(i);
					break;
				case "R" :
					list += moeglichR(i);
					break;
				case "K" :
					list += moeglichK(i);
					break;
				case "B" :
					list += moeglichB(i);
					break;
				case "Q" :
					list += moeglichQ(i);
					break;
				case "A" :
					list += moeglichA(i);
					break;
			}
		}
		return list; // x1,y1,x2,y2, captured piece
	}

	// Deklaration der Methode koenigSicher. Sie wird dazu benˆtigt um zu ermitteln ob der jetzige Standort des Kˆnigs sicher oder gef‰hrdet ist
	public static boolean koenigSicher() {

		// enemy bishop/ queen tracking
		int temp = 1;
		for (int i = -1; i <= 1; i += 2) {
			for (int j = -1; j <= 1; j += 2) {
				try {
					while (" ".equals(schachBrett[positionKoenigGroﬂ / 8 + temp * i][positionKoenigGroﬂ % 8 + temp * j])) {
						temp++;
					}
					if ("b".equals(schachBrett[positionKoenigGroﬂ / 8 + temp * i][positionKoenigGroﬂ % 8 + temp * j]) || 
							("q".equals(schachBrett[positionKoenigGroﬂ / 8 + temp * i][positionKoenigGroﬂ % 8 + temp * j]))) {
						// danger
						return false;
					}
				} catch (Exception e) {
				}
				temp = 1;
			}
		}

		// enemy rock/queen tracking
		for (int i = -1; i <= 1; i += 2) {
			try {
				while (" ".equals(schachBrett[positionKoenigGroﬂ / 8][positionKoenigGroﬂ % 8 + temp * i])) {
					temp++;
				}
				if ("r".equals(schachBrett[positionKoenigGroﬂ / 8][positionKoenigGroﬂ % 8 + temp * i]) || 
						("q".equals(schachBrett[positionKoenigGroﬂ / 8][positionKoenigGroﬂ % 8 + temp * i]))) {
					// danger
					return false;
				}
			} catch (Exception e) {
			}
			temp = 1;
			try {
				while (" ".equals(schachBrett[positionKoenigGroﬂ / 8 + temp * i][positionKoenigGroﬂ % 8])) {
					temp++;
				}
				if ("r".equals(schachBrett[positionKoenigGroﬂ / 8 + temp * i][positionKoenigGroﬂ % 8]) || 
						("q".equals(schachBrett[positionKoenigGroﬂ / 8 + temp * i][positionKoenigGroﬂ % 8]))) {
					// danger
					return false;
				}
			} catch (Exception e) {
			}
			temp = 1;
		}

		// enemy knight tracking
		for (int i = -1; i <= 1; i += 2) {
			for (int j = -1; j <= 1; j += 2) {
				try {
					if ("k".equals(schachBrett[positionKoenigGroﬂ / 8 + i][positionKoenigGroﬂ % 8 + j * 2])) {
						// danger
						return false;
					}
				} catch (Exception e) {
				}
				try {
					if ("k".equals(schachBrett[positionKoenigGroﬂ / 8 + i * 2][positionKoenigGroﬂ % 8 + j])) {
						// danger
						return false;
					}
				} catch (Exception e) {
				}
			}
		}

		// enemy pawn tracking
		if (positionKoenigGroﬂ >= 16) {
			try {
				if ("p".equals(schachBrett[positionKoenigGroﬂ / 8 - 1][positionKoenigGroﬂ % 8 - 1])) {
					// danger
					return false;
				}
				if ("p".equals(schachBrett[positionKoenigGroﬂ / 8 + 1][positionKoenigGroﬂ % 8 + 1])) {
					// danger
					return false;
				}
			} catch (Exception e) {
			}
		}
		// enemy king tracking
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i != 0 || j != 0) {
					try {
						if ("a".equals(schachBrett[positionKoenigGroﬂ / 8 + i][positionKoenigGroﬂ % 8 + j])) {
							// danger
							return false;
						}
					} catch (Exception e) {
					}

				}
			}
		}

		return true;
	}

	// Deklaration der sortiere Bewegungen Methode. Sie wird zum Vorsortieren der mˆglichenZ¸ge genutzt um die AlphaBetaSuche zu unterst¸tzen.
	// Durch sie wird die Geschwindigkeit der AlphaBetaSuche erhˆht.
	public static String sortiereBewegungen(String liste) {
		int[] punkte = new int[liste.length() / 5];
		for (int i = 0; i < liste.length() / 5; i += 5) {
			spieleZug(liste.substring(i, i + 5));
			punkte[i / 5] = -Bewertung.bewerte(-1, 0);

			spieleZugRueckgaengig(liste.substring(i, i + 5));
		}

		String neueListeA = "";
		String neueListeB = "";

		for (int i = 0; i < Math.min(6, liste.length() / 5); i++) {
			int max = -1000000, maxLocation = 0;
			for (int j = 0; j < liste.length() / 5; j++) {
				if (punkte[j] > max) {
					max = punkte[j];
					maxLocation = j;
				}
			}
			punkte[maxLocation] = -1000000;
			neueListeA += liste.substring(maxLocation * 5, maxLocation * 5 + 5);
			neueListeB = neueListeB.replace(liste.substring(maxLocation * 5, maxLocation * 5 + 5), "");
		}

		return neueListeA + neueListeB;
	}
	
	// Die n‰chsten Methoden werden alle daf¸r benˆtigt mˆgliche Z¸ge f¸r einzelne Spielsteine auszulesen. Genaue Informationen zur implementierung
	// finden sie in der Dokumentation.
	
	// Bauern Logik
	public static String moeglichP(int i) {
		String liste = "", alterStein;
		int r = i / 8, c = i % 8;

		try { // move one up
			if (" ".equals(schachBrett[r - 1][c]) && i >= 16) {
				alterStein = schachBrett[r - 1][c];
				schachBrett[r][c] = " ";
				schachBrett[r - 1][c] = "P";
				if (koenigSicher()) {
					liste = liste + r + c + (r - 1) + c + alterStein;
				}
				schachBrett[r][c] = "P";
				schachBrett[r - 1][c] = alterStein;
			}
		} catch (Exception e) {
		}

		try { // move two up
			if (" ".equals(schachBrett[r - 1][c]) && " ".equals(schachBrett[r - 2][c]) && i >= 48) {
				alterStein = schachBrett[r - 2][c];
				schachBrett[r][c] = " ";
				schachBrett[r - 2][c] = "P";
				if (koenigSicher()) {
					liste = liste + r + c + (r - 2) + c + alterStein;
				}
				schachBrett[r][c] = "P";
				schachBrett[r - 2][c] = alterStein;
			}
		} catch (Exception e) {
		}

		try { // promotion without capture
			if (" ".equals(schachBrett[r - 1][c]) && i < 16) {
				String[] temp = {"Q", "R", "B", "K"};
				for (int k = 0; k <= temp.length; k++) {
					alterStein = schachBrett[r - 1][c];
					schachBrett[r][c] = " ";
					schachBrett[r - 1][c] = temp[k];
					if (koenigSicher()) {
						// column1, column2, captured-piece, new-piece,P
						liste = liste + c + c + alterStein + temp[k] + "P";
					}
					schachBrett[r][c] = "P";
					schachBrett[r - 1][c] = alterStein;
				}
			}
		} catch (Exception e) {
		}

		for (int j = -1; j <= 1; j += 2) {
			try { // capture
				if (Character.isLowerCase(schachBrett[r - 1][c + j].charAt(0)) && i >= 16) {
					alterStein = schachBrett[r - 1][c + j];
					schachBrett[r][c] = " ";
					schachBrett[r - 1][c + j] = "P";
					if (koenigSicher()) {
						liste = liste + r + c + (r - 1) + (c + j) + alterStein;
					}
					schachBrett[r][c] = "P";
					schachBrett[r - 1][c + j] = alterStein;
				}
			} catch (Exception e) {
			}

			try { // capture && promotion
				if (Character.isLowerCase(schachBrett[r - 1][c + j].charAt(0)) && i < 16) {
					String[] temp = {"Q", "R", "B", "K"};
					for (int k = 0; k < 4; k++) {
						alterStein = schachBrett[r - 1][c + j];
						schachBrett[r][c] = " ";
						schachBrett[r - 1][c + j] = temp[k];
						if (koenigSicher()) {
							// column1, column2, captured-piece, new-piece,P
							liste = liste + c + (c + j) + alterStein + temp[k] + "P";
						}
						schachBrett[r][c] = "P";
						schachBrett[r - 1][c + j] = alterStein;
					}
					alterStein = schachBrett[r - 1][c + j];
					schachBrett[r][c] = " ";
					schachBrett[r - 1][c + j] = "P";
					if (koenigSicher()) {
						liste = liste + r + c + (r - 1) + (c + j) + alterStein;
					}
					schachBrett[r][c] = "P";
					schachBrett[r - 1][c + j] = alterStein;
				}
			} catch (Exception e) {
			}

		}
		return liste;
	}

	// Turm Logik
	public static String moeglichR(int i) {
		String liste = "", alterStein;
		int r = i / 8, c = i % 8;

		int temp = 1;
		for (int j = -1; j <= 1; j += 2) {
			try {
				while (" ".equals(schachBrett[r][c + temp * j])) {
					alterStein = schachBrett[r][c + temp * j];
					schachBrett[r][c] = " ";
					schachBrett[r][c + temp * j] = "R";
					if (koenigSicher()) {
						liste = liste + r + c + r + (c + temp * j) + alterStein;
					}
					schachBrett[r][c] = "R";
					schachBrett[r][c + temp * j] = alterStein;
					temp++;
				}
				if (Character.isLowerCase(schachBrett[r][c + temp * j].charAt(0))) {
					alterStein = schachBrett[r][c + temp * j];
					schachBrett[r][c] = " ";
					schachBrett[r][c + temp * j] = "R";
					if (koenigSicher()) {
						liste = liste + r + c + r + (c + temp * j) + alterStein;
					}
					schachBrett[r][c] = "R";
					schachBrett[r][c + temp * j] = alterStein;
				}
			} catch (Exception e) {
			}
		}

		temp = 1;
		for (int j = -1; j <= 1; j += 2) {
			try {
				while (" ".equals(schachBrett[r + temp * j][c])) {
					alterStein = schachBrett[r + temp * j][c];
					schachBrett[r][c] = " ";
					schachBrett[r + temp * j][c] = "R";
					if (koenigSicher()) {
						liste = liste + r + c + (r + temp * j) + c + alterStein;
					}
					schachBrett[r][c] = "R";
					schachBrett[r + temp * j][c] = alterStein;
					temp++;
				}
				if (Character.isLowerCase(schachBrett[r + temp * j][c].charAt(0))) {
					alterStein = schachBrett[r + temp * j][c];
					schachBrett[r][c] = " ";
					schachBrett[r + temp * j][c] = "R";
					if (koenigSicher()) {
						liste = liste + r + c + (r + temp * j) + c + alterStein;
					}
					schachBrett[r][c] = "R";
					schachBrett[r + temp * j][c] = alterStein;
				}
			} catch (Exception e) {
			}
			temp = 1;
		}
		return liste;
	}

	// Springer Logik
	public static String moeglichK(int i) {
		String liste = "", alterStein;
		int r = i / 8, c = i % 8;
		for (int j = -1; j <= 1; j += 2) {
			for (int k = -1; k <= 1; k += 2) {
				try {
					if (Character.isLowerCase(schachBrett[r + j][c + k * 2].charAt(0)) || " ".equals(schachBrett[r + j][c + k * 2])) {
						alterStein = schachBrett[r + j][c + k * 2];
						schachBrett[r][c] = " ";
						schachBrett[r + j][c + k * 2] = "K";
						if (koenigSicher()) {
							liste = liste + r + c + (r + j) + (c + k * 2) + alterStein;
						}
						schachBrett[r][c] = "K";
						schachBrett[r + j][c + k * 2] = alterStein;
					}
				} catch (Exception e) {
				}

				try {
					if (Character.isLowerCase(schachBrett[r + j * 2][c + k].charAt(0)) || " ".equals(schachBrett[r + j * 2][c + k])) {
						alterStein = schachBrett[r + j * 2][c + k];
						schachBrett[r][c] = " ";
						schachBrett[r + j * 2][c + k] = "K";
						if (koenigSicher()) {
							liste = liste + r + c + (r + j * 2) + (c + k) + alterStein;
						}
						schachBrett[r][c] = "K";
						schachBrett[r + j * 2][c + k] = alterStein;
					}
				} catch (Exception e) {
				}
			}
		}

		return liste;
	}

	// L‰ufer Logik
	public static String moeglichB(int i) {
		String liste = "", alterStein;
		int r = i / 8, c = i % 8;
		int temp = 1;
		for (int j = -1; j <= 1; j += 2) {
			for (int k = -1; k <= 1; k += 2) {
				try {
					while (" ".equals(schachBrett[r + temp * j][c + temp * k])) {
						alterStein = schachBrett[r + temp * j][c + temp * k];
						schachBrett[r][c] = " ";
						schachBrett[r + temp * j][c + temp * k] = "B";
						if (koenigSicher()) {
							liste = liste + r + c + (r + temp * j) + (c + temp * k) + alterStein;
						}
						schachBrett[r][c] = "B";
						schachBrett[r + temp * j][c + temp * k] = alterStein;
						temp++;
					}
					if (Character.isLowerCase(schachBrett[r + temp * j][c + temp * k].charAt(0))) {
						alterStein = schachBrett[r + temp * j][c + temp * k];
						schachBrett[r][c] = " ";
						schachBrett[r + temp * j][c + temp * k] = "B";
						if (koenigSicher()) {
							liste = liste + r + c + (r + temp * j) + (c + temp * k) + alterStein;
						}
						schachBrett[r][c] = "B";
						schachBrett[r + temp * j][c + temp * k] = alterStein;
					}
				} catch (Exception e) {
				}
				temp = 1;
			}
		}
		return liste;
	}

	// Kˆnigin Logik
	public static String moeglichQ(int i) {
		String liste = "", alterStein;
		int r = i / 8, c = i % 8;
		int temp = 1;
		for (int j = -1; j <= 1; j++) {
			for (int k = -1; k <= 1; k++) {
				if (j != 0 || k != 0) {
					try {
						while (" ".equals(schachBrett[r + temp * j][c + temp * k])) {

							alterStein = schachBrett[r + temp * j][c + temp * k];
							schachBrett[r][c] = " ";
							schachBrett[r + temp * j][c + temp * k] = "Q";
							if (koenigSicher()) {
								liste = liste + r + c + (r + temp * j) + (c + temp * k) + alterStein;
							}
							schachBrett[r][c] = "Q";
							schachBrett[r + temp * j][c + temp * k] = alterStein;
							temp++;
						}
						if (Character.isLowerCase(schachBrett[r + temp * j][c + temp * k].charAt(0))) {
							alterStein = schachBrett[r + temp * j][c + temp * k];
							schachBrett[r][c] = " ";
							schachBrett[r + temp * j][c + temp * k] = "Q";
							if (koenigSicher()) {
								liste = liste + r + c + (r + temp * j) + (c + temp * k) + alterStein;
							}
							schachBrett[r][c] = "Q";
							schachBrett[r + temp * j][c + temp * k] = alterStein;
						}
					} catch (Exception e) {
					}
					temp = 1;
				}
			}
		}
		return liste;
	}

	// Kˆnig Logik
	public static String moeglichA(int i) {
		String liste = "", alterStein;
		int r = i / 8, c = i % 8;
		for (int j = 0; j < 9; j++) {
			if (j != 4) {
				try {
					if (Character.isLowerCase(schachBrett[r - 1 + j / 3][c - 1 + j % 3].charAt(0)) || " ".equals(schachBrett[r - 1 + j / 3][c - 1 + j % 3])) {
						alterStein = schachBrett[r - 1 + j / 3][c - 1 + j % 3];
						schachBrett[r][c] = " ";
						schachBrett[r - 1 + j / 3][c - 1 + j % 3] = "A";
						int kingTemp = positionKoenigGroﬂ;
						positionKoenigGroﬂ = i + (j / 3) * 8 + j % 3 - 9;
						if (koenigSicher()) {
							liste = liste + r + c + (r - 1 + j / 3) + (c - 1 + j % 3) + alterStein;
						}
						schachBrett[r][c] = "A";
						schachBrett[r - 1 + j / 3][c - 1 + j % 3] = alterStein;
						positionKoenigGroﬂ = kingTemp;
					}
				} catch (Exception e) {
				}
			}
		}
		// need to add casting later
		return liste;
	}

	
	
	// //////////////////////////////////////////////////////////////////////////////////////////////////
}
