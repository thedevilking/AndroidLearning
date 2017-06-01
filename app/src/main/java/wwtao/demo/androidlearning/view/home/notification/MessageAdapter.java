package wwtao.demo.androidlearning.view.home.notification;

import com.google.common.collect.Lists;

import com.annimon.stream.Optional;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wwtao.demo.androidlearning.R;
import wwtao.demo.androidlearning.view.home.notification.detail.MessageDetailActivity;

/**
 * Created by wangweitao04 on 17/6/1.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    List<String> messageList = Lists.newArrayList();

    public List<String> getMessageList() {

        return messageList;
    }

    public void setMessageList(List<String> messageList) {
        this.messageList = messageList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageViewHolder(parent, R.layout.adapter_message_item);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Optional.ofNullable(messageList)
                .filter(list -> position >= 0 && position < list.size())
                .ifPresent(list -> holder.bind(list.get(position)));
    }

    @Override
    public int getItemCount() {
        return Optional.ofNullable(messageList)
                .map(List::size)
                .orElse(0);
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvMessageAdapterItem)
        TextView textView;

        public MessageViewHolder(ViewGroup parent, @LayoutRes int layoutId) {
            super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
            ButterKnife.bind(this, itemView);
        }

        public void bind(String message) {
            textView.setText(message);

            textView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), MessageDetailActivity.class);
                intent.putExtra("message", message);
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
