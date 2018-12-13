import entity.Neuron;
import utils.ExcelParserResult;
import utils.Utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Arrays;

public class Controller {

    private Window window;
    private String neuroNetName;
    private int layerNumber = 1; //amount of layers in neuronet
    private Neuron neuroNet[][]; //neuronet
    private int neuronNumber = 1; //amount of neurons in current layer
    private int inputsLength = 1; //amount of input values
    private int outputsLength = 1; //amount of teacher values
    private int presetError = 5;
    private double[][] inputs;
    private double[][] teacher;
    private double vector[];

    private boolean isGenerated = false;
    private boolean isDataFileChosen = false;

    public Controller(Window window){
        this.window = window;
    }

    public void init(){

        //show or hide input fields
        window.getBox().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (layerNumber > window.getBox().getSelectedIndex() + 1){
                    for (int i = (window.getBox().getSelectedIndex() + 1); i < layerNumber; i++) {
                        window.getLayerLabel(i).setVisible(false);
                        window.getNeuronNumberField(i).setVisible(false);
                    }
                }
                layerNumber = window.getBox().getSelectedIndex() + 1;
                for (int i = 0; i < layerNumber; i++) {
                    window.getLayerLabel(i).setVisible(true);
                    window.getNeuronNumberField(i).setVisible(true);
                }

            }
        });

        //create new neuronet
        window.getNewItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                neuroNetName = JOptionPane.showInputDialog(window,"Введите имя сети");
                if(neuroNetName.isEmpty()){
                    neuroNetName = "Новая сеть";
                }
                window.getNetName().setTitle(neuroNetName);
                window.getMainPanel().setVisible(true);
            }
        });

        //save neuronet
        window.getSaveItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utils.saveNeuroNet(neuroNetName, neuroNet);
            }
        });

        //open saved neuro net
        window.getOpenItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File savedNeuroNet = openDataFile();
                neuroNet = Utils.openNeuroNet(savedNeuroNet);
                neuroNetName = savedNeuroNet.getName();
                neuroNetName = neuroNetName.substring(0, neuroNetName.length() - 4);

                layerNumber = neuroNet.length;

                for (int i = 0; i < layerNumber - 1; i++) {
                    window.getNeuronNumberField(i).setText(String.valueOf(neuroNet[i].length));
                }

                window.getBox().setSelectedIndex(layerNumber-2);//sub output layer and 1 for getting index
                window.getBox().setEnabled(false);
                window.getNetName().setTitle(neuroNetName);

                isGenerated = true;

                window.getMainPanel().setVisible(true);
                layerNumber++;

            }
        });

        //data file choose button
        window.getDataFileChooseButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File dataSourceFile = openDataFile();
                ExcelParserResult result = Utils.parseExcelFile(dataSourceFile);

                window.getFileName().setText("Выбранный файл: " + dataSourceFile.getName());

                inputs = result.getData();
                teacher = result.getTeacher();
                vector = inputs[0];
                inputsLength = inputs[0].length;
                outputsLength = teacher[0].length;

                isDataFileChosen = true;
            }
        });

        //net generate button
        window.getGenerateButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isGenerated && isDataFileChosen) {
                    generateNet();
                    isGenerated = true;
                }else{
                    JOptionPane.showMessageDialog(null, "Сеть уже сгенерирована или не выбран файл с данными!");
                }
            }
        });

        //neuronet learning
        window.getStartButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isGenerated && isDataFileChosen) {
                    window.getResultTextArea().setText("");
                    if(window.getPresetError().getText() != null){
                        presetError = Integer.parseInt(window.getPresetError().getText());
                    }
                    String text = startLearning(inputs, teacher);
                    window.getResultTextArea().setText(text);
                } else{
                    JOptionPane.showMessageDialog(null, "Сеть не сгенерирована или не выбран файл с данными!");
                }
            }
        });

        //reset current neuronet
        window.getResetButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isGenerated){
                    neuroNet = null;
                    isGenerated = false;
                    layerNumber = window.getBox().getSelectedIndex() + 1;
                    window.getBox().setEnabled(true);
                    window.getBox().setSelectedIndex(0);
                    window.getNeuronNumberField(0).setText("");
                    for (int i = 1; i < layerNumber; i++) {
                        window.getLayerLabel(i).setVisible(false);
                        window.getNeuronNumberField(i).setText("");
                        window.getNeuronNumberField(i).setVisible(false);
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Сеть не сгенерирована!");
                }
            }
        });

        //test neuronet
        window.getTestButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isGenerated && isDataFileChosen) {
                    window.getResultTextArea().setText("");
                    if(window.getPresetError().getText() != null){
                        presetError = Integer.parseInt(window.getPresetError().getText());
                    }

                    int count = testNeuroNet(inputs, teacher);
                    window.getResultTextArea().setText("Записей, прошедших тестирование :" + count);
                } else{
                    JOptionPane.showMessageDialog(null, "Сеть не сгенерирована или не выбран файл с данными!");
                }
            }
        });

        //exit
        window.getExitItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        //ban on invalid characters
        for (int i = 0; i < 5; i++) {
            window.getNeuronNumberField(i).addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    if (!Character.isDigit(e.getKeyChar()))
                        e.consume();
                }
            });
        }

    }

    //generate neuronet method
    private void generateNet(){
        layerNumber++;//add output layer
        neuroNet = new Neuron[layerNumber][];
        for (int i = 0; i < layerNumber; i++) {
            if(i != layerNumber-1) {
                neuronNumber = Integer.parseInt(window.getNeuronNumberField(i).getText());
                neuroNet[i] = new Neuron[neuronNumber];
                for (int j = 0; j < neuronNumber; j++) {
                    neuroNet[i][j] = new Neuron(inputsLength);
                }
            }else{
                //output layer
                neuroNet[i] = new Neuron[outputsLength];
                for (int j = 0; j < outputsLength; j++) {
                    neuroNet[i][j] = new Neuron(inputsLength);
                }
            }
            inputsLength = neuronNumber;
            System.out.println("\n");
        }
    }

    //main logic method
    private String startLearning(double[][] inputs, double[][] teacher ){
        int counter = 0;
        double superDelta = 0;
        double error = 100;
        double subError;
        String result;
        StringBuilder resultString = new StringBuilder();
        layerNumber--;
        for (int row = 0; row < inputs.length; row++) {
            while (error > presetError) {
                error = 0;
                for (int i = 0; i <= layerNumber; i++) {
                    double vectorNew[] = new double[neuroNet[i].length];
                    //layer learning
                    for (int j = 0; j < neuroNet[i].length; j++) {
                        neuroNet[i][j].setInput(vector);
                        neuroNet[i][j].calculateOutput();
                        vectorNew[j] = neuroNet[i][j].getOutput();
                    }
                    vector = new double[vectorNew.length];
                    for (int k = 0; k < vectorNew.length; k++) {
                        vector[k] = vectorNew[k];
                        vectorNew[k] = 0;
                    }
                }
                vector = inputs[row];
                //weights adjustment
                for (int i = 0; i < neuroNet[layerNumber].length; i++) {
                    neuroNet[layerNumber][i].calculateW(teacher[row][i]);
                    subError = Math.abs(neuroNet[layerNumber][i].getOutput() - teacher[row][i]) / teacher[row][i] * 100;
                    System.out.println(subError);
                    if(subError > error){
                        error = subError;
                    }
                }
                //back propagation
                for (int i = layerNumber - 1; i >= 0; i--) {
                    for (int j = 0; j < neuroNet[i].length; j++) {
                        for (int k = 0; k < neuroNet[i + 1].length; k++) {
                            superDelta += neuroNet[i + 1][k].getDelta() * neuroNet[i + 1][k].getSingleW(j);
                        }
                        neuroNet[i][j].calculateHiddenW(superDelta);
                        superDelta = 0;
                    }
                }
                counter++;
            }
            resultString.append("Строка ").append(row).append(" :").append("\n").append("Число проходов: ").append(counter).append("\t").append("Выходы :");
            for (int i = 0; i < outputsLength; i++) {
                resultString.append(String.format("%.3f",neuroNet[layerNumber][i].getOutput())).append("\t");
            }
            resultString.append("\n");
            counter = 0;
            error = 100;
        }
        layerNumber++;
        result = String.valueOf(resultString);
        return result;
    }

    private int testNeuroNet(double[][] inputs, double[][] teacher) {
        int counter = 0;
        double subError;
        double error = 0;
        for (int row = 0; row < inputs.length; row++) {
            vector = inputs[row];
                for (int i = 0; i < layerNumber; i++) {
                    double vectorNew[] = new double[neuroNet[i].length];
                    for (int j = 0; j < neuroNet[i].length; j++) {
                        neuroNet[i][j].setInput(vector);
                        neuroNet[i][j].calculateOutput();
                        vectorNew[j] = neuroNet[i][j].getOutput();
                    }
                    vector = new double[vectorNew.length];
                    for (int k = 0; k < vectorNew.length; k++) {
                        vector[k] = vectorNew[k];
                        vectorNew[k] = 0;
                    }
                }

                for (int i = 0; i < neuroNet[layerNumber-1].length; i++) {
                    subError = Math.abs(neuroNet[layerNumber-1][i].getOutput() - teacher[row][i] / teacher[row][i]);
                    if(subError > error){
                        error = subError;
                    }
                }
            System.out.println(error);
                if(error < presetError){
                    counter++;
                }
                error = 0;
        }
        return counter;
    }

    //file open method
    private File openDataFile(){
        JFileChooser fileopen = new JFileChooser();
        File dataSourceFile = null;
        File workingDirectory = new File(System.getProperty("user.dir"));

        fileopen.setCurrentDirectory(workingDirectory);
        int ret = fileopen.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            //file with data
            dataSourceFile = fileopen.getSelectedFile();
        }

        return dataSourceFile;
    }

}
