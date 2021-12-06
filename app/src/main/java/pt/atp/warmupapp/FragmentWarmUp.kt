package pt.atp.warmupapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FragmentWarmUp : Fragment(R.layout.fragment_warmup) {

    private val db = FirebaseFirestore.getInstance()
    private var mAuth: FirebaseAuth? = null


    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView: View = inflater.inflate(R.layout.fragment_warmup,container,false)
        val headText: TextView = rootView.findViewById(R.id.warm_up_head)
        val exercisesText: TextView = rootView.findViewById(R.id.warm_up_exercises)
        mAuth= FirebaseAuth.getInstance()

        mAuth!!.currentUser?.email?.let {
            db.collection("users").document(it).collection("default").document("warm-up").get()
                    .addOnSuccessListener { result ->
                        if(result["name"]!=null){
                            headText.text = result["name"].toString() + " - " + result["type"].toString() + " - " + result["time"].toString()+" min"
                            exercisesText.text = result["exercises"].toString()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context,getString(R.string.error_getting_documents), Toast.LENGTH_LONG).show()
                    }
        }

        return rootView
    }
}