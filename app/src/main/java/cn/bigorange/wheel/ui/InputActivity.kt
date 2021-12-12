package cn.bigorange.wheel.ui

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.addTextChangedListener
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
        binding.header.setOnCommonTitleBackClickListener { finish() }
        binding.header.setOnCommonTitleIconSubClickListener {
            val question = binding.etQuestion.text.toString();
            if (StringUtils.isEmpty(question)) {
                ToastUtils.showShort("请输入问题");
                return@setOnCommonTitleIconSubClickListener
            }
            val optionList = ListUtils.strSplitToList(binding.etOptions.text.toString())
            if (optionList == null || optionList.size < 2) {
                ToastUtils.showShort("请至少输入2个选项");
                return@setOnCommonTitleIconSubClickListener
            }
            val record = Record()
            record.question = question
            record.optionList = optionList
            DatabaseHelper.getInstance().insertRecord(record);
            val intent = Intent()
            this@InputActivity.setResult(RESULT_OK, intent)
            this@InputActivity.finish()
        }
        binding.etOptions.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s0: Editable) {
                val tempOptionList = ListUtils.strSplitToList(s0.toString())
                binding.tvAmount.text = tempOptionList.size.toString()
            }
        })
    }

}