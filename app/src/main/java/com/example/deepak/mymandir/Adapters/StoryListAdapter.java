package com.example.deepak.mymandir.Adapters;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.example.deepak.mymandir.MainActivity;
import com.example.deepak.mymandir.Models.StoryData;
import com.example.deepak.mymandir.Presenter.MyOnclickListener;
import com.example.deepak.mymandir.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Sharmaji on 6/2/2018.
 */

public class StoryListAdapter extends RecyclerView.Adapter<StoryListAdapter.MyViewHolder> {

    Context m_context;
    private ImageLoader imageLoader;
    public MyOnclickListener myOnclickListener;
    ArrayList<StoryData> storyList=new ArrayList<>();
    public StoryListAdapter(ArrayList<StoryData> storyList, Context m_context) {
        this.storyList = storyList;
        this.m_context = m_context;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView storyTittleTV;
        TextView storyUserName;
        CircleImageView userImageIV;
        ImageView storyImageIV;
        TextView likeCountTV;
        TextView commentCountTV;
        ImageView likeButton;
        ImageView shareButton;
        ImageView commentButton;
        TextView followButton;
        public MyViewHolder(View convertView){
            super(convertView);
            storyTittleTV = (TextView) convertView.findViewById(R.id.titleTV);
            storyUserName = (TextView) convertView.findViewById(R.id.usernameTV);
            userImageIV = (CircleImageView) convertView.findViewById(R.id.userImageIV);
            storyImageIV = (ImageView) convertView.findViewById(R.id.storyImageIV);
            likeCountTV = (TextView) convertView.findViewById(R.id.likeCountTV);
            commentCountTV = (TextView) convertView.findViewById(R.id.commentCountTV);
            followButton = (TextView) convertView.findViewById(R.id.followButton);
            likeButton = (ImageView) convertView.findViewById(R.id.likeButton);
            shareButton = (ImageView) convertView.findViewById(R.id.shareButton);
            commentButton = (ImageView) convertView.findViewById(R.id.commentButton);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.story_view, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final StoryData story = storyList.get(position);

        holder.storyTittleTV.setText(story.getTitle());
        holder.storyUserName.setText(story.getUserData().getUserName());
        int likeCount=story.getLikeCount();
        holder.likeCountTV.setText(String.valueOf(likeCount));
        int commentCount=story.getCommentCount();
        holder.commentCountTV.setText(String.valueOf(commentCount));
        holder.likeButton.setImageResource(R.drawable.like);
        holder.followButton.setText("Follow");
        holder.followButton.setTextColor(m_context.getResources().getColor(R.color.black));
        holder.followButton.setBackgroundResource(R.drawable.follow);

        holder.commentButton.setImageResource(R.drawable.comment);
        WindowManager windowManager = (WindowManager) m_context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        int width = p.x;

        String url = "";
        if(story.getAttachData()!=null) {
            url=story.getAttachData().getUrl();
            if(story.getAttachData().getType().equals("video")||story.getAttachData().getType().equals("audio"))
            {
                url=story.getAttachData().getThumbnailUrl();
            }
            Picasso.with(m_context)
                    .load(url)
                    .placeholder(R.drawable.ropo)
                    .resize(width, 0)
                    .error(R.color.colorAccent)
                    .into(holder.storyImageIV);
        } else {
            holder.storyImageIV.setVisibility(View.GONE);
        }
        if(story.getUserData()!=null) {
            Picasso.with(m_context)
                    .load(story.getUserData().getUserImage())
                    .placeholder(R.color.colorPrimary)
                    .error(R.color.colorAccent)
                    .into(holder.userImageIV);
        } else {
            holder.userImageIV.setVisibility(View.GONE);
        }

        holder.storyTittleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callListener(position);
            }
        });
        holder.storyUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callListener(position);
            }
        });
        holder.userImageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callListener(position);
            }
        });
        holder.storyImageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callListener(position);
            }
        });
        holder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callListener(position);
            }
        });
    }

    public int getItemCount() {
        return storyList.size();
    }

    public void callListener(int position){
        myOnclickListener = ((MainActivity)m_context);
        myOnclickListener.openDialog(storyList.get(position));
    }

    public void updateItems(ArrayList<StoryData> storyList) {
        this.storyList = storyList;
        notifyDataSetChanged();
    }

}
