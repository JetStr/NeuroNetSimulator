package entity;

import java.util.Arrays;

public class Neuron {

    private double input[];
    private double w[];
    private double delta;
    private double output;

    public Neuron (int n){
        input = new double[n];
        w = new double[n];
        for (int i = 0; i < w.length ; i++) {
            w[i] = Math.random()/100;
        }
    }

    public void calculateOutput(){
        double NET = 0;
        output = 0;
        for (int i = 0; i < input.length; i++) {
            NET += input[i]*w[i];
        }
        output = function(NET);
    }

    public void calculateW(double t){
        delta = output * (1 - output) * (t - output);
        for (int i = 0; i < w.length; i++) {
            w[i] = w[i] + input[i]*delta;
        }
    }

    public void calculateHiddenW(double d){
        delta = output * (1 - output) * d;
        for (int i = 0; i < w.length; i++) {
            w[i] = w[i] + input[i]*delta;
        }
    }

    private double function(double x){
        return 1/(1 + Math.exp(-x));
    }

    public void debugInfo(){
        System.out.println("Inputs vector " + Arrays.toString(input) + "\n" +
                "Coefficients " + Arrays.toString(w) + "\n" +
                "Output " + output + "\n");

    }

    public void setInput(double input[]) {
        this.input = input;
    }

    public void setW(double[] w) {
        this.w = w;
    }

    public double[] getInput() {
        return input;
    }

    public double[] getW() {
        return w;
    }

    public double getSingleW(int i){
        return w[i];
    }

    public double getDelta() {
        return delta;
    }

    public double getOutput() {
        return output;
    }

}
