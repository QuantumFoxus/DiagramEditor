package qfox.diagrameditor.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javafx.geometry.Point2D;
import qfox.diagrameditor.command.AddElementCommand;
import qfox.diagrameditor.command.CommandInvoker;
import qfox.diagrameditor.command.MoveElementCommand;
import qfox.diagrameditor.command.RemoveElementCommand;
import qfox.diagrameditor.command.ResizeElementCommand;
import qfox.diagrameditor.command.UpdateElementCommand;
import qfox.diagrameditor.elementShapes.ShapesFactory;
import qfox.diagrameditor.elements.Diagram;
import qfox.diagrameditor.elements.DiagramConnector;
import qfox.diagrameditor.elements.DiagramElement;
import qfox.diagrameditor.elements.DiagramNode;
import qfox.diagrameditor.memento.HistoryManager;
import qfox.diagrameditor.memento.Memento;

public class DiagramController {

    private Diagram model;
    private CommandInvoker invoker;
    private HistoryManager history;

    public DiagramController() {
        this.model = new Diagram("unnammed");
        this.history = new HistoryManager();
        this.invoker = new CommandInvoker();

    }

    public DiagramController(String diagramName) {
        this.model = new Diagram(diagramName);
        this.history = new HistoryManager();
        this.invoker = new CommandInvoker();
    }

    public void undo() {
        boolean flag = invoker.canUndo();
        invoker.undoLast();
        System.out.println(flag ? model.toString() : "Can't Undo");
    }

    public void redo() {
        boolean flag = invoker.canRedo();
        invoker.redoLast();
        System.out.println(flag ? model.toString() : "Can't Redo");
    }

    public void addElement(String type, Point2D pos) {
        if (Arrays.asList(ShapesFactory.BLOCK_TYPE).contains(type) || Arrays.asList(ShapesFactory.OTHER_TYPE).contains(type)) { // Box
            invoker.executeCommand(new AddElementCommand(model, new DiagramNode(type, pos)));
            System.out.println("added: " + type);
        }
        if (Arrays.asList(ShapesFactory.LINE_TYPE).contains(type)) { // Line
            invoker.executeCommand(new AddElementCommand(model, new DiagramConnector(type, null, null, pos)));
            System.out.println("added: " + type);
        }
        System.out.println(model.toString());
    }

    public void moveElement(DiagramElement element, Point2D newPos) {
        invoker.executeCommand(new MoveElementCommand(model, element, newPos));
        System.out.println("Element moved on: " + newPos.toString());
    }

    public void updateElement(DiagramElement element, Point2D newPos, Point2D newSize, String[] newTexts) {
        if (newPos == null && newSize == null && newTexts == null) {
            return;
        }
        invoker.executeCommand(new UpdateElementCommand(element, newPos, newSize, newTexts));
        System.out.println("Element updated: new position = " + element.getPosition().toString() + " new size = " + element.getSize().toString() + " Texts:" + Arrays.deepToString(element.getTexts()));
    }

    public void resizeElement(DiagramElement element, Point2D newSize) {
        invoker.executeCommand(new ResizeElementCommand(model, element, newSize));
        System.out.println("Element resized: " + newSize.toString());
    }

    public void removeElement(DiagramElement element) {
        invoker.executeCommand(new RemoveElementCommand(model, element));
        System.out.println("Element removed");
    }

    public Diagram getModel() {
        return model;
    }

    public void saveToFile(File file) {
        try {
            Memento memento = model.createMemento();
            history.saveToFile(memento, file);
            System.out.println("Diagram saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile(File file) {
        try {
            Memento memento = history.loadFromFile(file);
            model.restore(memento);
            invoker.clear();
            System.out.println("Diagram loaded");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isUndoEmpty() {
        return !invoker.canUndo();
    }

}
