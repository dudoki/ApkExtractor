package com.droid.app.extractor.apps

import android.os.Bundle
import com.anthonycr.grant.PermissionsManager
import com.anthonycr.grant.PermissionsResultAction
import com.droid.app.extractor.BaseActivity
import com.droid.app.extractor.R
import com.droid.app.extractor.data.source.AppsRepository
import com.droid.app.extractor.util.ActivityUtils
import com.droid.app.extractor.util.v

class AppsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        v("onCreate")
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_apps)

        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, action)

        var fragment = supportFragmentManager.findFragmentById(R.id.contentFrame) as AppsFragment?
        if (fragment == null) {
            fragment = AppsFragment.newInstance()
            ActivityUtils.addFragmentToActivity(supportFragmentManager, fragment, R.id.contentFrame)
        }

        AppsPresenter(AppsRepository.getInstance(this), fragment)
    }

    private val action = object : PermissionsResultAction() {
        override fun onGranted(): Unit {
            v("OK")
        }

        override fun onDenied(permission: String): Unit {
            v("$permission is denied")
            finish()
        }
    }

}