package cn.bigorange.wheel.ui

import android.content.Intent
import cn.bigorange.common.BaseActivity
import cn.bigorange.common.utils.ListUtils
import cn.bigorange.wheel.database.DatabaseHelper
import cn.bigorange.wheel.databinding.ActivityInputBinding
import cn.bigorange.wheel.entity.Record
import com.blankj.utilcode.util.ToastUtils
import org.apache.commons.lang3.StringUtils

class InputActivity : BaseActivity<ActivityInputBinding>() {

    override fun initView() {
    }


    override fun initData() {


    }


    override fun initListener() {
        binding.header.setOnCommonTitleIconSubClickListener {
            if (StringUtils.isEmpty(binding.etQuestion.text.toString())) {
                ToastUtils.showShort("请输入问题");
                return@setOnCommonTitleIconSubClickListener
            }
            val record = Record()
            record.question = binding.etQuestion.text.toString()
            record.optionList = ListUtils.strSplitToList(binding.etOptions.text.toString())
            DatabaseHelper.getInstance().insertRecord(record);
            val intent = Intent()
            this@InputActivity.setResult(RESULT_OK, intent)
            this@InputActivity.finish()
        }
    }

}