package prvnimilion.vutindex.home.view

import android.Manifest
import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.system_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel
import prvnimilion.vutindex.R
import prvnimilion.vutindex.auth.viewmodel.LoginViewModel
import prvnimilion.vutindex.webscraper.util.LOGIN_URL
import timber.log.Timber

class SystemFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.system_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showWebPage()
    }


    private fun showWebPage() {
        webview.settings.apply {
            javaScriptEnabled = true
            databaseEnabled = true
            setAppCacheEnabled(true)
            domStorageEnabled = true
            builtInZoomControls = true
            displayZoomControls = false
        }
        webview.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        loadingPanel.visibility = View.VISIBLE
        setClickable(false)

        webview.loadUrl(LOGIN_URL)
        loginViewModel.getCredentials()

        loginViewModel.userCredentials.observe(this, Observer {
            lateinit var username: String
            lateinit var password: String

            it.forEach { credentials ->
                if (credentials.key.isNotEmpty()) {
                    username = credentials.key
                    password = credentials.value
                }
            }
            val js = "(function() {" +
                    "var form = document.getElementById(\"login_form\");" +
                    "var username = form.elements[\"login7\"];" +
                    "var password = form.elements[\"passwd7\"];" +
                    "username.value = '" + username + "';" +
                    "password.value = '" + password + "';" +
                    "document.getElementsByName(\"login\")[0].click();" +
                    "})();"

            webview.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)

                    view?.evaluateJavascript(js, {})

                    if (url?.contains("vutbr.cz/studis")!! || url.contains("vutbr.cz/intra")) {

                        loadingPanel.visibility = View.GONE
                        setClickable(true)
                        isStoragePermissionGranted()
                    }
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    webview.loadUrl(request?.url.toString())
                    return true
                }

            }

            webview.setDownloadListener { url12, userAgent, contentDisposition, mimeType, _ ->
                if (isStoragePermissionGranted()) {
                    val request = DownloadManager.Request(Uri.parse(url12))

                    request.setMimeType(mimeType)
                    val cookies = CookieManager.getInstance().getCookie(url12)
                    request.addRequestHeader("cookie", cookies)
                    request.addRequestHeader("User-Agent", userAgent)
                    request.setDescription("Downloading file...")
                    request.setTitle(URLUtil.guessFileName(url12, contentDisposition, mimeType))
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    request.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                        URLUtil.guessFileName(url12, contentDisposition, mimeType)
                    )
                    val dm = context!!.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                    dm.enqueue(request)
                    Toast.makeText(context!!, "Downloading a file", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    fun isStoragePermissionGranted(): Boolean {
        return if (context!!.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            Timber.tag("VUTdebug").d("Permission is granted")
            true
        } else {
            Timber.tag("VUTdebug").d("Permission is revoked")
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
            false
        }
    }

    private fun goBack(): Boolean {
        if (webview == null) return false
        if (webview.url.contains("www.vutbr.cz/?armsgt") || getPreviousUrl().contains("www.vutbr.cz/login")) {
            return false
        }
        if (webview.canGoBack()) {
            webview.goBack()
            return true
        }
        return false
    }

    private fun getPreviousUrl(): String {
        var previousUrl = ""
        val webBackForwardList = webview.copyBackForwardList()
        if (webBackForwardList.currentIndex > 0) {
            previousUrl = webBackForwardList.getItemAtIndex(webBackForwardList.currentIndex - 1).url
        }
        return previousUrl
    }

    private fun setClickable(clickable : Boolean) {
        if (clickable) {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else {
            activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }


}