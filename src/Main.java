import javafx.stage.Stage;
import javafx.stage.StageStyle;
import link.riley.csc254.gameboard.GameBoard;

public class Main {
    public static void main(String[] args) {
        GameBoard gameBoard = new GameBoard();
        gameBoard.launchGame(args);
    }
}
