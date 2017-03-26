package LibraryBoardGame.Model.Board;

import LibraryBoardGame.Model.Direction;
import LibraryBoardGame.Model.Piece.Piece;
import LibraryBoardGame.Model.Piece.Position;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

/**
 * Created by Eriwyr on 18/02/2017.
 */
public class ModelBoard extends Observable{
    private Grid grid;
    private List<Piece> pieces;

    public ModelBoard(Grid grid, List<Piece> pieces) {
        this.grid = grid;
        this.pieces = pieces;
    }

    public ModelBoard() {
        grid = new Grid(4, 10);
        pieces = new ArrayList<Piece>();
    }

    public Grid getGrid() {
        return grid;
    }

    public ModelBoard(int x, int y){
        this.grid = new Grid(x,y);
        pieces = new ArrayList<Piece>();
    }



    public int movePiece(Piece piece, Direction direction) {

        int returnValue = 0;
        Piece anticipatedPiece = new Piece();
        removePiece(piece);
        anticipatedPiece.anticipationCalc(piece, direction);

        if (pieceIsAuthorized(anticipatedPiece)) {
            piece = anticipatedPiece;
            returnValue = 0;
        } else {
            returnValue =1;
        }

        addPieceOnBoardInOrder(piece, 0);
        return returnValue;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public void udateGrid(){
        for(int i =0; i<grid.getSizeY(); i++) {
            for (int j=0; j<grid.getSizeX(); j++ ) {
                grid.setCellXY(new Position(j, i ), true);
            }
        }

        for (Piece piece : pieces){
            for (Position position : piece.getShape()) {
                grid.setCellXY(position, false);
            }
        }

    }


    public void moveOneCell(Position position, Direction direction) {

        Position anticipatedPosition = position.anticipatePosition(direction);
        position.setX(anticipatedPosition.getX());
        position.setY(anticipatedPosition.getY());



        setChanged();
        notifyObservers();
    }


    public void removePiece(Piece piece){

        for (Position position: piece.getShape()){
            grid.getCellXY(position).setEmpty(true);
        }
        pieces.remove(piece);
}

    public List<Piece> getPieces(){
        return pieces;
    }

    public void addPiece(Piece piece){
        for (Position position: piece.getShape()){
            grid.getCellXY(position).setEmpty(false);
        }
        pieces.add(piece);


    }

    public void addPiece(Piece piece, int index){
        for (Position position: piece.getShape()){
            grid.getCellXY(position).setEmpty(false);
        }
        pieces.add(index, piece);

    }

    public void addPieceOnBoard(Piece piece) {
        for (Position position :piece.getShape()) {
            grid.setCellXY(position, false);
        }
        addPiece(piece);
        setChanged();
        notifyObservers();

    }

    public  void addPieceOnBoardInOrder(Piece piece, int index) {
        for (Position position :piece.getShape()) {
            grid.setCellXY(position, false);
        }
        addPiece(piece, index);
        setChanged();
        notifyObservers();
    }


    public boolean AuthorizedAddPieceOnBoard(Piece piece) {
        for (Position position : piece.getShape()) {
            if (grid.getCellXY(position).isEmpty()) return true ;
        }

        for (Position position :piece.getShape()) {

            grid.setCellXY(position, false);
        }
        addPiece(piece);
        setChanged();
        notifyObservers();
        return false;

    }

    public boolean AuthorizedAddPieceOnBoard(Piece piece, int index) {
        for (Position position : piece.getShape()) {
            if (!grid.getCellXY(position).isEmpty()){
                return false ;
            }
        }

        for (Position position :piece.getShape()) {

            grid.setCellXY(position, false);
        }
        addPiece(piece, index);
        setChanged();
        notifyObservers();
        return true;

    }


    public void emptyMultipleCells(List<Position> list, Piece piece) {

        for (Position position : list) {
            grid.getCellXY(position).setEmpty(true);

            for (Iterator<Position> iterator = piece.getShape().iterator(); iterator.hasNext();) {
                Position cell = iterator.next();
                if (cell.getY() == position.getY() && cell.getX() == position.getX()) {
                    // Remove the current element from the iterator and the list.
                    iterator.remove();
                }
            }
        }

        setChanged();
        notifyObservers();

    }
    public void emptyCell(Position position, Piece piece) {

        for (Iterator<Position> iterator = piece.getShape().iterator(); iterator.hasNext();) {
            Position cell = iterator.next();
            if (cell.getY() == position.getY() && cell.getX() == position.getX()) {
                //System.out.println("removing Position : "+position.getX()+" "+position.getY());
                // Remove the current element from the iterator and the list.
                iterator.remove();
            }
        }

        setChanged();
        notifyObservers();

    }


    public int rotatePiece(Piece piece, int rotation) { // rotation = 1 for clockwise rotation, -1 for anticlockwise

        removePiece(piece);
        int returnValue = 0;

        Piece anticipatedPiece = new Piece();
        anticipatedPiece.anticipationCalc(piece, rotation);

        if (pieceIsAuthorized(anticipatedPiece)) {
            piece = anticipatedPiece;
            returnValue = 0;
        } else {
            returnValue = 1;
        }
        addPieceOnBoardInOrder(piece, 0);
        return returnValue;
    }

    public boolean pieceIsAuthorized(Piece piece) {
        for (Position position : piece.getShape()) {
            try {
                if (!grid.getCellXY(position).isEmpty()) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    /*
    public void replacePieceOnBoard(Piece newPiece, int index) {
        //On enlève l'ancienne
        for (Position position: pieces.get(index).getShape()){
            grid.getCellXY(position).setEmpty(true);
        }

        for (Position position :newPiece.getShape()) {
            grid.setCellXY(position, false);
        }
        //addPiece(piece, index);
        pieces.set(index, newPiece);
        setChanged();
        notifyObservers();
    }*/


}
