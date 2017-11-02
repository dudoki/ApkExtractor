package com.droid.app.extractor.apps

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.droid.app.extractor.AppsApplication
import com.droid.app.extractor.R
import com.droid.app.extractor.data.App
import com.droid.app.extractor.util.v
import com.droid.app.extractor.views.ScrollChildSwipeRefreshLayout

class AppsFragment : Fragment(), AppsContract.View, Toolbar.OnMenuItemClickListener {

    private val mSwipeRefreshLayout: ScrollChildSwipeRefreshLayout by lazy {
        view!!.findViewById<ScrollChildSwipeRefreshLayout>(R.id.refresh_layout)
    }

    private val mDownloadPath: TextView by lazy {
        view!!.findViewById<TextView>(R.id.downloadPath)
    }

    private val mAppsList: RecyclerView by lazy {
        view!!.findViewById<RecyclerView>(R.id.apps_list)
    }

    private val mNoApps: LinearLayout by lazy {
        view!!.findViewById<LinearLayout>(R.id.noApps)
    }
    private val mNoAppsIcon: ImageView by lazy {
        view!!.findViewById<ImageView>(R.id.noAppsIcon)
    }
    private val mNoAppsMain: TextView by lazy {
        view!!.findViewById<TextView>(R.id.noAppsMain)
    }

    private val mListAdapter: AppsAdapter = AppsAdapter()
    private var mPresenter: AppsContract.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v("onCreateView")
        return inflater.inflate(R.layout.fragment_apps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        v("onViewCreated")

        initializeDownloadPath()
        initializeAppsList()
        initializeSwipeRefreshLayout()
    }

    override fun onResume() {
        v("onResume")
        super.onResume()
        if (mListAdapter.isEmpty()) {
            mPresenter?.start()
        }
    }

    private fun initializeDownloadPath() {
        mDownloadPath.text = AppsApplication.destDir.absolutePath
    }

    private fun initializeSwipeRefreshLayout() {

        val colors = ResourcesCompat.getColor(resources, R.color.colorPrimary, activity.theme)

        mSwipeRefreshLayout.setColorSchemeColors(colors)
        mSwipeRefreshLayout.setScrollUpChild(mAppsList)
        mSwipeRefreshLayout.setOnRefreshListener {
            mPresenter?.loadApps()
        }
    }

    private fun initializeAppsList() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mAppsList.layoutManager = layoutManager
        mAppsList.setHasFixedSize(true)
        mAppsList.adapter = mListAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        v("onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        initializeMenu()
    }

    private fun initializeMenu() {
        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)
        toolbar.inflateMenu(R.menu.apps_fragment_menu)
        toolbar.setOnMenuItemClickListener(this)
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        val itemId = item.itemId
        when (itemId) {
            R.id.menu_export -> {
                prepareExportApps()
                return true
            }
            R.id.menu_select_all -> {
                mListAdapter.selectAllApps()
                return true
            }
        }
        return false
    }

    private fun prepareExportApps() {
        val apps = mListAdapter.checkedApps
        mPresenter?.exportApps(apps)
    }

    override fun setLoadingIndicator(active: Boolean) {
        if (view == null) {
            return
        }

        mSwipeRefreshLayout.post { mSwipeRefreshLayout.isRefreshing = active }
    }

    override fun setExportIndicator(active: Boolean) {
        if (active) {
            ExportProgress.show(fragmentManager)
        } else {
            ExportProgress.dismiss(fragmentManager)
        }
    }

    override fun showApps(apps: List<App>) {
        mListAdapter.replaceData(apps)

        mNoApps.visibility = View.GONE
        mAppsList.visibility = View.VISIBLE
    }

    override fun showNoApps() {
        showNoAppsViews("You have no Apps!", R.drawable.ic_assignment_turned_in_24dp)
    }

    private fun showNoAppsViews(mainText: String, iconRes: Int) {
        mNoAppsIcon.setImageResource(iconRes)
        mNoAppsMain.text = mainText

        mAppsList.visibility = View.GONE
        mNoApps.visibility = View.VISIBLE
    }

    override fun showExportProgress(app: App) {
        val progress = ExportProgress.find(fragmentManager)
        progress?.updateMessage(getString(R.string.progress_exporting, app.label))
    }

    override fun updateItemState(app: App) {
        with(app) {
            checked = !exported
            val position = mListAdapter.findAppPosition(this)
            mListAdapter.notifyItemChanged(position)
        }
    }

    override fun setPresenter(presenter: AppsContract.Presenter) {
        mPresenter = presenter
    }

    class ExportProgress : DialogFragment() {

        private var mProgressDialog: ProgressDialog? = null

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            mProgressDialog = ProgressDialog(activity, theme)
            mProgressDialog?.setMessage(getString(R.string.progress_export_prepare))
            mProgressDialog?.setCanceledOnTouchOutside(false)
            return mProgressDialog!!
        }

        fun updateMessage(message: CharSequence) {
            mProgressDialog?.setMessage(message)
        }

        companion object {

            private val TAG = ExportProgress::class.java.name

            fun show(fragmentManager: FragmentManager) {
                val progress = ExportProgress()
                progress.isCancelable = false
                progress.show(fragmentManager, TAG)
            }

            fun dismiss(fragmentManager: FragmentManager) {
                val progress = find(fragmentManager)
                progress?.dismissAllowingStateLoss()
            }

            fun find(fragmentManager: FragmentManager): ExportProgress? {
                return fragmentManager.findFragmentByTag(TAG) as ExportProgress
            }
        }
    }

    companion object {
        fun newInstance(): AppsFragment {
            return AppsFragment()
        }
    }
}
