package cn.bigorange.wheel.utils

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @packageName: yicun
 * @fileName: SpaceItemDecoration
 * @author: chitian
 * @date: 2020/7/30 14:43
 * @description:
 * @org: http://www.idictec.com (安徽阿迪卡尔信息科技有限公司)
 *
 */
class SpaceItemImagesDecoration : RecyclerView.ItemDecoration {
    private var spanCount = 0
    private var spacing = 0
    private var includeEdge = false

    constructor(
        context: Context,
        spanCount: Int,
        spacing: Int,
        includeEdge: Boolean
    ) {
        this.spanCount = spanCount
        this.spacing = dp2px(context, spacing)
        this.includeEdge = includeEdge
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % spanCount // item column
        if (includeEdge) {
            outRect.left =
                spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
            outRect.right =
                (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
            if (position < spanCount) { // top edge
                outRect.top = spacing
            }
            outRect.bottom = spacing // item bottom
        } else {
            outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
            outRect.right =
                spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing // item top
            }
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private fun dp2px(context: Context, dpValue: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue.toFloat(), context.resources.displayMetrics
        ).toInt()
    }
}