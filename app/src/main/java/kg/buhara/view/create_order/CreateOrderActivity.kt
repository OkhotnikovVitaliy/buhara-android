package kg.buhara.view.create_order

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import kg.buhara.R
import kg.buhara.utils.extentions.toast
import android.widget.RadioButton
import dmax.dialog.SpotsDialog
import kg.buhara.data.model.Address
import kg.buhara.data.model.ProfileResponseModel
import kg.buhara.data.model.cart.CartModel
import kg.buhara.utils.ID
import kg.buhara.utils.UserInfoPref
import kotlinx.android.synthetic.main.activity_order_create.*
import org.koin.android.viewmodel.ext.android.viewModel


class CreateOrderActivity : AppCompatActivity(), TextWatcher {

    private val viewModel: CreateOrderViewModel by viewModel()
    var dialog: AlertDialog? = null
    var profile = ProfileResponseModel()
    var cart_id: Int = 0
    var cart: CartModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_create)
        cart_id = intent.getIntExtra(ID, 0)
        cart = intent.getSerializableExtra("cart") as CartModel
        initToolbar()
        onClickActions()
        dialog = SpotsDialog.Builder().setContext(this).setMessage(R.string.loading_message).build()
        configureAuthViewModel()
        setPayment()
    }

    private fun setPayment() {
        radioElsom.text = "${resources.getString(R.string.elsom)} ${intent.getStringExtra("elsom")}"
        subtotal.text = "${cart?.cart_total} сом"
        total.text = "${cart?.cart_total} сом"
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""
        toolbar.setNavigationOnClickListener { v -> onBackPressed() }
    }

    private fun configureAuthViewModel() {

        viewModel.profileDetail()
        viewModel.loadingVisibility.observe(
            this,
            androidx.lifecycle.Observer {
                if (it != null) {
                    val isLoader = it.contentIfNotHandled
                    if (isLoader != null)
                        if (isLoader) {
                            dialog?.show()
                        } else {
                            dialog?.dismiss()
                        }
                }
            })

        viewModel.profileResponseLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                if (it != null) {
                    val data = it.contentIfNotHandled
                    if (data != null)
                        setData(data)
                }
            })


        viewModel.errorMessageLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                toast(it)
            })

        viewModel.successCreateLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                if (it != null) {
                    val isSuccess: Boolean? = it.contentIfNotHandled
                    if (isSuccess != null && isSuccess) {
                        successOrderAlert()
                    }
                }
            })

        setAddress(UserInfoPref.getAddress(this))

    }

    private fun successOrderAlert() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(R.string.message_success_order)
            .setCancelable(false)
            .setPositiveButton(R.string.ok) { dialog, id ->
                run {
                    finish()
                }
            }
        val alert = dialogBuilder.create()
        alert.setTitle(R.string.thank)
        alert.show()

    }


    private fun setData(data: ProfileResponseModel?) {
        if (data == null) return
        profile = data
        name.setText(data.name)
        phone.setText(data.phone_number)
        street.setText(data.street)
        house.setText(data.building)
        flat.setText(data.flat)
        discount.text = "(${data.bonuses})"

    }

    private fun setAddress(data: Address?) {
        if (data == null) return
        street.setText(data.street)
        house.setText(data.house)
        flat.setText(data.flat)
        entrance.setText(data.entrance)
        floor.setText(data.floor)
    }

    private fun onClickActions() {
        phone.addTextChangedListener(this)
        name.addTextChangedListener(this)
        street.addTextChangedListener(this)
        house.addTextChangedListener(this)
        edit_discount.addTextChangedListener(this)

        btnSave.setOnClickListener {
            if (house.text.toString().isEmpty()) {
                toast("Введите дом")
                return@setOnClickListener
            }
            if (!checkValidate()) {
                this.toast(resources.getString(R.string.all_data_is_request))
            } else {
                createData()
            }
        }

    }

    private fun createData() {
        val selectedId = radioPay.checkedRadioButtonId
        // find the radiobutton by returned id
        val radioPayButton = findViewById<RadioButton>(selectedId)
        var payMethod: String = radioPayButton.text.toString()

        if (payMethod == resources.getString(R.string.in_cash)) {
            payMethod = "cash"
        } else {
            payMethod = "elsom"
        }

        var bonus = 0
        if (edit_discount.text.toString().isNotEmpty()) {
            bonus = edit_discount.text.toString().toInt()
        }

        val addressData = Address()
        addressData.street = street.text.toString()
        addressData.house = house.text.toString()
        addressData.flat = flat.text.toString()
        addressData.entrance = entrance.text.toString()
        addressData.floor = floor.text.toString()
        UserInfoPref.setAddress(this, addressData)

        viewModel.createOrder(
            name.text.toString(),
            phone.text.toString(),
            street.text.toString(),
            house.text.toString(),
            flat.text.toString(),
            entrance.text.toString(),
            floor.text.toString(),
            comment.text.toString(),
            cart_id,
            payMethod,
            0,
            bonus
        )
    }


    override fun afterTextChanged(p0: Editable?) {
        val editDiscount = edit_discount.text.toString()
        if (editDiscount.isNotEmpty() && editDiscount != "0") {
            toCount()
        }
        if (!checkValidate()) {
            buttonOnCancel()
        } else
            buttonOnActive()
    }

    private fun toCount() {
        try {
            val editDiscount = edit_discount.text.toString()
            if (editDiscount.toInt() < profile.bonuses ?: 0) {
                total.text = "${cart?.cart_total?.minus(editDiscount.toInt())} сом"
                discount.text = "(${profile.bonuses?.minus(editDiscount.toInt())})"

            } else {
                toast("У вас не достаточно балла")
                edit_discount.setText("")
                total.text = "${cart?.cart_total} сом"
                discount.text = "(${profile.bonuses})"
            }
        } catch (e: Exception) {
        }
    }

    private fun checkValidate(): Boolean {
        if (name.text.toString().isEmpty()) {
            return false
        }
        if (phone.text.toString().count() < 3) {
            return false
        }
        if (street.text.toString().isEmpty()) {
            return false
        }
        if (house.text.toString().isEmpty()) {
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

}
