
package editor.action;

import editor.Document;
import editor.Editor;
import java.awt.event.ActionEvent;

/**
 *
 * @author Ole Christian & Jonathan
 */
public class MiscAction extends EditorAction {

    Editor editor;
    String key;

    public MiscAction(String name, Editor ed, String key) {
        super(name);
        this.editor = ed;
	this.key = key;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Document doc = editor.getDocument();      
	doc.miscKeyPressed(key);
    }
}
