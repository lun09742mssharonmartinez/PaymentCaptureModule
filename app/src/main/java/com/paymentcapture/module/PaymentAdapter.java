package com.paymentcapture.module;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.paymentcapture.module.models.PaymentData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 支付数据列表适配器
 */
public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    private List<PaymentData> dataList;
    private SimpleDateFormat dateFormat;

    public PaymentAdapter(List<PaymentData> dataList) {
        this.dataList = dataList;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_payment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PaymentData data = dataList.get(position);

        holder.tvType.setText(data.getType());
        holder.tvUrl.setText(data.getUrl());
        holder.tvAmount.setText("金额: " + data.getAmount());
        holder.tvOrderId.setText("订单: " + data.getOrderId());
        holder.tvTime.setText(dateFormat.format(new Date(data.getTimestamp())));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvType, tvUrl, tvAmount, tvOrderId, tvTime;

        ViewHolder(View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tv_type);
            tvUrl = itemView.findViewById(R.id.tv_url);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}
