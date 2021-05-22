package cz.kudlav.vutindex.system.viewmodel

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.net.Uri
import android.os.Environment
import android.view.View
import android.webkit.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import cz.kudlav.vutindex.repository.repos.AuthRepository
import cz.kudlav.vutindex.webscraper.util.*
import timber.log.Timber
import java.net.URLEncoder

class SystemViewModel(private val repository: AuthRepository) : ViewModel() {

    private var url12: String? = null
    private var userAgent: String? = null
    private var contentDisposition: String? = null
    private var mimeType: String? = null
    private var contentLength = 0

    val loginCredentials: MutableLiveData<Triple<String, String, String>> = MutableLiveData()
    fun getCookies(webView: WebView) {
        webView.loadUrl(LOGIN_URL)
    }

    fun getLoginCredentials() {
        viewModelScope.launch(Dispatchers.IO) {
            loginCredentials.postValue(getCredentials())
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun loginIntoSystem(webView: WebView) {
        webView.settings.apply {
            javaScriptEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            setSupportZoom(true)
            domStorageEnabled = true
            builtInZoomControls = true
            displayZoomControls = false
        }
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        val sentTime = System.currentTimeMillis() / 1000
        val request = "special_p4_form=1" +
                "&login_form=1" +
                "&sentTime=${URLEncoder.encode(sentTime.toString(), "UTF-8")}" +
                "&sv[fdkey]=${URLEncoder.encode(loginCredentials.value?.first, "UTF-8")}" +
                "&LDAPlogin=${URLEncoder.encode(loginCredentials.value?.second, "UTF-8")}" +
                "&LDAPpasswd=${URLEncoder.encode(loginCredentials.value?.third, "UTF-8")}" +
                "&login="
        contentLength = request.length
        webView.postUrl(REQUEST_URL, request.toByteArray())
    }


    fun setupWebViewClient(webView: WebView, finishedLoading: (Boolean) -> Unit) {
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (url?.contains("cz/?armsgt")!!) {
                    webView.loadUrl(INTRA_URL)
                } else if (url == INTRA_URL) {
                    finishedLoading(true)
                }
                finishedLoading(false)
                Timber.d(url)
            }
        }
    }

    fun setupChromeWebClient(webView: WebView) {
        webView.webChromeClient = WebChromeClient()
    }

    fun setupDownloadListener(
        webView: WebView,
        downloadManager: DownloadManager,
        hasStoragePermission: () -> Boolean
    ) {
        webView.setDownloadListener { url12, userAgent, contentDisposition, mimeType, _ ->
            this.url12 = url12
            this.userAgent = userAgent
            this.contentDisposition = contentDisposition
            this.mimeType = mimeType

            if (hasStoragePermission()) {
                downloadFile(downloadManager)
            }
        }
    }

    fun downloadFile(downloadManager: DownloadManager) {
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
        downloadManager.enqueue(request)
    }

    private fun goBack(webView: WebView): Boolean {
        if (webView.url.contains("www.vutbr.cz/?armsgt") || getPreviousUrl(webView).contains("www.vutbr.cz/login")) {
            return false
        }
        if (webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return false
    }

    private fun getPreviousUrl(webView: WebView): String {
        var previousUrl = ""
        val webBackForwardList = webView.copyBackForwardList()
        if (webBackForwardList.currentIndex > 0) {
            previousUrl = webBackForwardList.getItemAtIndex(webBackForwardList.currentIndex - 1).url
        }
        return previousUrl
    }

    private suspend fun getCredentials(): Triple<String, String, String> {
        var fdKey = ""
        val job = viewModelScope.launch(Dispatchers.IO) {
            fdKey = repository.getLoginFdKey()
        }
        job.join()
        var credentials = Pair("", "")
        val job2 = viewModelScope.launch(Dispatchers.IO) {
            credentials = repository.getCredentials()
        }
        job2.join()
        return Triple(fdKey, credentials.first, credentials.second)
    }
}