package cn.bigorange.wheel.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 文 件 名: CustomDecoration
 * 创 建 人: sineom
 * 创建日期: 2019-10-25 15:52
 * 修改时间：
 * 修改备注：
 */

class CustomDecoration(context: Context, orientation: Int) : RecyclerView.ItemDecoration() {
    private var mDivider: Drawable? = null
    private var mOrientation: Int = 0
    private val mBounds = Rect()

    init {
        val a = context.obtainStyledAttributes(ATTRS)
        this.mDivider = a.getDrawable(0)
        if (this.mDivider == null) {
//            XLog.w(
//                "DividerItem",
//                "@android:attr/listDivider was not set in the theme used for this DividerItemDecoration. Please set that attribute all call setDrawable()"
//            )
        }

        a.recycle()
        this.setOrientation(orientation)
    }

    fun setOrientation(orientation: Int) {
        require(!(orientation != 0 && orientation != 1)) { "Invalid orientation. It should be either HORIZONTAL or VERTICAL" }
        this.mOrientation = orientation
    }

    fun setDrawable(drawable: Drawable) {
        requireNotNull(drawable) { "Drawable cannot be null." }
        this.mDivider = drawable
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager != null && this.mDivider != null) {
            if (this.mOrientation == 1) {
                this.drawVertical(c, parent)
            } else {
                this.drawHorizontal(c, parent)
            }

        }
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val left: Int
        val right: Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(left, parent.paddingTop, right, parent.height - parent.paddingBottom)
        } else {
            left = 0
            right = parent.width
        }

        val childCount = parent.childCount

        //当最后一个子项的时候去除下划线
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, this.mBounds)
            val bottom = this.mBounds.bottom + Math.round(child.translationY)
            val top = bottom - this.mDivider!!.intrinsicHeight
            this.mDivider!!.setBounds(left, top, right, bottom)
            this.mDivider!!.draw(canvas)
        }

        canvas.restore()
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val top: Int
        val bottom: Int
        if (parent.clipToPadding) {
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
            canvas.clipRect(parent.paddingLeft, top, parent.width - parent.paddingRight, bottom)
        } else {
            top = 0
            bottom = parent.height
        }

        val childCount = parent.childCount

        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            parent.layoutManager!!.getDecoratedBoundsWithMargins(child, this.mBounds)
            val right = this.mBounds.right + Math.round(child.translationX)
            val left = right - this.mDivider!!.intrinsicWidth
            this.mDivider!!.setBounds(left, top, right, bottom)
            this.mDivider!!.draw(canvas)
        }

        canvas.restore()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (mDivider == null) {
            outRect.set(0, 0, 0, 0)
            return
        }
        if (mOrientation == VERTICAL) {
            //parent.getChildCount() 不能拿到item的总数
            val lastPosition = state.itemCount - 1
            val position = parent.getChildAdapterPosition(view)
            if (position < lastPosition) {
                outRect.set(0, 0, 0, mDivider!!.intrinsicHeight)
            } else {
                outRect.set(0, 0, 0, 0)
            }
        } else {
            val lastPosition = state.itemCount - 1
            val position = parent.getChildAdapterPosition(view)
            if (position < lastPosition) {
                outRect.set(0, 0, mDivider!!.intrinsicWidth, 0)
            } else {
                outRect.set(0, 0, 0, 0)
            }
        }
    }

    companion object {

        val HORIZONTAL = 0
        val VERTICAL = 1
        private val TAG = "DividerItem"
        private val ATTRS = intArrayOf(16843284)
    }

}