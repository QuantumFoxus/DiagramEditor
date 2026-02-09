package qfox.diagrameditor.memento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Memento implements Serializable {

    private final String name;
    private final List<ElementData> elementsData;
    private final Date date;

    public Memento(String name, List<ElementData> elementsData) {
        this.name = name;
        this.elementsData = new ArrayList<>(elementsData);
        this.date = new Date();
    }

    public String getName() {
        return name;
    }

    public List<ElementData> getElementsData() {
        return new ArrayList<>(elementsData);
    }

    public Date getDate() {
        return new Date(date.getTime());
    }

    public static class ElementData implements Serializable {

        public final String type;
        public final double posX;
        public final double posY;
        public final double sizeX;
        public final double sizeY;
        public final String[] texts;

        public ElementData(String type, double posX, double posY, double sizeX, double sizeY, String[] texts) {
            this.type = type;
            this.posX = posX;
            this.posY = posY;
            this.sizeX = sizeX;
            this.sizeY = sizeY;
            this.texts = texts;
        }
    }
}
