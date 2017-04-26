package adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userone.shoppingproject.MainActivity;
import com.example.userone.shoppingproject.ProfileActivity;
import com.example.userone.shoppingproject.R;
import com.example.userone.shoppingproject.SubCategoryActivity;
import com.example.userone.shoppingproject.SubScriptionsActivity;
import com.example.userone.shoppingproject.UserLikesSectionActivity;

import java.util.ArrayList;

/**
 * Created by user1 on 15-Dec-16.
 */

public class UserDetailsAdapter extends RecyclerView.Adapter<UserDetailsAdapter.MyViewHolder> {

    private ArrayList<String> user_details_array;
    private MainActivity.OnLogoutListener mListener;
    Context context;
    MainActivity main = new MainActivity();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_user_details;

        public MyViewHolder(View view) {
            super(view);
            tv_user_details = (TextView) view.findViewById(R.id.tv_user_details);

        }
    }


    public UserDetailsAdapter(Context context, ArrayList<String> user_details, MainActivity.OnLogoutListener logoutListener) {
        this.user_details_array = user_details;
        this.context = context;
        this.mListener = logoutListener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_details_row, parent, false);
        /*itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSubject.onNext(element);
            }
        });*/
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_user_details.setText(user_details_array.get(position));
        Log.d("userDetailsAdapter", user_details_array.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 0:
                        Toast.makeText(context, "Profile you clicked", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(context, ProfileActivity.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(in);

                        break;
                    case 1:
                        Toast.makeText(context, "subscriptions you clicked", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, SubScriptionsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                        break;
                    case 2:
                        Intent likes = new Intent(context.getApplicationContext(), UserLikesSectionActivity.class);
                        likes.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(likes);
                        Toast.makeText(context, "likes you clicked", Toast.LENGTH_SHORT).show();

                        break;
                    case 3:
                        if (mListener != null) {
                            mListener.onLogout();

                        }
                        break;

                }
                Log.d("clicked_item", user_details_array.get(position));

            }
        });
    }


    @Override
    public int getItemCount() {
        return user_details_array.size();
    }
}
