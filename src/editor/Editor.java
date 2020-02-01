/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package editor;

import editor.action.*;
import editor.display.CharacterDisplay;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

/**
 * Editor is the main class of the editor application. It is mainly responsible
 * for creating the document and display models, and to connect them up.
 *
 * @author evenal
 */
public class Editor extends JFrame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        Editor editor = new Editor();
        editor.setVisible(true);

        editor.doc = new Document(editor.display);
    }

    private InputMap inputMap;
    private ActionMap actionMap;
    private CharacterDisplay display;
    Document doc;

    public Editor() throws HeadlessException {
        super("Simple Text Editor");

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

        display = new CharacterDisplay();
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(display, BorderLayout.CENTER);

        /**
         * The inputMap and actionMap determine what happens when the user
         * presses a key on the keyboard. The keys are not hard-coded to the
         * actions. The keyboard is
         */
        inputMap = display.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        actionMap = display.getActionMap();
        addKeyMappings();
        pack();
    }

    protected void exit() {
        for (java.awt.Window win : java.awt.Dialog.getWindows()) {
            win.dispose();
        }
        for (java.awt.Frame frame : java.awt.Frame.getFrames()) {
            frame.dispose();
        }
    }

    /**
     * Add a key mapping, which binds an action to a particular key (represented
     * by the keyStroke). Whenever the key is pressed, the actionPerformed()
     * method in the action will be called
     *
     * @param keyStroke key to bind
     * @param action action to bind the key to
     */
    public void addKeyMapping(KeyStroke keyStroke, EditorAction action) {
        inputMap.put(keyStroke, action.getName());
        actionMap.put(action.getName(), action);
    }
    
    

    public void addKeyMappings() {
        inputMap.clear();
        actionMap.clear();
	
	
	addKeyMapping(KeyStroke.getKeyStroke("released ENTER"),		new CursorAction("enter", this, "enter"));
	addKeyMapping(KeyStroke.getKeyStroke("released UP"),		new CursorAction("up", this, "up"));
	addKeyMapping(KeyStroke.getKeyStroke("released DOWN"),		new CursorAction("down", this, "down"));
	addKeyMapping(KeyStroke.getKeyStroke("released RIGHT"),		new CursorAction("right", this, "right"));
	addKeyMapping(KeyStroke.getKeyStroke("released LEFT"),		new CursorAction("left", this, "left"));
	addKeyMapping(KeyStroke.getKeyStroke("released BACK_SPACE"),	new CursorAction("backspace", this, "backspace"));
	
	addKeyMapping(KeyStroke.getKeyStroke("released INSERT"),	new MiscAction("insert", this, "insert"));
	
	
        for (char ch = '!'; ch <= 'ÿ'; ch++) { //
            String name = "insertChar";
            EditorAction action = new InsertAction(name, this);
            addKeyMapping(KeyStroke.getKeyStroke(ch), action);
        }
	addKeyMapping(KeyStroke.getKeyStroke(' '), new InsertAction("insertChar", this));
	
	/*
	for (int i = 0; i < 255; i++) {
	    KeyStroke ks = KeyStroke.getKeyStroke((char) i);
	    if (inputMap.get(ks) == null) {
		String name = "insertChar";
		EditorAction action = new InsertAction(name, this);

		addKeyMapping(KeyStroke.getKeyStroke((char) i), action);
	    }
	    
	}
	*/
	
	
	
    }

    public CharacterDisplay getDisplay() {
        return display;
    }

    public Document getDocument() {
        return doc;
    }
}
