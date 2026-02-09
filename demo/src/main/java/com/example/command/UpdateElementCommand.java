package com.example.command;

import com.example.elements.DiagramElement;

import javafx.geometry.Point2D;

public class UpdateElementCommand implements Command {

    private DiagramElement element;
    private Point2D newPos;
    private Point2D newSize;
    private String[] newTexts;
    private Point2D oldPos;
    private Point2D oldSize;
    private String[] oldTexts;

    public UpdateElementCommand(DiagramElement element, Point2D newPos, Point2D newSize, String[] newTexts) {
        this.element = element;
        this.newPos = newPos;
        this.newSize = newSize;
        this.newTexts = newTexts;
    }

    @Override
    public void execute() {
        oldPos = element.getPosition();
        oldSize = element.getSize();
        oldTexts = element.getTexts();
        if (newPos != null) {
            element.setPosition(newPos);
        }
        if (newSize != null) {
            element.setSize(newSize);
        }
        if (newTexts != null && newTexts.length > 0) {
            element.setTexts(newTexts);
        }
    }

    @Override
    public void undo() {
        if (oldPos != null) {
            element.setPosition(oldPos);
        }
        if (oldSize != null) {
            element.setSize(oldSize);
        }
        if (oldTexts != null) {
            element.setTexts(oldTexts);
        }
    }

}
