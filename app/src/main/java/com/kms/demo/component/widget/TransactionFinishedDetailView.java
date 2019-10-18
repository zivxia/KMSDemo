package com.kms.demo.component.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kms.demo.R;
import com.kms.demo.entity.Transaction;
import com.kms.demo.entity.TransactionStatus;
import com.kms.demo.utils.BigDecimalUtil;
import com.kms.demo.utils.DateUtil;
import com.kms.demo.utils.NumberParserUtil;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author matrixelement
 */
public class TransactionFinishedDetailView extends TransactionDetailView {

    @BindView(R.id.tv_sum_amount)
    TextView tvSumAmount;
    @BindView(R.id.tv_status_desc)
    TextView tvStatusDesc;
    @BindView(R.id.tv_refunded_amount)
    TextView tvRefundedAmount;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_send_amount)
    TextView tvSendAmount;
    @BindView(R.id.tv_fee_amount)
    TextView tvFeeAmount;
    @BindView(R.id.tv_send_time)
    TextView tvSendTime;
    @BindView(R.id.tv_finish_time)
    TextView tvFinishTime;
    @BindView(R.id.tv_note)
    TextView tvNote;
    @BindView(R.id.tv_transaction_hash)
    TextView tvTransactionHash;
    @BindView(R.id.tv_from_address)
    TextView tvFromAddress;
    @BindView(R.id.tv_to_address)
    TextView tvToAddress;
    @BindView(R.id.iv_transation_status)
    ImageView ivTransationStatus;
    @BindView(R.id.tv_sender_wallet_name)
    TextView tvSenderWalletName;

    private Context context;
    private Unbinder unbinder;

    public TransactionFinishedDetailView(Context context) {
        super(context, null, 0);
    }

    public TransactionFinishedDetailView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TransactionFinishedDetailView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init(Context context) {
        this.context = context;
        View contentView = LayoutInflater.from(context).inflate(R.layout.layout_transaction_finished, this);
        unbinder = ButterKnife.bind(this, contentView);
    }

    @Override
    public void showDetail(Transaction transaction, String walletAddress) {

        double sendAmount = transaction.getSendAmount();
        double feeAmount = transaction.getFeeAmount();
        double sumAmount = BigDecimalUtil.add(sendAmount, feeAmount);
        double refundedAmount = sendAmount;
        boolean isReceiver = transaction.isReceiver(walletAddress);
        String txType = isReceiver ? context.getString(R.string.receive) : context.getString(R.string.send);
        String note = transaction.getNote();
        String hash = transaction.getHash();
        String fromAddress = transaction.getFromAddress();
        String toAddress = transaction.getToAddress();
        boolean isCompleted = transaction.getTransactionStatus() == TransactionStatus.COMPLETED;

        ivTransationStatus.setImageResource(isCompleted ? R.drawable.icon_succeed : R.drawable.icon_failed);
        tvSumAmount.setText(String.format("%s%s", isReceiver ? "+" : "-", NumberParserUtil.getPrettyBalance(sumAmount)));
        tvRefundedAmount.setVisibility(isCompleted ? GONE : VISIBLE);
        tvRefundedAmount.setText(context.getString(R.string.refunded, NumberParserUtil.getPrettyBalance(refundedAmount)));
        tvStatusDesc.setText(isCompleted ? context.getString(R.string.transaction_succeed) : context.getString(R.string.transaction_failed));
        tvType.setText(txType);
        tvSendAmount.setText(String.format("%s%s", isReceiver ? "" : "-", context.getString(R.string.amount_with_unit, NumberParserUtil.getPrettyBalance(sendAmount))));
        tvFeeAmount.setText(String.format("%s%s", isReceiver ? "" : "-", context.getString(R.string.amount_with_unit, NumberParserUtil.getPrettyBalance(feeAmount))));
        tvSendTime.setText(DateUtil.format(transaction.getCreateTime(), DateUtil.DATETIME_FORMAT_PATTERN_WITH_SECOND));
        //TODO 完成时间
        tvFinishTime.setText(DateUtil.format(transaction.getEndTime(), DateUtil.DATETIME_FORMAT_PATTERN_WITH_SECOND));
        tvNote.setText(note);
        tvTransactionHash.setText(hash);
        tvFromAddress.setText(fromAddress);
        tvToAddress.setText(toAddress);
        tvSenderWalletName.setText(transaction.getWalletName());
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
