import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class Window extends JFrame {

    private JComboBox<String> box;

    private JTextField neuronNumber[];
    private JTextField presetError;
    private JLabel labels[];
    private JLabel fileName;

    private JMenuItem newItem;
    private JMenuItem openItem;
    private JMenuItem saveItem;
    private JMenuItem exitItem;

    private TitledBorder netName;
    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private JTextArea resultTextArea;

    private JButton dataFileChooseButton;
    private JButton generateButton;
    private JButton startButton;
    private JButton resetButton;
    private JButton testButton;

    public Window(String name){
        initComponents();
        setTitle(name);
    }

    public void initComponents(){

        setSize(900, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Файл");

        newItem = new JMenuItem("Новая сеть");
        fileMenu.add(newItem);

        openItem = new JMenuItem("Открыть сеть");
        fileMenu.add(openItem);

        saveItem = new JMenuItem("Сохранить сеть");
        fileMenu.add(saveItem);

        fileMenu.addSeparator();

        exitItem = new JMenuItem("Выйти");
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);

        setJMenuBar(menuBar);

        JLabel layerNumber = new JLabel("Количество слоев: ");

        String layersNumber[]= new String[]{"1", "2", "3",
                "4", "5"};

        box = new JComboBox<String>(layersNumber);

        neuronNumber = new JTextField[5];
        labels = new JLabel[5];
        for (int i = 0; i < 5; i++) {
            neuronNumber[i] = new JTextField(10);
            labels[i] = new JLabel("Нейронов в " + (i+1) + " слое: ");
        }

        fileName = new JLabel("Выбранный файл: ");
        JLabel errorLabel = new JLabel("Ошибка :");
        presetError = new JTextField(5);

        netName = new TitledBorder("Имя сети");

        labels[0].setVisible(true);
        neuronNumber[0].setVisible(true);
        for (int i = 1; i < 5; i++) {
            labels[i].setVisible(false);
            neuronNumber[i].setVisible(false);
        }

        dataFileChooseButton = new JButton("Выбрать файл данных");
        generateButton = new JButton("Сгенерировать");
        startButton = new JButton("Начать обучение");
        resetButton = new JButton("Сброс настроек сети");
        testButton = new JButton("Тестирование сети");

        resultTextArea = new JTextArea(30,25);


        scrollPane = new JScrollPane(resultTextArea);

        JPanel settingPanel = new JPanel();
        JPanel resultPanel = new JPanel();

        resultPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel = new JPanel();

        settingPanel.setLayout(new GridBagLayout());
        settingPanel.setBorder(new TitledBorder("Настройки сети"));
        settingPanel.add(layerNumber, new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,0,5,0),0,0));
        settingPanel.add(box, new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,0,5,0),0,0));
        settingPanel.add(labels[0], new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,0,5,0),0,0));
        settingPanel.add(neuronNumber[0], new GridBagConstraints(1,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,0,5,0),0,0));
        settingPanel.add(labels[1], new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,0,5,0),0,0));
        settingPanel.add(neuronNumber[1], new GridBagConstraints(1,2,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,0,5,0),0,0));
        settingPanel.add(labels[2], new GridBagConstraints(0,3,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,0,5,0),0,0));
        settingPanel.add(neuronNumber[2], new GridBagConstraints(1,3,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,0,5,0),0,0));
        settingPanel.add(labels[3], new GridBagConstraints(0,4,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,0,5,0),0,0));
        settingPanel.add(neuronNumber[3], new GridBagConstraints(1,4,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,0,5,0),0,0));
        settingPanel.add(labels[4], new GridBagConstraints(0,5,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,0,5,0),0,0));
        settingPanel.add(neuronNumber[4], new GridBagConstraints(1,5,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,0,5,0),0,0));
        settingPanel.add(dataFileChooseButton, new GridBagConstraints(0,6,3,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(5,0,5,0),0,0));
        settingPanel.add(fileName, new GridBagConstraints(0,7,3,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(5,0,5,0),0,0));
        settingPanel.add(errorLabel, new GridBagConstraints(0,8,3,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(5,0,5,0),0,0));
        settingPanel.add(presetError, new GridBagConstraints(1,8,3,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(5,0,5,0),0,0));
        settingPanel.add(generateButton, new GridBagConstraints(0,9,3,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(5,0,5,0),0,0));
        settingPanel.add(startButton, new GridBagConstraints(0,10,3,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(5,0,5,0),0,0));
        settingPanel.add(resetButton, new GridBagConstraints(0,11,3,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(5,0,5,0),0,0));
        settingPanel.add(testButton, new GridBagConstraints(0,12,3,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(5,0,5,0),0,0));


        resultPanel.setBorder(new TitledBorder("Окно результатов"));

        mainPanel.setLayout(new GridBagLayout());
        mainPanel.add(settingPanel,new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0) );
        mainPanel.add(resultPanel,new GridBagConstraints(1,0,1,1,1,1,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0) );
        mainPanel.setBorder(netName);
        mainPanel.setVisible(false);

        add(mainPanel);

    }

    public JComboBox<String> getBox() {
        return box;
    }

    public JTextField getPresetError() {
        return presetError;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JTextArea getResultTextArea() {
        return resultTextArea;
    }

    public TitledBorder getNetName() {
        return netName;
    }

    public JMenuItem getNewItem() {
        return newItem;
    }

    public JMenuItem getOpenItem() {
        return openItem;
    }

    public JMenuItem getSaveItem() {
        return saveItem;
    }

    public JMenuItem getExitItem() {
        return exitItem;
    }

    public JButton getDataFileChooseButton() {
        return dataFileChooseButton;
    }

    public JButton getGenerateButton() {
        return generateButton;
    }

    public JButton getStartButton(){
        return startButton;
    }

    public JButton getResetButton() {
        return resetButton;
    }

    public JButton getTestButton() {
        return testButton;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public JLabel getLayerLabel(int n){
        return labels[n];
    }

    public JLabel getFileName() {
        return fileName;
    }

    public JTextField getNeuronNumberField(int n){
        return neuronNumber[n];
    }
}
