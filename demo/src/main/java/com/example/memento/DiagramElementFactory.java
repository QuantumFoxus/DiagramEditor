package com.example.memento;

import com.example.elements.DiagramConnector;
import com.example.elements.DiagramNode;

import javafx.geometry.Point2D;

public class DiagramElementFactory {

    public DiagramNode createNode(Memento.ElementData data) {
        DiagramNode node = new DiagramNode(data.type, new Point2D(data.posX, data.posY));
        node.setSize(new Point2D(data.sizeX, data.sizeY));
        node.setTexts(data.texts);
        return node;
    }

    public DiagramConnector createConnector(Memento.ElementData data) {
        return new DiagramConnector(data.type, data.texts, null, null, new Point2D(data.posX, data.posY), new Point2D(data.sizeX, data.sizeY));
    }
}
