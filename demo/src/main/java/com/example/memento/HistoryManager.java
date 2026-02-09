package com.example.memento;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.Deque;

import com.example.elements.Diagram;

public class HistoryManager {

    public Deque<Memento> history = new ArrayDeque<>();
    private static final int MAX_HISTORY_SIZE = 50;

    public void saveState(Diagram diagram) {
        Memento memento = diagram.createMemento();
        history.push(memento);

        while (history.size() > MAX_HISTORY_SIZE) {
            history.removeLast();
        }
    }

    public void restorePrevious(Diagram diagram) {
        if (!history.isEmpty()) {
            Memento memento = history.pop();
            diagram.restore(memento);
        }
    }

    public void saveToFile(Memento memento, File file) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(memento);
        }
    }

    public Memento loadFromFile(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (Memento) in.readObject();
        }
    }
}
