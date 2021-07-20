package no.mhl.showroom.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import no.mhl.showroom.R

class InfiniteViewPager2(
    context: Context,
    attrs: AttributeSet?
) : FrameLayout(context, attrs) {

    // region View Properties
    private val viewPager2 by lazy { findViewById<ViewPager2>(R.id.view_pager_infinite) }
    private val internalRecycler by lazy { viewPager2.getChildAt(0) as RecyclerView }
    // endregion

    // region Properties
    private var totalItemCount = 0
    var currentItemPosition: Int
        get() = when(viewPager2.currentItem) {
            0 -> totalItemCount - 3
            totalItemCount - 1 -> 0
            else -> viewPager2.currentItem - 1
        }
        set(value) {
            viewPager2.setCurrentItem(value + 1, false)
        }
    // endregion

    // region Initialisation
    init {
        LayoutInflater.from(context).inflate(R.layout.view_pager, this, true)
    }
    // endregion

    // region Adapter IO
    fun <T : RecyclerView.ViewHolder> setAdapter(theAdapter: RecyclerView.Adapter<T>, preloadLimit: Int = 3) {
        totalItemCount = theAdapter.itemCount

        viewPager2.apply {
            adapter = theAdapter
            setCurrentItem(1, false)
            offscreenPageLimit = preloadLimit
        }

        internalRecycler.apply {
            addOnScrollListener(
                InfiniteScrollBehavior(totalItemCount, layoutManager as LinearLayoutManager)
            )
        }
    }
    // endregion

    // region On Page Callback event
    fun registerOnPageCallback(callback: OnPageChangeCallback) = viewPager2.registerOnPageChangeCallback(callback)
    // endregion

    // region Infinite Scroll Behavior
    inner class InfiniteScrollBehavior(
        private val itemCount: Int,
        private val layoutManager: LinearLayoutManager
    ) : RecyclerView.OnScrollListener() {

        // region On Scrolled
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val firstItemVisible = layoutManager.findFirstVisibleItemPosition()
            val lastItemVisible = layoutManager.findLastVisibleItemPosition()

            when {
                firstItemVisible == (itemCount - 1) && dx > 0 -> recyclerView.scrollToPosition(1)
                lastItemVisible == 0 && dx < 0 -> recyclerView.scrollToPosition(itemCount - 2)
            }

        }
        // endregion

    }
    // endregion

}