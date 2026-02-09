package com.example.elementShapes;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.text.TextAlignment;

public class TextShape extends RectangleShape {

    private String text;
    private boolean LeftAlign = false;

    public TextShape(EditablePoint2D pos, EditablePoint2D size, String text, boolean isLeftAlign) {
        super(pos, size);
        this.text = text;
        this.LeftAlign = isLeftAlign;
    }

    public TextShape(EditablePoint2D pos, EditablePoint2D size, String text) {
        super(pos, size);
        this.text = text;
    }

    public String getText() {
        return !text.isEmpty() ? text : "none";
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Node render(Point2D globalPos) {
        Label label = new Label(text);

        label.setPrefSize(Math.abs(size.getX()), Math.abs(size.getY()));
        label.setBackground(Background.EMPTY);
        if (LeftAlign) {
            label.setAlignment(Pos.TOP_LEFT);
            label.setTextAlignment(TextAlignment.LEFT);
        } else {
            label.setAlignment(Pos.CENTER);
            label.setTextAlignment(TextAlignment.CENTER);
        }
        label.setWrapText(true);
        // label.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        label.setLayoutX(pos.getX() + globalPos.getX());
        label.setLayoutY(pos.getY() + globalPos.getY());

        return label;
    }

    public boolean isIsLeftAlign() {
        return LeftAlign;
    }

    public void setLeftAlign(boolean LeftAlign) {
        this.LeftAlign = LeftAlign;
    }

    public Node render(Point2D globalPos, double angle) {
        Label label = new Label(text);
        Point2D size = getSize();
        Point2D centerPos = new Point2D(size.getX() / 2, size.getY() / 2).add(globalPos);
        // label.setPrefSize(Math.hypot(size.getX(), size.getY()), 0);
        label.setBackground(Background.EMPTY);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setWrapText(true);

        label.setLayoutX(pos.getX() + centerPos.getX());
        label.setLayoutY(pos.getY() + centerPos.getY());
        // label.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        label.setRotate(angle);

        return label;
    }

    @Override
    public Shape clone() {
        return new TextShape(pos.copy(), size.copy(), text, LeftAlign);
    }

}
