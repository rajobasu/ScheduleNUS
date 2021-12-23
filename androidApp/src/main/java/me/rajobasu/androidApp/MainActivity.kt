package me.rajobasu.androidApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.rajobasu.shared.Greeting
import android.widget.TextView
import me.rajobasu.shared.TesterClass

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            
        val tv: TextView = findViewById(R.id.text_view)
        tv.text = TesterClass().printFunky()
    }
}
