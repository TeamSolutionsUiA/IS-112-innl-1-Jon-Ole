/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package editor;

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
    
    
    

    public Document(CharacterDisplay display) {
        this.display = display;
        //data = new char[CharacterDisplay.HEIGHT][CharacterDisplay.WIDTH];
        data = new LinkedList<>();
        data.add(new LinkedList<>());
        
        
        cursorCol = cursorRow = 0;
        display.displayCursor(' ', cursorRow, cursorCol);
    }
    
    
    /**
     * Oppdaterer displayet til radnr
     * @param rowNum 
     */
    public void updateDisplayRow(int rowNum) {
	if (rowNum >= data.size())
	    return;
	
        LinkedList<Character> dataRow = data.get(rowNum);
        for (int i = 0; i < CharacterDisplay.WIDTH; i++) {
             
            char c = '\u0000';
            
            if (i < dataRow.size())
                c = dataRow.get(i);
            
            
            
            display.displayChar(c, rowNum, i);
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
            
            
            //LinkedList<Character> newRow = new LinkedList<Character>();
	    
	    char c2 = currentRow.getLast();
	    currentRow.removeLast();
            addCharToStartOfRow(c2, cursorRow + 1);
            
            
            
	    updateDisplayFromRow(cursorRow);
            
        }
            
        
        cursorCol++;
        if (cursorCol >= CharacterDisplay.WIDTH) {
            cursorCol = 0;
            cursorRow++;
            if (data.size() <= cursorRow) 
		data.add(cursorRow, new LinkedList<>());
            
        }
        
        if (cursorCol > 8 && cursorRow == 2) {
            cursorCol = 4;
            cursorRow = 0;
        }
        
        
	
        display.displayCursor(c,
                              cursorRow, cursorCol);
        /*
        display.displayCursor(data[cursorRow][cursorCol],
                              cursorRow, cursorCol);
        */
    }
}
