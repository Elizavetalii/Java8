package com.example.java8;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button One, Two, Three, Four, Five, Six, Seven, Eight, Nine, Zero;
    private Button Minus, Plus, Division, Multiply, Result, Clear, SquareRoot, Square, Percentage;
    private TextView Formula, EndResult;
    private char Action;
    private double ResultValue = Double.NaN;
    private double Value = Double.NaN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setupView();

        View.OnClickListener numberClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = (Button) view;
                Formula.setText(Formula.getText().toString() + button.getText().toString());
            }
        };

        One.setOnClickListener(numberClickListener);
        Two.setOnClickListener(numberClickListener);
        Three.setOnClickListener(numberClickListener);
        Four.setOnClickListener(numberClickListener);
        Five.setOnClickListener(numberClickListener);
        Six.setOnClickListener(numberClickListener);
        Seven.setOnClickListener(numberClickListener);
        Eight.setOnClickListener(numberClickListener);
        Nine.setOnClickListener(numberClickListener);
        Zero.setOnClickListener(numberClickListener);

        View.OnClickListener actionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = (Button) view;
                Action = button.getText().charAt(0);
                Formula.setText(Formula.getText().toString() + Action);
                EndResult.setText(null);
            }
        };

        Plus.setOnClickListener(actionClickListener);
        Minus.setOnClickListener(actionClickListener);
        Division.setOnClickListener(actionClickListener);
        Multiply.setOnClickListener(actionClickListener);
        Square.setOnClickListener(actionClickListener);
        Percentage.setOnClickListener(actionClickListener);

        Result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
                EndResult.setText(String.valueOf(ResultValue));
                Formula.setText(null);
            }
        });

        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultValue = Double.NaN;
                Value = Double.NaN;
                Formula.setText("");
                EndResult.setText("");
            }
        });

        SquareRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Formula.setText("√");
            }
        });

        Percentage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
                Action = '%';
                Formula.setText(String.valueOf(ResultValue));
                EndResult.setText("");
            }
        });
    }

    private void setupView() {
        One = findViewById(R.id.One);
        Two = findViewById(R.id.Two);
        Three = findViewById(R.id.Three);
        Four = findViewById(R.id.Four);
        Five = findViewById(R.id.Five);
        Six = findViewById(R.id.Six);
        Seven = findViewById(R.id.Seven);
        Eight = findViewById(R.id.Eight);
        Nine = findViewById(R.id.Nine);
        Zero = findViewById(R.id.Zero);
        Minus = findViewById(R.id.Minus);
        Plus = findViewById(R.id.Plus);
        Result = findViewById(R.id.Result);
        Division = findViewById(R.id.Division);
        Multiply = findViewById(R.id.Multiply);
        EndResult = findViewById(R.id.EndResult);
        Formula = findViewById(R.id.Formula);
        Clear = findViewById(R.id.Clear);
        SquareRoot = findViewById(R.id.SquareRoot);
        Square = findViewById(R.id.Square);
        Percentage = findViewById(R.id.Percentage);
    }

    private void calculate() {
        String textFormula = Formula.getText().toString();
        if (textFormula.isEmpty()) {
            return;
        }

        try {
            if (textFormula.charAt(0) == '√') {
                Value = Double.parseDouble(textFormula.substring(1));
                ResultValue = Math.sqrt(Value);
            } else {
                // Проверка на первый символ действия
                if (isActionSymbol(textFormula.charAt(0))) {
                    throw new IllegalArgumentException("Ошибка: некорректный ввод");
                }

                int index = textFormula.indexOf(Action);
                if (index != -1) {
                    String leftValue = textFormula.substring(0, index);
                    String rightValue = textFormula.substring(index + 1);

                    if (!leftValue.isEmpty()) {
                        ResultValue = Double.parseDouble(leftValue);
                    }

                    if (!rightValue.isEmpty()) {
                        Value = Double.parseDouble(rightValue);
                    }

                    // Проверка на два действия подряд
                    if (isActionSymbol(leftValue.charAt(leftValue.length() - 1))
                            && isActionSymbol(rightValue.charAt(0))) {
                        throw new IllegalArgumentException("Ошибка: два действия подряд");
                    }

                    switch (Action) {
                        case '/':
                            if (Value == 0) {
                                Toast.makeText(MainActivity.this, "Ошибка: нельзя делить на ноль!", Toast.LENGTH_SHORT).show();
                                ResultValue = Double.POSITIVE_INFINITY;
                            } else {
                                ResultValue /= Value;
                            }
                            break;
                        case '*':
                            ResultValue *= Value;
                            break;
                        case '+':
                            ResultValue += Value;
                            break;
                        case '-':
                            ResultValue -= Value;
                            break;
                        case '%':
                            // Проверка на правильное использование операции процента
                            if (!Double.isNaN(Value)) {
                                ResultValue = Value * (Double.parseDouble(Formula.getText().toString()) / 100);
                            } else {
                                ResultValue = 0.0;
                            }
                            break;
                        case '²':
                            ResultValue = Math.pow(ResultValue, 2);
                            break;
                    }
                } else {
                    ResultValue = Double.parseDouble(textFormula);
                }
            }
        } catch (NumberFormatException e) {
            ResultValue = Double.NaN;
        } catch (IllegalArgumentException e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            ResultValue = Double.NaN;
        }

        EndResult.setText(String.valueOf(ResultValue));
        Formula.setText("");
    }

    // Метод для проверки, является ли символ символом действия
    private boolean isActionSymbol(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '²' || c == '%';
    }
}
