package kg.buhara.view.auth

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kg.buhara.R
import kotlinx.android.synthetic.main.activity_verification.*
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import dmax.dialog.SpotsDialog
import kg.buhara.utils.PHONE_NUMBER
import kg.buhara.utils.extentions.toast
import kg.buhara.view.profile.AuthViewModel
import kg.buhara.view.profile.ProfileActivity
import org.koin.android.viewmodel.ext.android.viewModel

class VerificationActivity : AppCompatActivity(), TextWatcher {

    private val viewModel: VerificationViewModel by viewModel()
//    private val authViewModel: AuthViewModel by viewModel()

    var dialog: AlertDialog? = null
    var phoneNumber: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)
        init()
    }

    fun init() {
        dialog = SpotsDialog.Builder().setContext(this).setMessage(R.string.loading_message).build()
        phoneNumber = intent.getStringExtra(PHONE_NUMBER)
        configureViewModel()
        setOnClickAction()
        numberSet(phoneNumber)
        code.addTextChangedListener(this)
    }

    private fun configureViewModel() {
        startPhoneNumberVerification()
        timerStart()

        viewModel.loadingVisibility.observe(
            this,
            androidx.lifecycle.Observer {
                if (it != null) {
                    val isSuccess = it.contentIfNotHandled
                    if (isSuccess != null)
                        if (isSuccess) {
                            dialog?.show()
                        } else {
                            dialog?.dismiss()
                        }
                }


            })

        viewModel.errorMessageLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                if (it != null) {
                    val message = it.contentIfNotHandled
                    if (message != null)
                        toast(message)
                }
            })

        viewModel.successCodeLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                if (it != null) {
                    val isSuccess = it.contentIfNotHandled
                    if (isSuccess != null)
                        if (isSuccess == true) {
                            val intent = Intent(this, ProfileActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                }
            })
    }

    private fun startPhoneNumberVerification() {
        viewModel.signUp(phoneNumber)
    }


    private fun setOnClickAction() {
        container_send_again.setOnClickListener {
            startPhoneNumberVerification()
            timerStart()
        }
        txt_wrong_number.setOnClickListener {
            startActivityLogin()
        }
        wrong_number.setOnClickListener {
            startActivityLogin()
        }
        txt_number_verify_2.setOnClickListener {
            startActivityLogin()
        }

        btnSend.setOnClickListener {
            if (code.text.toString().length == 6) {
                viewModel.auth(phoneNumber, code.text.toString())
            }
        }
    }

    private fun startActivityLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun numberSet(number: String) {
        txt_number_verify.text = "${resources.getString(R.string.confirm)}  ${number}"
        wrong_number.text = "${number}  "
    }

    private fun timerStart() {
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millis: Long) {
                container_send_again.background =
                    resources.getDrawable(R.drawable.rounded_shape_silver)
                container_send_again.isClickable = false
                var seconds = (millis / 1000).toInt()
                val minutes = seconds / 60
                seconds %= 60
                timerTextView.setText(String.format("%d:%02d", minutes, seconds))
            }

            override fun onFinish() {
                timerTextView.text = ""
                container_send_again.isClickable = true
                container_send_again.background =
                    resources.getDrawable(R.drawable.rounded_shape_primary)
            }
        }.start()
    }

    override fun onBackPressed() {
        this.toast("Подтвердите верификацию")
    }

    override fun afterTextChanged(p0: Editable?) {
        if (code.text.toString().count() == 6) {
            btnSend.isClickable = true
            btnSend.background = resources.getDrawable(R.drawable.rounded_shape_primary)
        } else {
            btnSend.isClickable = false
            btnSend.background = resources.getDrawable(R.drawable.rounded_shape_silver)
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }
}
