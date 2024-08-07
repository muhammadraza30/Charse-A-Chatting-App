package interfaces;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public interface ChatEvent {

    public void mousePressedSendButton(ActionEvent evt);

    public void mousePressedFileButton(ActionEvent evt);

    public void keyPressed(KeyEvent evt);
}
