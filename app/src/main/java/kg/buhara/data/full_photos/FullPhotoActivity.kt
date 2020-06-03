package kg.buhara.data.full_photos

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kg.buhara.R
import kg.buhara.utils.FILES
import kg.buhara.utils.POSTITION
import kg.buhara.view.detail_restaurant.PagerGalaryAdapter
import kotlinx.android.synthetic.main.activity_full_photo.*

class FullPhotoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_photo)
        var list = intent.getSerializableExtra(FILES) as ArrayList<String>
        var pos = intent.getIntExtra(POSTITION, 0)
        initGallaries(list, pos)

        close.setOnClickListener {
            finish()
        }
    }

    private fun initGallaries(sliders: ArrayList<String>, pos: Int) {
        val pageAdapter = PagerGalaryAdapter(this, sliders, true)
        pager.adapter = pageAdapter
        pager.currentItem = pos
        indicator.setViewPager(pager)
        if (sliders.size == 1) {
            indicator.visibility = View.GONE
        } else {
            indicator.visibility = View.VISIBLE
        }
    }

}
