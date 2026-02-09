package qfox.diagrameditor.elementShapes;

public class ShapesFactory {

    public static final String[] BLOCK_TYPE = {
        "Process",
        "Box",
        "Condition",
        "Start / End",
        "Input / Output",
        "UML Class 1 field",
        "UML Class 2 fields",
        "UML Abstract class",
        "UML Interface",
        "UML Enumeration",};
    public static final String[] LINE_TYPE = {
        "Line",
        "Arrow",
        "UML Direct Association",
        "UML Aggregation",
        "UML Composition",
        "UML Generalization",
        "UML Realization",
        "UML Dependency",
        "Dashed Line",};

    public static final String[] OTHER_TYPE = {
        "Text",};

    public static Shape[] createShapeGroupByName(String name) {

        //===================== BLOCKS =====================
        if (name.equals(BLOCK_TYPE[0])) { // Process
            Shape[] shape = {
                new RectangleShape(new EditablePoint2D(), new EditablePoint2D(100, 100)),
                new TextShape(new EditablePoint2D(0, 0, true, false), new EditablePoint2D(100, 20, true, false), "title"),
                new LineShape(new EditablePoint2D(0, 20, true, false), new EditablePoint2D(100, 0, true, false), false, true),
                new TextShape(new EditablePoint2D(0, 20), new EditablePoint2D(100, 80), "text")
            };
            System.out.println("Process shape created");
            return shape;
        }

        if (name.equals(BLOCK_TYPE[1])) { // Box
            Shape[] shape = {
                new RectangleShape(new EditablePoint2D(), new EditablePoint2D(100, 100)),
                new TextShape(new EditablePoint2D(0, 0), new EditablePoint2D(100, 100), "text")
            };
            System.out.println("Box shape created");
            return shape;
        }

        if (name.equals(BLOCK_TYPE[2])) { // Condition
            Shape[] shape = {
                new RectangleShape(new EditablePoint2D(), new EditablePoint2D(100, 100), false),
                new DiamondShape(new EditablePoint2D(0, 0, false, false), new EditablePoint2D(100, 100)),
                new TextShape(new EditablePoint2D(0, 0, false, false), new EditablePoint2D(100, 100), "text")
            };
            System.out.println("Condition shape created");
            return shape;
        }

        if (name.equals(BLOCK_TYPE[3])) { // Start / End
            Shape[] shape = {
                new RectangleShape(new EditablePoint2D(), new EditablePoint2D(200, 100), false),
                new OvalShape(new EditablePoint2D(0, 0, false, false), new EditablePoint2D(200, 100)),
                new TextShape(new EditablePoint2D(0, 0, false, false), new EditablePoint2D(200, 100), "text")
            };
            System.out.println("Start / End shape created");
            return shape;
        }

        if (name.equals(BLOCK_TYPE[4])) { // Input / Output
            Shape[] shape = {
                new RectangleShape(new EditablePoint2D(), new EditablePoint2D(200, 100), false),
                new LineShape(new EditablePoint2D(20, 0), new EditablePoint2D(180, 0), false, true),
                new LineShape(new EditablePoint2D(200, 0), new EditablePoint2D(-20, 100), false, true),
                new LineShape(new EditablePoint2D(180, 100), new EditablePoint2D(-180, 0), false, true),
                new LineShape(new EditablePoint2D(0, 100), new EditablePoint2D(20, -100), false, true),
                new TextShape(new EditablePoint2D(0, 0, false, false), new EditablePoint2D(200, 100), "text")
            };
            System.out.println("Input / Output shape created");
            return shape;
        }

        if (name.equals(BLOCK_TYPE[5])) { // UML Class 1 field
            Shape[] shape = {
                new RectangleShape(new EditablePoint2D(), new EditablePoint2D(100, 100)),
                new TextShape(new EditablePoint2D(0, 0), new EditablePoint2D(100, 20, true, false), "class"),
                new LineShape(new EditablePoint2D(0, 20, false, false), new EditablePoint2D(100, 0, true, false), false, true),
                new TextShape(new EditablePoint2D(0, 20, false, false), new EditablePoint2D(100, 80), "-\n-\n-", true)
            };
            System.out.println("UML Class 1 field shape created");
            return shape;
        }
        if (name.equals(BLOCK_TYPE[6])) { // UML Class 2 fields
            Shape[] shape = {
                new RectangleShape(new EditablePoint2D(), new EditablePoint2D(100, 100)),
                new TextShape(new EditablePoint2D(0, 0), new EditablePoint2D(100, 20, true, false), "class"),
                new LineShape(new EditablePoint2D(0, 20, false, false), new EditablePoint2D(100, 0, true, false), false, true),
                new TextShape(new EditablePoint2D(0, 20, false, false), new EditablePoint2D(100, 40), "-\n-\n-", true),
                new LineShape(new EditablePoint2D(0, 60, false, true), new EditablePoint2D(100, 0, true, true), false, true),
                new TextShape(new EditablePoint2D(0, 60, false, true), new EditablePoint2D(100, 40), "+\n+\n+", true)
            };
            System.out.println("UML Class 2 fields shape created");
            return shape;
        }
        if (name.equals(BLOCK_TYPE[7])) { // UML Abstract class
            Shape[] shape = {
                new RectangleShape(new EditablePoint2D(), new EditablePoint2D(100, 120)),
                new TextShape(new EditablePoint2D(0, 0), new EditablePoint2D(100, 40, true, false), "<<abstract>>\nclass"),
                new LineShape(new EditablePoint2D(0, 40, false, false), new EditablePoint2D(100, 0, true, false), false, true),
                new TextShape(new EditablePoint2D(0, 40, false, false), new EditablePoint2D(100, 40), "-\n-\n-", true),
                new LineShape(new EditablePoint2D(0, 80, false, true), new EditablePoint2D(100, 0, true, true), false, true),
                new TextShape(new EditablePoint2D(0, 80, false, true), new EditablePoint2D(100, 40), "+\n+\n+", true)
            };
            System.out.println("UML Abstract class shape created");
            return shape;
        }
        if (name.equals(BLOCK_TYPE[8])) { // UML Interface
            Shape[] shape = {
                new RectangleShape(new EditablePoint2D(), new EditablePoint2D(100, 120)),
                new TextShape(new EditablePoint2D(0, 0), new EditablePoint2D(100, 40, true, false), "<<interface>>\ninterface"),
                new LineShape(new EditablePoint2D(0, 40, false, false), new EditablePoint2D(100, 0, true, false), false, true),
                new TextShape(new EditablePoint2D(0, 40, false, false), new EditablePoint2D(100, 80), "+\n+\n+", true)
            };
            System.out.println("UML Interface shape created");
            return shape;
        }
        if (name.equals(BLOCK_TYPE[9])) { // UML Enumeration
            Shape[] shape = {
                new RectangleShape(new EditablePoint2D(), new EditablePoint2D(100, 120)),
                new TextShape(new EditablePoint2D(0, 0), new EditablePoint2D(100, 40, true, false), "<<enumeration>>\nenum"),
                new LineShape(new EditablePoint2D(0, 40, true, false), new EditablePoint2D(100, 0, true, false), false, true),
                new TextShape(new EditablePoint2D(0, 40), new EditablePoint2D(100, 80), "", true)
            };
            System.out.println("UML Enumeration shape created");
            return shape;
        }

        //===================== LINES =====================
        if (name.equals(LINE_TYPE[0])) { // Line
            EditablePoint2D p1 = new EditablePoint2D(0, 0, false, false);
            EditablePoint2D size = new EditablePoint2D(100, 0, true, true);
            EditablePoint2D pText1 = new EditablePoint2D(
                    p1.getX() + size.getX() / 2 - 50,
                    p1.getY() + size.getY() / 2 + 10
            );
            EditablePoint2D pText2 = new EditablePoint2D(p1.getX() + 50, size.getY() + 20);
            Shape[] shape = {
                new LineShape(p1, size),
                new TextShape(pText1, pText2, "text")
            };
            System.out.println("Line shape created");
            return shape;
        }
        if (name.equals(LINE_TYPE[1])) { // Arrow
            EditablePoint2D p1 = new EditablePoint2D(0, 0, false, false);
            EditablePoint2D size = new EditablePoint2D(100, 0, true, true);
            EditablePoint2D pText1 = new EditablePoint2D(
                    p1.getX() + size.getX() / 2 - 50,
                    p1.getY() + size.getY() / 2 + 10
            );
            EditablePoint2D pText2 = new EditablePoint2D(p1.getX() + 50, size.getY() + 20);
            Shape[] shape = {
                new ArrowShape(p1, size),
                new TextShape(pText1, pText2, "text")
            };
            System.out.println("Arrow shape created");
            return shape;
        }
        if (name.equals(LINE_TYPE[2])) { // UML Direct Association
            EditablePoint2D p1 = new EditablePoint2D(0, 0, false, false);
            EditablePoint2D size = new EditablePoint2D(100, 0, true, true);
            EditablePoint2D pText1 = new EditablePoint2D(
                    p1.getX() + size.getX() / 2 - 50,
                    p1.getY() + size.getY() / 2 + 10
            );
            EditablePoint2D pText2 = new EditablePoint2D(p1.getX() + 50, size.getY() + 20);
            Shape[] shape = {
                new ArrowShape(p1, size, false, false, ArrowShape.HEAD_TYPES.TICK),
                new TextShape(pText1, pText2, "text")
            };
            System.out.println("UML Direct Association shape created");
            return shape;
        }
        if (name.equals(LINE_TYPE[3])) { // UML Aggregation
            EditablePoint2D p1 = new EditablePoint2D(0, 0, false, false);
            EditablePoint2D size = new EditablePoint2D(100, 0, true, true);
            EditablePoint2D pText1 = new EditablePoint2D(
                    p1.getX() + size.getX() / 2 - 50,
                    p1.getY() + size.getY() / 2 + 10
            );
            EditablePoint2D pText2 = new EditablePoint2D(p1.getX() + 50, size.getY() + 20);
            Shape[] shape = {
                new ArrowShape(p1, size, false, false, ArrowShape.HEAD_TYPES.DIAMOND_EMPTY),
                new TextShape(pText1, pText2, "text")
            };
            System.out.println("UML Aggregation shape created");
            return shape;
        }
        if (name.equals(LINE_TYPE[4])) { // UML Composition
            EditablePoint2D p1 = new EditablePoint2D(0, 0, false, false);
            EditablePoint2D size = new EditablePoint2D(100, 0, true, true);
            EditablePoint2D pText1 = new EditablePoint2D(
                    p1.getX() + size.getX() / 2 - 50,
                    p1.getY() + size.getY() / 2 + 10
            );
            EditablePoint2D pText2 = new EditablePoint2D(p1.getX() + 50, size.getY() + 20);
            Shape[] shape = {
                new ArrowShape(p1, size, false, false, ArrowShape.HEAD_TYPES.DIAMOND),
                new TextShape(pText1, pText2, "text")
            };
            System.out.println("UML Composition shape created");
            return shape;
        }
        if (name.equals(LINE_TYPE[5])) { // UML Generalization
            EditablePoint2D p1 = new EditablePoint2D(0, 0, false, false);
            EditablePoint2D size = new EditablePoint2D(100, 0, true, true);
            EditablePoint2D pText1 = new EditablePoint2D(
                    p1.getX() + size.getX() / 2 - 50,
                    p1.getY() + size.getY() / 2 + 10
            );
            EditablePoint2D pText2 = new EditablePoint2D(p1.getX() + 50, size.getY() + 20);
            Shape[] shape = {
                new ArrowShape(p1, size, false, false, ArrowShape.HEAD_TYPES.EMPTY),
                new TextShape(pText1, pText2, "text")
            };
            System.out.println("UML Generalization shape created");
            return shape;
        }
        if (name.equals(LINE_TYPE[6])) { // UML Realization
            EditablePoint2D p1 = new EditablePoint2D(0, 0, false, false);
            EditablePoint2D size = new EditablePoint2D(100, 0, true, true);
            EditablePoint2D pText1 = new EditablePoint2D(
                    p1.getX() + size.getX() / 2 - 50,
                    p1.getY() + size.getY() / 2 + 10
            );
            EditablePoint2D pText2 = new EditablePoint2D(p1.getX() + 50, size.getY() + 20);
            Shape[] shape = {
                new ArrowShape(p1, size, true, false, ArrowShape.HEAD_TYPES.EMPTY),
                new TextShape(pText1, pText2, "text")
            };
            System.out.println("UML Realization shape created");
            return shape;
        }
        if (name.equals(LINE_TYPE[7])) { // UML Dependency
            EditablePoint2D p1 = new EditablePoint2D(0, 0, false, false);
            EditablePoint2D size = new EditablePoint2D(100, 0, true, true);
            EditablePoint2D pText1 = new EditablePoint2D(
                    p1.getX() + size.getX() / 2 - 50,
                    p1.getY() + size.getY() / 2 + 10
            );
            EditablePoint2D pText2 = new EditablePoint2D(p1.getX() + 50, size.getY() + 20);
            Shape[] shape = {
                new ArrowShape(p1, size, true, false, ArrowShape.HEAD_TYPES.TICK),
                new TextShape(pText1, pText2, "text")
            };
            System.out.println("UML Dependency shape created");
            return shape;
        }

        if (name.equals(LINE_TYPE[8])) { // Dashed Line
            EditablePoint2D p1 = new EditablePoint2D(0, 0, false, false);
            EditablePoint2D size = new EditablePoint2D(100, 0, true, true);
            EditablePoint2D pText1 = new EditablePoint2D(
                    p1.getX() + size.getX() / 2 - 50,
                    p1.getY() + size.getY() / 2 + 10
            );
            EditablePoint2D pText2 = new EditablePoint2D(p1.getX() + 50, size.getY() + 20);
            Shape[] shape = {
                new LineShape(p1, size, true, false),
                new TextShape(pText1, pText2, "text")
            };
            System.out.println("Dashed Line shape created");
            return shape;
        }

        //===================== OTHER =====================
        if (name.equals(OTHER_TYPE[0])) {
            Shape[] shape = {
                new TextShape(new EditablePoint2D(0, 0), new EditablePoint2D(100, 20), "text")
            };
            System.out.println("Text shape created");
            return shape;
        }

        return null; // shape not found
    }
}
