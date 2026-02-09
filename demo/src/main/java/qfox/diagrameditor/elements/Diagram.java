package qfox.diagrameditor.elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import qfox.diagrameditor.elementShapes.ShapesFactory;
import qfox.diagrameditor.memento.DiagramElementFactory;
import qfox.diagrameditor.memento.Memento;

import javafx.geometry.Point2D;

public class Diagram {

    private String name;
    private List<DiagramElement> elements = new ArrayList<>();
    private static final DiagramElementFactory defaultFactory = new DiagramElementFactory();

    public Diagram(String name, DiagramElement... diagramElements) {
        this.name = name;
        elements.addAll(Arrays.asList(diagramElements));
    }

    public Diagram(String name) {
        this.name = name;
    }

    public void addElement(DiagramElement element) {
        elements.add(element);
    }

    public void removeElement(DiagramElement element) {
        elements.remove(element);
    }

    public List<DiagramElement> getElements() {
        return elements;
    }

    public Memento createMemento() {
        List<Memento.ElementData> dataList = new ArrayList<>();
        for (DiagramElement el : elements) {
            dataList.add(new Memento.ElementData(
                    el.getType(),
                    el.getPosition().getX(),
                    el.getPosition().getY(),
                    el.getSize().getX(),
                    el.getSize().getY(),
                    el.getTexts()
            ));
        }
        return new Memento(this.name, dataList);
    }

    public void restore(Memento memento) {
        this.name = memento.getName();
        this.elements.clear();

        List<DiagramNode> nodes = new ArrayList<>();
        List<DiagramConnector> connectors = new ArrayList<>();

        for (Memento.ElementData data : memento.getElementsData()) {
            if (Arrays.asList(ShapesFactory.BLOCK_TYPE).contains(data.type) || Arrays.asList(ShapesFactory.OTHER_TYPE).contains(data.type)) {
                DiagramNode node = defaultFactory.createNode(data);
                elements.add(node);
                nodes.add(node);
            } else if (Arrays.asList(ShapesFactory.LINE_TYPE).contains(data.type)) {
                DiagramConnector conn = defaultFactory.createConnector(data);
                elements.add(conn);
                connectors.add(conn);
            }
        }

        for (DiagramConnector conn : connectors) {
            Point2D start = conn.getPosition();
            Point2D end = start.add(conn.getSize());

            for (DiagramNode node : nodes) {
                if (pointInsideNode(start, node)) {
                    conn.setFromElement(node);
                    node.addConnectedElement(conn);
                    System.out.println("Connected from " + node.toString() + " to " + conn.toString());
                }
                if (pointInsideNode(end, node)) {
                    conn.setToElement(node);
                    node.addConnectedElement(conn);
                    System.out.println("Connected to " + node.toString() + " from " + conn.toString());
                }
            }
            conn.updateConnections();
        }
    }

    private boolean pointInsideNode(Point2D point, DiagramNode node) {
        Point2D pos = node.getPosition();
        Point2D size = node.getSize();
        return point.getX() >= pos.getX() - 2 && point.getX() <= pos.getX() + size.getX() + 2 && point.getY() >= pos.getY() - 2 && point.getY() <= pos.getY() + size.getY() + 2;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Diagram{name='").append(name).append("', elements=[\n");
        for (int i = 0; i < elements.size(); i++) {
            sb.append(elements.get(i).toString());
            if (i < elements.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");

        }
        sb.append("]}");
        return sb.toString();
    }

}
