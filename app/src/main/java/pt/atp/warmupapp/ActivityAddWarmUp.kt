package pt.atp.warmupapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ActivityAddWarmUp : AppCompatActivity() {

    private var nameTV: EditText? = null
    private var typeTV: EditText? = null
    private var timeTV: EditText? = null
    private var exercisesTV: EditText? = null
    private var buttonAddWarmUp: Button? = null
    private val db = FirebaseFirestore.getInstance()
    private var mAuth: FirebaseAuth? = null
    private var numWarmUps = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_warm_up)
        initializeUI()
        mAuth= FirebaseAuth.getInstance()

        buttonAddWarmUp!!.setOnClickListener{
            writeWarmUpToDatabase(nameTV?.text.toString(),typeTV?.text.toString(),timeTV?.text.toString(),exercisesTV?.text.toString())
        }

        mAuth!!.currentUser?.email?.let {
            db.collection("users").document(it).get()
                    .addOnSuccessListener { result ->
                        numWarmUps=result["numWarmUps"].toString().toInt()
                    }
                    .addOnFailureListener {
                        Toast.makeText(applicationContext, getString(R.string.error_getting_numWarmUps), Toast.LENGTH_LONG).show()
                    }
        }

    }

    private fun writeWarmUpToDatabase(name: String, type: String, time: String, exercises: String){
        val warmUp = hashMapOf(
                "name" to name,
                "type" to type,
                "time" to time,
                "exercises" to exercises
        )
        // add zeros before the numWarmUps so that the documents stay in order
        val docName: String = if((numWarmUps+1)<10){
            "00"+(numWarmUps+1).toString()
        } else if ((numWarmUps+1)>9 && (numWarmUps+1)<100){
            "0"+(numWarmUps+1).toString()
        } else{
            (numWarmUps+1).toString()
        }
        mAuth?.currentUser?.email?.let { it1 ->
            db.collection("users").document(it1).collection("warm-ups").document(docName).set(warmUp)
                    .addOnSuccessListener {
                        Toast.makeText(applicationContext, getString(R.string.warm_up_addition_successful), Toast.LENGTH_LONG).show()
                        updateNumWarmUps()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(applicationContext, getString(R.string.error_adding_warm_up), Toast.LENGTH_LONG).show()
                    }
        }
    }

    private fun updateNumWarmUps() {
        mAuth?.currentUser?.email?.let {
            db.collection("users").document(it).update(mapOf("numWarmUps" to (numWarmUps+1)))
        }
    }

    private fun initializeUI() {
        nameTV = findViewById(R.id.add_warm_up_name)
        typeTV = findViewById(R.id.add_warm_up_type)
        timeTV = findViewById(R.id.add_warm_up_time)
        exercisesTV = findViewById(R.id.add_warm_up_exercises)
        buttonAddWarmUp = findViewById(R.id.add_warm_up_submit)
    }
}