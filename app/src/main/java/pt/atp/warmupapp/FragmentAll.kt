package pt.atp.warmupapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import pt.atp.warmupapp.models.WarmUpAdapter

class FragmentAll : Fragment(R.layout.fragment_all) {

    private val db = FirebaseFirestore.getInstance()
    private var mAuth: FirebaseAuth? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val rootView: View = inflater.inflate(R.layout.fragment_all,container,false)
        val arrayName: ArrayList<String> = ArrayList()
        val arrayTime: ArrayList<String> = ArrayList()
        val arrayType: ArrayList<String> = ArrayList()
        val newButton: FloatingActionButton = rootView.findViewById(R.id.newButton)
        mAuth= FirebaseAuth.getInstance()

        newButton.setOnClickListener{
            val intent = Intent(context,ActivityAddWarmUp::class.java)
            startActivity(intent)
        }

        mAuth!!.currentUser?.email?.let {
            db.collection("users").document(it).collection("warm-ups").get()
                    .addOnSuccessListener { result ->
                        for(document in result){
                            arrayName.add(document["name"].toString())
                            arrayType.add(document["type"].toString())
                            arrayTime.add(document["time"].toString()+" min")
                        }
                        if(arrayName.size == 1){
                            sendData(rootView, arrayName,arrayType, arrayTime)
                        } else{
                            sendData(rootView, arrayName.reversed() as ArrayList<String>, arrayType.reversed() as ArrayList<String>, arrayTime.reversed() as ArrayList<String>)
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context,getString(R.string.error_getting_documents), Toast.LENGTH_LONG).show()
                    }
        }

        return rootView
    }

    private fun sendData(rootView: View, arrayName: ArrayList<String>, arrayType: ArrayList<String>, arrayTime: ArrayList<String>) {
        val myListAdapter = WarmUpAdapter(context as Activity, arrayName.toTypedArray(), arrayType.toTypedArray(), arrayTime.toTypedArray())
        val listView: ListView = rootView.findViewById(R.id.listViewAll)
        listView.adapter = myListAdapter
    }
}