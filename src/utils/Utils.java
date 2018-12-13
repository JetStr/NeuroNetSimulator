package utils;
import entity.Neuron;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.*;
import java.util.Arrays;
import java.util.Iterator;

public class Utils {

    //excel parser method
    public static ExcelParserResult parseExcelFile(File excel) {
        int i = 0;
        int j = 0;
        Object[][] buffer;
        double[][] data;
        double[][] teacher;

        int inputsNumber = 0;
        int teacherNumber = 0;

        ExcelParserResult result = null;

        try {
            //access to excel file via apache poi
            FileInputStream fis = new FileInputStream(excel);
            XSSFWorkbook book = new XSSFWorkbook(fis);
            XSSFSheet sheet = book.getSheetAt(0);

            buffer = new Object[sheet.getLastRowNum()+1][sheet.getRow(0).getLastCellNum()];
            Iterator<Row> itr = sheet.iterator();

            // Iterating over Excel file in Java
            while (itr.hasNext()) {
                Row row = itr.next();

                // Iterating over each column of Excel file
                Iterator<Cell> cellIterator = row.cellIterator();
                //copying excel file into buffer array
                while (cellIterator.hasNext()) {

                    Cell cell = cellIterator.next();

                    switch (cell.getCellType()) {
                        case STRING:
                            buffer[i][j] = cell.getStringCellValue();
                            inputsNumber = j;
                            teacherNumber = buffer[i].length - j - 1;
                            break;
                        case NUMERIC:
                            buffer[i][j] = cell.getNumericCellValue();
                            break;
                    }
                    j++;
                }
                j = 0;
                i++;
                System.out.println("");
            }

            data = new double[sheet.getLastRowNum()+1][inputsNumber];
            teacher = new double[sheet.getLastRowNum()+1][teacherNumber];

            //split buffer array into two arrays - data array and teacher array
            for (int k = 0; k < data.length; k++) {
                for (int l = 0; l < data[k].length; l++) {
                    data[k][l] = (Double) buffer[k][l];
                }
            }
            for (int k = 0; k < teacher.length; k++) {
                for (int l = 0; l < teacher[k].length; l++) {
                    teacher[k][l] = (Double) buffer[k][inputsNumber+l+1];
                }
            }
            result = new ExcelParserResult();

            result.setData(data);
            result.setTeacher(teacher);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //save current neuronet method
    public static void saveNeuroNet(String name, Neuron[][] neuroNet){

        File saveDirectory = new File(System.getProperty("user.dir") + File.separator + "saves");

        if(!saveDirectory.exists()){
            saveDirectory.mkdir();
        }

        File saveFile = new File(saveDirectory + File.separator + name + ".txt");

        try {
            if(saveFile.createNewFile()){
                JOptionPane.showMessageDialog(null, "Сеть успешно сохранена");
            }else if(saveFile.exists()){
                int input = JOptionPane.showConfirmDialog(null, "Файл уже существует, перезаписать?","Перезаписать файл", JOptionPane.YES_NO_CANCEL_OPTION);
                switch (input){
                    case 0:
                        saveFile.delete();
                        saveFile.createNewFile();
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null, "Сеть не была сохранена");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            PrintWriter writer = new PrintWriter(saveFile.getAbsoluteFile());
            writer.print(neuroNet.length + "\n");
            writer.print(neuroNet[0][0].getInput().length + "\n");
            for (int i = 0; i < neuroNet.length; i++) {
                writer.print(neuroNet[i].length + "\n");
                for (int j = 0; j < neuroNet[i].length; j++) {
                    StringBuilder builder = new StringBuilder();
                    for (Double value : neuroNet[i][j].getW()) {
                        builder.append(String.format("%.5f", value)).append(" ");
                    }
                    String weights = builder.toString();
                    writer.print(weights + "\n");
                }
            }
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    //open neuronet method
    public static Neuron[][] openNeuroNet(File savedNet){
        int layerNumber;
        Neuron[][] neuroNet = null;
        int neuronNumber;
        int inputsLength;
        double[] w;
        String line;
        String[] splitted;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(savedNet));
            layerNumber = Integer.parseInt(reader.readLine());
            inputsLength = Integer.parseInt(reader.readLine());

            neuroNet = new Neuron[layerNumber][];

            for (int i = 0; i < layerNumber; i++) {
                neuronNumber = Integer.parseInt(reader.readLine());
                neuroNet[i] = new Neuron[neuronNumber];
                for (int j = 0; j < neuronNumber; j++) {
                    line = reader.readLine();
                    line = line.replace(",", ".");
                    splitted = line.split(" ");
                    w = new double[splitted.length];
                    for (int k = 0; k < splitted.length; k++) {
                        w[k] = Double.parseDouble(splitted[k]);
                    }
                    neuroNet[i][j] = new Neuron(inputsLength);
                    neuroNet[i][j].setW(w);
                }
                inputsLength = neuronNumber;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return neuroNet;
    }

}