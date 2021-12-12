package cn.bigorange.wheel.ui

import android.content.Intent
import cn.bigorange.common.BaseActivity
import cn.bigorange.common.utils.ListUtils
import cn.bigorange.wheel.database.DatabaseHelper
import cn.bigorange.wheel.databinding.ActivityEditBinding
import cn.bigorange.wheel.entity.Record
import com.blankj.utilcode.util.ToastUtils
import org.apache.commons.lang3.StringUtils
import java.util.*


class EditActivity : BaseActivity<ActivityEditBinding>() {

    private var record: Record? = null

    override fun initView() {
    }

    override fun initListener() {
        binding.header.setOnCommonTitleBackClickListener { finish() }
        binding.header.setOnCommonTitleIconSubClickListener {
            record?.let {
                if (StringUtils.isEmpty(binding.etQuestion.text.toString())) {
                    ToastUtils.showShort("请输入问题");
                    return@let
                }
                it.question = binding.etQuestion.text.toString()
                it.optionList = ListUtils.strSplitToList(binding.etOptions.text.toString())
                DatabaseHelper.getInstance().updateRecordById(it)
                val intent = Intent()
                this@EditActivity.setResult(RESULT_OK, intent)
                this@EditActivity.finish()
            }
        }
    }

    override fun initData() {
        record = intent.getSerializableExtra("record") as Record?
        record?.let {
            binding.etQuestion.setText(it.question)
            binding.etOptions.setText(StringUtils.join(it.optionList, ", "))
        }
    }

}