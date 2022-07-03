package com.nabin.assignmentapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.HolderUsers> {
    private Context mContext;
    private ArrayList<UserModel> userModelArrayList;
    private DatabaseHandler databaseHandler;

    public UserAdapter(Context mContext, ArrayList<UserModel> userModelArrayList) {
        this.mContext = mContext;
        this.userModelArrayList = userModelArrayList;
        databaseHandler = new DatabaseHandler(mContext);
    }

    @NonNull
    @Override
    public HolderUsers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(mContext).inflate(R.layout.user_details_row, parent, false);

        return new HolderUsers(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderUsers holder, int position) {

        UserModel userModel = userModelArrayList.get(position);
        String id = userModel.getId();
        String name = userModel.getName();
        String address = userModel.getAddress();
        String email = userModel.getEmail();
        String registerDate = userModel.getRegisterTime();
        String updateDate = userModel.getUpdateTime();

        holder.nameTv.setText("Name: "+name);
        holder.addressTv.setText("Address: "+address);
        holder.emailTv.setText("Email: "+email);
        holder.registerTv.setText("Register Date: "+registerDate);
        holder.updateTv.setText("Update Date: "+updateDate);

        if (updateDate.equals("")){
            holder.updateTv.setVisibility(View.GONE);
        }else {
            holder.updateTv.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Are you sure you want to delete "+name + "?"  );
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                      databaseHandler.deleteUser(id);
                        Toast.makeText(mContext, "You delete User"+name, Toast.LENGTH_SHORT).show();
                     //   userModelArrayList.remove(get)
                        userModelArrayList.remove(position);
                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyItemRangeChanged(holder.getAdapterPosition(),userModelArrayList.size());
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return userModelArrayList.size();
    }

    class HolderUsers extends RecyclerView.ViewHolder{

        TextView nameTv, addressTv, emailTv, registerTv, updateTv;
        public HolderUsers(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.nameTv);
            addressTv = itemView.findViewById(R.id.addTv);
            emailTv = itemView.findViewById(R.id.emailTv);
            registerTv = itemView.findViewById(R.id.regTv);
            updateTv = itemView.findViewById(R.id.updTv);
        }
    }
}
