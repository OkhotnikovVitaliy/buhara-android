package kg.buhara.view.qr

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import kg.buhara.R
import kg.buhara.data.model.AuthResponseModel
import kg.buhara.utils.Event
import kg.buhara.utils.UserInfoPref
import kg.buhara.view.auth.LoginActivity


class WaiterActivity : AppCompatActivity() {
    private var btn: Button? = null
    private var btnSignOut: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiter)
        saveToken("waiter")
        tvresult = findViewById(R.id.tvresult)

        btn = findViewById(R.id.btn)
        btnSignOut = findViewById(R.id.signOutBtn)

        btn!!.setOnClickListener {
            Permissions.check(
                this /*context*/,
                Manifest.permission.CAMERA,
                null,
                object : PermissionHandler() {
                    override fun onGranted() {
                        // do your task.
                        val intent = Intent(this@WaiterActivity, ScanActivity::class.java)
                        startActivity(intent)
                    }
                })

        }

        btnSignOut!!.setOnClickListener {
            signOut()
        }


    }

    private fun saveToken(token: String) {
        UserInfoPref.setUserToken(this, "${token}")
    }


    companion object {

        var tvresult: TextView? = null
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
