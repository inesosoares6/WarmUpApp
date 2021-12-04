package pt.atp.warmupapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class FragmentHome : Fragment(R.layout.fragment_home) {

    private val db = FirebaseFirestore.getInstance()
    private var mAuth: FirebaseAuth? = null
    private val arrayDocs: ArrayList<String> = ArrayList()
    private val arrayTime: ArrayList<String> = ArrayList()
    private val arrayType: ArrayList<String> = ArrayList()
    private val arrayName: ArrayList<String> = ArrayList()
    private val arrayExercises: ArrayList<String> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_home,container,false)
        val userNameText: TextView = rootView.findViewById(R.id.userNameText)
        val genButton: Button = rootView.findViewById(R.id.genButton)
        val setTime: EditText = rootView.findViewById(R.id.set_time)
        mAuth= FirebaseAuth.getInstance()

        var userName : String
        mAuth!!.currentUser?.email?.let {
            db.collection("users").document(it).get()
                    .addOnSuccessListener { result ->
                        userName = result["name"].toString()
                        userNameText.text = userName
                    }
                    .addOnFailureListener {
                        Toast.makeText(context,getString(R.string.error_name), Toast.LENGTH_LONG).show()
                    }
        }

        genButton.setOnClickListener{
            mAuth!!.currentUser?.email?.let {
                db.collection("users").document(it).collection("warm-ups").get()
                        .addOnSuccessListener { result ->
                            for(document in result){
                                arrayDocs.add(document.id)
                                arrayName.add(document["name"].toString())
                                arrayType.add(document["type"].toString())
                                arrayTime.add(document["time"].toString())
                                arrayExercises.add(document["exercises"].toString())
                            }
                            when (arrayDocs.size) {
                                0 -> {
                                    Toast.makeText(context,getString(R.string.add_new_warm_up), Toast.LENGTH_LONG).show()
                                }
                                else -> {
                                    generateWarmUp(setTime.text.toString().toInt())
                                }
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(context,getString(R.string.error_getting_documents), Toast.LENGTH_LONG).show()
                        }
            }
        }

        return rootView
    }

    private fun generateWarmUp(max_time: Int) {
        val possibleSolutions: ArrayList<Int> = ArrayList()
        var i = 0
        for(time in arrayTime){
            if(time.toInt() <= max_time){
                possibleSolutions.add(i)
            }
            i += 1
        }
        if(possibleSolutions.size != 0){
            val randomNum = Random.nextInt(0,possibleSolutions.size)
            val randomWarmUp = hashMapOf(
                    "name" to arrayName[possibleSolutions[randomNum]],
                    "type" to arrayType[possibleSolutions[randomNum]],
                    "time" to arrayTime[possibleSolutions[randomNum]],
                    "exercises" to arrayExercises[possibleSolutions[randomNum]]
            )
            mAuth?.currentUser?.email?.let {
                db.collection("users").document(it).collection("default").document("warm-up").set(randomWarmUp)
                        .addOnSuccessListener {
//                        Toast.makeText(context, activity?.getString(R.string.warm_up_addition_successful), Toast.LENGTH_LONG).show()
                            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.flFragment,FragmentWarmUp())?.commit()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, activity?.getString(R.string.error_adding_warm_up), Toast.LENGTH_LONG).show()
                        }
            }
        } else{
            Toast.makeText(context, activity?.getString(R.string.error_adding_warm_up), Toast.LENGTH_LONG).show()
        }

    }
}