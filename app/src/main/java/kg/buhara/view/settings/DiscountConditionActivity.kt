package kg.buhara.view.settings

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kg.buhara.R
import kotlinx.android.synthetic.main.toolbar.*
import dmax.dialog.SpotsDialog
import kg.buhara.utils.extentions.toast
import kotlinx.android.synthetic.main.activity_discount_condition.*
import org.koin.android.viewmodel.ext.android.viewModel


class DiscountConditionActivity : AppCompatActivity() {

    var dialog: AlertDialog? = null
    private val viewModel: SettingsViewModel by viewModel()
    var isDelivery = false
    var isTerms = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discount_condition)
        isDelivery = intent.getBooleanExtra("isDelivery", false)
        isTerms = intent.getBooleanExtra("isTerms", false)
        dialog = SpotsDialog.Builder().setContext(this).setMessage(R.string.loading_message).build()
        initToolbar()
        configureViewModel()
    }

    private fun configureViewModel() {

        viewModel.fetchDiscountCondution()

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

        viewModel.discountCondutionLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                if (isDelivery) {
                    if (it.delivery_info != null) {
                        printWebView(it.delivery_info!!)
                        textView.text = it.delivery_info
                    }
                } else if (isTerms) {
                    if (it.privacy_terms != null) {
                        printWebView(it.privacy_terms!!)
                        textView.text = it.privacy_terms
                        Log.e("MESSAE", it.privacy_terms)
                    }
                } else {
                    if (it.discount_condition != null) {
                        printWebView(it.discount_condition!!)
                        textView.text = it.discount_condition

                    }
                }
            })

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun printWebView(htmlAsString: String) {
        webView.settings.javaScriptEnabled = true
        webView.loadDataWithBaseURL(
            null,
            "<style>img{display: inline;height: auto;max-width: 100%;}</style>$htmlAsString",
            "text/html",
            "UTF-8",
            null
        );

//        webView.loadDataWithBaseURL(null, htmlAsString, "text/html", "utf-8", null)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if (isDelivery) {
            supportActionBar!!.title = resources.getString(R.string.delivery_condution)
        } else if (isTerms) {
            supportActionBar!!.title = resources.getString(R.string.policy_privacy)
        } else {
            supportActionBar!!.title = resources.getString(R.string.discount_condution)
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

}
