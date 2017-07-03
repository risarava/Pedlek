package com.icm.calapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icm.calapp.MainActivity;
import com.icm.calapp.R;
import com.icm.calapp.custom.AbstractAppCompatActivity;
import com.icm.calapp.custom.Calculator;
import com.icm.calapp.database.UserInfoManager;
import com.icm.calapp.model.UserInfoObject;

import static com.icm.calapp.MainActivity.EXTRA_CALORIE;

public class CalculatorResultActivity extends AbstractAppCompatActivity implements View.OnClickListener{

    private int calorie = 0;
    private double bmr;
    private double tdee;
    private UserInfoObject userInfoObject;

    private TextView txtAge;
    private TextView txtWeight;
    private TextView txtHeight;
    private TextView txtReligion;
    private TextView txtBMR;
    private TextView txtTDEE;
    private TextView txtCalorie;
    private TextView txtResult;
    private TextView txtRecommend;
    private ImageView imgIcon;

    private UserInfoManager userInfoManager;

    @Override
    protected int setContentView() {
        return R.layout.activity_calculatetor_result;
    }

    @Override
    protected void bindActionbar(ImageView menuLeft, ImageView menuRight, LinearLayout toolbar, TextView txtTitleToolbar) {

    }

    @Override
    protected void bindUI(Bundle savedInstanceState) {
        setTitle(R.string.title_toolbar_calculation_result);
        txtAge = (TextView) findViewById(R.id.textviewAge);
        txtWeight = (TextView) findViewById(R.id.textviewWeight);
        txtHeight = (TextView) findViewById(R.id.textviewHeight);
        txtReligion = (TextView) findViewById(R.id.textviewReligion);
        txtBMR = (TextView) findViewById(R.id.textviewBMR);
        txtTDEE = (TextView) findViewById(R.id.textviewTDEE);
        txtCalorie = (TextView) findViewById(R.id.textviewCalorie);
        txtResult = (TextView) findViewById(R.id.textveiwResult);
        txtRecommend = (TextView) findViewById(R.id.textviewRecommend);
        imgIcon = (ImageView) findViewById(R.id.imageviewIconGender);

        calorie = getIntent().getIntExtra(EXTRA_CALORIE, 0);

        userInfoManager = new UserInfoManager();

        userInfoObject = userInfoManager.queryAll().get(0);

        txtRecommend.setOnClickListener(this);

    }

    @Override
    protected void setupUI() {
        onBackPressedButtonLeft();
        calculate();

        updateProfile();
    }

    private void calculate() {
        txtCalorie.setText(getString(R.string.calculator_result_calorie, (calorie)));

        bmr = Calculator.calculateBMR(userInfoObject.getGender(), userInfoObject.getWeight(),
                userInfoObject.getHeight(), userInfoObject.getAge());
        txtBMR.setText(getString(R.string.calculator_result_bmr, bmr));
        tdee = Calculator.calculateTDEE(bmr, userInfoObject.getExercise());
        txtTDEE.setText(getString(R.string.calculator_result_tdee, tdee));
        double part = calorie - tdee;
//        if (part > (part + 100)) {
//
//        }

//        String recommend = (part + 100 > 0) ?
//                getResources().getString(R.string.button_recommend_food) :
//                getResources().getString(R.string.button_recommend_food);

        txtRecommend.setText(((calorie - tdee) > 0) ?
                getResources().getString(R.string.button_recommend_exercise) :
                getResources().getString(R.string.button_recommend_food));

    }

    private void updateProfile() {
        if (userInfoObject == null) {
            return;
        }
        txtWeight.setText(getString(R.string.calculator_result_weight, userInfoObject.getWeight()));
        txtHeight.setText(getString(R.string.calculator_result_height, (userInfoObject.getHeight())));
        txtAge.setText(getString(R.string.calculator_result_age, (userInfoObject.getAge())));
        txtReligion.setText(getString(R.string.calculator_result_religion, (userInfoObject.getReligionName())));
        imgIcon.setImageResource((userInfoObject.getGender() == 1) ? R.drawable.img_male :
                R.drawable.img_female);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textviewRecommend:
                double part = (calorie - tdee);
                Intent intent = new Intent(activity, ExerciseActivity.class);
                intent.putExtra(EXTRA_CALORIE, part);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
