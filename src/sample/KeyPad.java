package sample;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class KeyPad {
    private GridPane keyPad = new GridPane();
    private Key q, w, e, r, t, y, u, i, o, p, a, s, d, f, g, h, j, k, l, z, x, c, v, b, n, m;
    private Button enter, clear, exit;
    private TextField display;
    private Group root = new Group();
    private boolean isCodeCorrect = false;
    private int WIDTH, HEIGHT;
    private Font font;

    {
        try {
            font = Font.loadFont(new FileInputStream(new File("src/graphics/Fleftex_M.ttf")), 16);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    KeyPad(int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
    }

    void initialise() {
        Image background = new Image("/graphics/neo.png");
        ImageView backgroundView = new ImageView(background);
        backgroundView.setFitWidth(WIDTH / 4);
        backgroundView.setFitHeight(HEIGHT / 4);
        backgroundView.setTranslateX(WIDTH / 2);
        backgroundView.setTranslateY(HEIGHT / 2 - 100);

        /*Selection keys*/
        q = new Key("q");
        w = new Key("w");
        e = new Key("e");
        r = new Key("r");
        t = new Key("t");
        y = new Key("y");
        u = new Key("u");
        i = new Key("i");
        o = new Key("o");
        p = new Key("p");
        a = new Key("a");
        s = new Key("s");
        d = new Key("d");
        f = new Key("f");
        g = new Key("g");
        h = new Key("h");
        j = new Key("j");
        k = new Key("k");
        l = new Key("l");
        z = new Key("z");
        x = new Key("x");
        c = new Key("c");
        v = new Key("v");
        b = new Key("b");
        n = new Key("n");
        m = new Key("m");

        display = new TextField("");
        display.setMinSize(200, 50);
        display.setFont(font);
        String style = "-fx-background-color: #000000;  -fx-text-fill: #39ff14; -fx-opacity: 1;";
        display.setStyle(style);

        /*Buttons*/
        enter = new Button("ENTER");
        enter.setMinSize(50, 50);
        clear = new Button("C");
        clear.setMinSize(50, 50);
        exit = new Button("EXIT");
        exit.setMinSize(50, 50);
        enter.setFont(font);
        enter.setStyle(style);
        clear.setFont(font);
        clear.setStyle(style);
        exit.setFont(font);
        exit.setStyle(style);


        /*Add buttons to keyPad*/
        keyPad.add(display, 0, 0, 10, 1);

        keyPad.add(q.getButton(), 0, 1);
        keyPad.add(w.getButton(), 1, 1);
        keyPad.add(e.getButton(), 2, 1);
        keyPad.add(r.getButton(), 3, 1);
        keyPad.add(t.getButton(), 4, 1);
        keyPad.add(y.getButton(), 5, 1);
        keyPad.add(u.getButton(), 6, 1);
        keyPad.add(i.getButton(), 7, 1);
        keyPad.add(o.getButton(), 8, 1);
        keyPad.add(p.getButton(), 9, 1);

        keyPad.add(a.getButton(), 0, 2);
        keyPad.add(s.getButton(), 1, 2);
        keyPad.add(d.getButton(), 2, 2);
        keyPad.add(f.getButton(), 3, 2);
        keyPad.add(g.getButton(), 4, 2);
        keyPad.add(h.getButton(), 5, 2);
        keyPad.add(j.getButton(), 6, 2);
        keyPad.add(k.getButton(), 7, 2);
        keyPad.add(l.getButton(), 8, 2);

        keyPad.add(z.getButton(), 1, 3);
        keyPad.add(x.getButton(), 2, 3);
        keyPad.add(c.getButton(), 3, 3);
        keyPad.add(v.getButton(), 4, 3);
        keyPad.add(b.getButton(), 5, 3);
        keyPad.add(n.getButton(), 6, 3);
        keyPad.add(m.getButton(), 7, 3);

        keyPad.add(clear, 0, 4, 3, 1);
        keyPad.add(enter, 4, 4, 3, 1);
        keyPad.add(exit, 7, 4, 3, 1);

        keyPad.setPadding(new Insets(10));
        keyPad.setHgap(10);
        keyPad.setVgap(10);

        root.getChildren().addAll(backgroundView, keyPad);

    }

    Group getRoot() {
        return root;
    }

    String getDisplayText() {
        return display.getText();
    }

    void setDisplayText(String displayText) {
        this.display.setText(displayText);
    }

    public Key getQ() {
        return q;
    }

    public Key getW() {
        return w;
    }

    public Key getE() {
        return e;
    }

    public Key getR() {
        return r;
    }

    public Key getT() {
        return t;
    }

    public Key getY() {
        return y;
    }

    public Key getU() {
        return u;
    }

    public Key getI() {
        return i;
    }

    public Key getO() {
        return o;
    }

    public Key getP() {
        return p;
    }

    public Key getA() {
        return a;
    }

    public Key getS() {
        return s;
    }

    public Key getD() {
        return d;
    }

    public Key getF() {
        return f;
    }

    public Key getG() {
        return g;
    }

    public Key getH() {
        return h;
    }

    public Key getJ() {
        return j;
    }

    public Key getK() {
        return k;
    }

    public Key getL() {
        return l;
    }

    public Key getZ() {
        return z;
    }

    public Key getX() {
        return x;
    }

    public Key getC() {
        return c;
    }

    public Key getV() {
        return v;
    }

    public Key getB() {
        return b;
    }

    public Key getN() {
        return n;
    }

    public Key getM() {
        return m;
    }

    Button getEnter() {
        return enter;
    }

    Button getClear() {
        return clear;
    }

    Button getExit() {
        return exit;
    }

    boolean isCodeCorrect() {
        return isCodeCorrect;
    }

    void setCodeCorrect(boolean codeCorrect) {
        isCodeCorrect = codeCorrect;
    }
}
