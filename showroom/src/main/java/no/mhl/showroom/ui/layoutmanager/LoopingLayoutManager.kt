package no.mhl.showroom.ui.layoutmanager

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import no.mhl.showroom.util.indexOrigin


class LoopingLayoutManager : RecyclerView.LayoutManager() {

    private val looperEnable: Boolean = true

    override fun generateDefaultLayoutParams() =
        RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

    override fun canScrollHorizontally() = true

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (itemCount <= 0) {
            return
        }

        if (state.isPreLayout) {
            return
        }

        detachAndScrapAttachedViews(recycler)

        var actualWidth: Int = 0

        for (i in 0..itemCount) {
            val itemView = recycler.getViewForPosition(i)

            addView(itemView)

            measureChildWithMargins(itemView, 0, 0)

            val width = getDecoratedMeasuredWidth(itemView)
            val height = getDecoratedMeasuredHeight(itemView)

            layoutDecorated(itemView, actualWidth, 0, actualWidth + width, height)

            actualWidth += width

            if (actualWidth > width) {
                break
            }
        }

    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ): Int {
        val travel: Int = fill(dx, recycler, state)

        if (travel == 0) {
            return 0
        }

        offsetChildrenHorizontal(travel * -1)



        return travel
    }

    private fun fill(dx: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        if (dx > 0) {

            val lastView = getChildAt(childCount.indexOrigin) ?: return 0

            val lastPosition = getPosition(lastView)

            if (lastView.right < width) {

                var scrap: View? = null

                if (lastPosition == itemCount.indexOrigin) {

                    if (looperEnable) {

                        scrap = recycler.getViewForPosition(0)

                    }

                } else {

                    scrap = recycler.getViewForPosition(lastPosition + 1)

                }

                if (scrap == null) {
                    return dx
                }

                addView(scrap)

                measureChildWithMargins(scrap, 0, 0)

                val width = getDecoratedMeasuredWidth(scrap)
                val height = getDecoratedMeasuredHeight(scrap)

                layoutDecorated(scrap, lastView.right, 0, lastView.right + width, height)

                return dx

            }
        } else {

            val firstView = getChildAt(0) ?: return 0

            val firstPos = getPosition(firstView)

            if (firstView.getLeft() >= 0) {
                var scrap: View? = null
                if (firstPos == 0) {
                    if (looperEnable) {
                        scrap = recycler.getViewForPosition(getItemCount() - 1)
                    }
                } else {
                    scrap = recycler.getViewForPosition(firstPos - 1)
                }
                if (scrap == null) {
                    return 0
                }
                addView(scrap, 0)
                measureChildWithMargins(scrap, 0, 0)
                val width = getDecoratedMeasuredWidth(scrap)
                val height = getDecoratedMeasuredHeight(scrap)
                layoutDecorated(
                    scrap, firstView.getLeft() - width, 0,
                    firstView.getLeft(), height
                )
            }
        }
        return dx;
    }

    private fun recyclerHideView(
        dx: Int,
        recycler: Recycler,
        state: RecyclerView.State
    ) {
        for (i in 0 until childCount) {
            val view = getChildAt(i) ?: continue
            if (dx > 0) {
                if (view.right < 0) {
                    removeAndRecycleView(view, recycler)
                }
            } else {
                if (view.left > width) {
                    removeAndRecycleView(view, recycler)
                }
            }
        }
    }

}
