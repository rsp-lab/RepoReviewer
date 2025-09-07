package pl.radek.model;

public class Calculator
{
    public int add(int a, int b) {
        return a + b;
    }
    
    public int substract(int a, int b) {
        return a - b;
    }
    
    public int divide(int a, int b) {
        return a / b;
    }
    
    public void printResult(int result) {
        System.out.println("Result is: " + result);
    }
    
    public static void main(String[] args) {
        var notNeeded = new Calculator();
        Calculator calculator = new Calculator();
        var result = calculator.add(5, 10);
        calculator.printResult(result);
        result = calculator.divide(5, 10);
        calculator.printResult(result);
    }
}