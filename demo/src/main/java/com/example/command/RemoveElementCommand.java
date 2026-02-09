package com.example.command;

import com.example.elements.Diagram;
import com.example.elements.DiagramConnector;
import com.example.elements.DiagramElement;

public class RemoveElementCommand implements Command {

    private Diagram diagram;
    private DiagramElement element;

    public RemoveElementCommand(Diagram diagram, DiagramElement element) {
        this.diagram = diagram;
        this.element = element;
    }

    @Override
    public void execute() {
        if (diagram.getElements().contains(element)) {
            if (element instanceof DiagramConnector dc) {

            }
            diagram.removeElement(element);
        }
    }

    @Override
    public void undo() {
        if (!diagram.getElements().contains(element)) {
            diagram.addElement(element);
        }
    }
}
