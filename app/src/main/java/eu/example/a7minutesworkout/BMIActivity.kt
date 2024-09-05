package eu.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import eu.example.a7minutesworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object{
        private const val METRIC_UNITS_VIEW ="METRIC_UNITS_VIEW"
        private const val US_UNITS_VIEW = "US_UNIT_VIEW"
    }


   private var binding: ActivityBmiBinding? = null
    private  var currentVisibleView: String = METRIC_UNITS_VIEW



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarBmiActivity)
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }

        binding?.toolbarBmiActivity?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        makeVisibleMetricUnitsView()

        binding?.rgUnits?.setOnCheckedChangeListener{ _, checkedId: Int ->
            if (checkedId == R.id.rbMetricUnits){
                makeVisibleMetricUnitsView()
            }else{
                makeVisibleMetricUsUnitsView()
            }
        }

        binding?.btnCalculateUnits?.setOnClickListener {
            calculateUnits()
        }
    }

    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.tilMetricUnitWeight?.visibility =View.VISIBLE
        binding?.tilMetricUnitHeight?.visibility = View.VISIBLE
        binding?.tilUsMetricUnitWeightLb?.visibility = View.INVISIBLE
        binding?.tilMetricUsUnitHeightFeet?.visibility =View.INVISIBLE
        binding?.tilMetricUsUnitHeightInch?.visibility = View.INVISIBLE

        binding?.etMetricHeight?.text!!.clear()
        binding?.etMetricWeight?.text!!.clear()

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }

    private fun makeVisibleMetricUsUnitsView(){
        currentVisibleView = US_UNITS_VIEW
        binding?.tilMetricUnitWeight?.visibility =View.INVISIBLE
        binding?.tilMetricUnitHeight?.visibility = View.INVISIBLE
        binding?.tilUsMetricUnitWeightLb?.visibility = View.VISIBLE
        binding?.tilMetricUsUnitHeightFeet?.visibility =View.VISIBLE
        binding?.tilMetricUsUnitHeightInch?.visibility = View.VISIBLE

        binding?.etMetricUsWeightLb?.text!!.clear()
        binding?.etUsMetricUnitHeightFeet?.text!!.clear()
        binding?.etUsMetricUnitHeightInch?.text!!.clear()

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }

    private fun displayBMIResult(bmi: Float){

        val bmiLabel: String
        val bmiDescription: String

        if(bmi.compareTo(15f)<=0){
            bmiLabel ="Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        }else if(bmi.compareTo(15f)>0&& bmi.compareTo(16f)<=0){
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        }else if (bmi.compareTo(16f)>0&&bmi.compareTo(18.5f)<=0 ){
            bmiLabel="Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        }else if(bmi.compareTo(18.5f)>0&&bmi.compareTo(25f)<=0){
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape"
        }else if(bmi.compareTo(25f)>0&&bmi.compareTo(30f)<=0){
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take better care of yourself!Workout more!"
        }else if(bmi.compareTo(30f)>0&&bmi.compareTo(35f)<=0){
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take better care of yourself!Workout more!"
        }else if(bmi.compareTo(35f)>0&&bmi.compareTo(40f)<=0){
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }else {
            bmiLabel = "Obese Class ||| (Vert Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        val bmiValue = BigDecimal(bmi.toDouble())
            .setScale(2,RoundingMode.HALF_EVEN).toString()

        binding?.llDisplayBMIResult?.visibility = View.VISIBLE
        binding?.tvBMIValue?.text = bmiValue
        binding?.BMIType?.text = bmiLabel
        binding?.tvBMIDescription?.text = bmiDescription


    }

    private fun validateMetricUnits():Boolean{
        var isValid = true

        if(binding?.etMetricWeight?.text.toString().isEmpty()){
            isValid = false
        }else if(binding?.etMetricHeight?.text.toString().isEmpty()){
            isValid = false
        }

        return isValid
    }


    private fun calculateUnits(){
        if(currentVisibleView == METRIC_UNITS_VIEW){
            if(validateMetricUnits()){
                val heightValue: Float = binding?.etMetricHeight?.text.toString().toFloat()/100

                val weightValue: Float = binding?.etMetricWeight?.text.toString().toFloat()

                val bmi = weightValue/(heightValue*heightValue)

                displayBMIResult(bmi)

            }else{
                Toast.makeText(this@BMIActivity,
                    "Please enter valid values.", Toast.LENGTH_SHORT).show()
            }
        }else{
            if(validateUSUnits()){
                val usUnitHeightValueFeet : String = binding?.etUsMetricUnitHeightFeet?.text.toString()

                val usUnitHeightValueInch : String = binding?.etUsMetricUnitHeightInch?.text.toString()

                val usUnitWeightValueLb : Float= binding?.etMetricUsWeightLb?.text.toString().toFloat()

                val heightValue =
                    usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() *12

                val bmi = 703 * (usUnitWeightValueLb/(heightValue*heightValue))

                displayBMIResult(bmi)
            }else{
                Toast.makeText(this@BMIActivity,
                    "Please enter valid values.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateUSUnits():Boolean{
        var isValid = true

        when{
            binding?.etMetricUsWeightLb?.text.toString().isEmpty() ->{
                isValid = false
            }
            binding?.etUsMetricUnitHeightFeet?.text.toString().isEmpty() ->{
                isValid = false
            }
            binding?.etUsMetricUnitHeightInch?.text.toString().isEmpty() ->{
                isValid = false
            }
        }
        return isValid
    }
}