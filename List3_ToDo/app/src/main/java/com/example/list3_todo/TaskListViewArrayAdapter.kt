package com.example.list3_todo

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class TaskListViewArrayAdapter(context: Context, var layout: Int, var tasks: List<ToDoTask>) :
    ArrayAdapter<ToDoTask>(context, layout, tasks) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(layout, parent, false)
        }
        val task = tasks[position]
//        Log.d("TODOTASK_adapter", "$task")
        if (layout == R.layout.task_layout_full_view) {
            view!!.findViewById<TextView>(R.id.taskTitle).text = task.name
            view.findViewById<TextView>(R.id.taskDue).text = task.date
            view.findViewById<TextView>(R.id.taskPriority).text = task.priority
            when (task.priority) {
                "trivial" -> (view.findViewById<TextView>(R.id.taskTitle)).setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.lowPriority
                    )
                )
                "important" -> (view.findViewById<TextView>(R.id.taskTitle)).setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.highPriority
                    )
                )
                else -> (view.findViewById<TextView>(R.id.taskTitle)).setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorPrimary
                    )
                )
            }
            when (task.type) {
                "home" -> view.findViewById<ImageView>(R.id.taskImage).setImageResource(R.drawable.home)
                "office" -> view.findViewById<ImageView>(R.id.taskImage).setImageResource(R.drawable.office)
                "school" -> view.findViewById<ImageView>(R.id.taskImage).setImageResource(R.drawable.school)
                else -> view.findViewById<ImageView>(R.id.taskImage).setImageResource(R.drawable.custom)
            }
        } else {
            view!!.findViewById<TextView>(R.id.taskTitleOnly).text = task.name
            when (task.priority) {
                "trivial" -> (view.findViewById<TextView>(R.id.taskTitleOnly)).setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.lowPriority
                    )
                )
                "important" -> (view.findViewById<TextView>(R.id.taskTitleOnly)).setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.highPriority
                    )
                )
                else -> (view.findViewById<TextView>(R.id.taskTitleOnly)).setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorPrimary
                    )
                )
            }
        }
        return view
    }
}