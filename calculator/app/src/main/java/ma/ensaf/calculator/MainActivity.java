package ma.ensaf.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private double firstNumber = 0;
    private String operator = "";
    private boolean firstInput = true;
    private String expression = "";
    private String currentNumber = "";
    private boolean resultShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setTitle("Calculator");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void HandleButtons(View v) {
        TextView t = findViewById(R.id.Text);
        Button b = (Button) v;
        String value = b.getText().toString();
        if (resultShown) {
            expression = "";
            currentNumber = "";
            resultShown = false;
        }
        if (value.equals(".")) {
            if (currentNumber.contains(".")) {
                return;
            }
            if (currentNumber.isEmpty()) {
                value = "0.";
            }
        }
        currentNumber += value;
        expression += value;
        t.setText(expression);
    }
    public void opretions(View v) {
        Button b = (Button) v;
        String value = b.getText().toString();
        TextView t = findViewById(R.id.Text);
        if (value.equals("AC")) {
            t.setText("");
            expression = "";
            currentNumber = "";
            firstNumber = 0;
            firstInput = true;
            operator = "";
            resultShown = false;
            return;
        }
        if (value.equals("C")) {
            if (!expression.isEmpty()) {
                expression = expression.substring(0, expression.length() - 1);
                t.setText(expression);
                currentNumber = "";
                firstNumber = 0;
                firstInput = true;
                operator = "";
                resultShown = false;
                String temp = expression;
                expression = "";
                for (int i = 0; i < temp.length(); i++) {
                    char c = temp.charAt(i);
                    if (c == '+' || c == '-' || c == '×' || c == '÷') {
                        if (!currentNumber.isEmpty()) {
                            firstNumber = Double.parseDouble(currentNumber);
                            firstInput = false;
                            operator = String.valueOf(c);
                            currentNumber = "";
                        }
                    } else {
                        currentNumber += c;
                    }
                    expression += c;
                }
            }
            return;
        }
        if (value.equals("+/-") || value.equals("±") || value.equals("＋/－")) {
            if (!currentNumber.isEmpty()) {
                double number = Double.parseDouble(currentNumber);
                number = -number;
                String newNumber = formatNumber(number);
                expression = expression.substring(0, expression.length() - currentNumber.length());
                currentNumber = newNumber;
                expression += currentNumber;
                t.setText(expression);
            } else if (resultShown) {
                double number = -Double.parseDouble(expression);
                expression = formatNumber(number);
                t.setText(expression);
            }
            return;
        }
        if (value.equals("%")) {
            if (!currentNumber.isEmpty()) {
                double number = Double.parseDouble(currentNumber);
                number = number / 100;
                String newNumber = formatNumber(number);
                expression = expression.substring(0, expression.length() - currentNumber.length());
                currentNumber = newNumber;
                expression += currentNumber;
                t.setText(expression);
            } else if (resultShown) {
                double number = Double.parseDouble(expression) / 100;
                expression = formatNumber(number);
                t.setText(expression);
            }
            return;
        }
        if (value.equals("=")) {
            if (!currentNumber.isEmpty() && !firstInput) {
                double number = Double.parseDouble(currentNumber);
                double result = 0;
                if (operator.equals("+")) {
                    result = firstNumber + number;
                } else if (operator.equals("-")) {
                    result = firstNumber - number;
                } else if (operator.equals("×")) {
                    result = firstNumber * number;
                } else if (operator.equals("÷")) {
                    if (number == 0) {
                        t.setText("Error");
                        expression = "";
                        currentNumber = "";
                        firstNumber = 0;
                        firstInput = true;
                        operator = "";
                        resultShown = false;
                        return;
                    }
                    result = firstNumber / number;
                } else {
                    return;
                }
                expression = formatNumber(result);
                t.setText(expression);
                currentNumber = "";
                firstNumber = 0;
                firstInput = true;
                operator = "";
                resultShown = true;
            }
            return;
        }
        if (value.equals("+") || value.equals("-") || value.equals("×") || value.equals("÷")) {
            if (resultShown) {
                currentNumber = expression;
                resultShown = false;
            }
            if (!currentNumber.isEmpty()) {
                double number = Double.parseDouble(currentNumber);
                if (firstInput) {
                    firstNumber = number;
                    firstInput = false;
                } else {
                    if (operator.equals("+")) {
                        firstNumber += number;
                    } else if (operator.equals("-")) {
                        firstNumber -= number;
                    } else if (operator.equals("×")) {
                        firstNumber *= number;
                    } else if (operator.equals("÷")) {
                        if (number == 0) {
                            t.setText("Error");
                            expression = "";
                            currentNumber = "";
                            firstNumber = 0;
                            firstInput = true;
                            operator = "";
                            resultShown = false;
                            return;
                        }
                        firstNumber /= number;
                    }
                }
                operator = value;
                expression += value;
                t.setText(expression);
                currentNumber = "";
            }
        }
    }
    private String formatNumber(double number) {
        if (Double.isNaN(number) || Double.isInfinite(number)) {
            return "Error";
        }
        if (number == Math.rint(number) && number <= Long.MAX_VALUE && number >= Long.MIN_VALUE) {
            return String.valueOf((long) number);
        }
        return String.valueOf(number);
    }
}