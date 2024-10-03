package com.example.emi_calculator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // Insert logic to calculate EMI
    Button emiCalcBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Locate the user inputs and the corresponding input fields
        final EditText userPrincipal = findViewById(R.id.principal);
        final EditText userYearlyInterest = findViewById(R.id.interest);
        final EditText userAmortizationYears = findViewById(R.id.years);
        final EditText outputEmi = findViewById(R.id.emi_calc);
        final EditText outputTotalInterest = findViewById(R.id.total_interest);
        final EditText outputTotalAmount = findViewById(R.id.total_amount);
        emiCalcBtn = findViewById(R.id.btn_calculate);

        //If nothing is entered in one of the boxes, an error will appear
        emiCalcBtn.setOnClickListener(v -> {
            String str1 = userPrincipal.getText().toString();
            String str2 = userYearlyInterest.getText().toString();
            String str3 = userAmortizationYears.getText().toString();
            if (TextUtils.isEmpty(str1)) {
                userPrincipal.setError("Enter principal amount and try again!");
                userPrincipal.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(str2)) {
                userYearlyInterest.setError("Enter interest rate and try again!");
                userYearlyInterest.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(str3)) {
                userAmortizationYears.setError("Enter amortization period and try again!");
                userAmortizationYears.requestFocus();
                return;
            }

            //Change variables from text to float, perform calculations
            float rawPrincipal = Float.parseFloat(str1);
            float rawInterest = Float.parseFloat(str2);
            float rawYears = Float.parseFloat(str3);
            float Principal = getPrincipal(rawPrincipal);
            float Rate = calcInterest(rawInterest);
            float Months = calcAmortizationMonths(rawYears);
            float monthlyInterest = calcRateExponent(Rate, Months);
            float emi = calcEmi(Principal, Rate, monthlyInterest);
            float ta = calcTotalAmount(emi, Months);
            float ti = calcTotalInterest(ta, Principal);
            outputEmi.setText(String.valueOf(emi));
            outputTotalInterest.setText(String.valueOf(ti));
            outputTotalAmount.setText(String.valueOf(ta));
        });

        //INTENT USAGE: Sends the user to a link when they click on the URL
        findViewById(R.id.infotext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url;
                url = "https://www.investopedia.com/terms/m/mortgage.asp";

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    //Getter function, calculation function
    public float getPrincipal(float rawPrincipal) {
        return rawPrincipal;
    }
    //Interest conversion from yearly to monthly
    public float calcInterest(float rawInterest) {
        return rawInterest / 12 / 100;
    }
    //Amortization period unit conversion
    public float calcAmortizationMonths(float rawYears) {
        return rawYears * 12;
    }
    //Calculate rate exponent, (1+i)*n
    public float calcRateExponent(float Rate, float Months) {
        return (float)(Math.pow(1 + Rate, Months));
    }
    //Calculate emi
    public float calcEmi(float Principal, float Rate, float monthlyInterest) {
        return (Principal * Rate * monthlyInterest) / (monthlyInterest - 1);
    }
    //Calculate total amount that needs to be paid
    public float calcTotalAmount(float emi, Float Months) {
        return emi * Months;
    }
    //Calculate total interest paid for duration of mortgage
    public float calcTotalInterest(float ta, float Principal) {
        return ta - Principal;
    }


}