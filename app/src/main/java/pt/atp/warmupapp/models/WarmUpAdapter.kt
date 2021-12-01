package pt.atp.warmupapp.models

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import pt.atp.warmupapp.R

class WarmUpAdapter(private val context: Activity, private val name: Array<String>, private val type: Array<String>, private val time: Array<String>)
    : ArrayAdapter<String>(context, R.layout.layout_all_list, name) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.layout_all_list, null, true)

        val imageView = rowView.findViewById(R.id.iconHistory) as ImageView
        val nameText = rowView.findViewById(R.id.nameHistory) as TextView
        val typeText = rowView.findViewById(R.id.typeHistory) as TextView
        val timeText = rowView.findViewById(R.id.timeHistory) as TextView

        imageView.setImageResource(R.drawable.logo)
        nameText.text = name[position]
        typeText.text = type[position]
        timeText.text = time[position]

        return rowView
    }
}