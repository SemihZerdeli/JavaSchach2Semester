import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

// Das ist die Grafikklasse. Sie ist eine Erweiterung der JPanel Klasse und implementiert die Interfaces
// MouseListener sowie MousMotionListener. Sie wird genutzt um das Spielfeld darzustellen und Mausabfragen zu tätigen.
public class Grafik extends JPanel implements MouseListener {

	private static final long	serialVersionUID	= 1L;

	// Deklaration einiger Variablen.
	private static int			mausX, mausY, neueMausX, neueMausY;
	private static int			felderGroesse		= 64;
	private boolean				mousePressed		= false;

	// Der Konstruktor der Klasse Grafik. Wird lediglich zum zuweisen der Listener genutzt.
	public Grafik() {
		this.addMouseListener(this);
	}

	// Die paintComponent Methode ist ein Teil der JComponent Klasse. Sie wird beim initialisieren der JComponent, sowie bei jedem Aufruf der 
	// repaint() Methode aufgerufen. In ihr werden alles Grafischen Elemente auf das JPanel gezeichnet.
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		this.setBackground(Color.yellow);

		// Zeichnen des Raster Musters also dem Spielfeld
		for (int i = 0; i < 64; i += 2) {
			g.setColor(new Color(255, 200, 100));
			g.fillRect((i % 8 + (i / 8) % 2) * felderGroesse, (i / 8) * felderGroesse, felderGroesse, felderGroesse);
			g.setColor(new Color(150, 50, 30));
			g.fillRect(((i + 1) % 8 - ((i + 1) / 8) % 2) * felderGroesse, ((i + 1) / 8) * felderGroesse, felderGroesse, felderGroesse);
		}

		// Zeichnen der möglichen Felder bei Auswahl eines Spielsteines
		if (mousePressed) {
			String userPosibilities = Schach.moeglicheZuege();

			for (int i = 0; i < userPosibilities.length(); i += 5) {
				if (Integer.valueOf(userPosibilities.substring(i, i + 1)) == mausY / felderGroesse && Integer.valueOf(userPosibilities.substring(i + 1, i + 2)) == mausX / felderGroesse) {
					g.setColor(new Color(0, 150, 0, 130));
					g.fillRect(Integer.valueOf(userPosibilities.substring(i + 3, i + 4)) * felderGroesse, Integer.valueOf(userPosibilities.substring(i + 2, i + 3)) * felderGroesse, felderGroesse, felderGroesse);
				}
			}
		}

		// Zeichnen der Spielsteine auf ihren Feldern
		Image chessPieceImage = new ImageIcon("ChessPieces.png").getImage();
		for (int i = 0; i < 64; i++) {
			int j = -1, k = -1;
			switch (Schach.schachBrett[i / 8][i % 8]) { // [row][column]
				case "P" :
					j = 5;
					k = 0;
					break;
				case "p" :
					j = 5;
					k = 1;
					break;
				case "R" :
					j = 2;
					k = 0;
					break;
				case "r" :
					j = 2;
					k = 1;
					break;
				case "K" :
					j = 4;
					k = 0;
					break;
				case "k" :
					j = 4;
					k = 1;
					break;
				case "B" :
					j = 3;
					k = 0;
					break;
				case "b" :
					j = 3;
					k = 1;
					break;
				case "Q" :
					j = 1;
					k = 0;
					break;
				case "q" :
					j = 1;
					k = 1;
					break;
				case "A" :
					j = 0;
					k = 0;
					break;
				case "a" :
					j = 0;
					k = 1;
					break;
			}
			
			// Wenn kein Spielstein gefunden wurde Zeichne nichts.
			if (j != -1 && k != -1) {
				g.drawImage(chessPieceImage, (i % 8) * felderGroesse, (i / 8) * felderGroesse, (i % 8 + 1) * felderGroesse, (i / 8 + 1) * felderGroesse, j * 64, k * 64, (j + 1) * 64, (k + 1) * 64, this);
			}

		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	// Sobald die Maus gedrückt wurde wird überprüft ob sie im Spielfeld liegt. Wenn nicht passiert auch nichts. Wenn doch 
	// werden auf die Variablen die jetzigen x und y koordinaten gespeichert und mousePressed wird gleich true.
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getX() <= 8 * felderGroesse && e.getY() <= 8 * felderGroesse) {
			// if inside the board
			mausX = e.getX();
			mausY = e.getY();
			mousePressed = true;
			repaint();
		}
	}

	
	// Sobald die Maus losgelassen wurde wird überprüft, ob es ein gültiger Zug war und wenn ja welcher Art. Je nach Ergebnis wird gehandelt.
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getX() <= 8 * felderGroesse && e.getY() <= 8 * felderGroesse) {
			// if inside the board
			neueMausX = e.getX();
			neueMausY = e.getY();
			if (e.getButton() == MouseEvent.BUTTON1) {
				String dragMove;
				if (neueMausY / felderGroesse == 0 && mausY / felderGroesse == 1 && "P".equals(Schach.schachBrett[mausY / felderGroesse][mausX / felderGroesse])) {
					// pawn promotion
					dragMove = "" + mausX / felderGroesse + neueMausX / felderGroesse + Schach.schachBrett[neueMausY / felderGroesse][neueMausX / felderGroesse] + "QP";
				} else {
					// regular moves
					dragMove = "" + mausY / felderGroesse + mausX / felderGroesse + neueMausY / felderGroesse + neueMausX / felderGroesse + Schach.schachBrett[neueMausY / felderGroesse][neueMausX / felderGroesse];
				}
				String userPosibilities = Schach.moeglicheZuege();
				if (userPosibilities.replace(dragMove, "").length() < userPosibilities.length()) {
					Schach.spieleZug(dragMove);
					Schach.dreheSpielfeld();
					Schach.spieleZug(Schach.alphaBetaSuche(Schach.globaleTiefe, 1000000, -1000000, "", 0));
					Schach.dreheSpielfeld();
					repaint();
					// Verloren
					if(Schach.moeglicheZuege().length() == 0){
						int janein = 0;
						Object[] optionen = {"Neues Spiel", "Beenden"};
						janein = JOptionPane.showOptionDialog
						(null, "Neues Spiel ?", "Leider Verloren !", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionen, optionen[0]);
						if (janein == 0){
							Schach.main(null);
						}
						else {
							System.exit(0);
						}
					}
					// Gewonnen
					Schach.dreheSpielfeld();
					if(Schach.moeglicheZuege().length() == 0){
						int janein = 0;
						Object[] optionen = {"Neues Spiel", "Beenden"};
						janein = JOptionPane.showOptionDialog
						(null, "Neues Spiel ?", "Gewonnen !", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionen, optionen[0]);
						if (janein == 0){
							Schach.main(null);
						}
						else {
							System.exit(0);
						}
					}
					Schach.dreheSpielfeld();
				}
			}
			mousePressed = false;
			repaint();
		}
	}


}
