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
        data.add(new LinkedList<Character>());
        
        
        cursorCol = cursorRow = 0;
        display.displayCursor(' ', cursorRow, cursorCol);
    }
    
    
    
    public void updateDisplayRow(int row) {
        LinkedList<Character> dataRow = data.get(cursorRow);
        for (int i = 0; i < CharacterDisplay.WIDTH; i++) {
             
            Character c = '\u0000';
            
            if (dataRow.size() > i)
                c = dataRow.get(i);
            
            
            
            display.displayChar(c, row, i);
        }
        
    }
    
    /**
     * Blir kalt ved tastetrykk, skal printe til skjerm og manipulere data-listen
     * @param c 
     */
    public void insertChar(char c) {
        
        display.displayChar(c, cursorRow, cursorCol);
        
        LinkedList<Character> currentRow = data.get(cursorRow);
        
        if (cursorCol > currentRow.size())
            currentRow.add(c);
        else {
            //setter inn character i midten
            currentRow.add(cursorCol,c);
            updateDisplayRow(cursorRow);
        }
        
        
        if (currentRow.size() > CharacterDisplay.WIDTH) {
            //line overflow, split row into new
            
            
            LinkedList<Character> newRow = new LinkedList<Character>();
            newRow.add(currentRow.getLast());
            currentRow.removeLast();
            data.add(cursorRow, newRow);
            
            updateDisplayRow(cursorRow);
        }
            
        
        cursorCol++;
        if (cursorCol >= CharacterDisplay.WIDTH) {
            cursorCol = 0;
            
            data.add(cursorRow, new LinkedList<>());
            cursorRow++;
        }
        
        if (cursorCol > 6 && cursorRow == 1) {
            cursorCol = 6;
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
