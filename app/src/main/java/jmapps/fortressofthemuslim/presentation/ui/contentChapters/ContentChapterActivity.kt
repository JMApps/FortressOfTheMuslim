package jmapps.fortressofthemuslim.presentation.ui.contentChapters

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import jmapps.fortressofthemuslim.R
import kotlinx.android.synthetic.main.activity_content_chapter.*

class ContentChapterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_chapter)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}