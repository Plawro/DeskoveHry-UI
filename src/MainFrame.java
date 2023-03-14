

import com.sun.tools.javac.Main;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;


public class MainFrame extends JFrame{
    private JFileChooser chooser = new JFileChooser(".");
    ArrayList<JRadioButton> rateButtons = new ArrayList<>();
    private JRadioButton rateButton1;
    private JRadioButton rateButton2;
    private JRadioButton rateButton3;

    private JCheckBox ownedButton;
    private JButton previousButton;
    private JButton nextButton;

    private JButton button1;
    private JTextField textField1;
    private JPanel mainPanel;
    private JTextField textField2;
    String line;

    ArrayList<String> nazevHry = new ArrayList();
    ArrayList<String> vlastnimeHru = new ArrayList();
    ArrayList<Integer> oblibenostHry = new ArrayList();
    static String separator = "-";
    int aktualniDeskovka;

    public MainFrame(){
        initComponents();
        rateButtons.add(0, rateButton1);
        rateButtons.add(1, rateButton2);
        rateButtons.add(2, rateButton3);
    }
    private void readFromFile() throws FileNotFoundException {
        int result = chooser.showOpenDialog(this);
        // Klikl uživatel na "Open"? Pokud ano, zpracuj událost
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            textField2.setText(selectedFile.getAbsoluteFile().toString());

            loadFile(selectedFile);
            zobrazAktualniDeskovku();
        }
    }

    private void loadFile(File selectedFile) throws FileNotFoundException {
        try(Scanner scanner = new Scanner(new BufferedReader(new FileReader(selectedFile)))) {
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                String parametry[] = line.split(separator);
                nazevHry.add(parametry[0]);


                try {
                    if(Integer.parseInt(parametry[2]) <= 3 && Integer.parseInt(parametry[2]) > 0){
                        oblibenostHry.add(Integer.parseInt(parametry[2]));
                    } else {
                        JOptionPane.showMessageDialog(this,"Oblíbenost musí být číslo v rozmezí 1-3!");
                    }
                }catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this,"Oblíbenost musí být číslo!");
                    mainPanel.setVisible(false);
                    throw new RuntimeException("Oblíbenost musí být číslo!");

                }


                if(parametry[1].equals("Ano")){
                    vlastnimeHru.add(parametry[1]);
                }else if (parametry[1].equals("Ne")){
                    vlastnimeHru.add(parametry[1]);
                } else {
                    JOptionPane.showMessageDialog(this,"Špatná informace o vlastnění hry! Musí být Ano/Ne");
                    vlastnimeHru.add("Err"); // Nebo udělat, že se "automaticky" nastaví na "Ne"

                }
            }
        }catch (FileNotFoundException e){
            throw new RuntimeException(e);
        }

    }
    private void initComponents() {
        setTitle("Deskové hry");
        setContentPane(mainPanel);
        button1.addActionListener(e -> {
            try {
                readFromFile();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        nextButton.addActionListener(e -> {
            dalsiDeskovka(1);
        });

        previousButton.addActionListener(e -> {
            dalsiDeskovka(-1);
        });
    }



    private void dalsiDeskovka(int smer){
        int pocetDeskovek = nazevHry.size();
        if(pocetDeskovek == 0){
            aktualniDeskovka = 0;
            return;
        }

        aktualniDeskovka += smer;

        if(aktualniDeskovka >= pocetDeskovek){
            aktualniDeskovka = 0;
        }

        if(aktualniDeskovka <= -1){
            aktualniDeskovka = pocetDeskovek-1;
        }
        zobrazAktualniDeskovku();
    }


    private void zobrazAktualniDeskovku() {
        if(nazevHry.size() == 0){
            textField1.setText("V seznamu nic není");
        }else {
            textField1.setText(nazevHry.get(aktualniDeskovka));

            for (JRadioButton radioButton : rateButtons) {
                radioButton.setSelected(false);
            }
            rateButtons.get(oblibenostHry.get(aktualniDeskovka)-1).setSelected(true);
            if(vlastnimeHru.get(aktualniDeskovka).equals("Ano")){
                ownedButton.setSelected(true);
                ownedButton.setVisible(true);
            }else if (vlastnimeHru.get(aktualniDeskovka).equals("Ne")){
                ownedButton.setSelected(false);
                ownedButton.setVisible(true);
            }else{
                ownedButton.setSelected(false);
                ownedButton.setVisible(false);
            }

        }
    }

    public  static  void main(String[] args){
        MainFrame frame = new MainFrame();
        frame.setSize(600,200);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}



