package com.ssevening.bestcuts.object;

import com.ssevening.bestcuts.R;
import com.ssevening.bestcuts.util.Utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CutResultAdapter extends BaseExpandableListAdapter {

    private Context                   context;
    // ��������ͼ����ʾ����
    private String[]                  parentArray;
    // ����ͼ��ʾ����
    private String[][]                childArray;

    private LayoutInflater            mInflater;

    private CutResultParentViewHolder cutResultViewHolder;

    private CutResultChildViewHolder  cutResultChildViewHolder;

    public CutResultAdapter(Context context, String[] parentArray, String[][] childArray) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.parentArray = parentArray;
        this.childArray = childArray;

    }

    @Override
    public int getGroupCount() {
        return parentArray.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childArray[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentArray[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childArray[groupPosition][childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            cutResultViewHolder = new CutResultParentViewHolder();
            convertView = mInflater.inflate(R.layout.item_cut_result_parent, null);
            cutResultViewHolder.tv_pipe_num = (TextView) convertView.findViewById(R.id.tv_pipe_num);
            cutResultViewHolder.iv_arrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
            convertView.setTag(cutResultViewHolder);
        } else {
            cutResultViewHolder = (CutResultParentViewHolder) convertView.getTag();
        }

        convertView.setTag(cutResultViewHolder);
        cutResultViewHolder.tv_pipe_num.setText(getGroup(groupPosition).toString());

        if (isExpanded) {
            cutResultViewHolder.iv_arrow.setImageResource(R.drawable.icon_arrow_up);
        } else {
            cutResultViewHolder.iv_arrow.setImageResource(R.drawable.icon_arrow_down);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {

        if (convertView == null) {
            cutResultChildViewHolder = new CutResultChildViewHolder();
            convertView = mInflater.inflate(R.layout.item_cut_result_child, null);
            cutResultChildViewHolder.tv_pipe_length = (TextView) convertView.findViewById(R.id.tv_pipe_length);
            cutResultChildViewHolder.iv_cut_staus = (ImageView) convertView.findViewById(R.id.iv_cut_staus);
            cutResultChildViewHolder.rl_cut_result = (RelativeLayout) convertView.findViewById(R.id.rl_cut_result);
            convertView.setTag(cutResultViewHolder);
        } else {
            cutResultChildViewHolder = (CutResultChildViewHolder) convertView.getTag();
        }

        convertView.setTag(cutResultChildViewHolder);
        cutResultChildViewHolder.tv_pipe_length.setText(getChild(groupPosition, childPosition).toString());

        final String key = groupPosition + "" + childPosition;
        String cutted = Utils.getSharedPerferences(context, "cutted", "");
        if (cutted.contains(key)) {
            cutResultChildViewHolder.iv_cut_staus.setImageResource(R.drawable.status_cutted);
            cutResultChildViewHolder.rl_cut_result.setBackgroundColor(Color.LTGRAY);
        } else {
            cutResultChildViewHolder.iv_cut_staus.setImageResource(R.drawable.status_wait_cut);
            cutResultChildViewHolder.rl_cut_result.setBackgroundColor(context.getResources().getColor(R.color.listbg));

        }
        cutResultChildViewHolder.rl_cut_result.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String cutted = Utils.getSharedPerferences(context, "cutted", "");
                if (cutted.equals("")) {
                    Utils.setSharedPerferences(context, "cutted", key);
                    notifyDataSetChanged();
                } else {
                    if (cutted.contains(key)) {
                        notifyDataSetChanged();
                    } else {
                        Utils.setSharedPerferences(context, "cutted", cutted + "," + key);
                        notifyDataSetChanged();
                    }
                }
            }
        });

        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}

class CutResultParentViewHolder {

    public TextView  tv_pipe_num;
    public ImageView iv_arrow;
}

class CutResultChildViewHolder {

    public RelativeLayout rl_cut_result;
    public TextView       tv_pipe_length;
    public ImageView      iv_cut_staus;
}
