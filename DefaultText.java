package com.webRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

// 默认显示文本
public class DefaultText implements FocusListener {
    private String defaultText;
    private JTextField defaultField;

    public DefaultText(String Text, JTextField textField){
        this.defaultText = Text;
        this.defaultField = textField;
        textField.setText(Text);
        textField.setForeground(Color.GRAY);
    }

    @Override
    public void focusGained(FocusEvent e) {
        String temp = defaultField.getText();
        if (temp.equals(defaultText)){
            defaultField.setText("");
            defaultField.setForeground(Color.BLACK);
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        String temp = defaultField.getText();
        if (temp.equals("")){
            defaultField.setForeground(Color.GRAY);
            defaultField.setText(defaultText);
        }
    }
}

class DefaultTextarea implements FocusListener{
    private String defaultText;
    private JTextArea defaultTextarea;

    public DefaultTextarea(String Text, JTextArea TextArea){
        this.defaultText = Text;
        this.defaultTextarea = TextArea;
        defaultTextarea.setText(Text);
        defaultTextarea.setForeground(Color.GRAY);
    }

    @Override
    public void focusGained(FocusEvent e) {
        String temp = defaultTextarea.getText();
        if (temp.equals(defaultText)){
            defaultTextarea.setText("");
            defaultTextarea.setForeground(Color.BLACK);
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        String temp = defaultTextarea.getText();
        if (temp.equals("")){
            defaultTextarea.setForeground(Color.GRAY);
            defaultTextarea.setText(defaultText);
        }
    }
}
