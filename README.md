# hyperskill-text-editor

Worked on project Text editor with the use of Swing library

# Work on project. Stage 4/4: Searching

In this stage, you will improve the usability of your text editor.

The first thing to point out is that the "Save" and "Load" buttons take up a lot of space on the screen. You can't add a lot of buttons to the top bar when they contain text. This problem can be solved using icons instead of text. The constructor of JButton can take an ImageIcon instead of text. You should replace the buttons "Load" and "Save" with corresponding icons. You can download the icons from the internet.

It is also not useful to allow users to load a file from only one directory — the project folder. The Swing library has a useful component for navigating your filesystem - JFileChooser. So, when the user clicks on the "Open" button (is was the button that previously named "Load") you should open a file manager using JFileChooser and let the user choose the file he wants to open. Then, the contents of this file should be visible in the text editor.

Also, you should add a search panel. It should contain a text field, a button "Start search", a button "Previous match" and a button "Next match.” All of these buttons should also be icons. The search can be by regular expressions or by plain text. For this, you should add a checkbox that is checked when the user wants to search using a regular expression. You can use JCheckBox for this. The search can slow down the GUI thread, so you should implement the search in a separate thread.

After the user presses the "Start search" button, the program should select the first part of the text that is matched and set the caret to the end of the selected part. Use buttons "Next match" and "Previous match" to iterate through all matches in the text.

Do not forget about the menu. You can add a new menu list with search functionality that copies all the search buttons.

# Final changes

Changed the shape and location of the buttons.

Added buttons to start search, search forward in the text and back. Added search menu with duplicate functions.

Added a checkbox for searching by regular expressions.

Added icons to buttons and menu items.

Added convenient downloading and writing files using the file manager.
