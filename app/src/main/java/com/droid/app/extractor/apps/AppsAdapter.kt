package com.droid.app.extractor.apps

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.droid.app.extractor.R
import com.droid.app.extractor.data.App
import java.util.*

class AppsAdapter : RecyclerView.Adapter<AppsAdapter.ViewHolder>() {

    internal var mApps: List<App> = ArrayList(0)

    fun replaceData(apps: List<App>) {
        setList(apps)
        notifyDataSetChanged()
    }

    internal fun setList(apps: List<App>) {
        mApps = apps
    }

    internal fun getItem(index: Int): App {
        return mApps[index]
    }

    internal fun isEmpty(): Boolean {
        return mApps.isEmpty()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.app_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val app = getItem(position)
            val context = itemView.context

            icon.setImageDrawable(app.icon)
            label.text = app.label
            packageName.text = app.packageName
            done.visibility = if (app.exported) View.VISIBLE else View.GONE
            version.text = context.getString(R.string.app_version, app.versionName ?: "1.0")
            size.text = app.size
            checkBox.isChecked = app.checked

            val clickListener = View.OnClickListener {
                app.checked = !app.checked
                checkBox.isChecked = app.checked
            }
            checkBox.setOnClickListener(clickListener)
            itemView.setOnClickListener(clickListener)
        }
    }

    override fun getItemCount(): Int {
        return mApps.size
    }

    val checkedApps: List<App>
        get() {
            val apps = ArrayList<App>()
            for (app in mApps) {
                if (app.checked) {
                    apps.add(app)
                }
            }

            return apps
        }

    fun selectAllApps() {
        for (app in mApps) {
            app.checked = !app.checked
        }
        notifyDataSetChanged()
    }

    fun findAppPosition(app: App): Int {
        for (index in mApps.indices) {
            if (app.packageName === mApps[index].packageName) {
                return index
            }
        }
        return 0
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val icon: ImageView by lazy {
            itemView.findViewById(R.id.icon) as ImageView
        }
        internal val label: TextView by lazy {
            itemView.findViewById(R.id.label) as TextView
        }
        internal val version: TextView by lazy {
            itemView.findViewById(R.id.version) as TextView
        }
        internal val packageName: TextView by lazy {
            itemView.findViewById(R.id.packageName) as TextView
        }
        internal val size: TextView by lazy {
            itemView.findViewById(R.id.size) as TextView
        }
        internal val checkBox: CheckBox by lazy {
            itemView.findViewById(R.id.checkbox) as CheckBox
        }
        internal val done: ImageView by lazy {
            itemView.findViewById(R.id.done) as ImageView
        }
    }
}
