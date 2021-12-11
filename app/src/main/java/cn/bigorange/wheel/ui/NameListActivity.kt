package cn.bigorange.wheel.ui

import android.content.Intent
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cn.bigorange.wheel.R
import cn.bigorange.wheel.adapter.NameListAdapter
import cn.bigorange.wheel.database.DatabaseHelper
import cn.bigorange.wheel.databinding.ActivityNameListBinding
import cn.bigorange.wheel.entity.Record
import cn.bigorange.wheel.utils.DensityUtils
import cn.bigorange.wheel.utils.RecyclerUtils
import cn.bigorange.wheel.utils.SpaceItemDecoration
import com.blankj.utilcode.util.ActivityUtils


class NameListActivity : cn.bigorange.common.BaseActivity<ActivityNameListBinding>() {

    private val adapter = NameListAdapter(ArrayList())

    override fun initView() {
        initRecycler()
    }

    override fun initListener() {
        binding.fab.setOnClickListener {
            ActivityUtils.startActivity(InputActivity::class.java)
        }

    }

    private fun initRecycler() {
        RecyclerUtils.initRecycler(binding.rvList, RecyclerView.VERTICAL, false)
        val distance = DensityUtils.dp2px(this, 10f)
        binding.rvList.addItemDecoration(
            SpaceItemDecoration(
                distance,
                distance
            )
        )
        binding.rvList.adapter = adapter
        adapter.setNewInstance(mockData())
        adapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_edit -> {
                    Toast.makeText(this@NameListActivity, "点击了编辑：$position", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, EditActivity::class.java))
                }
            }
        }
        adapter.setOnItemClickListener { adapter, view, position ->
            Toast.makeText(this@NameListActivity, "点击了某一项：$position", Toast.LENGTH_SHORT).show()
        }

    }

    private fun mockData(): MutableList<Record> {
        val records = DatabaseHelper.getInstance().getAllRecords();
        return records
    }

}