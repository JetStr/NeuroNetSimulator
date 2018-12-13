package utils;
/* This class is used for returning two arrays - data array and teacher array -
 parsed from excel file*/
public class ExcelParserResult {

    private double[][] data;
    private double[][] teacher;

    public double[][] getData() {
        return data;
    }

    public void setData(double[][] data) {
        this.data = data;
    }

    public double[][] getTeacher() {
        return teacher;
    }

    public void setTeacher(double[][] teacher) {
        this.teacher = teacher;
    }
}
