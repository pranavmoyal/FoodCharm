package com.pranav.foodcharm.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import com.pranav.foodcharm.R;
import com.pranav.foodcharm.view.TextView;

public class NavigationFragmentAdapter extends RecyclerView.Adapter<NavigationFragmentAdapter.ViewHolder> {
    private FragmentActivity mActivity;
    private static final int TYPE_ITEM = 1;
    public Dialog dialogMapMain;

    public NavigationFragmentAdapter(FragmentActivity mActivity) // MyAdapter Constructor with titles and icons parameter
    {
        this.mActivity = mActivity;
    }

    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_logout, rl_tour, rl_user_feedback, rl_my_network,
                rl_user_dash_board, rl_help_screen, rl_chat, rl_event, rl_settings;
        private TextView tv_my_yoNet;

        public ViewHolder(View itemView, int ViewType) // Creating ViewHolder Constructor with View and viewType As a parameter
        {
            super(itemView);
            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created
            rl_logout = (RelativeLayout) itemView.findViewById(R.id.rl_logout);
            rl_settings = (RelativeLayout) itemView.findViewById(R.id.rl_settings);
            rl_event = (RelativeLayout) itemView.findViewById(R.id.rl_event);
            rl_chat = (RelativeLayout) itemView.findViewById(R.id.rl_chat);
            rl_user_dash_board = (RelativeLayout) itemView.findViewById(R.id.rl_user_dash_board);
            rl_tour = (RelativeLayout) itemView.findViewById(R.id.rl_taketour);
            rl_user_feedback = (RelativeLayout) itemView.findViewById(R.id.rl_user_feedback);
            rl_my_network = (RelativeLayout) itemView.findViewById(R.id.rl_my_network);
            rl_help_screen = (RelativeLayout) itemView.findViewById(R.id.rl_help_screen);
            tv_my_yoNet = (TextView) itemView.findViewById(R.id.tv_my_yoNet);

        }
    }

    //Below first we ovverride the method onCreateViewHolder which is called when the ViewHolder is
    //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
    // if the viewType is TYPE_HEADER
    // and pass it to the view holder

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_item_view, parent, false); //Inflating the layout

        ViewHolder vhHeader = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

        return vhHeader; //returning the object created

    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        try {
            holder.rl_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.rl_settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.rl_help_screen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.rl_tour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
            holder.rl_my_network.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.rl_user_feedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.rl_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.rl_user_dash_board.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.rl_event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDialogOnLogout(final Context context, final String msg) {
        try {
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog,
                    null, false);

            if (dialogMapMain != null) {
                dialogMapMain.dismiss();
            }

            try {
                dialogMapMain = new Dialog(context);
                dialogMapMain.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogMapMain.setContentView(view);
                //dialogMapMain.setCancelable(false);
                dialogMapMain.setCanceledOnTouchOutside(false);
                dialogMapMain.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Setting Dialog Title

            TextView ok = (TextView) view.findViewById(R.id.exit_ok);
            ok.setText("Yes");
            TextView cancel = (TextView) view.findViewById(R.id.exit_cancel);
            cancel.setText("No");
            cancel.setVisibility(View.VISIBLE);

            // Setting Dialog Message
            ((TextView) view.findViewById(R.id.msg)).setText(msg);

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogMapMain.dismiss();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogMapMain.dismiss();
                }
            });

            dialogMapMain.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {

        return 1;
    }

    // Witht the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;

    }


}