package kg.buhara.view.review

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kg.buhara.R
import kg.buhara.utils.ID
import kg.buhara.utils.extentions.gone
import kg.buhara.utils.extentions.toast
import kg.buhara.utils.extentions.visible
import kotlinx.android.synthetic.main.activity_profile.toolbar
import kotlinx.android.synthetic.main.activity_review.*
import org.koin.android.viewmodel.ext.android.viewModel


class ReviewActivity : AppCompatActivity(), TextWatcher {


    private val viewModel: ReviewViewModel by viewModel()
    var dialog: AlertDialog? = null
    var id: Int = 0
    lateinit var adapter: ReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        id = intent.getIntExtra(ID, 0)
        init()
    }

    private fun init() {
        edit_message.addTextChangedListener(this)
        initToolbar()
        initRV()
        configureAuthViewModel()
        onClickActions()
    }

    private fun onClickActions() {
        btnSend.setOnClickListener {
            var message = edit_message.text.toString()
            if (message.isEmpty()) {
                toast("Введите отзыв")
            } else {
                viewModel.createReview(id, message)
                edit_message.setText("")
            }
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun configureAuthViewModel() {

        viewModel.fetchReviewsRestaurant(id)

        viewModel.loadingVisibility.observe(
            this,
            androidx.lifecycle.Observer {
                if (it) {
                    dialog?.show()
                } else {
                    dialog?.dismiss()
                }
            })

        viewModel.errorMessageLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                toast(it)
            })

        viewModel.reviewsListLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                adapter.swapData(it)
                isCountItemNone()
            })

        viewModel.reviewLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                adapter.insert(it)
                recyclerView.smoothScrollToPosition(adapter.getSize())
                isCountItemNone()
            })
    }

    private fun isCountItemNone(){
        if (adapter.getSize() == 0){
            isEmtyText.visible()
        }else{
            isEmtyText.gone()
        }
    }

    private fun initRV() {
        adapter = ReviewAdapter()
        val llm = LinearLayoutManager(this)
        recyclerView.layoutManager = llm
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.adapter = adapter
    }

    override fun afterTextChanged(p0: Editable?) {
        if (edit_message.text.toString().isEmpty()) {
            btnSend.background = resources.getDrawable(R.drawable.rounded_top_menu_shimmer)
        } else {
            btnSend.background = resources.getDrawable(R.drawable.rounded_top_menu)
        }
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

}
