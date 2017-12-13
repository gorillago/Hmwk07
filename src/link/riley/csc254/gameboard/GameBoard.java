package link.riley.csc254.gameboard;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import link.riley.csc254.gameentities.Entity;
import link.riley.csc254.gameentities.Human;
import link.riley.csc254.gameentities.Mobile;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameBoard extends Application{
    private static final int SCREEN_SIZE = 900;
    private static final int TILE_SIZE = 300;

    private static final int maximumCellSlots = 5;

    public static final int rows = 3;
    public static final int columns = 3;

    public TextArea textArea = null;
    Cell[][] board;

    private Parent createContent() {
        board = new Cell[rows][columns];
        Pane root = new Pane();
        root.setPrefSize(SCREEN_SIZE+600, SCREEN_SIZE);

        Button roundButton = new Button("Round");
        roundButton.setOnAction(e -> {
            round();
        });

        roundButton.setTranslateX(900);
        roundButton.setTranslateY(0);
        roundButton.setPrefWidth(598);
        roundButton.setPrefHeight(49);
        roundButton.setFont(Font.font(30));

        textArea = new TextArea();
        textArea.setTranslateX(900);
        textArea.setTranslateY(70);
        textArea.setPrefWidth(598);
        textArea.setPrefHeight(400);
        textArea.setEditable(false);

        root.getChildren().addAll(roundButton, textArea);

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                Cell cell = new Cell(x, y);
                board[x][y] = cell;
                root.getChildren().add(cell);
            }
        }


        board[0][0].add(new Slot(new Entity(),0, 0, 0));
        board[0][0].add(new Slot(new Human(),0, 0, 1));
        board[0][0].add(new Slot(new Entity(),0, 0, 2));
        board[0][1].add(new Slot(new Entity(),0, 1, 0));
        board[0][1].add(new Slot(new Human(),0, 1, 1));
        board[0][1].add(new Slot(new Entity(),0, 1, 2));
        board[0][2].add(new Slot(new Entity(),0, 2, 0));
        board[0][2].add(new Slot(new Human(),0, 2, 1));
        board[0][2].add(new Slot(new Entity(),0, 2, 2));
        board[1][0].add(new Slot(new Entity(),1, 0, 0));
        board[1][0].add(new Slot(new Human(),1, 0, 1));
        board[1][0].add(new Slot(new Entity(),1, 0, 2));

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                Cell cell = board[x][y];
                for (Slot slot : cell.slots) {
                    root.getChildren().add(slot);
                }
            }
        }



        return root;
    }
    public void round() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                board[i][j].round(i, j);
    }

    public void launchGame(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createContent());

        stage.setScene(scene);
        stage.show();
    }

    public class Slot extends StackPane {
        private Rectangle rectangle;
        private Text text;
        private Entity entity;
        int x, y, slotNumber;

        public Slot(Entity entity, int x, int y, int slotNumber) {
            this.rectangle = new Rectangle(TILE_SIZE-2, (TILE_SIZE/maximumCellSlots)-2);
            rectangle.setFill(Color.WHITE);
            this.entity = entity;
            this.x = x;
            this.y = y;
            this.slotNumber = slotNumber;
            text = new Text();
            text.setText(entity.toString());
            text.fontProperty().setValue(Font.font(20));
            updateSlot(x, y, slotNumber);
            getChildren().addAll(rectangle, text);
        }

        public void kill() {
            getChildren().removeAll(rectangle, text);
        }

        public void updateSlot(int x, int y, int slotNumber) {
            setTranslateX(x*TILE_SIZE+.25);
            setTranslateY((y*TILE_SIZE)+((TILE_SIZE/maximumCellSlots)*slotNumber)+.25);
        }

        public void updateText() {
            text.setText(entity.toString());
        }

        public Rectangle getRectangle() {
            return rectangle;
        }
        public Entity getEntity() {
            return entity;
        }
    }

    public class Cell extends StackPane {
        private int x, y;
        CopyOnWriteArrayList<Slot> slots;

        private Rectangle border = new Rectangle(TILE_SIZE - 2, TILE_SIZE - 2);

        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
            slots = new CopyOnWriteArrayList<>();

            border.setStroke(Color.GREEN);

            getChildren().add(border);

            setTranslateX(x * TILE_SIZE);
            setTranslateY(y * TILE_SIZE);
        }

        public boolean isFull() {
            return (slots.size() >= maximumCellSlots);
        }

        /**
         * @param slot Entity to add to slots.
         * @return returns true if there was room for the entity.
         */
        public boolean add(Slot slot) {
            boolean result = !isFull();
            if (result) {
                slots.add(slot);
            }
            return result;
        }

        public void remove(Slot slot) {
            if (slots.contains(slot)) {
                slots.remove(slot);
                int i = 0;
                for (Slot updateSlot : slots) {
                    updateSlot.updateSlot(x, y, i);
                    i++;
                }
            }
        }
        void round(int startRow, int startColumn) {
            for (Slot slot : slots) {
                boolean dead = false;

                //Attack management;
                if (slots.size() > 1 && !slot.equals(slots.get(1))) {
                    Entity entity1 = slot.getEntity();
                    Entity entity2 = slots.get(1).getEntity();
                    //System.out.printf("%s attacks %s\n", entity1, entity2);
                    textArea.appendText(String.format("%s attacks %s\n", entity1, entity2));
                    Attack combat = new Attack();
                    combat.attack(entity1, entity2);
                    entity2.subtractHealth(combat.damage);
                    String currentMessage = "\t" + combat.getMessage();
                    slot.updateText();
                    if (entity2.getHealth() < .10) {
                        currentMessage += " and is killed";
                        slots.get(1).kill();
                        board[startRow][startColumn].remove(slots.get(1));
                    }
                    //System.out.println(currentMessage);
                    textArea.appendText(currentMessage+"\n");
                }

                //Now try to move the slot if it is mobile.
                move(slot, startRow, startColumn);

            }
        }

        void move(Slot slot, int startRow, int startColumn) {
            Random r = new Random();
            Entity entity = slot.getEntity();
            int distance = 0;
            if (entity instanceof Mobile) {
                distance = ((Mobile) entity).getRange();
                if (distance > 0) {
                    int direction = r.nextInt(9);
                    int deltax = 0;
                    int deltay = 0;
                    switch (direction) {
                        case 0: //no movement
                            break;
                        case 1: //south
                            deltay = (int) Math.round(distance * Math.random());
                            break;
                        case 2: //north
                            deltay = -(int) Math.round(distance * Math.random());
                            break;
                        case 3: //east
                            deltax = (int) Math.round(distance * Math.random());
                            break;
                        case 4: //west
                            deltax = -(int) Math.round(distance * Math.random());
                            break;
                        case 5: //southeast
                            deltax = (int) Math.round(distance * Math.random());
                            deltay = (int) Math.round(distance * Math.random());
                            break;
                        case 6: //southwest
                            deltax = -(int) Math.round(distance * Math.random());
                            deltay = (int) Math.round(distance * Math.random());
                            break;
                        case 7: //northheast
                            deltax = (int) Math.round(distance * Math.random());
                            deltay = -(int) Math.round(distance * Math.random());
                            break;
                        case 8: //northwest
                            deltax = -(int) Math.round(distance * Math.random());
                            deltay = -(int) Math.round(distance * Math.random());
                            break;

                    }
                    int newRow = (startRow + deltay) % rows;
                    newRow = (newRow < 0) ? rows + newRow : newRow;
                    int newColumn = (startColumn + deltax) % columns;
                    newColumn = (newColumn < 0) ? columns + newColumn : newColumn;
                    //System.out.printf("Trying to move %s from (%d, %d) to (%d,%d)\n", entity,startRow,startColumn,newRow,newColumn);
                    if (board[newRow][newColumn].add(slot)) {
                        board[startRow][startColumn].remove(slot);
                        slot.updateSlot(newRow, newColumn, board[newRow][newColumn].slots.size()-1);
                    }
                }
            }

        }
    }
    final class Attack {
        String message;
        double damage;

        protected void attack(Entity a, Entity b) {
            message = a.getSymbol() + " ";

            //Is there an attack?
            boolean attackHappens = (Math.random() < a.getAggressiveness());
            if (attackHappens) {
                message += a.getAttackMessage() + " at " + b.getSymbol();
                damage = Math.random() * 0.5 * a.getStrength();
                message += String.format(" and does %1.2f damage", damage);
            } else {
                message += a.getPassiveMessage();
                damage = 0;
            }
        }

        String getMessage() {
            return message;
        }
    }

}
