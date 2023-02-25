package com.example.app_konachi_kotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.app_konachi_kotlin.Classes.Creditor
import com.example.app_konachi_kotlin.Classes.GaveGot
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalTime

class Calcule_Activity : AppCompatActivity() {
    private var canAddOperation = false;
    private var canAddDecimal = true;
    lateinit var result : TextView;
    lateinit var working_space: TextView;
    lateinit var btn_c : Button;
    lateinit var btn_ac : Button;
    lateinit var btn_save : Button
    private val creditors = mutableListOf<Creditor>()

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calcule)
        result = findViewById(R.id.result);
        working_space = findViewById(R.id.working_space);
        btn_c =findViewById(R.id.btn_c);
        btn_ac =findViewById(R.id.btn_ac);
        btn_save = findViewById(R.id.btn_save);

        btn_save.setOnClickListener(){
            val creditorKey = intent.getStringExtra("creditorKey")
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("GaveGot")
            val amount = result.text.toString()
            val amountInt = amount.toDouble();
            val key = myRef.push().key

            val gavegot = GaveGot(creditorKey.toString(),"gave", "2023-2-19",amountInt)
            if (creditorKey != null) {
                myRef.child(key!!).setValue(gavegot).addOnCompleteListener(){
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Amount Added Successfully ", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, SecendMain::class.java)
                        intent.putExtra("creditorKey", creditorKey)
                        this.startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
        btn_c.setOnClickListener(){
            result.text = ""
            working_space.text = ""
        }
        btn_ac.setOnClickListener(){
            result.text = ""
            working_space.text = ""
        }

    }

    fun numberAction(view: View)
    {
        if(view is Button)
        {
            if(view.text == ".")
            {
                if(canAddDecimal)
                    working_space.append(view.text)

                canAddDecimal = false
            }
            else
                working_space.append(view.text)

            canAddOperation = true
        }
    }
    fun operationAction(view: View)
    {
        if(view is Button && canAddOperation)
        {
            working_space.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }
    fun backSpaceAction(view: View)
    {
        val length = working_space.length()
        if(length > 0)
            working_space.text = working_space.text.subSequence(0, length - 1)
    }

    fun equalsAction(view: View)
    {

        result.text = calculateResults()
        working_space.text = calculateResults()



    }

    private fun calculateResults(): String
    {
        val digitsOperators = digitsOperators()
        if(digitsOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperators)
        if(timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float
    {
        var result = passedList[0] as Float

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
            }
        }

        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any>
    {
        var list = passedList
        while (list.contains('*') || list.contains('/'))
        {
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any>
    {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex && i < restartIndex)
            {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when(operator)
                {
                    '*' ->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' ->
                    {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    '%' ->
                    {
                        newList.add(prevDigit % nextDigit)
                        restartIndex = i + 1
                    }
                    else ->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if(i > restartIndex)
                newList.add(passedList[i])
        }

        return newList
    }

    private fun digitsOperators(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in working_space.text)
        {
            if(character.isDigit() || character == '.')
                currentDigit += character
            else
            {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if(currentDigit != "")
            list.add(currentDigit.toFloat())

        return list
    }

}