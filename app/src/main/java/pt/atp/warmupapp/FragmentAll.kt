package pt.atp.warmupapp

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import pt.atp.warmupapp.models.WarmUpAdapter

class FragmentAll : Fragment(R.layout.fragment_all) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView: View = inflater.inflate(R.layout.fragment_all,container,false)
        val arrayName: ArrayList<String> = ArrayList()
        val arrayTime: ArrayList<String> = ArrayList()
        val arrayType: ArrayList<String> = ArrayList()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun sendData(rootView: View, arrayName: ArrayList<String>, arrayType: ArrayList<String>, arrayTime: ArrayList<String>, arrayPrice: ArrayList<String>, arrayCharger: ArrayList<String>) {
        val myListAdapter = WarmUpAdapter(context as Activity, arrayName.toTypedArray(), arrayType.toTypedArray(), arrayTime.toTypedArray())
        val listView: ListView = rootView.findViewById(R.id.listViewAll)
        listView.adapter = myListAdapter
    }
}