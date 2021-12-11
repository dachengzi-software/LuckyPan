package cn.bigorange.wheel.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * 文 件 名: SpaceItemDecoration
 * 创 建 人: sineom
 * 创建日期: 2019-10-28 19:30
 * 修改时间：
 * 修改备注：
 * @author chitian
 */
class SpaceItemDecoration(
    private val leftRight: Int,
    private val topBottom: Int
) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        val layoutManager = getLinearLayoutManager(parent)
        //竖直方向的
        if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
            //最后一项需要 bottom
            if (parent.getChildAdapterPosition(view) == layoutManager.itemCount - 1) {
                outRect.bottom = topBottom
            }
            outRect.top = topBottom
            outRect.left = leftRight
            outRect.right = leftRight
        } else {
            //最后一项需要right
            if (parent.getChildAdapterPosition(view) == layoutManager.itemCount - 1) {
                outRect.right = leftRight
            }
            outRect.top = topBottom
            outRect.left = leftRight
            outRect.bottom = topBottom
        }
    }

    private fun getLinearLayoutManager(parent: RecyclerView): LinearLayoutManager {
        if (parent.layoutManager is LinearLayoutManager) {
            return parent.layoutManager as LinearLayoutManager
        } else {
            throw java.lang.IllegalStateException(
                "SpaceItemDecoration can only be used with a LinearLayoutManager."
            )
        }
    }

}