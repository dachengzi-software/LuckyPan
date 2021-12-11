package cn.bigorange.common.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import cn.bigorange.common.R

/**
 * 通用头部，包括标题，返回键，右上交的icon
 */
class CommonTitleView(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
    LinearLayout(context, attrs, defStyleAttr, defStyleRes), View.OnClickListener {
    private var llRoot: LinearLayout? = null
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null
    private var ivIcon: ImageView? = null
    private var ivIconSub: ImageView? = null
    private var tvRight: TextView? = null
    private var llTxTitle: LinearLayout? = null
    private var ivTitleDrawable: ImageView? = null

    private var titleBackClick: (() -> Unit)? = null
    private var titleIconClick: (() -> Unit)? = null
    private var titleSubmitSubClick: (() -> Unit)? = null
    private var titleSubmitClick: (() -> Unit)? = null
    private var titleTxClick: (() -> Unit)? = null


    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : this(context, attrs, defStyleAttr, 0) {
    }

    private fun initView(context: Context) {
        val view = inflate(context, R.layout.common_view_title, this)
        llRoot = view.findViewById(R.id.ll_root)
        tvTitle = view.findViewById(R.id.tv_title)
        ivBack = view.findViewById(R.id.iv_back)
        ivIcon = view.findViewById(R.id.iv_icon)
        ivIconSub = view.findViewById(R.id.iv_icon_sub)
        tvRight = view.findViewById(R.id.tv_right)
        llTxTitle = findViewById(R.id.ll_tx_title)
        ivTitleDrawable = findViewById(R.id.iv_title_drawable)
        ivBack?.setOnClickListener(this)
        ivIcon?.setOnClickListener(this)
        ivIconSub?.setOnClickListener(this)
        tvRight?.setOnClickListener(this)
        llTxTitle?.setOnClickListener(this)
    }

    /**
     * 动态设置标题
     *
     * @param s
     */
    fun setTvTitle(s: String?) {
        tvTitle!!.text = s
    }

    /**
     * 获取标题
     *
     * @return
     */
    fun getTvTitle(): String {
        return tvTitle!!.text.toString()
    }

    /**
     * 获取右边按钮的显示文字
     *
     * @return
     */
    val subCommitTitleTx: String
        get() = tvRight!!.text.toString()

    override fun onClick(view: View) {
        val i = view.id
        if (i == R.id.iv_back) {
            titleBackClick?.invoke()
        } else if (i == R.id.iv_icon) {
            titleIconClick?.invoke()
        } else if (i == R.id.iv_icon_sub) {
            titleSubmitSubClick?.invoke()
        } else if (i == R.id.tv_right) {
            titleSubmitClick?.invoke()
        } else if (i == R.id.ll_tx_title) {
            titleTxClick?.invoke()
        }
    }

    /**
     * 动态设置右边文字按钮的内容
     *
     * @param subTxName
     */
    fun setTxSubName(subTxName: String?) {
        tvRight!!.visibility = VISIBLE
        tvRight!!.text = subTxName
    }

    /**
     * 动态设置Icon
     *
     * @param resourceId
     */
    fun setIvIconSub(resourceId: Int) {
        ivIconSub!!.setImageResource(resourceId)
    }

    fun setIvIconSubVisible(visible: Int) {
        ivIconSub!!.visibility = visible
    }

    fun setIvIconIsShow(isShow: Boolean) {
        ivIcon!!.visibility =
            if (isShow) VISIBLE else INVISIBLE
    }

    fun setOnCommonTitleBackClickListener(click: (() -> Unit)) {
        this.titleBackClick = click
    }

    fun setOnCommonTitleIconClickListener(click: (() -> Unit)) {
        titleIconClick = click
    }

    fun setOnCommonTitleTxSubmitClickListener(click: (() -> Unit)) {
        titleSubmitClick = click
    }

    fun setOnCommonTitleIconSubClickListener(click: (() -> Unit)) {
        titleSubmitSubClick = click
    }

    fun setOnCommonTitleTxClickListener(click: (() -> Unit)) {
        titleTxClick = click
    }

    init {
        initView(context)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonTitleView)
        val title = typedArray.getString(R.styleable.CommonTitleView_common_title)
        val titleColor = typedArray.getColor(R.styleable.CommonTitleView_common_title_color,
            ContextCompat.getColor(context, R.color.common_black))
        val iconBack = typedArray.getResourceId(R.styleable.CommonTitleView_common_back_visible,
            R.drawable.common_icon_back_black)
        val iconRight = typedArray.getResourceId(R.styleable.CommonTitleView_common_icon,
            R.drawable.common_icon_share_white)
        val iconRightSub = typedArray.getResourceId(R.styleable.CommonTitleView_common_icon_sub,
            R.drawable.common_home_message)
        val backVisible =
            typedArray.getBoolean(R.styleable.CommonTitleView_common_back_visible, true)
        val backDrawable =
            typedArray.getResourceId(R.styleable.CommonTitleView_common_back_drawable,
                R.drawable.common_icon_back_black)
        val iconRightVisible =
            typedArray.getBoolean(R.styleable.CommonTitleView_common_icon_visible, false)
        val iconRightSubVisible =
            typedArray.getBoolean(R.styleable.CommonTitleView_common_icon_sub_visible, false)
        val txSubName = typedArray.getString(R.styleable.CommonTitleView_common_tx_sub_name)
        val txSubVisible =
            typedArray.getBoolean(R.styleable.CommonTitleView_common_tx_sub_visible, false)
        val bgColor = typedArray.getColor(R.styleable.CommonTitleView_common_ctv_background,
            ContextCompat.getColor(context, R.color.common_white))
        val txDrawableVisible =
            typedArray.getBoolean(R.styleable.CommonTitleView_common_ctv_tx_drawable_visible, false)
        val txDrawableRes =
            typedArray.getResourceId(R.styleable.CommonTitleView_common_ctv_tx_drawable,
                R.drawable.common_location)
        tvTitle!!.text = title
        tvTitle!!.setTextColor(titleColor)
        ivBack!!.setImageResource(iconBack)
        ivBack!!.visibility = if (backVisible) VISIBLE else INVISIBLE
        ivBack!!.setImageResource(backDrawable)
        ivIcon!!.setImageResource(iconRight)
        ivIconSub!!.setImageResource(iconRightSub)
        ivIcon!!.visibility =
            if (iconRightVisible) VISIBLE else GONE
        ivIconSub!!.visibility =
            if (iconRightSubVisible) VISIBLE else GONE
        tvRight!!.text = txSubName
        tvRight!!.visibility = if (txSubVisible) VISIBLE else GONE
        llRoot!!.setBackgroundColor(bgColor)
        ivTitleDrawable!!.visibility = if (txDrawableVisible) VISIBLE else GONE
        ivTitleDrawable!!.setImageResource(txDrawableRes)
        typedArray.recycle()
    }
}

