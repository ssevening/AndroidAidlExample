package com.ssevening.bestcuts.object;

import java.util.List;

import com.ssevening.bestcuts.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class PipeInfoAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<PipeInfo> pipeInfos;
    private ViewHolder     holder;
    private Context        context;

    public PipeInfoAdapter(Context context, List<PipeInfo> imageInfos) {
        this.mInflater = LayoutInflater.from(context);
        this.pipeInfos = imageInfos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return pipeInfos.size();
    }

    @Override
    public Object getItem(int arg0) {
        return pipeInfos.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_pipe_info, null);
            holder.tv_length = (TextView) convertView.findViewById(R.id.tv_length);
            holder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
            holder.tv_x = (TextView) convertView.findViewById(R.id.tv_x);
            holder.btn_delete = (Button) convertView.findViewById(R.id.btn_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        convertView.setTag(holder);

        final PipeInfo pipe = pipeInfos.get(position);
        holder.tv_length.setText(pipe.getLength() + "");
        holder.tv_count.setText(pipe.getCount() + "");
        holder.btn_delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context).setTitle("��ʾ").setMessage("ȷ��Ҫɾ��������¼��").setPositiveButton("ȷ��",
                                                                                                            new AlertDialog.OnClickListener() {

                                                                                                                @Override
                                                                                                                public void onClick(DialogInterface dialog,
                                                                                                                                    int which) {
                                                                                                                    if (dialog != null) {
                                                                                                                        dialog.cancel();
                                                                                                                    }
                                                                                                                    pipeInfos.remove(pipe);
                                                                                                                    notifyDataSetChanged();

                                                                                                                }
                                                                                                            }).setNegativeButton("ȡ��",
                                                                                                                                 new AlertDialog.OnClickListener() {

                                                                                                                                     @Override
                                                                                                                                     public void onClick(DialogInterface dialog,
                                                                                                                                                         int which) {
                                                                                                                                         dialog.cancel();
                                                                                                                                     }
                                                                                                                                 }).create().show();

            }
        });

        return convertView;
    }

    public void addMessage(PipeInfo mess) {
        this.pipeInfos.add(mess);
    }

    public void clear() {
        this.pipeInfos.clear();
    }

}

class ViewHolder {

    public TextView tv_length;
    public TextView tv_count;
    public TextView tv_x;
    public Button   btn_delete;
}
