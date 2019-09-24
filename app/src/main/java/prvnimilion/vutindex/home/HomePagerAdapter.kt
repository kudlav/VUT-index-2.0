package prvnimilion.vutindex.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.button.MaterialButton
import prvnimilion.vutindex.R

class HomePagerAdapter(
    private val context: Context,
    private val views: List<View>,
    private val tabNames: List<String>,
    private val icons: List<Int>,
    private val colors: List<Int>
) : PagerAdapter() {

    fun getView(position: Int): View {
        return views[position]
    }

    override fun getCount(): Int {
        return views.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(views[position])
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = views[position]
        container.addView(view)
        return view
    }

    override fun getItemPosition(`object`: Any): Int {
        for (index in 0 until count) {
            if (`object` as View === views[index]) {
                return index
            }
        }
        return POSITION_NONE
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabNames[position]
    }


    fun getTabView(position: Int): View {
        val v = LayoutInflater.from(context).inflate(R.layout.custom_home_tab, null)
        v.findViewById<ImageView>(R.id.tab_button_idle)
            .setImageDrawable(context.getDrawable(icons[position]))
        v.findViewById<MaterialButton>(R.id.tab_button_active).text = tabNames[position]
        v.findViewById<MaterialButton>(R.id.tab_button_active).icon =
            context.getDrawable(icons[position])
        v.findViewById<MaterialButton>(R.id.tab_button_active)
            .setBackgroundColor(context.getColor(colors[position]))
        return v
    }
}