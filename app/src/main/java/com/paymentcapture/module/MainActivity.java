package com.paymentcapture.module;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paymentcapture.module.models.PaymentData;
import com.paymentcapture.module.utils.DataStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面 - 显示拦截到的支付链接列表
 */
public class MainActivity extends AppCompatActivity {

    private TextView tvModuleStatus;
    private RecyclerView rvPaymentList;
    private PaymentAdapter adapter;
    private List<PaymentData> paymentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        loadData();
        checkXposedStatus();
    }

    private void initViews() {
        tvModuleStatus = findViewById(R.id.tv_module_status);
        rvPaymentList = findViewById(R.id.rv_payment_list);

        // 设置 RecyclerView
        paymentList = new ArrayList<>();
        adapter = new PaymentAdapter(paymentList);
        rvPaymentList.setLayoutManager(new LinearLayoutManager(this));
        rvPaymentList.setAdapter(adapter);
    }

    private void loadData() {
        // 从存储中加载拦截的支付数据
        paymentList.clear();
        paymentList.addAll(DataStorage.loadPaymentData(this));
        adapter.notifyDataSetChanged();

        if (paymentList.isEmpty()) {
            Toast.makeText(this, "暂无拦截数据", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 检查 Xposed 模块是否激活
     */
    private void checkXposedStatus() {
        if (isModuleActive()) {
            tvModuleStatus.setText("✅ 模块已激活");
            tvModuleStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            tvModuleStatus.setText("❌ 模块未激活 - 请在 LSPosed 中启用");
            tvModuleStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    /**
     * 检测模块是否激活
     * 这个方法会被 Xposed 框架 Hook，如果被 Hook 则返回 true
     */
    private boolean isModuleActive() {
        return false;  // 默认返回 false，Xposed 会 Hook 这个方法返回 true
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}
