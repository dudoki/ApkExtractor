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

class AppsAdapter : RecyclerView.Adapter<AppsAdapter.ViewHolder>() {

    private var mApps: List<App> = ArrayList(0)

    fun replaceData(apps: List<App>) {
        setList(apps)
        notifyDataSetChanged()
    }

    private fun setList(apps: List<App>) {
        mApps = apps
    }

    private fun getItem(index: Int): App {
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

    val checkedApps: Iterable<App>
        get() {
            return mApps.filter { it.checked }
        }

    fun selectAllApps() {
        for (app in mApps) {
            app.checked = !app.checked
        }
        notifyDataSetChanged()
    }

    fun findAppPosition(app: App): Int {
        mApps.indices.forEach { index ->
            if (app.packageName === mApps[index].packageName) {
                return index
            }
        }
        return 0
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val icon: ImageView by lazy {
            itemView.findViewById<ImageView>(R.id.icon)
        }
        internal val label: TextView by lazy {
            itemView.findViewById<TextView>(R.id.label)
        }
        internal val version: TextView by lazy {
            itemView.findViewById<TextView>(R.id.version)
        }
        internal val packageName: TextView by lazy {
            itemView.findViewById<TextView>(R.id.packageName)
        }
        internal val size: TextView by lazy {
            itemView.findViewById<TextView>(R.id.size)
        }
        internal val checkBox: CheckBox by lazy {
            itemView.findViewById<CheckBox>(R.id.checkbox)
        }
        internal val done: ImageView by lazy {
            itemView.findViewById<ImageView>(R.id.done)
        }
    }
}
