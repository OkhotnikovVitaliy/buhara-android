package kg.buhara.view.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import dmax.dialog.SpotsDialog
import kg.buhara.R
import kg.buhara.data.model.ProfileResponseModel
import kg.buhara.utils.IS_UPDATE_PROFILE
import kg.buhara.utils.UserInfoPref
import kg.buhara.utils.extentions.toast
import kg.buhara.view.auth.LoginActivity
import kg.buhara.view.main.MainActivity
import kg.buhara.view.settings.DiscountConditionActivity
import kotlinx.android.synthetic.main.activity_profile.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class ProfileActivity : AppCompatActivity(), TextWatcher {

    var isUpdate: Boolean = false
    private val authViewModel: AuthViewModel by viewModel()
    var dialog: AlertDialog? = null
    var profile = ProfileResponseModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        isUpdate = intent.getBooleanExtra(IS_UPDATE_PROFILE, false)
        onClickActions()
        dialog = SpotsDialog.Builder().setContext(this).setMessage(R.string.loading_message).build()
        configureAuthViewModel()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""
        toolbar.setNavigationOnClickListener { v -> onBackPressed() }
    }

    private fun configureAuthViewModel() {
        if (isUpdate) {
            initToolbar()
            exit.visibility = View.VISIBLE
        }
        authViewModel.profileDetail()

        authViewModel.loadingVisibility.observe(
            this,
            androidx.lifecycle.Observer {
                if (it != null) {
                    val isLoad = it.contentIfNotHandled
                    if (isLoad != null)
                        if (isLoad) {
                            dialog?.show()
                        } else {
                            dialog?.dismiss()
                        }
                }

            })

        authViewModel.errorMessageLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                if (it != null) {
                    val message = it.contentIfNotHandled
                    if (message != null)
                        toast(message)
                }
            })

//        authViewModel.profileResponseLiveData.observe(
//            this,
//            androidx.lifecycle.Observer {
//                if (it != null) {
//                        setData(it)
//                }
//            })

        authViewModel.getprofileResponseLiveDate().observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                setData(it)
            }
        })

        authViewModel.successUpdateLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                if (it != null) {
                    val success = it.contentIfNotHandled
                    if (success != null && success)
                        if (isUpdate) {
                            buttonOnCancel()
                            toast("Личные данные успешно отредактированы")
                        } else {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                }
            })
    }

    private fun setData(data: ProfileResponseModel) {
        profile = data
        name.setText(data.name)
        street.setText(data.street)
        house.setText(data.building)
        office.setText(data.flat)
        phone.setText(data.phone_number)
        date.setText(data.birth_date)
        discount.text = "${data.user_status?.discount}%"
        bonuse.text = "${data.bonuses}"
        Glide.with(this).load(data.user_status?.status_image).into(status_image)

        if (data.gender.equals("male")) {
            radioSex.check(R.id.radioMale)
        } else {
            radioSex.check(R.id.radioFemale)
        }
        if (isUpdate) {
            buttonOnCancel()
        }
    }

    private fun onClickActions() {

        val udata = resources.getString(R.string.discount_terms)
        val content = SpannableString(udata)
        content.setSpan(UnderlineSpan(), 0, udata.length, 0)
        discount_terms.text = content

        phone.addTextChangedListener(this)
        date.addTextChangedListener(this)
        name.addTextChangedListener(this)
        street.addTextChangedListener(this)
        house.addTextChangedListener(this)
        office.addTextChangedListener(this)

        date.setOnClickListener {
            showDateCalendar()
        }

        discount_terms.setOnClickListener {
            startActivity(Intent(this, DiscountConditionActivity::class.java))
        }

        btnSave.setOnClickListener {
            if (!checkValidate()) {
                this.toast(resources.getString(R.string.all_data_is_request))
            } else {
                updateData()
            }
        }

        exit.setOnClickListener {
            signOut()
        }
    }

    private fun updateData() {
        val selectedId = radioSex.checkedRadioButtonId
        // find the radiobutton by returned id
        val radioSexButton = findViewById<RadioButton>(selectedId)
        var sex: String = radioSexButton.text.toString()

        if (sex == "Мужчина") {
            sex = "male"
        } else {
            sex = "female"
        }
        authViewModel.profileUpdate(
            name.text.toString(),
            street.text.toString(),
            house.text.toString(),
            office.text.toString(),
            date.text.toString(),
            sex
        )
    }

    private fun showDateCalendar() {
        /*val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in Toast
                val calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                val format = SimpleDateFormat("yyyy-MM-dd");
                val strDate = format.format(calendar.getTime());
                date.setText(strDate)
            },
            year,
            month,
            day
        )
        dpd.datePicker.touchables[0].performClick()
        dpd.show()*/

        val calendar = Calendar.getInstance()

        SpinnerDatePickerDialogBuilder()
            .context(this)
            .callback { view, year, monthOfYear, dayOfMonth ->
                calendar.clear()
                calendar.set(year, monthOfYear, dayOfMonth)
                Log.e("CAL", calendar.time.toString())

                val format = SimpleDateFormat("dd/MM/yyyy");
                val strDate = format.format(calendar.time);
                date.setText(strDate)
            }
            .showTitle(true)
            .showDaySpinner(true)
            .defaultDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            .maxDate(calendar.get(Calendar.YEAR) + 5, 11, 31)
            .minDate(1950, 0, 1)
            .build()
            .show()

    }


    override fun afterTextChanged(p0: Editable?) {
        if (!checkValidate()) {
            buttonOnCancel()
        } else
            buttonOnActive()
    }

    private fun checkValidate(): Boolean {
        name_container.isErrorEnabled = false
        phone_container.isErrorEnabled = false
        street_container.isErrorEnabled = false
        house_container.isErrorEnabled = false
        date_container.isErrorEnabled = false

        if (name.text.toString().isEmpty()) {
            name_container.error = "Введите имя"
            return false
        }
        if (phone.text.toString().count() < 3) {
            phone_container.error = "Введите номер телефона"
            return false
        }
        if (street.text.toString().isEmpty()) {
            street_container.error = "Введите улицу"
            return false
        }
        if (house.text.toString().isEmpty()) {
            house_container.error = "Введите дом"
            return false
        }
        if (date.text.toString().count() < 3) {
            date_container.error = "Введите дата рождения"
            return false
        }
        return true
    }

    private fun buttonOnActive() {
        btnSave.background = resources.getDrawable(R.drawable.rounded_shape_primary)
    }

    private fun buttonOnCancel() {
        btnSave.background = resources.getDrawable(R.drawable.rounded_shape_silver)
    }


    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    private fun signOut() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(R.string.message_sign_out)
            .setCancelable(false)
            .setPositiveButton(R.string.yes) { dialog, id ->
                run {
                    UserInfoPref.setAccessToken(this, "")
                    UserInfoPref.deleteUserInfo(this)
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
            .setNegativeButton(R.string.cancel) { dialog, id ->
                dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.setTitle(R.string.sign_out)
        alert.show()

    }

}
