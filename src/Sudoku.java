import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Sudoku {
   int borderWidth = 600;
   int borderHeight = 650;
   String[][] generate = SudokuGenerator.generate(30);

   String[] puzzle = generate[0];
   String[] solution = generate[1];



    JFrame frame = new JFrame("Sudoku");
   JLabel textLabel = new JLabel();
   JPanel textPanel = new JPanel();
   JPanel boardPanel = new JPanel();
   JPanel buttonPanel = new JPanel();
   JButton numSelected = null;
   int errors = 0;


   Sudoku(){
       frame.setSize(borderWidth, borderHeight);
       frame.setResizable(false);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setLocationRelativeTo(null);
       frame.setLayout(new BorderLayout());

       textLabel.setFont(new Font("Arial", Font.BOLD, 30));
       textLabel.setHorizontalAlignment(SwingConstants.CENTER);
       textLabel.setText("Sudoku: 0");
       textPanel.add(textLabel, BorderLayout.CENTER);
       frame.add(textPanel, BorderLayout.NORTH);


       boardPanel.setLayout(new GridLayout(9,9));
       setupTiles();
       frame.add(boardPanel, BorderLayout.CENTER);
       frame.setVisible(true);
       buttonPanel.setLayout(new GridLayout(1,9));
       setupButtons();
       frame.add(buttonPanel, BorderLayout.SOUTH);

   }

   private void setupButtons() {
       for (int i = 0; i < 9; i++) {
           JButton button = new JButton();
           button.setFont(new Font("Arial", Font.BOLD, 20));
           button.setText(String.valueOf(i+1));
           button.setFocusPainted(false);
           button.setBackground(Color.WHITE);
           button.setFocusable(false);
           button.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                   JButton button = (JButton) e.getSource();
                   if(numSelected != null){
                       numSelected.setBackground(Color.WHITE);
                   }
                   numSelected = button;
                   numSelected.setBackground(Color.lightGray);
               }

           });
           buttonPanel.add(button);
       }
   }

   private void setupTiles(){
       for(int i = 0; i < 9; i++){
           for(int j = 0; j < 9; j++){
               Tile tile = new Tile(i, j);
               char tilechar = puzzle[i].charAt(j);
               if(tilechar != '-'){
                   tile.setFont(new Font("Arial", Font.BOLD, 20));
                   tile.setText(tilechar + "");
                   tile.setBackground(Color.lightGray);
               }else{
                   tile.setFont(new Font("Arial", Font.PLAIN, 20));
                   tile.setBackground(Color.white);
               }

               if((i == 2 && j == 2) || (i == 2 && j == 5) || (i == 5 && j == 2) || (i == 5 && j == 5)){
                   tile.setBorder(BorderFactory.createMatteBorder(1, 1, 5, 5, Color.BLACK));
               }else
               if(i == 2|| i == 5){
                   tile.setBorder(BorderFactory.createMatteBorder(1, 1, 5, 1, Color.BLACK));
               }else if(j == 2 || j == 5){
                   tile.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 5, Color.BLACK));
               } else{
                   tile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
               }
               tile.setFocusable(false);
               boardPanel.add(tile);


               tile.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                     Tile tile = (Tile) e.getSource();
                     int r = tile.getR();
                     int c = tile.getC();

                     if(numSelected != null){
                         if(tile.getText() != ""){
                             return;
                         }
                         String s = numSelected.getText();
                         if(solution[r].charAt(c) == s.charAt(0)){
                             tile.setText(s);
                             tile.setBackground(Color.lightGray);
                         }else{
                             errors++;
                             textLabel.setText("Sudoku: " + errors);
                         }

                     }
                   }
               });

           }
       }
   }

}
