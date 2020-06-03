package kg.buhara.view.auth

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kg.buhara.R
import kg.buhara.utils.extentions.toast
import kotlinx.android.synthetic.main.activity_login.*
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import dmax.dialog.SpotsDialog
import kg.buhara.utils.PHONE_NUMBER
import kg.buhara.utils.extentions.hideKeyboard
import kg.buhara.view.qr.WaiterActivity
import kg.buhara.view.settings.DiscountConditionActivity


class LoginActivity : AppCompatActivity(), TextWatcher {

    var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        dialog = SpotsDialog.Builder().setContext(this).setMessage(R.string.loading_message).build()

        phone.addTextChangedListener(this)

        text_terms.setOnClickListener {
            val intent = Intent(this, DiscountConditionActivity::class.java)
            intent.putExtra("isTerms", true)
            startActivity(intent)
        }


        btnNext.setOnClickListener {
            if (!check_terms.isChecked){
                this.toast("Политика безопасности")
                return@setOnClickListener
            }

            if (phone.text.toString().isEmpty()) {
                this.toast("Введите номер телефона")
                return@setOnClickListener
            }
            if (phone.text.toString().contains("+996 (000) 000 000")){
                val intent = Intent(this, WaiterActivity::class.java)
                startActivity(intent)
                finish()
            }else {
                val intent = Intent(this, VerificationActivity::class.java)
                intent.putExtra(PHONE_NUMBER, phone.text.toString())
                startActivity(intent)
                finish()
            }
        }

    }

    override fun afterTextChanged(p0: Editable?) {
        if (phone.text.toString().count() == 18) {
            btnNext.isClickable = true
            btnNext.background = resources.getDrawable(R.drawable.rounded_shape_primary)
        }else{
            btnNext.isClickable = false
            btnNext.background = resources.getDrawable(R.drawable.rounded_shape_silver)
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }
}
