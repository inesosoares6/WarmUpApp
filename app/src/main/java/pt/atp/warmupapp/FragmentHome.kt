package pt.atp.warmupapp

import android.content.Intent
import android.os.Bundle
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

class FragmentHome : Fragment(R.layout.fragment_home) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_home,container,false)
        val userNameText: TextView = rootView.findViewById(R.id.userNameText)
        val genButton: Button = rootView.findViewById(R.id.genButton)
        val setTime: EditText = rootView.findViewById(R.id.set_time)
        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()
        val mAuth: FirebaseAuth?
        mAuth= FirebaseAuth.getInstance()

        var userName : String
        mAuth.currentUser?.email?.let {
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
            // TODO generate warm-up
        }

        return rootView
    }
}