package cn.bigorange.wheel.ui

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import cn.bigorange.common.BaseActivity
import cn.bigorange.common.utils.ListUtils
import cn.bigorange.wheel.R
import cn.bigorange.wheel.database.DatabaseHelper
import cn.bigorange.wheel.databinding.ActivityEditRosterBinding
import cn.bigorange.wheel.entity.Roster
import com.blankj.utilcode.util.ToastUtils
import org.apache.commons.lang3.StringUtils


class EditRosterActivity : BaseActivity<ActivityEditRosterBinding>() {

    private var roster: Roster? = null

    override fun initView() {
    }

    override fun initListener() {
        binding.header.setOnCommonTitleBackClickListener {
//            isFangqi()
            finish()
        }
        binding.header.setOnCommonTitleIconSubClickListener {
            roster?.let {
                val title = binding.etTitle.text.toString();
                if (StringUtils.isEmpty(title)) {
                    ToastUtils.showShort("请输入名单标题");
                    return@let
                }
                val optionList = ListUtils.strSplitToList(binding.etOptions.text.toString())
                if (optionList == null || optionList.size < 2) {
                    ToastUtils.showShort("请至少输入2个选项");
                    return@let
                }
                it.title = title
                it.optionList = optionList
                if (it.id != 0L) {
                    DatabaseHelper.getInstance().updateRosterById(it)
                } else {
                    DatabaseHelper.getInstance().insertRoster(it)
                }
                val intent = Intent()
                this@EditRosterActivity.setResult(RESULT_OK, intent)
                this@EditRosterActivity.finish()
            }
        }
        binding.etOptions.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s0: Editable) {
                val tempOptionList = ListUtils.strSplitToList(s0.toString())
                binding.tvAmount.text = resources.getString(R.string.total_amount, tempOptionList.size.toString())
            }
        })
    }

    override fun initData() {
        intent.getStringExtra("id")?.let {
            roster = DatabaseHelper.getInstance().selectRosterById(it.toLong())
        }
        if (roster == null) {
            roster = Roster()
        }
        if (roster?.id != 0L) {
            binding.header.setTvTitle("修改名单")
            binding.etTitle.setText(roster!!.title)
            binding.tvAmount.text = resources.getString(R.string.total_amount, roster!!.optionList.size.toString())
            binding.etOptions.setText(StringUtils.join(roster!!.optionList, ", "))
        } else {
            binding.header.setTvTitle("新增名单")
            binding.etTitle.setText("")
            binding.tvAmount.text = resources.getString(R.string.total_amount, "0")
            binding.etOptions.setText("")
        }
    }

//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            isFangqi()
//            return true
//        }
//        return super.onKeyDown(keyCode, event)
//    }

//    private fun isFangqi() {
//        AlertDialog.Builder(this).setTitle("放弃更改？")
//            .setMessage("未保存的内容将会被丢失").setPositiveButton(
//                "取消"
//            ) { dialogInterface, i ->
//                dialogInterface.dismiss()
//            }.setNegativeButton(
//                "放弃"
//            ) { dialogInterface, i ->
//                dialogInterface.dismiss()
//                finish()
//            }.create().show()
//    }


}