package kg.buhara.view.qr

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import dmax.dialog.SpotsDialog
import kg.buhara.R
import kg.buhara.data.model.ProfileResponseModel
import kg.buhara.utils.extentions.gone
import kg.buhara.utils.extentions.visible
import kg.buhara.view.main.MainActivity
import kg.buhara.view.profile.AuthViewModel
import kotlinx.android.synthetic.main.fragment_qr.*
import kotlinx.android.synthetic.main.layout_progress.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class QRFragment : Fragment() {

    private val viewModel: QRViewModel by viewModel()
    var dialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_qr, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        dialog =
            SpotsDialog.Builder().setContext(activity).setMessage(R.string.loading_message).build()
        configureViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.profileDetail()
    }

    private fun configureViewModel() {
        viewModel.loadingVisibility.observe(
            this,
            androidx.lifecycle.Observer {
                if (it != null) {
                    val isLoad = it.contentIfNotHandled
                    if (isLoad != null)
                        if (isLoad) {
                            progress_layout.visible()
                        } else {
                            progress_layout.gone()
                        }
                }
            })

        viewModel.qrResponseLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                if (it != null) {
                    val data = it.contentIfNotHandled
                    if (data != null) {
                        setData(data)
                    }
                }
            })

//        viewModel.getprofileResponseLiveDate().observe(this, androidx.lifecycle.Observer {
//            if (it != null) {
//                setData(it)
//            }
//        })
    }

    @SuppressLint("CheckResult")
    fun setData(data: ProfileResponseModel) {
        discount.text = "${data.user_status?.discount}%"
        bonuse.text = "${data.bonuses}"
        phone.text = "${data.phone_number}"
        Glide.with(this).load(data.user_status?.status_image).into(status_image)
        generateQrcode(data)
    }

    private fun generateQrcode(data: ProfileResponseModel) {

        var content = "${data.phone_number} \n\nБаллы:  ${data.bonuses}"

        val charset = Charsets.UTF_8
        try {
            val bytes = content.toByteArray(charset)
            content = String(bytes, charset)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val encodingHints: MutableMap<EncodeHintType, Any> = HashMap()
        encodingHints[EncodeHintType.CHARACTER_SET] = "UTF-8"
        encodingHints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H

        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512, encodingHints)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        qrcodeImageView.setImageBitmap(bitmap)
    }


}
