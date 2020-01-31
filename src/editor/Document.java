/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package editor;

import java.util.Iterator;
import java.lang.Math;
import editor.display.CharacterDisplay;
import java.util.LinkedList;

/**
 * This class represents the document being edited. Using a 2d array to hold the
 * document content is probably not a very good choice. Fixing this class is the
 * main focus of the exercise. In addition to designing a better data model, you
 * must add methods to do at least basic editing: write and delete text, and
 * moving the cursor
 *
 * @author evenal
 */
public class Document {

    private CharacterDisplay display;
    private int cursorRow;
    private int cursorCol;
    private LinkedList<LinkedList<Character>> data;
    private boolean overwriteMode;
    
    

    public Document(CharacterDisplay display) {
        this.display = display;
        //data = new char[CharacterDisplay.HEIGHT][CharacterDisplay.WIDTH];
        data = new LinkedList<>();
        data.add(new LinkedList<>());
        
        overwriteMode = false;
        cursorCol = cursorRow = 0;
        display.displayCursor(' ', cursorRow, cursorCol);
    }
    
    
    /**
     * Oppdaterer displayet til radnr
     * @param rowNum 
     */
    public void updateDisplayRow(int rowNum) {
	//TODO: optimaliser
	if (rowNum >= data.size())
	    return;
	
        LinkedList<Character> dataRow = data.get(rowNum);
	Iterator<Character> it = dataRow.iterator();
	int i = 0;
	while(it.hasNext()) {
	    Character c = it.next();
	    
            display.displayChar(c, rowNum, i);
	    i++;
	}
	
	//print ut tomme tegn på resten av linjen
        for (int j = i; j < CharacterDisplay.WIDTH; j++) {
            display.displayChar('\u0000', rowNum, j);
        }
        
    }
    
    /**
     * Oppdaterer displayet fra radnr og nedover
     * @param rowNum 
     */
    public void updateDisplayFromRow(int rowNum) {
	for (int i = rowNum; i < CharacterDisplay.HEIGHT; i++)
            updateDisplayRow(i);
    }
    
    /**
     * Legg til char i starten på linjenummer, forsett recursive dersom linjen overflower
     * @param c 
     * @param rowNum radnummer
     */
    public void addCharToStartOfRow(char c, int rowNum) {
	if (rowNum >= data.size()) {
	    data.add(new LinkedList<Character>());
	}
	LinkedList<Character> currentRow = data.get(rowNum);
	
	currentRow.addFirst(c);
	if (currentRow.size() > CharacterDisplay.WIDTH) {
	    char c2 = currentRow.getLast();
	    currentRow.removeLast();
	    addCharToStartOfRow(c2,rowNum + 1);
	}
    }
    
    /**
     * Blir kalt ved tastetrykk, skal printe til skjerm og manipulere data-listen
     * @param c 
     */
    public void insertChar(char c) {
        
        display.displayChar(c, cursorRow, cursorCol);
        
        LinkedList<Character> currentRow = data.get(cursorRow);
        if (overwriteMode && cursorCol < (currentRow.size())) {
	    //overwriting
	    currentRow.set(cursorCol, c);
	}
	else {
	    //inserting
	    if (cursorCol >= currentRow.size())
		//sett inn char på slutten av linjen
		currentRow.add(c);
	    else {
		//setter inn character i midten av linjen
		currentRow.add(cursorCol,c);
		updateDisplayRow(cursorRow);
	    }


	    if (currentRow.size() > CharacterDisplay.WIDTH) {
		//line overflow, split row into new or move to next row if it has space


		char c2 = currentRow.getLast();
		currentRow.removeLast();
		addCharToStartOfRow(c2, cursorRow + 1);



		updateDisplayFromRow(cursorRow);

	    }
	}
        
        cursorCol++;
        if (cursorCol >= CharacterDisplay.WIDTH) {
            cursorCol = 0;
            cursorRow++;
	    
	    //add new row to data if needed
            if (data.size() <= cursorRow) 
		data.add(cursorRow, new LinkedList<>());
            
        }
	
        display.displayCursor(c,
                              cursorRow, cursorCol);
    }
    
    
    public void moveCursor(String key) {
	LinkedList<Character> currentRow = data.get(cursorRow);
	
	
	if (key.equals("enter")) {
	    
	    
	    LinkedList<Character> newRow = new LinkedList<>(currentRow.subList(cursorCol, currentRow.size()));
	    data.add(cursorRow + 1, newRow);
	    currentRow.removeAll(newRow);
	    
	    
	    updateDisplayFromRow(cursorRow);
	    
	    cursorCol = 0;
	    cursorRow++;
	}
	else if (key.equals("up")) {
	    
	    moveCursorToRow(cursorRow - 1);
	}
	else if (key.equals("down")) {
	    
	    moveCursorToRow(cursorRow + 1);
	}
	else if (key.equals("right")) {
	    
	    if (cursorCol < Math.min(CharacterDisplay.WIDTH,currentRow.size()))
		cursorCol++;
	    else if (cursorRow + 1 < data.size()) {
		moveCursorToRow(cursorRow + 1);
	    }
		
		
	    
	    
	}
	else if (key.equals("left")) {
	    
	    
	    if (cursorCol <= 0 && cursorRow > 0) {
		cursorCol = CharacterDisplay.WIDTH - 1;
		moveCursorToRow(cursorRow - 1);
	    }
	    else if (cursorCol <= 0 && cursorRow == 0)
		cursorCol = 0;
	    else
		cursorCol--;
	}
	else if (key.equals("backspace")) {
	    if(currentRow.size() <= 0) {
		//TODO: fjern row
		cursorRow--;
		moveCursorToRow(cursorRow);
		
	    }
	    else {
		currentRow.remove(cursorCol-1);
		moveCursor("left");
	    }
	    updateDisplayFromRow(cursorRow);
		
	}
	
	display.displayCursor('a',
                              cursorRow, cursorCol);
    }
    
    public void miscKeyPressed(String key) {
	if (key.equals("insert")) {
	    if (overwriteMode)
		overwriteMode = false;
	    else
		overwriteMode = true;
	}
    }
    
    public void moveCursorToRow(int rowNum) {
	cursorRow = Math.min(rowNum, data.size() -1);
	cursorRow = Math.max(0,cursorRow);
	
	LinkedList<Character> currentRow = data.get(cursorRow);
	
	cursorCol = Math.min(cursorCol, currentRow.size());
	
	if (cursorCol > currentRow.size())
	    cursorRow = currentRow.size();
    }
    
    
    
}
