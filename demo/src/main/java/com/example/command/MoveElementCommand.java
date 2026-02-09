package com.example.command;

import com.example.elements.Diagram;
import com.example.elements.DiagramElement;

import javafx.geometry.Point2D;

public class MoveElementCommand implements Command {

    private Diagram diagram;
    private DiagramElement element;
    private Point2D oldPosition;
    private Point2D newPosition;

    public MoveElementCommand(Diagram diagram, DiagramElement element, Point2D newPosition) {
        this.diagram = diagram;
        this.element = element;
        this.oldPosition = element.getPosition();
        this.newPosition = newPosition;
    }

    @Override
    public void execute() {
        element.setPosition(newPosition);
    }

    @Override
    public void undo() {
        element.setPosition(oldPosition);
    }

}
