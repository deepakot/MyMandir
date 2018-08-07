package com.example.deepak.mymandir;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.Status;
import com.example.deepak.mymandir.Models.StoryData;
import com.example.deepak.mymandir.Utils.MMUtils;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Sharmaji on 6/2/2018.
 */

public class DetailDialogFragment extends DialogFragment {

    Dialog d;
    String title,type,url,user_name,user_image,thumb_url;
    int likes_count,comment_count;
    int downloadIdOne;
    private static String dirPath;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        likes_count= getArguments().getInt("likes_count");
        comment_count= getArguments().getInt("comment_count");
        title= getArguments().getString("title");
        type= getArguments().getString("type");
        url= getArguments().getString("url");
        thumb_url= getArguments().getString("thumb_url");
        user_name= getArguments().getString("user_name");
        user_image= getArguments().getString("user_image");
        setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }
    static DetailDialogFragment newInstance(StoryData storyData) {
        DetailDialogFragment f = new DetailDialogFragment();
        Bundle args = new Bundle();
        args.putString("title",storyData.getTitle());
        if(storyData.getAttachData()!=null) {
            args.putString("type",storyData.getAttachData().getType());
            args.putString("url",storyData.getAttachData().getUrl());
            args.putString("thumb_url",storyData.getAttachData().getThumbnailUrl());
        }
        args.putInt("likes_count",storyData.getLikeCount());
        args.putInt("comment_count",storyData.getCommentCount());
        if(storyData.getUserData()!=null) {
            args.putString("user_name",storyData.getUserData().getUserName());
            args.putString("user_image",storyData.getUserData().getUserImage());
        }
        f.setArguments(args);
        return f;
    }

    @Override
    public void onStart() {
        super.onStart();
        d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (d != null && d.isShowing()) {
            d.dismiss();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment, container, false);
        TextView storyTittleTV = (TextView) root.findViewById(R.id.titleTV);
        TextView closeTV = (TextView) root.findViewById(R.id.closeTV);
        TextView storyUserName = (TextView) root.findViewById(R.id.usernameTV);
        CircleImageView userImageIV = (CircleImageView) root.findViewById(R.id.userImageIV);
        ImageView storyImageIV = (ImageView) root.findViewById(R.id.storyImageIV);
        RelativeLayout storyDownload = (RelativeLayout) root.findViewById(R.id.storyDownload);
        TextView likeCountTV = (TextView) root.findViewById(R.id.likeCountTV);
        TextView commentCountTV = (TextView) root.findViewById(R.id.commentCountTV);
        TextView followButton = (TextView) root.findViewById(R.id.followButton);
        final ImageView likeButton = (ImageView) root.findViewById(R.id.likeButton);
        ImageView closeButton = (ImageView) root.findViewById(R.id.closeButton);
        ImageView commentButton = (ImageView) root.findViewById(R.id.commentButton);
        final ProgressBar progressBarOne = root.findViewById(R.id.progressBarOne);
        final TextView textViewProgressOne = root.findViewById(R.id.textViewProgressOne);
        final Button buttonCancelOne = root.findViewById(R.id.buttonCancelOne);
        final Button buttonOne = root.findViewById(R.id.buttonOne);

        storyTittleTV.setText(title);
        storyUserName.setText(user_name);
        likeCountTV.setText(""+likes_count);
        commentCountTV.setText(""+comment_count);
        likeCountTV.setText(String.valueOf(likes_count));
        commentCountTV.setText(String.valueOf(comment_count));
        likeButton.setImageResource(R.drawable.like);
        followButton.setText("Follow");
        followButton.setTextColor(getActivity().getResources().getColor(R.color.black));
        followButton.setBackgroundResource(R.drawable.follow);
        commentButton.setImageResource(R.drawable.comment);
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        int width = p.x;
        dirPath = MMUtils.getRootDirPath(getActivity().getApplicationContext());

        String urlToUse = url;
        if(type!=null) {
            if(type.equals("video")||type.equals("audio")) {
                urlToUse = thumb_url;
                storyDownload.setVisibility(View.VISIBLE);
            }
            Picasso.with(getActivity())
                    .load(urlToUse)
                    .placeholder(R.drawable.ropo)
                    .resize(width, 0)
                    .error(R.color.colorAccent)
                    .into(storyImageIV);
        } else {
            storyImageIV.setVisibility(View.GONE);
        }


        Picasso.with(getActivity())
                .load(user_image)
                .placeholder(R.color.colorPrimary)
                .error(R.color.colorAccent)
                .into(userImageIV);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        closeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Status.RUNNING == PRDownloader.getStatus(downloadIdOne)) {
                    PRDownloader.pause(downloadIdOne);
                    return;
                }

                buttonOne.setEnabled(false);
                progressBarOne.setIndeterminate(true);
                progressBarOne.getIndeterminateDrawable().setColorFilter(
                        Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);

                if (Status.PAUSED == PRDownloader.getStatus(downloadIdOne)) {
                    PRDownloader.resume(downloadIdOne);
                    return;
                }

                downloadIdOne = PRDownloader.download(url, dirPath, "MyMandirFile")
                        .build()
                        .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                            @Override
                            public void onStartOrResume() {
                                progressBarOne.setIndeterminate(false);
                                buttonOne.setEnabled(true);
                                buttonOne.setText(R.string.pause);
                                buttonCancelOne.setEnabled(true);
                            }
                        })
                        .setOnPauseListener(new OnPauseListener() {
                            @Override
                            public void onPause() {
                                buttonOne.setText(R.string.resume);
                            }
                        })
                        .setOnCancelListener(new OnCancelListener() {
                            @Override
                            public void onCancel() {
                                buttonOne.setText(R.string.start);
                                buttonCancelOne.setEnabled(false);
                                progressBarOne.setProgress(0);
                                textViewProgressOne.setText("");
                                downloadIdOne = 0;
                                progressBarOne.setIndeterminate(false);
                            }
                        })
                        .setOnProgressListener(new OnProgressListener() {
                            @Override
                            public void onProgress(Progress progress) {
                                long progressPercent = progress.currentBytes * 100 / progress.totalBytes;
                                progressBarOne.setProgress((int) progressPercent);
                                textViewProgressOne.setText(MMUtils.getProgressDisplayLine(progress.currentBytes, progress.totalBytes));
                                progressBarOne.setIndeterminate(false);
                            }
                        })
                        .start(new OnDownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                buttonOne.setEnabled(false);
                                buttonCancelOne.setEnabled(false);
                                buttonOne.setText(R.string.completed);
                            }

                            @Override
                            public void onError(Error error) {
                                buttonOne.setText(R.string.start);
                                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.some_error_occurred) + " " + "1", Toast.LENGTH_SHORT).show();
                                textViewProgressOne.setText("");
                                progressBarOne.setProgress(0);
                                downloadIdOne = 0;
                                buttonCancelOne.setEnabled(false);
                                progressBarOne.setIndeterminate(false);
                                buttonOne.setEnabled(true);
                            }
                        });
            }
        });

        buttonCancelOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PRDownloader.cancel(downloadIdOne);
            }
        });
        return root;
    }

}
