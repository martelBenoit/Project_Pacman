import java.awt.*;

public class FruitPill extends Pill{

    public FruitPill(Tile tile, Color color) {
        super(tile,color);
        this.figures = new Figure[2];
        this.figures[0] = new Square(tile.getSize(), tile.getX(), tile.getY(), Color.black);
        int sg = tile.getSize()/4;
        int xg = tile.getX()+(tile.getSize()/2)-(sg/2);
        int yg = tile.getY()+(tile.getSize()/2)-(sg/2);
        this.figures[1] = new Circle(sg,xg,yg,color);
    }
}