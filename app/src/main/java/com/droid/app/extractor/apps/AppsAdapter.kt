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
        val app = getItem(position)
        val context = holder.itemView.context;

        holder.icon.setImageDrawable(app.icon)
        holder.label.text = app.label
        holder.packageName.text = app.packageName
        holder.done.visibility = if (app.exported) View.VISIBLE else View.GONE
        holder.version.text = context.getString(R.string.app_version, app.versionName ?: "1.0")
        holder.size.text = app.size
        holder.checkBox.isChecked = app.checked

        val clickListener = View.OnClickListener {
            app.checked = !app.checked
            holder.checkBox.isChecked = app.checked
        }
        holder.checkBox.setOnClickListener(clickListener)
        holder.itemView.setOnClickListener(clickListener)
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
        internal val icon: ImageView
        internal val label: TextView
        internal val version: TextView
        internal val packageName: TextView
        internal val size: TextView
        internal val checkBox: CheckBox
        internal val done: ImageView

        init {
            icon = itemView.findViewById(R.id.icon) as ImageView
            label = itemView.findViewById(R.id.label) as TextView
            version = itemView.findViewById(R.id.version) as TextView
            packageName = itemView.findViewById(R.id.packageName) as TextView
            size = itemView.findViewById(R.id.size) as TextView
            checkBox = itemView.findViewById(R.id.checkbox) as CheckBox
            done = itemView.findViewById(R.id.done) as ImageView
        }
    }
}
