package qfox.diagrameditor.elementShapes;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class ArrowShape extends LineShape {

    private HEAD_TYPES headType = HEAD_TYPES.DEFAULT;

    public static enum HEAD_TYPES {
        DEFAULT,
        EMPTY,
        TICK,
        CROSS,
        DIAMOND,
        DIAMOND_EMPTY
    };

    public ArrowShape(EditablePoint2D pos, EditablePoint2D size) {
        super(pos, size);
    }

    public ArrowShape(EditablePoint2D pos, EditablePoint2D size, boolean dashed, boolean isBold, HEAD_TYPES headType) {
        super(pos, size, dashed, isBold);
        this.headType = headType;
    }

    @Override

    public Node render(Point2D globalPos) {
        Line line = (Line) super.render(globalPos);

        double arrowLength = 15;

        double sx = line.getStartX();
        double sy = line.getStartY();
        double ex = line.getEndX();
        double ey = line.getEndY();

        double angle = Math.atan2(sy - ey, sx - ex);

        double x1 = ex + arrowLength * Math.cos(angle - Math.toRadians(15));
        double y1 = ey + arrowLength * Math.sin(angle - Math.toRadians(15));

        double x2 = ex + arrowLength * Math.cos(angle + Math.toRadians(15));
        double y2 = ey + arrowLength * Math.sin(angle + Math.toRadians(15));

        double x3 = x2 + arrowLength * Math.cos(angle - Math.toRadians(15));
        double y3 = y2 + arrowLength * Math.sin(angle - Math.toRadians(15));

        //cross points
        // double cx1 = ex  + (arrowLength / 2) * Math.cos(angle - Math.toRadians(45));
        // double cy1 = ey  + (arrowLength / 2) * Math.sin(angle - Math.toRadians(45));
        // double cx2 = ex  + (arrowLength / 2) * Math.cos(angle + Math.toRadians(45));
        // double cy2 = ey  + (arrowLength / 2) * Math.sin(angle + Math.toRadians(45));
        Polygon arrowHead = new Polygon();
        // if (closedHead) {
        //     arrowHead.getPoints().addAll(ex, ey, x1, y1, x2, y2);
        // } else {
        //     arrowHead.getPoints().addAll(ex, ey, x1, y1, ex, ey, x2, y2);
        // }
        // arrowHead.setFill(line.getStroke());
        // if (!emptyHead) {
        //     arrowHead.setFill(Color.BLACK);
        // }
        arrowHead.setStroke(Color.BLACK);
        switch (headType) {
            case EMPTY -> {
                arrowHead.getPoints().addAll(ex, ey, x1, y1, x2, y2);
                arrowHead.setFill(Color.WHITE);
            }
            case TICK -> {
                arrowHead.getPoints().addAll(x1, y1, ex, ey, x2, y2, ex, ey);
                arrowHead.setFill(Color.BLACK);
            }
            case CROSS -> {
                //TODO
            }
            case DIAMOND -> {
                arrowHead.getPoints().addAll(ex, ey, x1, y1, x3, y3, x2, y2);
                arrowHead.setFill(Color.BLACK);
            }
            case DIAMOND_EMPTY -> {
                arrowHead.getPoints().addAll(ex, ey, x1, y1, x3, y3, x2, y2);
                arrowHead.setFill(Color.WHITE);
            }
            default -> {
                arrowHead.getPoints().addAll(ex, ey, x1, y1, x2, y2);
                arrowHead.setFill(Color.BLACK);
            }
        }

        Group group = new Group();
        group.getChildren().addAll(line, arrowHead);

        return group;
    }

    @Override

    public Shape clone() {
        return new ArrowShape(pos.copy(), size.copy(), dashed, bold, headType);
    }

    @Override

    public Point2D getSize() {
        return size;
    }
}
