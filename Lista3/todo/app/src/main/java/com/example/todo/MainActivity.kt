package com.example.todo

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.my_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val arrows: IntArray = intArrayOf(
        R.drawable.ic_action_up,
        R.drawable.ic_action_down
    )

    private val cal = Calendar.getInstance()
    private val myFormatDate = "dd.MM.yyyy"
    private val myFormatTime = "hh : mm a"

    private var tasks = ArrayList<ListItem>()
    private lateinit var taskAdapter: MyArrayAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listview.setOnItemLongClickListener{ _,_, index, _ ->
            showConfirmation(index)
            true
        }

        taskAdapter = MyArrayAdapter(this, tasks)
        listview.adapter = taskAdapter

        tasks.add(ListItem("1", "work", 1, "19.19.1998", "05 : 11 pm"))
        tasks.add(ListItem("2", "home", 2, "20.19.1997", "03 : 11 am"))
        tasks.add(ListItem("3", "other", 1, "18.19.2010", "05 : 12 pm"))
        tasks.add(ListItem("4", "other", 0, "21.19.1997", "05 : 11 am"))
        tasks.add(ListItem("5", "shopping", 0, "09.19.1998", "05 : 11 pm"))
        tasks.add(ListItem("6", "date", 2, "09.19.1998", "05 : 11 am"))
        tasks.add(ListItem("7", "work", 2, "09.19.1998", "03 : 11 pm"))
        tasks.add(ListItem("8", "other", 1, "09.19.1998", "11 : 11 pm"))

        taskAdapter.notifyDataSetChanged()
    }

    fun bAddClick(view: View) {

        val task = newTask.text.toString()

        if(!task.isBlank()) {
            showDialog(task, "other", 0, "none", "none", -1)
        }
    }

    private fun showConfirmation(index: Int) { //tutaj można też dodać jakieś edytowanie
        val builder = AlertDialog.Builder(this)
        builder.setTitle("What do you want to do with this item?")
        builder.setPositiveButton("Delete") { _, _ ->
            deleteItem(index)
            Toast.makeText(applicationContext, "Task deleted!", Toast.LENGTH_SHORT).show()
        }
//        builder.setNegativeButton(android.R.string.no) { dialog, which ->
//            Toast.makeText(applicationContext, "Delete canceled", Toast.LENGTH_SHORT).show()
//        }
        builder.setNeutralButton("Edit") { _, _ ->
            showDialog(tasks[index].task, tasks[index].image, tasks[index].prior, tasks[index].date, tasks[index].time, index)
        }
        builder.show()
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun showDialog(task: String, image: String, prior: Int, date: String, time: String, idx: Int) { //można to od razu do buttona przypisać se i potem dalszą logikę

        var timage = image
        var tprior = prior
        var tdate = date
        var ttime = time

        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle(task)
        val dl = inflater.inflate(R.layout.my_dialog, null) //dialog layout
        builder.setView(dl)

        dl.pDate.text = date
        dl.pTime.text = time
        when (prior) {
            0 -> dl.pPrior.text = "normal"
            1 -> dl.pPrior.text = "important"
            2 -> dl.pPrior.text = "very important"
        }
        dl.pType.text = image

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val sdf = SimpleDateFormat(myFormatDate, Locale.UK)
            dl.pDate.text = sdf.format(cal.time)
            tdate = sdf.format(cal.time)
        }
        val timeSetListener = TimePickerDialog.OnTimeSetListener{ _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            val sdf = SimpleDateFormat(myFormatTime, Locale.UK)
            dl.pTime.text = sdf.format(cal.time)
            ttime = sdf.format(cal.time)
        }
        dl.pDate.setOnClickListener {
            DatePickerDialog(this,
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        dl.pTime.setOnClickListener{
            TimePickerDialog(this,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                false).show()
        }
        dl.bP0.setOnClickListener{ tprior = setPriorText(dl.pPrior, 0, "normal") }
        dl.bP1.setOnClickListener{ tprior = setPriorText(dl.pPrior, 1, "important") }
        dl.bP2.setOnClickListener{ tprior = setPriorText(dl.pPrior, 2, "very important") }
        dl.bOther.setOnClickListener{ timage = setTypeText(dl.pType, "other") }
        dl.bHome.setOnClickListener{ timage = setTypeText(dl.pType, "home") }
        dl.bWork.setOnClickListener{ timage = setTypeText(dl.pType, "work") }
        dl.bShopping.setOnClickListener{ timage = setTypeText(dl.pType, "shopping") }
        dl.bDate.setOnClickListener{ timage = setTypeText(dl.pType, "date") }
        builder.setPositiveButton("OK") {
            _, _ ->
            if(idx == -1) {
                addTask(task, timage, tprior, tdate, ttime)
                newTask.setText("")
            } else {
                println("XD")
                tasks[idx] = ListItem(task, timage, tprior, tdate, ttime)
                taskAdapter.notifyDataSetChanged()
            }
            setDefButtons()
        }
        builder.show()
    }

    private fun setPriorText(view: View, prior: Int, text: String) : Int {
        val tv = view as TextView
        tv.text = text
        return prior
    }

    private fun setTypeText(view: View, text: String) : String {
        val tv = view as TextView
        tv.text = text
        return text
    }

    private fun addTask(task: String, image: String, prior: Int, date: String, time: String) {
        val listItem = ListItem(task, image, prior, date, time)
        tasks.add(listItem)
        taskAdapter.notifyDataSetChanged()
    }

    private fun deleteItem(index: Int) {
        tasks.removeAt(index)
        taskAdapter.notifyDataSetChanged()
    }

    fun arrowButtonClick(view: View) {
        this.setDefButtons()
        val arrow = view as ImageButton
        arrow.setImageResource(arrows[1])
        val clicked = arrow.tag

        when (clicked) {
            "type" -> {
                tasks.sortBy { it.image }
                arrow.tag = "typeC"
            }
            "typeC" -> {
                tasks.sortByDescending { it.image }
                arrow.tag = "type"
            }
            "prio" -> {
                tasks.sortByDescending { it.prior }
                arrow.tag = "prioC"
            }
            "prioC" -> {
                tasks.sortBy { it.prior }
                arrow.tag = "prio"
            }
            "date" -> {
                tasks.sortWith(compareBy<ListItem> {it.date.takeLast(4)}.thenBy { it.date.takeLast(7).take(2) }.thenBy { it.date.take(2) })
                arrow.tag = "dateC"
            }
            "dateC" -> {
                tasks.sortWith(compareByDescending<ListItem> {it.date.takeLast(4)}.thenByDescending { it.date.takeLast(7).take(2) }.thenByDescending { it.date.take(2) })
                arrow.tag = "date"
            }
            "time" -> {
                tasks.sortWith(compareBy<ListItem> { it.time.takeLast(2) }.thenBy { it.time.take(2) }.thenBy { it.time.takeLast(5).take(2) })
                arrow.tag = "timeC"
            }
            "timeC" -> {
                tasks.sortWith(compareByDescending<ListItem> { it.time.takeLast(2) }.thenByDescending { it.time.take(2) }.thenByDescending { it.time.takeLast(5).take(2) })
                arrow.tag = "time"
            }
        }

        taskAdapter.notifyDataSetChanged()
    }

    private fun setDefButtons() {
        for (i in 0 .. 1) {
            (buttons1.getChildAt(i) as ImageButton).setImageResource(arrows[0])
            (buttons2.getChildAt(i) as ImageButton).setImageResource(arrows[0])
        }
    }
}
